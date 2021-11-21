package com.navigationtestapp

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

private typealias OnCreateMethod<T, D> = (T) -> D

val a = {

}

private typealias OnDestroyMethod = () -> Unit

//abstract class ScreenAbs<A, D> {
//
//    abstract val key: String
//
//    abstract val content: @Composable (D) -> Unit
//
//    abstract fun onCreate(argument: A): D
//
//    open fun onDestroy()
//}

//val t = Screen(
//    onCreate = {
//        val a = it.get()
//        return@Screen 1
//    },
//    content = {
//
//    }
//)

open class Screen<D>(
    override val key: String,
    val content: @Composable (DependencyProvider) -> Unit,
    val onCreate: ((SharedFlow<ChannelArgument>) -> D)? = null,
    val onDestroy: (() -> Unit)? = null
) : BaseScreen(key) {

  // override fun onCreate(channel: SharedFlow<ChannelArgument>): D? =
  //     onCreate?.invoke(channel) ?: super.onCreate(channel)

  // override fun onDestroy() = onDestroy?.invoke() ?: super.onDestroy()
}
//open class ScreenT<D : Dependency>(val key: String, val content: @Composable (D) -> Unit) :
//    BaseScreen<D>()

open class UnitDependency : Any() {

    companion object {
        val empty: UnitDependency = UnitDependency()
    }
}

open class BaseScreen(
    internal open val key: String,
    internal val channel: MutableStateFlow<ChannelArgument> = MutableStateFlow(ChannelArgument()),
    internal var dependency: DependencyProvider? = null,
) {
   // open fun onCreate(channel: SharedFlow<ChannelArgument>): D? = null
//
   // open fun onDestroy() {}
}

open class ChannelArgument {
    override fun equals(other: Any?): Boolean = false
    override fun hashCode(): Int = javaClass.hashCode()
}

class DependencyProvider(val rawData: Any?) {

    inline fun <reified C> get(): C {
        if (rawData is C) return rawData
        else throw IllegalArgumentException("Argument type incorrect")
    }
}

//class ScreenDataDescriptor<A, D>(
//    private val argument: A,
//    private val dependency: D
//) {
//companion object {
//    fun <A, D> default(): ScreenDataDescriptor<A, D> =
//        ScreenDataDescriptor(Argument(A), Dependency(D))
//}
//}
//class ScreenData<A, D>(
//    val argument: ArgumentType<A>,
//    val dependency: Dependency<D>
//)

//class ArgumentContainer<A>(val data: A)
//
//class Argument<A>(private val argument: A) : () -> A {
//    override fun invoke(): A = argument
//}
//
//class Dependency<D>(private val dependency: D) : () -> D {
//    override fun invoke(): D = dependency
//}

//class ArgumentDescriptor<A>
//
//fun test() {
//    val a = ArgumentDescriptor<Unit>()
//}
//open class DataContainer<A, D> {
//    open fun provideDependencies(arguments: A): D {
//
//    }
//}