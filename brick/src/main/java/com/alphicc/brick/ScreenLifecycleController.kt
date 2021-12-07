package com.alphicc.brick

internal class ScreenLifecycleController(private val screen: Screen<*>) {

    fun <A> onCreate(initialArgument: A): Screen<*> {
        val argumentDataContainer = DataContainer(initialArgument)
        val dependency = screen.onCreate?.invoke(screen.channel, argumentDataContainer)
        val dependencyDataContainer = DataContainer(dependency)
        screen.dependency = dependencyDataContainer
        return screen
    }

    fun onDestroy() {
        screen.onDestroy?.invoke(screen.dependency ?: DataContainer(null))
    }
}