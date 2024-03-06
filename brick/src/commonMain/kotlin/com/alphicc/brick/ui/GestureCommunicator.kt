package com.alphicc.brick.ui

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

@Immutable
data class GestureCommunicator(
    val bus: Flow<UpdateActions>,
    val publish: suspend (UpdateActions) -> Unit
)