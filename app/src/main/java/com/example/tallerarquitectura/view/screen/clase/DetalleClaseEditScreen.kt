package com.example.tallerarquitectura.view.screen.clase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionClaseListener
import com.example.tallerarquitectura.controller.ClaseController
import com.example.tallerarquitectura.dto.Alumno
import com.example.tallerarquitectura.dto.DetalleClase
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.model.DetalleClaseModel
import com.example.tallerarquitectura.model.ClaseModel
import com.example.tallerarquitectura.validation.detalleClaseFormValidate
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClaseEditScreen(
    detalleClase: DetalleClase,
    alumnos: List<Alumno>,
    listener: ActionClaseListener,
    uiProvider: UiProvider,
) {
    val code = remember { mutableStateOf(detalleClase.code.toString()) }

    val selectedAlumno = remember { mutableStateOf<Alumno?>(detalleClase.alumno) }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = detalleClaseFormValidate(code=code.value,selectedAlumno.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.updateDetail(
                                    detalleClase = DetalleClase(
                                        code = code.value.toInt(),
                                        alumno_id = selectedAlumno.value!!.id,
                                        clase_id = detalleClase.clase_id,
                                        alumno = selectedAlumno.value!!
                                    )
                            )

                        }
                    }
                    ) {
                        Text("Actualizar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            Text(
                "Formulario de actualización",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (errors.value.any()) {
                Text(
                    errors.value.joinToString("\n", transform = { "*$it" }),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = code.value,
                    onValueChange = { code.value = it },
                    label = { Text("Code") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Characters
                    )
                )

                SelectDropdownMenuAlumno(
                    alumnos=alumnos,
                    selectedTitle = selectedAlumno.value?.name ?: ""
                ) {
                    selectedAlumno.value = it
                }

            }

        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectDropdownMenuAlumno(
    alumnos: List<Alumno>,
    selectedTitle: String,
    onSelectItem: (Alumno) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    ContentDropdownMenu(
        expanded = expanded.value,
        title = "Productos",
        selectedTitle = selectedTitle,
        onCloseExpanded = {
            expanded.value = it
        }
    ) {
        alumnos.forEach {
            DropdownMenuItem(
                text = {
                    Text(it.name)
                },
                onClick = {
                    onSelectItem(it)
                    expanded.value = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
        }
    }
}


@Composable
@Preview
private fun ServiceNoteDetailEditScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    DetalleClaseEditScreen(
        detalleClase = DetalleClase(
            code = 8533,
            alumno_id = 1494,
            clase_id = 2563,
            alumno = Alumno(
                id = 2212,
                name = "Maggie Heath",
                detail = "nisl",
                urlImage = "https://duckduckgo.com/?q=idque"
            )
        ),
        alumnos = listOf(
            Alumno(
                id = 6046, name = "Les Griffin", detail = "leo", urlImage = "https://search.yahoo.com/search?p=invidunt"
            ),
            Alumno(
                id = 7453, name = "Carlo Doyle", detail = "tacimates", urlImage = "https://search.yahoo.com/search?p=audire"
            ),
            Alumno(
                id = 7210, name = "Bettie Butler", detail = "commodo", urlImage = "https://search.yahoo.com/search?p=reque"

            )
        ),
        listener = ClaseController(
            claseModel = ClaseModel(
                DetalleClaseModel(
                    AttendanceControlDbHelper.getInstance(
                        context
                    )
                ), AttendanceControlDbHelper.getInstance(context)
            ),
            materiaModel = MateriaModel(AttendanceControlDbHelper.getInstance(context)),
            horarioModel = HorarioModel(AttendanceControlDbHelper.getInstance(context)),
            alumnoModel = AlumnoModel(AttendanceControlDbHelper.getInstance(context)),
            grupoModel = GrupoModel(AttendanceControlDbHelper.getInstance(context)),
            view = View(uiProvider)
        ),
        uiProvider = uiProvider
    )
}
