//package com.alphicc.brick
//
//import kotlinx.atomicfu.AtomicRef
//import kotlinx.atomicfu.atomic
//import kotlinx.collections.immutable.ImmutableList
//import kotlinx.collections.immutable.ImmutableMap
//import kotlinx.collections.immutable.persistentListOf
//import kotlinx.collections.immutable.persistentMapOf
//import kotlinx.collections.immutable.toImmutableList
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//internal class TreeRouterImpl(
//    override val parentRouter: TreeRouter? = null,
//    config: RouterConfig
//) : TreeRouter {
//
//    private val keyManager: KeyManager = KeyManager()
//    private val navBus: NavBus = NavBus.create(config.scope, config.navAnimDelay)
//
//    private val childRouters: AtomicRef<List<Pair<String, TreeRouter>>> = atomic(emptyList())
//    private val tree: AtomicRef<List<Node>> = atomic(emptyList())
//
//    private val lastNode: Node?
//        get() {
//            return tree.value.lastOrNull()
//        }
//    private val lastDefaultNode: Node?
//        get() {
//            return tree.value.lastOrNull { node -> node.rootComponent.descriptor !is Descriptor.Main.RetainUi }
//        }
//
//    override val overlay: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
//    override val mainComponent: MutableStateFlow<Component<*>?> = MutableStateFlow(null)
//    override val childComponents: MutableStateFlow<ImmutableList<Component<*>>> =
//        MutableStateFlow(persistentListOf())
//    override val compositions: MutableStateFlow<ImmutableMap<String, Component<*>>> =
//        MutableStateFlow(persistentMapOf())
//    override val keepAliveNodes: MutableStateFlow<ImmutableList<KeepAliveNode>> =
//        MutableStateFlow(persistentListOf())
//    override val isRouterEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)
//    override val broadcastArgumentsFlow: MutableSharedFlow<Any> = MutableSharedFlow(
//        replay = config.broadcastFlowReplay,
//        extraBufferCapacity = config.broadcastFlowExtraBufferCapacity
//    )
//
//    override val topComponentKey: MutableStateFlow<String?> = MutableStateFlow(null)
//
//    override val animState: StateFlow<AnimState> = navBus.animState
//
//    override fun getRootRouter(): TreeRouter = parentRouter?.getRootRouter() ?: this
//
//    override fun cleanRouter() {
//        navBus.proceed {
//            withNavigationNodeFetching {
//                cleanGraph()
//                removeOverlay()
//            }
//        }
//    }
//
//    override fun setOverlay(component: Component<*>) {
//        setOverlay(component, null)
//    }
//
//    override fun <A> setOverlay(component: Component<*>, argument: A) {
//        navBus.proceed {
//            val rootRouter = getRootRouter()
//            if (rootRouter === this) setOverlayNode(component, argument)
//        }
//    }
//
//    override fun removeOverlay() {
//        navBus.proceed {
//            val rootRouter = getRootRouter()
//            if (rootRouter === this) {
//                overlay.value?.let { component ->
//                    keyManager.remove(component.key)
//                    component.onDestroy()
//                    overlay.value = null
//                }
//            }
//        }
//    }
//
//    override suspend fun branch(containerComponentKey: String): TreeRouter =
//        branch(containerComponentKey, RouterConfig.default())
//
//    override suspend fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter {
//        return navBus.proceedWithResult {
//            TreeRouterImpl(this, config).apply {
//                val childRouters = childRouters.value.toMutableList()
//                childRouters.add(containerComponentKey to this)
//                this.childRouters.value = childRouters
//            }
//        }
//    }
//
//    override suspend fun <A> passArgument(componentKey: String, argument: A) {
//        redirectArgument(this, componentKey, argument)
//    }
//
//    override suspend fun <A> passBroadcastArgument(argument: A) {
//        broadcastArgumentsFlow.emit(argument as Any)
//    }
//
//    //return "true" if has back navigation variants else false
//    override fun onBackClicked() {
//        when {
//            lastDefaultNode?.childComponents()?.isNotEmpty() == true -> backChild()
//            lastDefaultNode?.parent != null -> backComponent()
//            else -> backComponent()
//        }
//    }
//
//    override fun lastComponentKey(): String? = mainComponent.value?.key
//
//    override fun backComponent() {//backComponent call backToComponent?
//        navBus.proceed { updateNavState ->
//            val nodesTree = tree.value
//            tree.value.lastOrNull()?.let {
//                updateAnimState(it.rootComponent, checkComponent = false) {
//                    val lastIndex = tree.value.lastIndex
//                    val newKey =
//                        tree.value.getOrNull(lastIndex - 1)?.rootComponent?.key.orEmpty()
//                    val oldKey = it.rootComponent.key
//                    println("checkComponentTypeDelayed start ${newKey} // ${oldKey}")
//                    val executingAnimState = AnimState.Executing(
//                        direction = AnimDirection.BACK,
//                        newKey = newKey,
//                        oldKey = oldKey
//                    )
//                    updateNavState(executingAnimState)
//                    println("checkComponentTypeDelayed end")
//                }
//                withNavigationNodeFetching {
//                    it.childComponents().forEach { childComponent ->
//                        destroyChildRouters(childComponent.key)
//                        childComponent.onDestroy()
//                        keyManager.remove(childComponent.key)
//                    }
//                    it.compositions().forEach { entry ->
//                        destroyChildRouters(entry.key)
//                        entry.value.onDestroy()
//                        keyManager.remove(entry.key)
//                    }
//                    destroyChildRouters(it.rootComponent.key)
//                    it.rootComponent.onDestroy()
//                    keyManager.remove(it.rootComponent.key)
//                    tree.value = nodesTree.dropLast(1)//drop and after call all callbacks?
//                }
//            }
//        }
//    }
//
//    override fun backToComponent(key: String) {
//        navBus.proceed { updateNavState ->
//            withNavigationNodeFetching {
//                val droppedNodes = dropNodeUntilFoundKey(lastDefaultNode, key)
////                if (droppedNodes.firstOrNull()?.rootComponent?.descriptor is Descriptor.Main.RetainUi) {
////                    val lastIndex = tree.value.lastIndex
////                    val executingAnimState = AnimState.Executing(
////                        direction = AnimDirection.BACK,
////                        newIndex = lastIndex,
////                        oldIndex = lastIndex + droppedNodes.size
////                    )
////                    updateNavState(executingAnimState)
////                }
//                droppedNodes.forEach {
//                    it.childComponents().forEach { childComponent ->
//                        destroyChildRouters(childComponent.key)
//                        childComponent.onDestroy()
//                        keyManager.remove(childComponent.key)
//                    }
//                    it.compositions().forEach { entry ->
//                        destroyChildRouters(entry.key)
//                        entry.value.onDestroy()
//                        keyManager.remove(entry.key)
//                    }
//                    destroyChildRouters(it.rootComponent.key)
//                    it.rootComponent.onDestroy()
//                    keyManager.remove(it.rootComponent.key)
//                }
//            }
//        }
//    }
//
//    override fun replaceComponent(component: Component<*>) =
//        replaceComponentFromNode(component, null)
//
//    override fun <A> replaceComponent(component: Component<*>, argument: A) =
//        replaceComponentFromNode(component, argument)
//
//    override fun addComponent(component: Component<*>) = addComponentNode(component, null)
//
//    override fun <A> addComponent(component: Component<*>, argument: A) =
//        addComponentNode(component, argument)
//
//    override fun newRootComponent(component: Component<*>) = newRootComponentNode(component, null)
//
//    override fun <A> newRootComponent(component: Component<*>, argument: A) =
//        newRootComponentNode(component, argument)
//
//    override fun lastChildKey(): String? = childComponents.value.lastOrNull()?.key
//
//    override fun backChild() {
//        navBus.proceed {
//            withNavigationNodeFetching {
//                lastDefaultNode?.run {
//                    if (childComponents().isNotEmpty()) {
//                        val component = childComponents().last()
//                        dropLastChildComponent()
//                        keyManager.remove(component.key)
//                        destroyChildRouters(component.key)
//                        component.onDestroy()
//                    }
//                }
//            }
//        }
//    }
//
//    override fun backToChild(key: String) {
//        navBus.proceed {
//            withNavigationNodeFetching {
//                lastDefaultNode?.run {
//                    val droppedChildList = dropChildUntilFoundKey(this, key)
//                    droppedChildList.forEach {
//                        keyManager.remove(it.key)
//                        destroyChildRouters(it.key)
//                        it.onDestroy()
//                    }
//                }
//            }
//        }
//    }
//
//    override fun addChild(component: Component<*>) = addChildNode(component, null)
//
//    override fun <A> addChild(component: Component<*>, argument: A) =
//        addChildNode(component, argument)
//
//    override fun replaceChild(component: Component<*>) = replaceChildNode(component, null)
//
//    override fun <A> replaceChild(component: Component<*>, argument: A) =
//        replaceChildNode(component, argument)
//
//    override fun attachCompositeComponent(component: Component<*>) =
//        attachCompositeComponentToNode(component, null)
//
//    override fun <A> attachCompositeComponent(component: Component<*>, argument: A) =
//        attachCompositeComponentToNode(component, argument)
//
//    override fun detachCompositeComponent(key: String) {
//        navBus.proceed {
//            withNavigationNodeFetching {
//                lastDefaultNode?.run {
//                    keyManager.remove(key)
//                    val composition = this.compositions()[key]
//                    if (composition != null) {
//                        composition.onDestroy()
//                        this.removeComposition(key)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun <A> attachCompositeComponentToNode(component: Component<*>, argument: A?) {
//        checkComponentType(component, ComponentType.COMPOSITE)
//        navBus.proceed {
//            withNavigationNodeFetching {
//                lastDefaultNode?.run {
//                    val isSuccess = keyManager.add(component.key)
//                    if (isSuccess) {
//                        component.onCreate(argument)
//                        lastDefaultNode?.addComposition(component)
//                    }
//                }
//            }
//        }
//    }
//
//    override suspend fun <A> redirectArgument(
//        from: ArgumentTranslator,
//        componentKey: String,
//        argument: A
//    ): Boolean {
//        val isEmitted = emitArguments(componentKey, argument)
//        if (isEmitted) return true
//        childRouters.value.forEach {
//            val isEmittedChildRouter = it.second.redirectArgument(this, componentKey, argument)
//            if (isEmittedChildRouter) return true
//        }
//        if (parentRouter !== from) {
//            val isEmittedParentRouter = parentRouter?.redirectArgument(this, componentKey, argument)
//            if (isEmittedParentRouter == true) return true
//        }
//        return false
//    }
//
//    private suspend fun <A> emitArguments(componentKey: String, argument: A): Boolean {
//        overlay.value?.let { overlayComponent ->
//            if (overlayComponent.key == componentKey) {
//                val dataContainer = DataContainer(argument)
//                overlayComponent.channel.emit(dataContainer)
//                return true
//            }
//        }
//
//        tree.value.forEach { node ->
//            node.childComponents().forEach {
//                if (it.key == componentKey) {
//                    val dataContainer = DataContainer(argument)
//                    it.channel.emit(dataContainer)
//                    return true
//                }
//            }
//
//            node.compositions().forEach { entry ->
//                if (entry.key == componentKey) {
//                    val dataContainer = DataContainer(argument)
//                    entry.value.channel.emit(dataContainer)
//                    return true
//                }
//            }
//
//            if (node.rootComponent.key == componentKey) {
//                val dataContainer = DataContainer(argument)
//                node.rootComponent.channel.emit(dataContainer)
//                return true
//            }
//        }
//        return false
//    }
//
//    private fun <A> addChildNode(component: Component<*>, argument: A?) {
////        checkComponentType(component, ComponentType.CHILD)
////        navBus.proceed {
////            withNavigationNodeFetching {
////                lastDefaultNode?.run {
////                    val isSuccess = keyManager.add(component.key)
////                    if (isSuccess) {
////                        component.onCreate(argument)
////                        addChildComponent(component)
////                    }
////                }
////            }
////        }
//    }
//
//    private fun <A> replaceChildNode(component: Component<*>, argument: A?) {
////        checkComponentType(component, ComponentType.CHILD)
////        navBus.proceed {
////            withNavigationNodeFetching {
////                lastDefaultNode?.run {
////                    if (childComponents().isNotEmpty()) {
////                        val isSuccess =
////                            keyManager.replaceKey(childComponents().last().key, component.key)
////                        if (isSuccess) {
////                            val oldChildComponent = childComponents()[childComponents().size - 1]
////                            destroyChildRouters(oldChildComponent.key)
////                            oldChildComponent.onDestroy()
////                            component.onCreate(argument)
////                            replaceLastChildComponent(component)
////                        }
////                    }
////                }
////            }
////        }
//    }
//
//    private fun <A> newRootComponentNode(component: Component<*>, argument: A?) {
////        navBus.proceed {
////            cleanGraph()
////        }
////        addComponent(component, argument)
//    }
//
//    private fun <A> addComponentNode(component: Component<*>, argument: A?) {
////        checkComponentType(component, ComponentType.MAIN)
////        navBus.proceed { updateNavState ->
////            withNavigationNodeFetching {
////                val isSuccess = keyManager.add(component.key)
////                if (isSuccess) {
////                    component.onCreate(argument)
////                    val node = Node(
////                        rootComponent = component,
////                        parent = lastDefaultNode
////                    )
////                    tree.value = tree.value
////                        .toMutableList()
////                        .apply { add(node) }
////                    println("tree added=${tree.value?.lastOrNull()?.rootComponent?.key}")
////                }
////            }
////            updateAnimState(component, checkComponent = false) {
////                val lastIndex = tree.value.lastIndex
////                val newKey = component.key
////                val oldKey = tree.value.getOrNull(lastIndex - 1)?.rootComponent?.key.orEmpty()
////                val executingAnimState = AnimState.Executing(
////                    direction = AnimDirection.FORWARD,
////                    newKey = newKey,
////                    oldKey = oldKey
////                )
////                updateNavState(executingAnimState)
////            }
////        }
//    }
//
//    private fun <A> replaceComponentFromNode(component: Component<*>, argument: A?) {
////        checkComponentType(component, ComponentType.MAIN)
////        navBus.proceed { updateNavState ->
////            withNavigationNodeFetching {
////                lastNode?.let { node ->
////                    val isSuccess = keyManager.replaceKey(node.rootComponent.key, component.key)
////                    if (isSuccess) {
////                        node.rootComponent.let { component ->
////                            destroyChildRouters(component.key)
////                            component.onDestroy()
////                        }
////                        component.onCreate(argument)
////                        tree.value = tree.value.toMutableList().apply {
////                            if (this.isNotEmpty()) {
////                                this[this.lastIndex] = last().copy(rootComponent = component)
////                            }
////                        }
////                    }
////                }
////            }
////            updateAnimState(component, true) {
////                val newKey = component.key
////                val oldKey = lastNode?.rootComponent?.key.orEmpty()
////                val executingAnimState = AnimState.Executing(
////                    direction = AnimDirection.FORWARD,
////                    newKey = newKey,
////                    oldKey = oldKey
////                )
////                updateNavState(executingAnimState)
////            }
////        }
//    }
//
//    private fun <A> setOverlayNode(component: Component<*>, argument: A?) {
////        checkComponentType(component, ComponentType.OVERLAY)
////        navBus.proceed {
////            withNavigationNodeFetching {
////                val isSuccess = keyManager.add(component.key)
////                if (isSuccess) {
////                    component.onCreate(argument)
////                    overlay.value = component
////                }
////            }
////        }
//    }
//
//    private fun dropChildUntilFoundKey(
//        node: Node,
//        componentKey: String
//    ): List<Component<*>> {
//        val droppedChildList = ArrayList<Component<*>>()
//        node.childComponents().lastOrNull()?.let {
//            if (it.key != componentKey) {
//                node.dropLastChildComponent()
//                droppedChildList.add(it)
//                val innerDroppedChildList = dropChildUntilFoundKey(node, componentKey)
//                droppedChildList.addAll(innerDroppedChildList)
//            } else return@let
//        }
//        return droppedChildList
//    }
//
//    private fun dropNodeUntilFoundKey(node: Node?, componentKey: String): List<Node> {
//        val droppedNodes = ArrayList<Node>()
//        if (node != null && node.rootComponent.key != componentKey) {
//            droppedNodes.add(node)
//            val modifiedTree = tree.value.toMutableList().apply {
//                removeLastOrNull()
//            }
//            tree.value = modifiedTree
//            val newDroppedNodes = dropNodeUntilFoundKey(node.parent, componentKey)
//            droppedNodes.addAll(newDroppedNodes)
//        }
//        return droppedNodes
//    }
//
//    private fun cleanGraph() {
//        tree.value.forEach { node ->
//            node.childComponents().forEach { childComponent ->
//                destroyChildRouters(childComponent.key)
//                childComponent.onDestroy()
//                keyManager.remove(childComponent.key)
//            }
//            destroyChildRouters(node.rootComponent.key)
//            node.rootComponent.onDestroy()
//            keyManager.remove(node.rootComponent.key)
//        }
//        tree.value = emptyList()
//
//        removeOverlay()
//    }
//
//    private fun destroyChildRouters(key: String) {
//        val childRouters = childRouters.value.toMutableList()
//        childRouters.removeAll {
//            if (it.first == key) {
//                (it.second as TreeRouterImpl).run {
//                    cleanGraph()
//                    removeOverlay()
//                }
//                true
//            } else false
//        }
//        this.childRouters.value = childRouters
//    }
//
//    private suspend fun updateAnimState(
//        component: Component<*>,
//        checkComponent: Boolean,
//        action: suspend () -> Unit
//    ) {
//        val delayedAction = suspend {
//            delay(50)//wait while bus subscribe in launchedEffect inside FrameAnimator
//            action.invoke()
//        }
//        if (checkComponent) {//really needed? check todo
//            if (component.descriptor is Descriptor.Main.RetainUi) {
//                delayedAction()
//            }
//        } else {
//            delayedAction()
//        }
//    }
//
//    private fun checkComponentType(component: Component<*>, requiredComponentType: ComponentType) {
//        val componentType = component.descriptor.componentType
//        require(componentType == requiredComponentType) {
//            "Invalid component type $componentType"
//        }
//    }
//
//    private inline fun withNavigationNodeFetching(navigateAction: () -> Unit) {
//        navigateAction.invoke()
//        topComponentKey.value = tree.value.lastOrNull()?.rootComponent?.key
//        isRouterEmpty.value = tree.value.lastOrNull() == null
//        mainComponent.value = lastDefaultNode?.rootComponent
//        childComponents.value = lastDefaultNode?.childComponents() ?: persistentListOf()
//        compositions.value = lastDefaultNode?.compositions() ?: persistentMapOf()
//        keepAliveNodes.value = tree.value
//            .filter { it.rootComponent.descriptor is Descriptor.Main.RetainUi }
//            .map {
//                KeepAliveNode(
//                    it.rootComponent,
//                    it.childComponents(),
//                    it.compositions()
//                )
//            }.toImmutableList()
//    }
//}