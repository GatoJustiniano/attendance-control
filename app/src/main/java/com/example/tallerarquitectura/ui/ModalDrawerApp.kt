package com.example.tallerarquitectura.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.tallerarquitectura.view.navigation.ModuleApp
import com.example.tallerarquitectura.view.navigation.ReminderNoteRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerApp(
    onNavigateToRoute:(Any) -> Unit,
    navDestinations: Sequence<NavDestination>?,
    onCloseDrawer:() -> Unit,
) {

    ModalDrawerSheet(
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Arquitectura",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            Text(
                "Registro",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            ModuleApp.register.forEach {register->
                Log.d("TAG",register.route.toString())
                NavigationDrawerItem(
                    label = { Text(register.name) },
                    selected = navDestinations?.any { it.hasRoute(register.route::class) }==true,
                    icon =  { Icon(painter = painterResource(register.icon), contentDescription = null) },
                    onClick = {
                        onNavigateToRoute(register.route)
                        onCloseDrawer()
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                "Servicios",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            NavigationDrawerItem(
                label = { Text("Nota de servicios") },
                selected = false,
                icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = {
                    onNavigateToRoute(ServiceNoteRoute)
                    onCloseDrawer()
                }
            )
            NavigationDrawerItem(
                label = { Text("Recordatorios") },
                selected = false,
                icon = { Icon(Icons.Outlined.Notifications, contentDescription = null) },
                onClick = {
                    onNavigateToRoute(ReminderNoteRoute)
                    onCloseDrawer()
                }
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ShowDrawerApp() {
    ModalDrawerApp({}, null,{})
}