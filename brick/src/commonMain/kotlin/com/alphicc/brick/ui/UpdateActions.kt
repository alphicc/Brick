package com.alphicc.brick.ui

import com.alphicc.brick.Descriptor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class UpdateActions(open val key: String) {

    sealed class Animations(key: String) : UpdateActions(key) {

        sealed class SwipeBack(key: String) : Animations(key) {

        }

        data class CrossFade(override val key: String, val alpha: Float) : Animations(key) {

            companion object {
                suspend fun getShowActions(newKey: String): List<CrossFade> =
                    withContext(Dispatchers.Default) {
                        val actions = mutableListOf<CrossFade>()
                        var initialValue = 0f
                        while (initialValue < 1) {
                            initialValue += 0.05f
                            actions.add(CrossFade(newKey, initialValue))
                        }
                        return@withContext actions
                    }

                suspend fun getHideActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>
                ): List<CrossFade> =
                    withContext(Dispatchers.Default) {
                        val actions = mutableListOf<CrossFade>()
                        actions.add(CrossFade(newKey, 1f))
                        oldKeys.forEachIndexed { index, s ->
                            if (index != 0) actions.add(CrossFade(s, 0f))
                        }
                        var initialValue = 1f
                        while (initialValue > 0) {
                            initialValue -= 0.05f
                            actions.add(CrossFade(oldKeys.firstOrNull().orEmpty(), initialValue))
                        }
                        return@withContext actions
                    }

                suspend fun getCrossFadeActions(newKey: String, oldKey: String): List<CrossFade> =
                    withContext(Dispatchers.Default) {
                        val actions = mutableListOf<CrossFade>()
                        var initialValue = 0f
                        while (initialValue < 1) {
                            initialValue += 0.05f
                            actions.add(CrossFade(newKey, initialValue))
                            actions.add(CrossFade(oldKey, 1 - initialValue))
                        }
                        return@withContext actions
                    }
            }
        }
    }

    sealed class Triggers(
        newKey: String,
    ) : UpdateActions(newKey) {

        abstract suspend fun getActionsByTrigger(descriptor: Descriptor.Main.RetainUi): List<Animations>

        data class ForwardAction(
            val newKey: String,
            val oldKey: ImmutableList<String>
        ) : Triggers(newKey) {

            override suspend fun getActionsByTrigger(descriptor: Descriptor.Main.RetainUi): List<Animations> =

                when (descriptor) {
                    is Descriptor.Main.RetainUi.CrossFade -> {
                        println("getActionsByTrigger FORWARD")
                        Animations.CrossFade.getShowActions(newKey)
                    }

                    is Descriptor.Main.RetainUi.SwipeBack -> emptyList()
                    is Descriptor.Main.RetainUi.SwipeDown -> emptyList()
                }
        }

        data class BackAction(
            val newKey: String,
            val oldKey: ImmutableList<String>
        ) : Triggers(newKey) {

            override suspend fun getActionsByTrigger(descriptor: Descriptor.Main.RetainUi): List<Animations> {
                println("getActionsByTrigger BACK")
                return Animations.CrossFade.getHideActions(newKey, oldKey)
            }
        }
    }

    data object None : UpdateActions("")
}