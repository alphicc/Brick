package com.alphicc.brick

sealed class UpdateRouterActions {
    object OnBackClicked : UpdateRouterActions()
    object BackComponent : UpdateRouterActions()
    object CleanRouter : UpdateRouterActions()
    object RemoveOverlay : UpdateRouterActions()
    object BackChild : UpdateRouterActions()
    data class BackToChild(val key: String) : UpdateRouterActions()
    data class AttachCompositeComponent<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()
    data class DetachCompositeComponent(val key: String) : UpdateRouterActions()
    data class SetOverlay<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()
    data class BackToComponent(val key: String) : UpdateRouterActions()
    data class ReplaceComponent<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()
    data class AddComponent<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()
    data class NewRootComponent<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()
    data class ReplaceChild<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()
    data class AddChild<A>(val component: Component<*>, val argument: A?) : UpdateRouterActions()

}
