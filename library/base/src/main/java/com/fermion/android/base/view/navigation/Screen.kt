package com.fermion.android.base.view.navigation

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.fermion.android.base.R

class Screen(
    val routeName: String,
    @StringRes val title: Int = R.string.sample,
    val navIcon: (@Composable () -> Unit) = {
        Icon(
            Icons.Filled.Home, contentDescription = "home"
        )
    },
    val objectName: String = "",
    val objectPath: String = "",
    val view: @Composable (NavHostController, Context, Bundle?, Any?) -> Unit
)