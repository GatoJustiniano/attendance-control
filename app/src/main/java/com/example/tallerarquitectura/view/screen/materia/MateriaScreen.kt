package com.example.tallerarquitectura.view.screen.materia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.R
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionListener
import com.example.tallerarquitectura.dto.Materia
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.navigation.MateriaCreateRoute
import com.example.tallerarquitectura.view.navigation.MateriaEditRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ServiceScreen(
    materias: List<Materia> = emptyList<Materia>(),
    listener: ActionListener<Materia>,
    uiProvider: UiProvider
) {

    val coroutineScope= rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf<Materia?>(null)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Materia") },
            navigationIcon = {
                IconButton(onClick = {
                  coroutineScope.launch {
                      uiProvider.getUiAppViewModel().getDrawerState().apply {
                          if (isClosed){
                              open()
                          }else{
                              close()
                          }
                      }
                  }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu, contentDescription = "Back"
                    )
                }
            },
        )
    }, bottomBar = {
        BottomAppBar(
            actions = {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
                ) {
                    selectedItem.value?.let {
                        Row() {
                            IconButton(
                                onClick = {
                                    MainActivity.navManager.navigateTo(MateriaEditRoute(it.id))
                                }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "edit")
                            }
                            IconButton(
                                onClick = {
                                    listener.destroy(it.id)
                                }) {
                                Icon(Icons.Outlined.Delete, contentDescription = "delete")
                            }
                        }
                    }


                    FloatingActionButton(
                        onClick = {
                            MainActivity.navManager.navigateTo(MateriaCreateRoute)
                        },
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .align(Alignment.BottomEnd),
                        elevation = FloatingActionButtonDefaults.loweredElevation()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "add")
                    }
                }
            })
    }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn() {
                items(materias) {
                    Box(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {

                                },
                                onLongClick = {
                                    if (selectedItem.value == it) {
                                        selectedItem.value = null
                                    } else {
                                        selectedItem.value = it
                                    }

                                },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current
                            ).fillMaxWidth()
                            .then(
                                if (selectedItem.value == it) {
                                    Modifier.background(
                                        MaterialTheme.colorScheme.primaryContainer,

                                        )
                                } else {
                                    Modifier
                                }
                            )
                    ) {

                        Row (
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(painter = painterResource(R.drawable.no_img), contentDescription = "no_img", modifier = Modifier.height(56.dp))
                            Column(
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Text(it.name, style = MaterialTheme.typography.titleSmall)
                            }
                        }

                    }
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
private fun ServiceScreenPreview() {
            val data=listOf<Materia>(
            Materia(
                id = 1,
                name = "Arquitectura"
            ),
            Materia(
                id = 2,
                name = "Bases"
            ),
            Materia(
                id = 3,
                name = "Datos"
            )
        )
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    ServiceScreen(
        data,
        ControllerProvider().materiaController,
        uiProvider = uiProvider
    )
}