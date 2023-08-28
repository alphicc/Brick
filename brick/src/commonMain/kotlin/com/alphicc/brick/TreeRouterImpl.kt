package com.alphicc.brick

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

internal class TreeRouterImpl(
    override val initialComponent: Component<*>? = null,
    override val parentRouter: TreeRouter? = null,
    private val coroutineScope: CoroutineScope,
    config: RouterConfig
) : TreeRouter {

    private val _actionReceiver: MutableSharedFlow<UpdateRouterActions> =
        MutableSharedFlow(replay = 100, extraBufferCapacity = 100, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val keyManager = KeyManager()
    private val _keepAliveNode: MutableStateFlow<List<KeepAliveNode>> = MutableStateFlow(emptyList())
    private val _currentCompositionsFlow: MutableStateFlow<Map<String, Component<*>>> = MutableStateFlow(emptyMap())
    private val _currentOverlayFlow: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
    private val _currentComponentFlow: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
    private val _currentChildFlow: MutableStateFlow<List<Component<*>>> = MutableStateFlow(emptyList())
    private val _isRouterEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _broadcastFlow: MutableSharedFlow<Any> = MutableSharedFlow(
        replay = config.broadcastFlowReplay,
        extraBufferCapacity = config.broadcastFlowExtraBufferCapacity
    )

    private val childRouters: AtomicRef<List<Pair<String, TreeRouter>>> = atomic(emptyList())
    private val tree: AtomicRef<List<Node>> = atomic(emptyList())
    private val currentNode: Node?
        get() = tree.value.lastOrNull()

    override val overlay: StateFlow<Component<*>?> = _currentOverlayFlow

    override val mainComponent: StateFlow<Component<*>?> = _currentComponentFlow

    override val childComponentsList: StateFlow<List<Component<*>>> = _currentChildFlow

    override val compositions: StateFlow<Map<String, Component<*>>> = _currentCompositionsFlow

    override val keepAliveNodes: StateFlow<List<KeepAliveNode>> = _keepAliveNode

    override val isRouterEmpty: StateFlow<Boolean> = _isRouterEmpty

    override val broadcastFlow: MutableSharedFlow<Any> = _broadcastFlow

    override fun currentComponentKey(): String? = mainComponent.value?.key

    init {
        observeActionReceiverUpdates()
    }

    //return "true" if has back navigation variants else false
    override fun onBackClicked() {
        _actionReceiver.tryEmit(UpdateRouterActions.OnBackClicked)
    }

    override fun attachCompositeComponent(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.AttachCompositeComponent(component, null))
    }

    override fun <A> attachCompositeComponent(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.AttachCompositeComponent(component, argument))
    }

    override fun detachCompositeComponent(key: String) {
        _actionReceiver.tryEmit(UpdateRouterActions.DetachCompositeComponent(key))
    }

    override fun backComponent() {
        _actionReceiver.tryEmit(UpdateRouterActions.BackComponent)
    }

    override fun getRootRouter(): TreeRouter = parentRouter?.getRootRouter() ?: this

    override fun cleanRouter() {
        _actionReceiver.tryEmit(UpdateRouterActions.CleanRouter)
    }

    override fun branch(containerComponentKey: String): TreeRouter =
        branch(containerComponentKey, RouterConfig.default())

    override fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter {
        val newRouter = TreeRouterImpl(initialComponent, this, coroutineScope, config)
        val childRouters = childRouters.value.toMutableList()
        childRouters.add(containerComponentKey to newRouter)
        this.childRouters.value = childRouters
        return newRouter
    }

    override fun setOverlay(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.SetOverlay(component, null))
    }

    override fun <A> setOverlay(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.SetOverlay(component, argument))
    }

    override fun removeOverlay() {
        _actionReceiver.tryEmit(UpdateRouterActions.RemoveOverlay)
    }

    override suspend fun <A> passArgument(componentKey: String, argument: A) {
        redirectArgument(this, componentKey, argument)
    }

    override suspend fun <A> passBroadcastArgument(argument: A) {
        _broadcastFlow.emit(argument as Any)
    }

    override fun backToComponent(key: String) {
        _actionReceiver.tryEmit(UpdateRouterActions.BackToComponent(key))
    }

    override fun replaceComponent(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.ReplaceComponent(component, null))
    }

    override fun <A> replaceComponent(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.ReplaceComponent(component, argument))
    }

    override fun addComponent(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.AddComponent(component, null))
    }

    override fun <A> addComponent(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.AddComponent(component, argument))
    }

    override fun newRootComponent(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.NewRootComponent(component, null))
    }

    override fun <A> newRootComponent(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.NewRootComponent(component, argument))
    }

    override fun lastChildKey(): String? = childComponentsList.value.lastOrNull()?.key

    override fun backChild() {
        _actionReceiver.tryEmit(UpdateRouterActions.BackChild)
    }

    override fun backToChild(key: String) {
        _actionReceiver.tryEmit(UpdateRouterActions.BackToChild(key))
    }

    override fun replaceChild(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.ReplaceChild(component, null))
    }

    override fun <A> replaceChild(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.ReplaceChild(component, argument))
    }

    override fun addChild(component: Component<*>) {
        _actionReceiver.tryEmit(UpdateRouterActions.AddChild(component, null))
    }

    override fun <A> addChild(component: Component<*>, argument: A) {
        _actionReceiver.tryEmit(UpdateRouterActions.AddChild(component, argument))
    }

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

    private suspend fun <A> emitArguments(componentKey: String, argument: A): Boolean {
        _currentOverlayFlow.value?.let { overlayComponent ->
            if (overlayComponent.key == componentKey) {
                val dataContainer = DataContainer(argument)
                overlayComponent.channel.emit(dataContainer)
                return true
            }
        }

        tree.value.forEach { node ->
            node.childComponents().forEach {
                if (it.key == componentKey) {
                    val dataContainer = DataContainer(argument)
                    it.channel.emit(dataContainer)
                    return true
                }
            }

            node.compositions().forEach { entry ->
                if (entry.key == componentKey) {
                    val dataContainer = DataContainer(argument)
                    entry.value.channel.emit(dataContainer)
                    return true
                }
            }

            if (node.rootComponent.key == componentKey) {
                val dataContainer = DataContainer(argument)
                node.rootComponent.channel.emit(dataContainer)
                return true
            }
        }
        return false
    }

    private fun onBackClickedAction() {
        when {
            currentNode?.childComponents()?.isNotEmpty() == true -> backChild()
            currentNode?.parent != null -> backComponent()
            else -> backComponent()
        }
    }

    private fun <A> attachCompositeComponentToNode(component: Component<*>, argument: A?) {
        val isSuccess = keyManager.add(component.key)
        if (!isSuccess) return
        val currentNode = currentNode ?: return
        component.onCreate(argument)
        currentNode.addComposition(component)
        fetchNode()
    }

    private fun detachCompositeComponentFromNode(key: String) {
        val currentNode = currentNode ?: return
        keyManager.remove(key)
        val composition = currentNode.compositions()[key]
        if (composition != null) {
            composition.onDestroy()
            currentNode.removeComposition(key)
            fetchNode()
        }
    }

    private fun backComponentAction() {
        val nodesTree = tree.value
        nodesTree.lastOrNull()?.let {
            it.childComponents().forEach { childComponent ->
                destroyChildRouters(childComponent.key)
                childComponent.onDestroy()
                keyManager.remove(childComponent.key)
            }
            it.compositions().forEach { entry ->
                destroyChildRouters(entry.key)
                entry.value.onDestroy()
                keyManager.remove(entry.key)
            }
            destroyChildRouters(it.rootComponent.key)
            it.rootComponent.onDestroy()
            keyManager.remove(it.rootComponent.key)
            tree.value = nodesTree.dropLast(1)
            fetchNode()
        }
    }

    private fun cleanRouterAction() {
        cleanGraph()
        removeOverlay()
        fetchNode()
    }

    private fun <A> setOverlayAction(component: Component<*>, argument: A) {
        val rootRouter = getRootRouter()
        if (rootRouter === this) setOverlayNode(component, argument)
    }

    private fun removeOverlayAction() {
        val rootRouter = getRootRouter()
        if (rootRouter === this) {
            _currentOverlayFlow.value?.let { component ->
                keyManager.remove(component.key)
                component.onDestroy()
                _currentOverlayFlow.value = null
            }
        }
    }

    private fun backToComponentAction(key: String) {
        val droppedNodes = dropNodeUntilFoundKey(currentNode, key)
        droppedNodes.forEach {
            it.childComponents().forEach { childComponent ->
                destroyChildRouters(childComponent.key)
                childComponent.onDestroy()
                keyManager.remove(childComponent.key)
            }
            it.compositions().forEach { entry ->
                destroyChildRouters(entry.key)
                entry.value.onDestroy()
                keyManager.remove(entry.key)
            }
            destroyChildRouters(it.rootComponent.key)
            it.rootComponent.onDestroy()
            keyManager.remove(it.rootComponent.key)
        }
        fetchNode()
    }

    private fun backChildAction() {
        currentNode?.run {
            if (childComponents().isNotEmpty()) {
                val component = childComponents().last()
                dropLastChildComponent()
                keyManager.remove(component.key)
                destroyChildRouters(component.key)
                component.onDestroy()
                fetchNode()
            }
        }
    }

    private fun backToChildAction(key: String) {
        currentNode?.run {
            val droppedChildList = dropChildUntilFoundKey(this, key)
            droppedChildList.forEach {
                keyManager.remove(it.key)
                destroyChildRouters(it.key)
                it.onDestroy()
            }
            fetchNode()
        }
    }

    private fun <A> addChildNode(component: Component<*>, argument: A?) {
        currentNode?.run {
            val isSuccess = keyManager.add(component.key)
            if (!isSuccess) return
            component.onCreate(argument)
            addChildComponent(component)
            fetchNode()
        }
    }

    private fun <A> replaceChildNode(component: Component<*>, argument: A?) {
        currentNode?.run {
            if (childComponents().isNotEmpty()) {
                val isSuccess = keyManager.replaceKey(childComponents().last().key, component.key)
                if (!isSuccess) return
                val oldChildComponent = childComponents()[childComponents().size - 1]
                destroyChildRouters(oldChildComponent.key)
                oldChildComponent.onDestroy()
                component.onCreate(argument)
                replaceLastChildComponent(component)
                fetchNode()
            }
        }
    }

    private fun <A> newRootComponentNode(component: Component<*>, argument: A?) {
        cleanGraph()
        addComponent(component, argument)
    }

    private fun <A> addComponentNode(component: Component<*>, argument: A?) {
        val isSuccess = keyManager.add(component.key)
        if (!isSuccess) return
        component.onCreate(argument)
        val node = Node(
            rootComponent = component,
            parent = currentNode
        )
        tree.value = tree.value
            .toMutableList()
            .apply { add(node) }
        fetchNode()
    }

    private fun <A> replaceComponentFromNode(component: Component<*>, argument: A?) {
        currentNode?.let { node ->
            val isSuccess = keyManager.replaceKey(node.rootComponent.key, component.key)
            if (!isSuccess) return
        }
        currentNode?.rootComponent?.let {
            destroyChildRouters(it.key)
            it.onDestroy()
        }
        component.onCreate(argument)
        tree.value = tree.value.toMutableList().apply {
            if (this.isNotEmpty()) {
                this[this.lastIndex] = last().copy(rootComponent = component)
            }
        }
        fetchNode()
    }

    private fun <A> setOverlayNode(component: Component<*>, argument: A?) {
        val isSuccess = keyManager.add(component.key)
        if (!isSuccess) return

        component.onCreate(argument)
        _currentOverlayFlow.value = component
    }

    private fun dropChildUntilFoundKey(
        node: Node,
        componentKey: String
    ): List<Component<*>> {
        val droppedChildList = ArrayList<Component<*>>()
        node.childComponents().lastOrNull()?.let {
            if (it.key != componentKey) {
                node.dropLastChildComponent()
                droppedChildList.add(it)
                val innerDroppedChildList = dropChildUntilFoundKey(node, componentKey)
                droppedChildList.addAll(innerDroppedChildList)
            } else return@let
        }
        return droppedChildList
    }

    private fun dropNodeUntilFoundKey(node: Node?, componentKey: String): List<Node> {
        val droppedNodes = ArrayList<Node>()
        if (node != null && node.rootComponent.key != componentKey) {
            droppedNodes.add(node)
            val modifiedTree = tree.value.toMutableList().apply {
                removeLastOrNull()
            }
            tree.value = modifiedTree
            val newDroppedNodes = dropNodeUntilFoundKey(node.parent, componentKey)
            droppedNodes.addAll(newDroppedNodes)
        }
        return droppedNodes
    }

    private fun cleanGraph() {
        tree.value.forEach { node ->
            node.childComponents().forEach { childComponent ->
                destroyChildRouters(childComponent.key)
                childComponent.onDestroy()
                keyManager.remove(childComponent.key)
            }
            destroyChildRouters(node.rootComponent.key)
            node.rootComponent.onDestroy()
            keyManager.remove(node.rootComponent.key)
        }
        tree.value = emptyList()

        removeOverlay()
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

    private fun fetchNode() {
        _isRouterEmpty.value = currentNode == null
        _currentComponentFlow.value = currentNode?.rootComponent
        _currentChildFlow.value = currentNode?.childComponents() ?: emptyList()
        _currentCompositionsFlow.value = currentNode?.compositions() ?: emptyMap()
        _keepAliveNode.value = tree.value
            .filter { it.rootComponent.keepAliveCompose }
            .map {
                KeepAliveNode(
                    it.rootComponent,
                    it.childComponents(),
                    it.compositions()
                )
            }
    }

    private fun observeActionReceiverUpdates() {
        _actionReceiver.onEach { action ->
            when (action) {
                is UpdateRouterActions.AddChild<*> -> addChildNode(action.component, action.argument)
                is UpdateRouterActions.AddComponent<*> -> addComponentNode(action.component, action.argument)

                is UpdateRouterActions.AttachCompositeComponent<*> -> attachCompositeComponentToNode(
                    action.component,
                    action.argument
                )

                is UpdateRouterActions.BackChild -> backChildAction()
                is UpdateRouterActions.BackComponent -> backComponentAction()
                is UpdateRouterActions.BackToChild -> backToChildAction(action.key)
                is UpdateRouterActions.BackToComponent -> backToComponentAction(action.key)
                is UpdateRouterActions.CleanRouter -> cleanRouterAction()
                is UpdateRouterActions.DetachCompositeComponent -> detachCompositeComponentFromNode(action.key)
                is UpdateRouterActions.NewRootComponent<*> -> newRootComponentNode(action.component, action.argument)
                is UpdateRouterActions.OnBackClicked -> onBackClickedAction()
                is UpdateRouterActions.RemoveOverlay -> removeOverlayAction()
                is UpdateRouterActions.ReplaceChild<*> -> replaceChildNode(action.component, action.argument)
                is UpdateRouterActions.ReplaceComponent<*> -> replaceComponentFromNode(
                    action.component,
                    action.argument
                )

                is UpdateRouterActions.SetOverlay<*> -> setOverlayAction(action.component, action.argument)
            }
        }.launchIn(coroutineScope)
    }
}