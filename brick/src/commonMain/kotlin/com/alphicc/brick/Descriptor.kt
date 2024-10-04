package com.alphicc.brick

sealed class Descriptor(
    val componentType: ComponentType
) {

    sealed class Screen(val retain: Boolean) : Descriptor(ComponentType.MAIN) {
        sealed class Animated : Screen(retain = true) {

            data object SwipeBack : Animated()

            data object CrossFade : Animated()
        }

        data object Default : Screen(retain = false)
    }

    data object Child : Descriptor(ComponentType.CHILD)
    data object Composite : Descriptor(ComponentType.COMPOSITE)
    data object Overlay : Descriptor(ComponentType.OVERLAY)
}

enum class ComponentType {
    MAIN, CHILD, COMPOSITE, OVERLAY
}
