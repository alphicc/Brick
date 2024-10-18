package com.alphicc.brick.ui

import com.alphicc.brick.Descriptor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class UpdateActions(open val keys: ImmutableList<String>) {

    sealed class Animations(key: String) : UpdateActions(persistentListOf(key)) {
        sealed class Shift(
            open val key: String,
            open val percent: Float,
            open val finish: Boolean,
        ) : Animations(key) {

            data class ShiftScene(
                override val key: String,
                override val percent: Float,
                override val finish: Boolean,
            ) : Shift(key, percent, finish) {

                companion object {
                    suspend fun getActionsByVelocity(
                        key: String,
                        belowKey: String,
                        currentOffsetX: Float,
                        velocity: Float,
                        screenWidth: Float,
                        duration: Long,
                        frameRate: Int
                    ): List<Shift> = withContext(Dispatchers.Default) {
                        val actions = mutableListOf<Shift>()
                        var offsetX = currentOffsetX
                        actions.add(ShiftScene(key, offsetX / screenWidth, false))

                        actions.add(
                            ShiftBackdrop.getBackdropAction(
                                belowKey,
                                offsetX,
                                screenWidth
                            )
                        )
                        val stepX = (screenWidth - offsetX) / duration * frameRate
                        if (velocity > 0) {
                            while (offsetX < screenWidth) {
                                offsetX += stepX
                                val percent = (offsetX / screenWidth).coerceIn(0f, 1f)
                                actions.add(ShiftScene(key, percent, percent == 1f))
                                actions.add(
                                    ShiftBackdrop.getBackdropAction(
                                        belowKey,
                                        offsetX.coerceIn(0f, screenWidth),
                                        screenWidth
                                    )
                                )
                            }
                        } else {
                            while (offsetX > 0) {
                                offsetX -= stepX
                                val percent = (offsetX / screenWidth).coerceIn(0f, 1f)
                                actions.add(ShiftScene(key, percent, false))
                                actions.add(
                                    ShiftBackdrop.getBackdropAction(
                                        belowKey,
                                        offsetX.coerceIn(0f, screenWidth),
                                        screenWidth
                                    )
                                )
                            }
                        }
                        return@withContext actions
                    }
                }
            }

            data class ShiftBackdrop(
                override val key: String,
                override val percent: Float,
            ) : Shift(key, percent, false) {
                companion object {
                    fun getBackdropAction(
                        key: String,
                        currentOffsetX: Float,
                        screenWidth: Float,
                    ): ShiftBackdrop {
                        val percent = 1 - (currentOffsetX / screenWidth)
                        return ShiftBackdrop(key, percent)
                    }
                }
            }
        }

        data class SwipeBack(
            val key: String,
            val offsetX: Float,
            val alpha: Float
        ) : Animations(key) {
            companion object {

                const val SHIFT_THRESHOLD: Float = -.3f

                suspend fun getShowActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>,
                    duration: Long,
                    frameRate: Int
                ): List<SwipeBack> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<SwipeBack>()
                    var offsetX = 1f
                    actions.add(SwipeBack(newKey, offsetX, 0f))
                    actions.add(SwipeBack(newKey, offsetX, 1f))
                    val step = 1f / (duration / frameRate)
                    while (offsetX > 0) {
                        offsetX -= step
                        if (offsetX < 0) {
                            offsetX = 0f
                        }
                        actions.add(SwipeBack(newKey, offsetX, 1f))
                        oldKeys.lastOrNull()?.let { key ->
                            actions.add(SwipeBack(key, (1 - offsetX) * SHIFT_THRESHOLD, 1f))
                        }
                    }
                    return@withContext actions
                }

                suspend fun getHideActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>,
                    duration: Long,
                    frameRate: Int
                ): List<SwipeBack> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<SwipeBack>()
                    val offsetStep = 1f / (duration / frameRate)
                    oldKeys.forEach { oldKey ->
                        var offsetX = 0f
                        actions.add(SwipeBack(oldKey, offsetX, 1f))
                        while (offsetX < 1) {
                            offsetX += offsetStep
                            if (offsetX > 1) {
                                offsetX = 1f
                            }
                            actions.add(SwipeBack(oldKey, offsetX, 1f))
                        }
                    }
                    var offsetX = 0f
                    while (offsetX < 1) {
                        offsetX += offsetStep
                        if (offsetX > 1) {
                            offsetX = 1f
                        }
                        actions.add(SwipeBack(newKey, (1 - offsetX) * SHIFT_THRESHOLD, 1f))
                    }
                    return@withContext actions
                }
            }
        }

        data class CrossFade(val key: String, val alpha: Float) : Animations(key) {

            companion object {
                suspend fun getShowActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>,
                    duration: Long,
                    frameRate: Int,
                ): List<CrossFade> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<CrossFade>()
                    oldKeys.forEach { key -> actions.add(CrossFade(key, 1f)) }
                    var alpha = 0f
                    val step = 1f / (duration / frameRate)
                    while (alpha < 1) {
                        alpha += step
                        actions.add(CrossFade(newKey, alpha))
                    }
                    return@withContext actions
                }

                suspend fun getHideActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>,
                    duration: Long,
                    frameRate: Int
                ): List<CrossFade> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<CrossFade>()
                    actions.add(CrossFade(newKey, 1f))
                    oldKeys.forEachIndexed { index, s ->
                        if (index != 0) actions.add(CrossFade(s, 0f))
                    }
                    var alpha = 1f
                    val step = 1f / (duration / frameRate)
                    while (alpha > 0) {
                        alpha -= step
                        actions.add(CrossFade(oldKeys.firstOrNull().orEmpty(), alpha))
                    }
                    return@withContext actions
                }

                fun getShowAction(newKey: String): List<CrossFade> = listOf(CrossFade(newKey, 1f))
            }
        }
    }

    sealed class Triggers(
        keys: ImmutableList<String>
    ) : UpdateActions(keys) {

        abstract suspend fun getActionsByTrigger(
            descriptor: Descriptor.Screen,
            frameRate: Int
        ): List<Animations>

        data class ForwardAction(
            val newKey: String,
            val oldKey: ImmutableList<String>
        ) : Triggers(persistentListOf(newKey)) {

            override suspend fun getActionsByTrigger(
                descriptor: Descriptor.Screen,
                frameRate: Int
            ): List<Animations> =
                when (descriptor) {
                    is Descriptor.Screen.Animated.CrossFade -> {
                        Animations.CrossFade.getShowActions(
                            newKey,
                            oldKey,
                            descriptor.duration,
                            frameRate
                        )
                    }

                    is Descriptor.Screen.Animated.SwipeBack -> {
                        Animations.SwipeBack.getShowActions(
                            newKey,
                            oldKey,
                            descriptor.duration,
                            frameRate
                        )
                    }

                    is Descriptor.Screen.Default -> {
                        Animations.CrossFade.getShowAction(newKey)
                    }
                }
        }

        data class BackAction(
            val newKey: String,
            val oldKey: ImmutableList<String>
        ) : Triggers(oldKey) {

            override suspend fun getActionsByTrigger(
                descriptor: Descriptor.Screen,
                frameRate: Int
            ): List<Animations> =
                when (descriptor) {
                    is Descriptor.Screen.Animated.CrossFade -> {
                        Animations.CrossFade.getHideActions(
                            newKey,
                            oldKey,
                            descriptor.duration,
                            frameRate
                        )
                    }

                    is Descriptor.Screen.Animated.SwipeBack -> {
                        Animations.SwipeBack.getHideActions(
                            newKey,
                            oldKey,
                            descriptor.duration,
                            frameRate
                        )
                    }

                    is Descriptor.Screen.Default -> {
                        Animations.CrossFade.getShowAction(newKey)
                    }
                }
        }
    }

    data object None : UpdateActions(persistentListOf())
}