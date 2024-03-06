package com.alphicc.brick

interface ArgumentTranslator {
     @InternalApi
     suspend fun <A> redirectArgument(from: ArgumentTranslator, componentKey: String, argument: A): Boolean
}