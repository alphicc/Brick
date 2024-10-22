package io.github.alphicc.android.stackSample.profile

import com.alphicc.brick.TreeRouter

data class ProfileScreenDependencies(
    val profileRouter: TreeRouter,
    val login: String
)