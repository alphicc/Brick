package com.alphicc.brick.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.alphicc.brick.Descriptor
import com.alphicc.brick.KeepAliveNode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

@Composable
fun RootAnimator(keepAliveNodes: ImmutableList<KeepAliveNode>) {

    val bus = remember { MutableSharedFlow<UpdateActions>(extraBufferCapacity = 3) }

    keepAliveNodes.forEachIndexed { index, keepAliveNode ->
        val communicator = remember {
            val filteredBus = bus.filter { it.index == index }
            GestureCommunicator(filteredBus) { updateAction ->
                bus.emit(updateAction)
            }
        }
        if (keepAliveNode.mainComponent.descriptor is Descriptor.Main.RetainUi) {
            FrameAnimator(keepAliveNode.mainComponent.descriptor.navigateAnimation, communicator) {
                keepAliveNode.mainComponent.run {
                    showContent(
                        dataContainer = dependency
                            ?: throw IllegalArgumentException("Dependency can not be null"),
                        compositions = keepAliveNode.compositions
                    )
                }
                keepAliveNode.childComponentsList.forEach { childScreen ->
                    childScreen.showContent(
                        dataContainer = childScreen.dependency
                            ?: throw IllegalArgumentException("Dependency can not be null"),
                        compositions = keepAliveNode.compositions
                    )
                }
            }
        } else throw IllegalStateException("Main component must be Descriptor.Main.RetainUi")
    }
}