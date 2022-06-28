package com.alphicc.brick

interface ArgumentTranslator {
     suspend fun <A> redirectArgument(from: ArgumentTranslator, componentKey: String, argument: A)
}