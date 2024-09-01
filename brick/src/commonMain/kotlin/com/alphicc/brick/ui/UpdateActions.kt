package com.alphicc.brick.ui

import com.alphicc.brick.Descriptor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

sealed class UpdateActions(open val keys: ImmutableList<String>) {

    sealed class Animations(key: String) : UpdateActions(persistentListOf(key)) {
        sealed class Shift(
            open val key: String,
            open val percent: Float,
            open val finish: Boolean
        ) : Animations(key) {

            data class ShiftScene(
                override val key: String,
                override val percent: Float,
                override val finish: Boolean,
            ) : Shift(key, percent, finish) {

                companion object {
                    private const val ANIM_DURATION = 100

                    suspend fun getActionsByVelocity(
                        key: String,
                        backdropKey: String,
                        currentOffsetX: Float,
                        velocity: Float,
                        screenWidth: Float
                    ): List<Shift> = withContext(Dispatchers.Default) {
                        val actions = mutableListOf<Shift>()
                        var offsetX = currentOffsetX
                        actions.add(ShiftScene(key, offsetX / screenWidth, false))

                        actions.add(
                            ShiftBackdrop.getBackdropAction(
                                backdropKey,
                                offsetX,
                                screenWidth
                            )
                        )
                        val speed = velocity / 1.seconds.inWholeMilliseconds
                        val stepX = if ((speed * ANIM_DURATION) + currentOffsetX < screenWidth) {
                            (screenWidth - currentOffsetX) / ANIM_DURATION
                        } else {
                            speed
                        }
                        if (velocity > 0) {
                            while (offsetX < screenWidth) {
                                offsetX += stepX
                                val percent = (offsetX / screenWidth).coerceIn(0f, 1f)
                                actions.add(ShiftScene(key, percent, percent == 1f))
                                actions.add(
                                    ShiftBackdrop.getBackdropAction(
                                        backdropKey,
                                        offsetX.coerceIn(0f, screenWidth),
                                        screenWidth
                                    )
                                )
                            }
//                            actions.add(//!!!
//                                ShiftBackdrop.getBackdropAction(
//                                    backdropKey,
//                                    offsetX,
//                                    screenWidth
//                                )
//                            )
                        } else {
//                            println("UPDATE ACTIONS ELSE stepX=$stepX offsetX=$offsetX screenWidth=$screenWidth")
                            while (offsetX > 0) {
                                offsetX -= stepX
//                                println("UPDATE ACTIONS ELSE stepX=$stepX offsetX=$offsetX screenWidth=$screenWidth $key")
                                val percent = (offsetX / screenWidth).coerceIn(0f, 1f)
                                actions.add(ShiftScene(key, percent, false))
                                actions.add(
                                    ShiftBackdrop.getBackdropAction(
                                        backdropKey,
                                        offsetX.coerceIn(0f, screenWidth),
                                        screenWidth
                                    )
                                )
                            }
//                            actions.add(ShiftScene(key, 0f, true))
//                            actions.add(//!!!!
//                                ShiftBackdrop.getBackdropAction(
//                                    backdropKey,
//                                    offsetX,
//                                    screenWidth
//                                )
//                            )
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
                        screenWidth: Float
                    ): ShiftBackdrop {
                        val percent = 1 - (currentOffsetX / screenWidth)
                        return ShiftBackdrop(key, percent)
                    }
                }
            }
        }

        data class SwipeBack(val key: String, val offsetX: Float, val alpha: Float) :
            Animations(key) {
            companion object {

                const val SHIFT_THRESHOLD: Float = -.3f
                private const val OFFSET_STEP: Float = .008f

                suspend fun getShowActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>
                ): List<SwipeBack> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<SwipeBack>()
                    var offsetX = 1f
                    actions.add(SwipeBack(newKey, offsetX, 0f))
                    actions.add(SwipeBack(newKey, offsetX, 1f))
                    while (offsetX > 0) {
                        offsetX -= OFFSET_STEP
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
                ): List<SwipeBack> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<SwipeBack>()
                    val offsetStep = 1f / (150f / (oldKeys.size + 1))
                    println("==offsetStep=$offsetStep")
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
//                        actions.add(IdleFrame(newKey))
//                        actions.add(SwipeBack("DEBUG", offsetX, 1f))
                    }
                    var offsetX = 0f
                    while (offsetX < 1) {
                        offsetX += offsetStep
                        if (offsetX > 1) {
                            offsetX = 1f
                        }
                        actions.add(SwipeBack(newKey, (1 - offsetX) * SHIFT_THRESHOLD, 1f))
                    }
                    println("actionsSize=${actions.size}")
//                    val actions = mutableListOf<SwipeBack>()
//                    var offsetX = 0f
//                    val oldKey = oldKeys.lastOrNull()
//                    actions.add(SwipeBack(oldKey.orEmpty(), offsetX, 1f))
//                    while (offsetX < 1) {
//                        offsetX += OFFSET_STEP
//                        if (offsetX > 1) {
//                            offsetX = 1f
//                        }
//                        actions.add(SwipeBack(oldKey.orEmpty(), offsetX, 1f))
//                        actions.add(SwipeBack(newKey, (1 - offsetX) * SHIFT_THRESHOLD, 1f))
//                    }
                    return@withContext actions
                }
            }
        }

        data class CrossFade(val key: String, val alpha: Float) : Animations(key) {

            companion object {

                private const val ALPHA_STEP: Float = .05f

                suspend fun getShowActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>
                ): List<CrossFade> =
                    withContext(Dispatchers.Default) {
                        val actions = mutableListOf<CrossFade>()
                        oldKeys.forEach { key ->
                            actions.add(CrossFade(key, 1f))
                        }
                        var alpha = 0f
                        while (alpha < 1) {
                            alpha += ALPHA_STEP
                            actions.add(CrossFade(newKey, alpha))
                        }
                        return@withContext actions
                    }

                suspend fun getHideActions(
                    newKey: String,
                    oldKeys: ImmutableList<String>
                ): List<CrossFade> = withContext(Dispatchers.Default) {
                    val actions = mutableListOf<CrossFade>()
                    actions.add(CrossFade(newKey, 1f))
                    oldKeys.forEachIndexed { index, s ->
                        if (index != 0) actions.add(CrossFade(s, 0f))
                    }
                    var alpha = 1f
                    while (alpha > 0) {
                        alpha -= ALPHA_STEP
                        actions.add(CrossFade(oldKeys.firstOrNull().orEmpty(), alpha))
                    }
                    return@withContext actions
                }

                fun getShowAction(newKey: String): List<CrossFade> = listOf(CrossFade(newKey, 1f))

                suspend fun getCrossFadeActions(newKey: String, oldKey: String): List<CrossFade> =
                    withContext(Dispatchers.Default) {
                        val actions = mutableListOf<CrossFade>()
                        var initialValue = 0f
                        while (initialValue < 1) {
                            initialValue += ALPHA_STEP
                            actions.add(CrossFade(newKey, initialValue))
                            actions.add(CrossFade(oldKey, 1 - initialValue))
                        }
                        return@withContext actions
                    }
            }
        }
    }

    sealed class Triggers(
        keys: ImmutableList<String>
    ) : UpdateActions(keys) {

        abstract suspend fun getActionsByTrigger(descriptor: Descriptor.Main): List<Animations>

        data class ForwardAction(
            val newKey: String,
            val oldKey: ImmutableList<String>
        ) : Triggers(persistentListOf(newKey)) {

            override suspend fun getActionsByTrigger(descriptor: Descriptor.Main): List<Animations> =
                when (descriptor) {
                    is Descriptor.Main.Animated.CrossFade -> {
                        println("getActionsByTrigger FORWARD")
                        Animations.CrossFade.getShowActions(newKey, oldKey)
                    }

                    is Descriptor.Main.Animated.SwipeBack -> {
                        println("getActionsByTrigger SwipeBack")
                        Animations.SwipeBack.getShowActions(newKey, oldKey)
                    }

                    is Descriptor.Main.Default -> {
                        println("getActionsByTrigger Default_Forward $newKey")
                        Animations.CrossFade.getShowAction(newKey)
                    }
                }
        }

        data class BackAction(
            val newKey: String,
            val oldKey: ImmutableList<String>
        ) : Triggers(oldKey) {

            override suspend fun getActionsByTrigger(descriptor: Descriptor.Main): List<Animations> =
                when (descriptor) {
                    is Descriptor.Main.Animated.CrossFade -> {
                        println("getActionsByTrigger CrossFade")
                        Animations.CrossFade.getHideActions(newKey, oldKey)
                    }

                    is Descriptor.Main.Animated.SwipeBack -> {
                        println("getActionsByTrigger SwipeBack")
                        Animations.SwipeBack.getHideActions(newKey, oldKey)
                    }

                    is Descriptor.Main.Default -> {
                        println("getActionsByTrigger Default_Back $newKey")
                        Animations.CrossFade.getShowAction(newKey)
                    }
                }
        }
    }

    data object None : UpdateActions(persistentListOf())
}