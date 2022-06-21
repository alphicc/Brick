package com.alphicc.brick

interface ArgumentTranslator {
     suspend fun <A> redirectArgument(from: ArgumentTranslator, screenKey: String, argument: A)
}