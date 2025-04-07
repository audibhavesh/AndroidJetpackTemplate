package com.theoctacoder.cats_facts.routes

import com.fermion.android.base.view.navigation.Screen
import com.theoctacoder.cats_facts.ui.facts.FactsScreen
import com.theoctacoder.cats_facts.ui.home.HomeScreen

val HomeScreenRoute = Screen("Home", view = { navHostController, context, bundle, extra ->
    HomeScreen().Init(navHostController, context, bundle, extra)
})
val FactsScreenRoute = Screen("Facts", view = { navHostController, context, bundle, extra ->
    FactsScreen().Init(navHostController, context, bundle, extra)
})


var routes = listOf(HomeScreenRoute, FactsScreenRoute)
