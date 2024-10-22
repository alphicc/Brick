package com.alphicc.brick

import com.alphicc.brick.dataBuses.NavigationBus
import com.alphicc.brick.dataBuses.TransactionBus
import com.alphicc.brick.entities.NavAction
import com.alphicc.brick.entities.TransactionScope
import com.alphicc.brick.entities.ComponentUiState
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val EMPTY = ""

internal class TreeRouterImpl(
    override val parentRouter: TreeRouter? = null,
    config: RouterConfig
) : TreeRouter {

    private val childRouters: AtomicRef<List<Pair<String, TreeRouter>>> = atomic(emptyList())
    private val keyManager: KeyManager = KeyManager()
    private val navigationBus: NavigationBus = NavigationBus.create(config.scope)
    private val transactionBus: TransactionBus = TransactionBus.create(config.scope, navigationBus)
    private val tree: HashMap<String, NodeNew> = hashMapOf()

    override val broadcastArgumentsFlow: MutableSharedFlow<Any> = MutableSharedFlow(
        replay = config.broadcastFlowReplay,
        extraBufferCapacity = config.broadcastFlowExtraBufferCapacity
    )

    override val overlay: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
    override val nodes: MutableStateFlow<ImmutableList<Node>> = MutableStateFlow(persistentListOf())
    override val isRouterEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)

    @InternalApi
    override val topComponentKey: MutableStateFlow<String?> = MutableStateFlow(null)

    @InternalApi
    override val animState: StateFlow<AnimState> = navigationBus.animState

    @InternalApi
    override val componentUiState: MutableStateFlow<PersistentMap<String, ComponentUiState>> =
        MutableStateFlow(persistentMapOf())

    private var currentNode: NodeNew? = null

    override fun startTransaction(
        transactionSettings: TransactionSettings,
        action: TransactionScope.() -> Unit
    ) = transactionBus.startTransaction(
        router = this,
        transactionSettings = transactionSettings,
        action = action
    )

    override fun proceedAction(
        transactionSettings: TransactionSettings,
        action: () -> Unit
    ) = transactionBus.startTransaction(
        router = this,
        transactionSettings = transactionSettings,
        action = {
            transactionBus.publishMicroTransaction(
                transactionId = scopeId,
                navAction = NavAction(
                    settings = settings,
                    animState = {
                        AnimState.Executing(
                            direction = AnimDirection.ON_PLACE,
                            newKey = EMPTY,
                        )
                    },
                    preAction = { action() }
                )
            )
        }
    )

    override fun getRootRouter(): TreeRouter {
        return parentRouter?.getRootRouter() ?: this
    }

    override fun TransactionScope.cleanRouter() {
        transactionBus.publishMicroTransaction(
            transactionId = scopeId,
            navAction = NavAction(
                settings = settings,
                animState = {
                    AnimState.Executing(
                        direction = AnimDirection.ON_PLACE,
                        newKey = EMPTY,
                    )
                },
                preAction = {
                    cleanGraph()
                    removeOverlay()
                    fetchNode()
                }
            )
        )
    }

    override fun TransactionScope.setOverlay(component: Component<*>) = setOverlay(component, null)

    override fun <A> TransactionScope.setOverlay(
        component: Component<*>,
        argument: A
    ) {//todo check overlay only for root router
        transactionBus.publishMicroTransaction(
            transactionId = scopeId,
            navAction = NavAction(
                settings = settings,
                animState = {
                    AnimState.Executing(
                        direction = AnimDirection.ON_PLACE,
                        newKey = EMPTY,
                    )
                },
                preAction = {
                    val isSuccess = keyManager.add(component.key)
                    if (isSuccess) {
                        component.onCreate(argument)
                        this@TreeRouterImpl.overlay.value = component
                        fetchNode()
                    }
                },
                postAction = { setComponentUiStateShowed(component.key) }
            )
        )
    }

    override fun TransactionScope.removeOverlay() {
        transactionBus.publishMicroTransaction(
            transactionId = scopeId,
            navAction = NavAction(
                settings = settings,
                animState = {
                    AnimState.Executing(
                        direction = AnimDirection.ON_PLACE,
                        newKey = EMPTY
                    )
                },
                preAction = {
                    val rootRouter = getRootRouter()
                    if (rootRouter === this@TreeRouterImpl) {
                        overlay.value?.let { component ->
                            component.onDestroy()
                            keyManager.remove(component.key)
                            removeComponentUiState(component.key)
                            this@TreeRouterImpl.overlay.value = null
                        }
                    }
                    fetchNode()
                },
            )
        )
    }

    override suspend fun branch(containerComponentKey: String): TreeRouter =
        branch(containerComponentKey, RouterConfig.default())

    override suspend fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter {
        return navigationBus.proceedWithResult(
            navAction = NavAction(
                settings = TransactionSettings.immediately(),
                animState = {
                    AnimState.Executing(
                        direction = AnimDirection.ON_PLACE,
                        newKey = EMPTY
                    )
                },
                preAction = {
                    val newRouter = TreeRouterImpl(this, config)
                    val mutedChildRouters = childRouters.value.toMutableList()
                    mutedChildRouters.add(containerComponentKey to newRouter)
                    childRouters.value = mutedChildRouters
                    newRouter
                }
            )
        )
    }

    override suspend fun <A> passArgument(componentKey: String, argument: A) {
        redirectArgument(this, componentKey, argument)
    }

    override suspend fun <A> passBroadcastArgument(argument: A) {
        broadcastArgumentsFlow.emit(argument as Any)
    }

    override fun onBackClicked(immediately: Boolean) {
        val transactionSettings = if (immediately) {
            TransactionSettings.immediately()
        } else TransactionSettings.default()
        transactionBus.startTransaction(router = this, transactionSettings = transactionSettings) {
            when {
                currentNode?.childComponents()?.isNotEmpty() == true -> backChild()
                currentNode?.bottom != null -> backComponent()
                else -> with(copy(settings = TransactionSettings.immediately())) { backComponent() }
            }
        }
    }

    override fun lastComponentKey(): String? = currentNode?.key

    override fun TransactionScope.backComponent() {
        transactionBus.publishMicroTransaction(
            transactionId = scopeId,
            navAction = NavAction(
                settings = settings,
                animState = {
                    AnimState.Executing(
                        direction = AnimDirection.BACK,
                        newKey = currentNode?.bottom?.key.orEmpty(),
                        oldKeys = persistentListOf(currentNode?.key.orEmpty())
                    )
                },
                preAction = {},
                postAction = {
                    currentNode?.run {
                        childComponents().forEach { childComponent ->
                            destroyChildRouters(childComponent.key)
                            childComponent.onDestroy()
                            keyManager.remove(childComponent.key)
                            removeComponentUiState(childComponent.key)
                        }
                        compositions().forEach { entry ->
                            destroyChildRouters(entry.key)
                            entry.value.onDestroy()
                            keyManager.remove(entry.key)
                            removeComponentUiState(entry.key)
                        }
                        destroyChildRouters(component.key)
                        component.onDestroy()
                        keyManager.remove(component.key)
                        removeComponentUiState(component.key)
                        updateNodeInTree(
                            node = this.bottom,
                            transform = { transformNode -> transformNode.copy(top = null) },
                            afterTransform = {
                                updateNodeInTree(this, { null })
                                currentNode = this.bottom
                            }
                        )
                    }
                    fetchNode()
                }
            )
        )
    }

    override fun TransactionScope.backToComponent(key: String) {
        transactionBus.publishMicroTransaction(
            transactionId = scopeId,
            navAction = NavAction(
                settings = settings,
                animState = {
                    val droppedNodes = nodesUntilFoundKey(currentNode, key)
                    AnimState.Executing(
                        direction = AnimDirection.BACK,
                        newKey = key,
                        oldKeys = droppedNodes.map { node -> node.key }.toImmutableList()
                    )
                },
                preAction = {},
                postAction = {
                    val droppedNodes = nodesUntilFoundKey(currentNode, key)
                    droppedNodes.forEach { node ->
                        node.childComponents().forEach { childComponent ->
                            destroyChildRouters(childComponent.key)
                            childComponent.onDestroy()
                            keyManager.remove(childComponent.key)
                            removeComponentUiState(childComponent.key)
                        }
                        node.compositions().forEach { entry ->
                            destroyChildRouters(entry.key)
                            entry.value.onDestroy()
                            keyManager.remove(entry.key)
                            removeComponentUiState(entry.key)
                        }
                        destroyChildRouters(node.key)
                        node.component.onDestroy()
                        keyManager.remove(node.key)
                        removeComponentUiState(node.key)
                        tree.remove(node.key)
                        updateNodeInTree(
                            node = node.bottom,
                            transform = { it.copy(top = null) },
                            afterTransform = { currentNode = node.bottom }
                        )
                    }
                    fetchNode()
                }
            )
        )
    }

    private fun nodesUntilFoundKey(node: NodeNew?, componentKey: String): List<NodeNew> {
        val droppedNodes: MutableList<NodeNew> = mutableListOf()
        if (node != null && node.key != componentKey) {
            droppedNodes.add(node)
            val newDroppedNodes = nodesUntilFoundKey(node.bottom, componentKey)
            droppedNodes.addAll(newDroppedNodes)
        }
        return droppedNodes
    }

    override fun TransactionScope.replaceComponent(component: Component<*>) =
        replaceComponentFromNode(component, null)

    override fun <A> TransactionScope.replaceComponent(component: Component<*>, argument: A) =
        replaceComponentFromNode(component, argument)

    private fun <A> TransactionScope.replaceComponentFromNode(
        component: Component<*>,
        argument: A?
    ) {
        transactionBus.publishMicroTransaction(
            transactionId = scopeId,
            navAction = NavAction(
                settings = settings,
                filter = { keyManager.add(component.key) },
                animState = {
                    AnimState.Executing(
                        direction = AnimDirection.FORWARD,
                        newKey = component.key,
                        oldKeys = persistentListOf(currentNode?.key.orEmpty())
                    )
                },
                preAction = {
                    println("preAction keepAliveNodes = ${nodes.value.size}")
                    component.onCreate(argument)
                    val newNode = NodeNew(component = component, bottom = currentNode)
                    updateNodeInTree(
                        node = currentNode,
                        transform = { transformNode -> transformNode.copy(top = newNode) },
                        afterTransform = {
                            tree[newNode.key] = newNode
                            currentNode = newNode
                        }
                    )
                    fetchNode()
                    println("postAction keepAliveNodes = ${nodes.value.size}")
                },
                postAction = {
                    println("postAction call")
                    val replacedNode = currentNode?.bottom
                    updateNodeInTree(
                        node = currentNode,
                        transform = { transformNode -> transformNode.copy(bottom = replacedNode?.bottom) },
                    )
                    currentNode = tree[currentNode?.key]
                    setComponentUiStateShowed(currentNode?.key.orEmpty())
                    replacedNode?.component?.run {
                        destroyChildRouters(key)
                        onDestroy()
                    }
                    updateNodeInTree(
                        node = replacedNode?.bottom,
                        transform = { transformNode -> transformNode.copy(top = currentNode) },
                        afterTransform = {
                            keyManager.remove(replacedNode?.key.orEmpty())
                            removeComponentUiState(replacedNode?.key.orEmpty())
                            updateNodeInTree(replacedNode, { null })
                        }
                    )
                    fetchNode()
                }
            )
        )
    }

    override fun TransactionScope.addComponent(component: Component<*>) =
        addComponentNode(component, null)

    override fun <A> TransactionScope.addComponent(component: Component<*>, argument: A) =
        addComponentNode(component, argument)

    private fun <A> TransactionScope.addComponentNode(component: Component<*>, argument: A?) {
        val navAction = NavAction(
            settings = settings,
            filter = { keyManager.add(component.key) },
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.FORWARD,
                    newKey = component.key,
                    oldKeys = persistentListOf(currentNode?.key.orEmpty()),
                )
            },
            preAction = {
                component.onCreate(argument)
                val newNode = NodeNew(component = component, bottom = currentNode)
                updateNodeInTree(
                    node = currentNode,
                    transform = { transformNode -> transformNode.copy(top = newNode) },
                    afterTransform = {
                        tree[newNode.key] = newNode
                        currentNode = newNode
                    }
                )
                fetchNode()
            },
            postAction = { setComponentUiStateShowed(component.key) }
        )
        transactionBus.publishMicroTransaction(transactionId = scopeId, navAction = navAction)
    }

    override fun TransactionScope.newRootComponent(component: Component<*>) =
        newRootComponentNode(component, null)

    override fun <A> TransactionScope.newRootComponent(component: Component<*>, argument: A) =
        newRootComponentNode(component, argument)

    private fun <A> TransactionScope.newRootComponentNode(component: Component<*>, argument: A?) {
        val navAction = NavAction(
            filter = { keyManager.add(component.key) },
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.FORWARD,
                    newKey = component.key,
                    oldKeys = persistentListOf(currentNode?.key.orEmpty())
                )
            },
            preAction = {
                component.onCreate(argument)
                val newNode = NodeNew(component = component, bottom = currentNode)
                tree[newNode.key] = newNode
                currentNode = newNode
                fetchNode()
            },
            postAction = {
                updateNodeInTree(
                    node = currentNode,
                    transform = { transformNode -> transformNode.copy(bottom = null) },
                    afterTransform = { currentNode = tree[currentNode?.key] }
                )
                setComponentUiStateShowed(component.key)
                val removedNodes: MutableList<String> = mutableListOf()
                tree.values.forEach { node ->
                    if (node.key != currentNode?.key) {
                        node.childComponents()
                            .forEach { childComponent ->//destroy compositions ?
                                destroyChildRouters(childComponent.key)
                                childComponent.onDestroy()
                                keyManager.remove(childComponent.key)
                                removeComponentUiState(childComponent.key)
                            }
                        destroyChildRouters(node.component.key)
                        node.component.onDestroy()
                        removedNodes.add(node.key)
                    }
                }
                removedNodes.forEach { key ->
                    keyManager.remove(key)
                    removeComponentUiState(key)
                    tree.remove(key)
                }
                fetchNode()
            }
        )
        transactionBus.publishMicroTransaction(transactionId = scopeId, navAction = navAction)
    }

    override fun lastChildKey(): String? = currentNode?.childComponents()?.lastOrNull()?.key

    override fun TransactionScope.backChild() {
        val navAction = NavAction(
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.ON_PLACE,
                    newKey = currentNode?.childComponents()?.last()?.key.orEmpty()
                )
            },
            preAction = {
                currentNode?.run {
                    if (childComponents().isNotEmpty()) {
                        val component = childComponents().last()
                        dropLastChildComponent()
                        destroyChildRouters(component.key)
                        component.onDestroy()
                        keyManager.remove(component.key)
                        removeComponentUiState(component.key)
                        fetchNode()
                    }
                }
            }
        )
        transactionBus.publishMicroTransaction(transactionId = scopeId, navAction = navAction)
    }

    override fun TransactionScope.backToChild(key: String) {
        val navAction = NavAction(
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.ON_PLACE,
                    newKey = currentNode?.childComponents()?.last()?.key.orEmpty()
                )
            },
            preAction = {
                currentNode?.run {
                    childComponents().reversed().forEach { component ->
                        if (component.key != key) {
                            keyManager.remove(component.key)
                            removeComponentUiState(component.key)
                            destroyChildRouters(component.key)
                            component.onDestroy()
                        }
                    }
                    fetchNode()
                }
            }
        )
        transactionBus.publishMicroTransaction(scopeId, navAction)
    }

    override fun TransactionScope.addChild(component: Component<*>) = addChildNode(component, null)

    override fun <A> TransactionScope.addChild(component: Component<*>, argument: A) =
        addChildNode(component, argument)

    private fun <A> TransactionScope.addChildNode(component: Component<*>, argument: A?) {
        val navAction = NavAction(
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.ON_PLACE,
                    newKey = component.key
                )
            },
            preAction = {
                currentNode?.let {
                    if (keyManager.add(component.key)) {
                        currentNode?.let { node ->
                            node.addChildComponent(component)
                            component.onCreate(argument)
                        }
                        fetchNode()
                    }
                }
            },
            postAction = { setComponentUiStateShowed(component.key) }
        )
        transactionBus.publishMicroTransaction(scopeId, navAction)
    }

    override fun TransactionScope.replaceChild(component: Component<*>) =
        replaceChildNode(component, null)

    override fun <A> TransactionScope.replaceChild(component: Component<*>, argument: A) =
        replaceChildNode(component, argument)

    private fun <A> TransactionScope.replaceChildNode(component: Component<*>, argument: A?) {
        val navAction = NavAction(
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.ON_PLACE,
                    newKey = component.key
                )
            },
            preAction = {
                currentNode?.run {
                    if (childComponents().isNotEmpty()) {
                        val oldChildComponent = childComponents().last()
                        if (keyManager.replaceKey(oldChildComponent.key, component.key)) {
                            removeComponentUiState(oldChildComponent.key)
                            destroyChildRouters(oldChildComponent.key)
                            oldChildComponent.onDestroy()
                            component.onCreate(argument)
                            replaceLastChildComponent(component)
                        }
                        fetchNode()
                    }
                }
            },
            postAction = { setComponentUiStateShowed(component.key) }
        )
        transactionBus.publishMicroTransaction(scopeId, navAction)
    }

    override fun TransactionScope.attachCompositeComponent(component: Component<*>) =
        attachCompositeComponent(component, null)

    override fun <A> TransactionScope.attachCompositeComponent(
        component: Component<*>,
        argument: A
    ) {
        val navAction = NavAction(
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.ON_PLACE,
                    newKey = EMPTY
                )
            },
            preAction = {
                currentNode?.run {
                    val isSuccess = keyManager.add(component.key)
                    if (isSuccess) {
                        component.onCreate(argument)
                        addComposition(component)
                    }
                }
            },
            postAction = { setComponentUiStateShowed(component.key) }
        )
        transactionBus.publishMicroTransaction(scopeId, navAction)
    }

    override fun TransactionScope.detachCompositeComponent(key: String) {
        val navAction = NavAction(
            settings = settings,
            animState = {
                AnimState.Executing(
                    direction = AnimDirection.ON_PLACE,
                    newKey = EMPTY
                )
            },
            preAction = {
                currentNode?.run {
                    keyManager.remove(key)
                    removeComponentUiState(key)
                    val composition = compositions()[key]
                    if (composition != null) {
                        composition.onDestroy()
                        removeComposition(key)
                    }
                }
            }
        )
        transactionBus.publishMicroTransaction(scopeId, navAction)
    }

    @InternalApi
    override suspend fun <A> redirectArgument(
        from: ArgumentTranslator,
        componentKey: String,
        argument: A
    ): Boolean {
        val isEmitted = emitArguments(componentKey, argument)
        if (isEmitted) return true
        childRouters.value.forEach {
            val isEmittedChildRouter = it.second.redirectArgument(this, componentKey, argument)
            if (isEmittedChildRouter) return true
        }
        if (parentRouter !== from) {
            val isEmittedParentRouter = parentRouter?.redirectArgument(this, componentKey, argument)
            if (isEmittedParentRouter == true) return true
        }
        return false
    }

    private fun updateNodeInTree(
        node: NodeNew?,
        transform: (NodeNew) -> NodeNew?,
        afterTransform: () -> Unit = {}
    ) {
        node?.key?.let { key ->
            tree[key]?.let { treeNode ->
                val transformResult = transform(treeNode)
                if (transformResult == null) tree.remove(key)
                else tree[key] = transformResult
            }
        }
        afterTransform()
    }

    private inline fun fetchNode() {
        topComponentKey.value = currentNode?.key
        isRouterEmpty.value = currentNode == null
        nodes.value = getNodesList(currentNode).toImmutableList()
    }

    private fun getNodesList(node: NodeNew?): List<Node> {
        if (node == null) return emptyList()

        val nodes = mutableListOf<Node>()
        nodes.add(
            index = 0,
            element = Node(
                mainComponent = node.component,
                childComponentsList = node.childComponents(),
                compositions = node.compositions()
            )
        )
        nodes.addAll(index = 0, elements = getNodesList(node.bottom))
        return nodes
    }

    private fun TransactionScope.destroyChildRouters(key: String) {
        val childRouters = childRouters.value.toMutableList()
        childRouters.removeAll {
            if (it.first == key) {
                (it.second as TreeRouterImpl).run {
                    cleanGraph()
                    removeOverlay()
                }
                true
            } else false
        }
        this@TreeRouterImpl.childRouters.value = childRouters
    }

    private fun TransactionScope.cleanGraph() {
        tree.values.forEach { node ->
            node.childComponents().forEach { childComponent ->
                destroyChildRouters(childComponent.key)
                childComponent.onDestroy()
                keyManager.remove(childComponent.key)
            }
            destroyChildRouters(node.component.key)
            node.component.onDestroy()
            keyManager.remove(node.component.key)
        }
        tree.clear()
        cleanComponentUiState()
        currentNode = null
    }

    private suspend fun <A> emitArguments(componentKey: String, argument: A): Boolean {
        overlay.value?.let { overlayComponent ->
            if (overlayComponent.key == componentKey) {
                val dataContainer = DataContainer(argument)
                overlayComponent.channel.emit(dataContainer)
                return true
            }
        }

        tree.forEach { node ->
            node.value.childComponents().forEach {
                if (it.key == componentKey) {
                    val dataContainer = DataContainer(argument)
                    it.channel.emit(dataContainer)
                    return true
                }
            }
            node.value.compositions().forEach { entry ->
                if (entry.key == componentKey) {
                    val dataContainer = DataContainer(argument)
                    entry.value.channel.emit(dataContainer)
                    return true
                }
            }
            if (node.value.component.key == componentKey) {
                val dataContainer = DataContainer(argument)
                node.value.component.channel.emit(dataContainer)
                return true
            }
        }
        return false
    }

    private fun setComponentUiStateShowed(key: String) {
        componentUiState.value = componentUiState.value.put(key, ComponentUiState(true))
    }

    private fun removeComponentUiState(key: String) {
        componentUiState.value = componentUiState.value.remove(key)
    }

    private fun cleanComponentUiState() {
        componentUiState.value = persistentMapOf()
    }
}