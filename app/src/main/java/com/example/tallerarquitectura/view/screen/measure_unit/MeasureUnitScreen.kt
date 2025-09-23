package com.example.tallerarquitectura.view.screen.measure_unit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionListener
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.navigation.MeasureUnitCreateRoute
import com.example.tallerarquitectura.view.navigation.MeasureUnitEditRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MeasureUnitScreen(
    measuresUnit: List<MeasureUnit> =
        emptyList<MeasureUnit>(),
    listener: ActionListener<MeasureUnit>,
    uiProvider: UiProvider,
) {

    val selectedItem = remember {
        mutableStateOf<MeasureUnit?>(null)
    }
    val coroutineScope= rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unidad de Medida") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            uiProvider.getUiAppViewModel().getDrawerState().apply {
                                if(isClosed){
                                    open()
                                }else{
                                    close()
                                }
                            }
                        }
                    }) {
                        Icon(
//                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                           selectedItem.value?.let {
                        Row{
                            IconButton(
                                onClick = {
                                    MainActivity.navManager.navigateTo(MeasureUnitEditRoute(it.id))
                                }
                            ) {
                                Icon(Icons.Outlined.Edit, contentDescription = "edit")
                            }
                            IconButton(
                                onClick = {
                                    listener.destroy(it.id)
                                    //controller.destroy(it.id)
                                }
                            ) {
                                Icon(Icons.Outlined.Delete, contentDescription = "delete")
                            }
                        }
                        }


                        FloatingActionButton(
                            onClick = {
                                MainActivity.navManager.navigateTo(MeasureUnitCreateRoute)
                            },
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .align(Alignment.BottomEnd),
                            elevation = FloatingActionButtonDefaults.loweredElevation()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "add")
                        }
                    }
                }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn {
                items(measuresUnit) {
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
                            )
                            .then(
                                if (selectedItem.value == it) {
                                    Modifier
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,

                                            )
                                } else {
                                    Modifier
                                }
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()


                        ) {
                            Text(it.name, style = MaterialTheme.typography.titleSmall)
                            Text(it.short)
                        }
                    }
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
private fun MeasureUnitScreenPreview() {
    val context=LocalContext.current
    val navHostController=rememberNavController()
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    MeasureUnitScreen(emptyList(),ControllerProvider().measureUnitController,uiProvider)
}