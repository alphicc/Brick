package com.navigationtestapp.core

interface ArgumentTranslator {
    suspend fun <A> redirectArgument(from: ArgumentTranslator, screenKey: String, argument: A)
}