package com.example.tallerarquitectura.view.navigation

import androidx.navigation.NavHostController

class NavManager(
    private val navHostController: NavHostController
) {
    fun navigateTo(route: Any) {
        navHostController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateCurrentTo(route: Any){
        navHostController.navigate(route) {
            popUpTo(route) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    fun popBackStack(){
        navHostController.popBackStack()
    }
    fun getNaController(): NavHostController {
        return navHostController
    }
}