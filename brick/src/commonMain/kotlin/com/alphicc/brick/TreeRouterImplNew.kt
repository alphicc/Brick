package com.alphicc.brick

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class TreeRouterImpl(
    override val parentRouter: TreeRouter? = null,
    config: RouterConfig
) : TreeRouter {

    private val childRouters: AtomicRef<List<Pair<String, TreeRouter>>> = atomic(emptyList())
    private val keyManager: KeyManager = KeyManager()
    private val navBus: NavBus = NavBus.create(config.scope, config.navAnimDelay)
    private val tree: HashMap<String, NodeNew> = hashMapOf()

    override val broadcastArgumentsFlow: MutableSharedFlow<Any> = MutableSharedFlow(
        replay = config.broadcastFlowReplay,
        extraBufferCapacity = config.broadcastFlowExtraBufferCapacity
    )

    override val overlay: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
    override val mainComponent: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
    override val childComponents: MutableStateFlow<ImmutableList<Component<*>>> =
        MutableStateFlow(persistentListOf())
    override val compositions: MutableStateFlow<ImmutableMap<String, Component<*>>> =
        MutableStateFlow(persistentMapOf())
    override val keepAliveNodes: MutableStateFlow<ImmutableList<KeepAliveNode>> =
        MutableStateFlow(persistentListOf())
    override val isRouterEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private var currentNode: NodeNew? = null

    @InternalApi
    override val topComponentKey: MutableStateFlow<String?> = MutableStateFlow(null)

    @InternalApi
    override val animState: StateFlow<AnimState> = navBus.animState

    override fun getRootRouter(): TreeRouter {
        TODO("Not yet implemented")
    }

    override fun cleanRouter() {
//        TODO("Not yet implemented")
    }

    override fun setOverlay(component: Component<*>) {
        TODO("Not yet implemented")
    }

    override fun <A> setOverlay(component: Component<*>, argument: A) {
        TODO("Not yet implemented")
    }

    override fun removeOverlay() {
        TODO("Not yet implemented")
    }

    override suspend fun branch(containerComponentKey: String): TreeRouter {
        TODO("Not yet implemented")
    }

    override suspend fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter {
        TODO("Not yet implemented")
    }

    override suspend fun <A> passArgument(componentKey: String, argument: A) {
        TODO("Not yet implemented")
    }

    override suspend fun <A> passBroadcastArgument(argument: A) {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        when {
            lastDefaultNode(currentNode)?.childComponents()?.isNotEmpty() == true -> backChild()
            lastDefaultNode(currentNode)?.bottom != null -> backComponent()//??
            else -> backComponent()
        }
    }

    override fun lastComponentKey(): String? {
        TODO("Not yet implemented")
    }

    override fun backComponent() {
        navBus.proceed(
            animState = AnimState.Executing(
                direction = AnimDirection.BACK,
                newKey = currentNode?.bottom?.key.orEmpty(),
                oldKeys = persistentListOf(currentNode?.key.orEmpty())
            ),
            preAction = {},
            postAction = {
                currentNode?.run {
                    childComponents().forEach { childComponent ->
                        destroyChildRouters(childComponent.key)
                        childComponent.onDestroy()
                        keyManager.remove(childComponent.key)
                    }
                    compositions().forEach { entry ->
                        destroyChildRouters(entry.key)
                        entry.value.onDestroy()
                        keyManager.remove(entry.key)
                    }
                    destroyChildRouters(component.key)
                    component.onDestroy()
                    keyManager.remove(component.key)
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
    }

    override fun backToComponent(key: String) {
        navBus.proceed(
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
                    }
                    node.compositions().forEach { entry ->
                        destroyChildRouters(entry.key)
                        entry.value.onDestroy()
                        keyManager.remove(entry.key)
                    }
                    destroyChildRouters(node.key)
                    node.component.onDestroy()
                    keyManager.remove(node.key)
                    tree.remove(key)
                    updateNodeInTree(
                        node = node.bottom,
                        transform = { it.copy(top = null) },
                        afterTransform = { currentNode = node.bottom }
                    )
                }
                fetchNode()
            }
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

    override fun replaceComponent(component: Component<*>) =
        replaceComponentFromNode(component, null)

    override fun <A> replaceComponent(component: Component<*>, argument: A) =
        replaceComponentFromNode(component, argument)

    private fun <A> replaceComponentFromNode(component: Component<*>, argument: A?) {
        val isSuccess = keyManager.add(component.key)
        if (isSuccess) {
            navBus.proceed(
                animState = AnimState.Executing(
                    direction = AnimDirection.FORWARD,
                    newKey = component.key,
                    oldKeys = persistentListOf(currentNode?.key.orEmpty())
                ),
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
                postAction = {
                    val replacedNode = currentNode?.bottom
                    updateNodeInTree(
                        node = currentNode,
                        transform = { transformNode -> transformNode.copy(bottom = replacedNode?.bottom) },
                    )
                    currentNode = tree[currentNode?.key]
                    updateNodeInTree(
                        node = replacedNode?.bottom,
                        transform = { transformNode -> transformNode.copy(top = currentNode) },
                        afterTransform = {
                            keyManager.remove(replacedNode?.key.orEmpty())
                            updateNodeInTree(replacedNode, { null })
                        }
                    )
                    replacedNode?.component?.run {
                        destroyChildRouters(key)
                        onDestroy()
                    }
                    fetchNode()
                }
            )
        }
    }

    override fun addComponent(component: Component<*>) = addComponentNode(component, null)

    override fun <A> addComponent(component: Component<*>, argument: A) =
        addComponentNode(component, argument)

    private fun <A> addComponentNode(component: Component<*>, argument: A?) {
        val isSuccess = keyManager.add(component.key)
        if (isSuccess) {
            navBus.proceed(
                animState = AnimState.Executing(
                    direction = AnimDirection.FORWARD,
                    newKey = component.key,
                    oldKeys = persistentListOf(currentNode?.key.orEmpty())
                ),
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
                }
            )
        }
    }

    override fun newRootComponent(component: Component<*>) = newRootComponentNode(component, null)

    override fun <A> newRootComponent(component: Component<*>, argument: A) =
        newRootComponentNode(component, argument)

    private fun <A> newRootComponentNode(component: Component<*>, argument: A?) {
        val isSuccess = keyManager.add(component.key)
        if (isSuccess) {
            navBus.proceed(
                animState = AnimState.Executing(
                    direction = AnimDirection.FORWARD,
                    newKey = component.key,
                    oldKeys = persistentListOf(currentNode?.key.orEmpty())
                ),
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
                    val removedNodes: MutableList<String> = mutableListOf()
                    tree.values.forEach { node ->
                        if (node.key != currentNode?.key) {
                            node.childComponents()
                                .forEach { childComponent ->//destroy compositions ?
                                    destroyChildRouters(childComponent.key)
                                    childComponent.onDestroy()
                                    keyManager.remove(childComponent.key)
                                }
                            destroyChildRouters(node.component.key)
                            node.component.onDestroy()
                            removedNodes.add(node.key)
                        }
                    }
                    removedNodes.forEach { key ->
                        keyManager.remove(key)
                        tree.remove(key)
                    }
                    fetchNode()
                }
            )
        }
    }

    override fun lastChildKey(): String? {
        TODO("Not yet implemented")
    }

    override fun backChild() {
        TODO("Not yet implemented")
    }

    override fun backToChild(key: String) {
        TODO("Not yet implemented")
    }

    override fun addChild(component: Component<*>) {
        TODO("Not yet implemented")
    }

    override fun <A> addChild(component: Component<*>, argument: A) {
        TODO("Not yet implemented")
    }

    override fun replaceChild(component: Component<*>) {
        TODO("Not yet implemented")
    }

    override fun <A> replaceChild(component: Component<*>, argument: A) {
        TODO("Not yet implemented")
    }

    override fun attachCompositeComponent(component: Component<*>) {
        TODO("Not yet implemented")
    }

    override fun <A> attachCompositeComponent(component: Component<*>, argument: A) {
        TODO("Not yet implemented")
    }

    override fun detachCompositeComponent(key: String) {
        TODO("Not yet implemented")
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
        mainComponent.value = lastDefaultNode(currentNode)?.component
        childComponents.value =
            lastDefaultNode(currentNode)?.childComponents() ?: persistentListOf()
        compositions.value = lastDefaultNode(currentNode)?.compositions() ?: persistentMapOf()
        keepAliveNodes.value = keepAliveNodes(currentNode).toImmutableList()
        println("keepAliveNodes=${keepAliveNodes.value.size}")
    }

    private fun lastDefaultNode(node: NodeNew?): NodeNew? = when {
        node == null -> null
        node.component.descriptor is Descriptor.Main.Default -> node
        else -> lastDefaultNode(node.bottom)
    }

    private fun keepAliveNodes(node: NodeNew?): List<KeepAliveNode> {
        if (node == null) return emptyList()

        val nodes = mutableListOf<KeepAliveNode>()
        if (node.component.descriptor is Descriptor.Main.RetainUi) {
            nodes.add(
                index = 0,
                element = KeepAliveNode(
                    mainComponent = node.component,
                    childComponentsList = node.childComponents(),
                    compositions = node.compositions()
                )
            )
        }
        nodes.addAll(index = 0, elements = keepAliveNodes(node.bottom))
        return nodes
    }

    private fun destroyChildRouters(key: String) {
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
        this.childRouters.value = childRouters
    }

    private fun cleanGraph() {
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
        removeOverlay()
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
}