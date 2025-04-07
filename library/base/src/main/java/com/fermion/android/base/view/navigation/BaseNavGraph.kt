package com.fermion.android.base.view.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier? = Modifier,
    startDestination: String,
    navGraphScreenList: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController,
        startDestination = startDestination,
        modifier = modifier ?: Modifier.padding(0.dp),
        builder = navGraphScreenList
    )
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}