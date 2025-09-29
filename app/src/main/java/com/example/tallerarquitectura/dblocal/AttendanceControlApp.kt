package com.example.tallerarquitectura.dblocal

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.ui.theme.TallerArquitecturaTheme
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.ModalDrawerApp
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.navigation.AppNavGraph
import com.example.tallerarquitectura.view.navigation.MateriaRoute
import kotlinx.coroutines.launch

@Composable
fun AttendanceControlApp(controllerProvider: ControllerProvider,uiProvider: UiProvider) {

    val navBackStackEntry = MainActivity.Companion.navManager.getNaController().currentBackStackEntryAsState()
    val navDestinations=navBackStackEntry.value?.destination?.hierarchy
    val coroutineScope= rememberCoroutineScope()

    TallerArquitecturaTheme {
        ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerApp(
                navDestinations=navDestinations,
                onNavigateToRoute = {
                    MainActivity.Companion.navManager.navigateTo(it)
                },
                onCloseDrawer = {
                    coroutineScope.launch {
                        uiProvider.getUiAppViewModel().getDrawerState().apply {
                            if (isOpen) {
                                close()
                            } else {
                                open()
                            }
                        }
                    }
                }
                )
        },
            gesturesEnabled = true,
            drawerState = uiProvider.getUiAppViewModel().getDrawerState()
        ) {
            AppNavGraph(
                startDestination = MateriaRoute,
                navController = MainActivity.Companion.navManager.getNaController(),
                controllerProvider = controllerProvider
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ShowInQueueApp() {
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    val controllerProvider= ControllerProvider()
    AttendanceControlApp(controllerProvider,uiProvider)
}