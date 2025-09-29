package com.example.tallerarquitectura.view.screen.clase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.R
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionClaseListener
import com.example.tallerarquitectura.controller.ClaseController
import com.example.tallerarquitectura.dto.Horario
import com.example.tallerarquitectura.dto.Alumno
import com.example.tallerarquitectura.dto.Materia
import com.example.tallerarquitectura.dto.Clase
import com.example.tallerarquitectura.dto.DetalleClase
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.model.DetalleClaseModel
import com.example.tallerarquitectura.model.ClaseModel
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.DetalleClaseCreateRoute
import com.example.tallerarquitectura.view.navigation.DetalleClaseEditRoute
import com.example.tallerarquitectura.view.screen.service_note.ClaseCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaseShowScreen(
    clase: Clase,
    listener: ActionClaseListener,
    uiProvider: UiProvider
) {
    val coroutineScope = rememberCoroutineScope()

    val selectedItem = remember {
        mutableStateOf<Clase?>(null)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Clase") },
            navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        uiProvider.getUiAppViewModel().getDrawerState().apply {
                            if (isClosed) {
                                open()
                            } else {
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
    }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    "Detalle Clase - Asistencia",
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
                )
                ClaseCard(
                    clase = clase,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Alumnos")
                    TextButton(
                        onClick = {
                            MainActivity.navManager.navigateTo(DetalleClaseCreateRoute(clase.id))
                        },
                    ) {
                        Text("Marcar")
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clase.detail) { item ->
                        ClaseItemCard(item,
                            onDeleteDetail = {
                                coroutineScope.launch {
                                    listener.destroyDetail(
                                        clase_id = clase.id,
                                        alumno_id = it.alumno_id
                                    )
                                }
                            },
                            onSelectDetail = {
                                MainActivity.navManager.navigateTo(
                                    DetalleClaseEditRoute(
                                        clase_id = clase.id,
                                        alumno_id = it.alumno_id
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ClaseItemCard(
    detalleClase: DetalleClase,
    onDeleteDetail: (DetalleClase) -> Unit,
    onSelectDetail:(DetalleClase) -> Unit
) {
    val context=LocalContext.current
    OutlinedCard(
        modifier = Modifier.width(110.dp).
        clickable(onClick = {
            onSelectDetail(detalleClase)
        })
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.no_img),
                contentDescription = "product_img",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(90.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onDeleteDetail(detalleClase)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Outlined.Delete, contentDescription = "delete",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = detalleClase.alumno.name, style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
