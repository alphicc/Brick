package com.alphicc.brick

sealed class Descriptor(
    val componentType: ComponentType
) {

    sealed class Main(val retain: Boolean) : Descriptor(ComponentType.MAIN) {

        sealed class RetainUi(val navigateAnimation: NavigateAnimation) : Main(true) {

            data object SwipeBack : RetainUi(NavigateAnimation.SWIPE_BACK)

            data object SwipeDown : RetainUi(NavigateAnimation.SWIPE_DOWN)

            data object CrossFade : RetainUi(NavigateAnimation.CROSS_FADE)
        }

        data object Default: Main(false)
    }

    data object Child : Descriptor(ComponentType.CHILD)
    data object Composite : Descriptor(ComponentType.COMPOSITE)
    data object Overlay : Descriptor(ComponentType.OVERLAY)
}

enum class ComponentType {
    MAIN, CHILD, COMPOSITE, OVERLAY
}

enum class NavigateAnimation {
    SWIPE_BACK, SWIPE_DOWN, CROSS_FADE
}