package com.alphicc.brick

interface ArgumentTranslator {
     @Deprecated("Will be removed soon. Should not be used")
     suspend fun <A> redirectArgument(from: ArgumentTranslator, componentKey: String, argument: A): Boolean
}