package com.alphicc.brick

data class TransactionSettings(
    val subscribeDelay: Long,
    val animDurationDelay: Long
) {
    companion object {
        private const val EMPTY_DELAY = 0L
        private const val SUBSCRIBE_NODE_DELAY = 50L
        private const val DEFAULT_ANIM_DURATION = 300L
        fun default() = TransactionSettings(
            subscribeDelay = SUBSCRIBE_NODE_DELAY,
            animDurationDelay = DEFAULT_ANIM_DURATION
        )

        fun immediately() = TransactionSettings(
            subscribeDelay = EMPTY_DELAY,
            animDurationDelay = EMPTY_DELAY
        )
    }
}