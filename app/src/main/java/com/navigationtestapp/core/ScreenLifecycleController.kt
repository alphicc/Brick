package com.navigationtestapp.core

import com.navigationtestapp.DataProvider
import com.navigationtestapp.Screen

class ScreenLifecycleController(private val screen: Screen<*>) {

    fun <A> onCreate(initialArgument: A): Screen<*> {
        val argumentDataProvider = DataProvider(initialArgument)
        val dependency = screen.onCreate?.invoke(screen.channel, argumentDataProvider)
        val dependencyDataProvider = DataProvider(dependency)
        screen.dependency = dependencyDataProvider
        return screen
    }

    fun onDestroy() {
        screen.onDestroy?.invoke()
    }
}