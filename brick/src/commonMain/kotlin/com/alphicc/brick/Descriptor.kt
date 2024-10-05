package com.alphicc.brick

private const val DEFAULT_ANIM_DURATION = 300L

sealed class Descriptor(
    val componentType: ComponentType
) {

    sealed class Screen(val retain: Boolean) : Descriptor(ComponentType.MAIN) {
        sealed class Animated(open val duration: Long) : Screen(retain = true) {

            data class SwipeBack(
                override val duration: Long = DEFAULT_ANIM_DURATION
            ) : Animated(duration)

            data class CrossFade(
                override val duration: Long = DEFAULT_ANIM_DURATION
            ) : Animated(duration)
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
