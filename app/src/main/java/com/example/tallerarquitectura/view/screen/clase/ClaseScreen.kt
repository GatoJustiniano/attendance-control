package com.example.tallerarquitectura.view.screen.service_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.tallerarquitectura.dto.Materia
import com.example.tallerarquitectura.dto.Clase
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.model.DetalleClaseModel
import com.example.tallerarquitectura.model.ClaseModel
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ClaseCreateRoute
import com.example.tallerarquitectura.view.navigation.ClaseEditRoute
import com.example.tallerarquitectura.view.navigation.ClaseShowRoute

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClaseScreen(
    clases: List<Clase> = emptyList<Clase>(),
    listener: ActionClaseListener,
    uiProvider: UiProvider
) {
    val context = LocalContext.current
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
                                    MainActivity.navManager.navigateTo(ClaseEditRoute(it.id))
                                }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "edit")
                            }
                            IconButton(
                                onClick = {
                                    listener.delete(it.id)
                                }) {
                                Icon(Icons.Outlined.Delete, contentDescription = "delete")
                            }
                        }
                    }


                    FloatingActionButton(
                        onClick = {
                            MainActivity.navManager.navigateTo(ClaseCreateRoute)
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

            LazyColumn(

            ) {
                items(clases) {
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
                        ClaseCard(
                            clase = it,
                            showButtonDetail = true,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            MainActivity.navManager.navigateTo(ClaseShowRoute(it.id))
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun ClaseCard(
    clase: Clase,
    modifier: Modifier,
    showButtonDetail: Boolean=false,
    onSelectSaleNote: (Clase) -> Unit={}
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = clase.classDate.toString(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Code: ${clase.qrCode?:"Sin código"}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Attachment (Image or File)
            AttachmentDisplay(attachment = clase.qrCode)

            Spacer(modifier = Modifier.height(16.dp))

            // Materia
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_settings),
                    contentDescription = "Materia Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = clase.materia.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Grupo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_settings),
                    contentDescription = "Grupo Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = clase.grupo?.name ?: "Sin grupo",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            

            Spacer(modifier = Modifier.height(8.dp))

            // Horario
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_settings),
                    contentDescription = "Horario Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = clase.horario.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if(showButtonDetail){
                TextButton(
                    onClick = {
                        onSelectSaleNote(clase)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Ver detalles")
                }
            }

        }
    }
}


@Composable
fun AttachmentDisplay(attachment: Any?) {
    if (attachment != null) {
        when (attachment) {
            is Painter -> {
                Image(
                    painter = attachment,
                    contentDescription = "Sin archivo abjunto.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            is Int -> {
                Image(
                    painter = painterResource(id = attachment),
                    contentDescription = "Sin archivo abjunto.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            is String -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "File Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "File: $attachment",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            else -> {
                Text(
                    text = "Sin archivo abjunto.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            }
        }
    } else {
        Text(
            text = "Sin archivo abjunto.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ClaseScreenPreview() {
    val data = listOf<Clase>(
        Clase(
            1, "2024-04-10", "2024-04-10", "54545",
            materia = Materia(1, "Materia1"),
            grupo = null,
            horario = Horario(1, "Horario", "11:00", "12:30"),
        )
    )
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider(uiAppViewModel)
    ClaseScreen(
        data,
        listener = ClaseController(
            claseModel = ClaseModel(DetalleClaseModel(
                AttendanceControlDbHelper.getInstance(
                    context
                )
            ),AttendanceControlDbHelper.getInstance(context)),
            materiaModel = MateriaModel(AttendanceControlDbHelper.getInstance(context)),
            horarioModel = HorarioModel(AttendanceControlDbHelper.getInstance(context)),
            alumnoModel = AlumnoModel(AttendanceControlDbHelper.getInstance(context)),
            grupoModel = GrupoModel(AttendanceControlDbHelper.getInstance(context)),
            view = View(uiProvider)
        ),
        uiProvider = uiProvider
    )
}