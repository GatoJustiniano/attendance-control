package com.example.tallerarquitectura.view.screen.clase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.tallerarquitectura.dto.Horario
import com.example.tallerarquitectura.dto.Grupo
import com.example.tallerarquitectura.dto.Materia
import com.example.tallerarquitectura.dto.Clase
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.model.DetalleClaseModel
import com.example.tallerarquitectura.model.ClaseModel
import com.example.tallerarquitectura.validation.claseFormValidate
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.screen.clase.DatePickerFieldToModal
import com.example.tallerarquitectura.view.screen.clase.convertMillisToDateDb
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaseEditScreen(
    clase: Clase,
    horarios: List<Horario>,
    grupos: List<Grupo>,
    materias: List<Materia>,
    listener: ActionClaseListener,
    uiProvider: UiProvider,
) {
    val classDate = remember { mutableStateOf(LocalDate.parse(clase.classDate).format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    val qrCode = remember { mutableStateOf(clase.qrCode?:"") }
    val selectedMateria = remember { mutableStateOf<Materia?>(clase.materia) }
    val selectedHorario = remember { mutableStateOf<Horario?>(clase.horario) }
    val selectedGrupo = remember { mutableStateOf<Grupo?>(clase.grupo) }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clase") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = claseFormValidate(selectedHorario.value,selectedMateria.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.update(
                                    clase = Clase(
                                        id = clase.id,
                                        classDate = if(classDate.value.trim().isEmpty()) null else classDate.value,
                                        classCreate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                        qrCode = if (qrCode.value.trim().isEmpty()) null else qrCode.value,
                                        horario = selectedHorario.value!!,
                                        grupo = selectedGrupo.value,
                                        materia = selectedMateria.value!!
                                    )
                                )

                        }
                    }) {
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

                DatePickerFieldToModal(
                    initialDate = LocalDate.parse(clase.classDate).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                ){
                    classDate.value = convertMillisToDateDb(it)
                }

                OutlinedTextField(
                    value = qrCode.value,
                    onValueChange = { qrCode.value = it },
                    label = { Text("Codigo de coprobante") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Characters
                    )
                )

                SelectDropdownMenuGrupo(
                    grupos = grupos,
                    selectedTitle = selectedGrupo.value?.name ?: ""
                ) {
                    selectedGrupo.value = it
                }
                SelectDropdownMenuMateria(
                    materias=materias,
                    selectedTitle = selectedMateria.value?.name ?: "",

                ) {
                    selectedMateria.value=it
                }
                SelectDropdownMenuHorario(
                    horarios=horarios,
                    selectedTitle = selectedHorario.value?.name ?: "",
                ) {
                    selectedHorario.value=it
                }
            }

        }

    }
}

@Composable
@Preview
private fun ServiceNoteEditScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    ClaseEditScreen(
        horarios = listOf(
            Horario(
                id = 9266,
                name = "maximus",
                starttime = "dicunt",
                endtime = "mediocrem"

            ),
            Horario(
                id = 5144,
                name = "ante",
                starttime = "epicuri",
                endtime = "cetero"
            )
        ),
        grupos = listOf(
            Grupo(
                id = 8017,
                name = "Tony Newman"

            ),
            Grupo(
                id = 5126,
                name = "Basil Howell"
            ),
            Grupo(
                id = 8017,
                name = "Tony Newman"
            )
        ),
        materias = listOf(
            Materia(
                id = 6946,
                name = "Gwen Boyle"
            ),
            Materia(
                id = 2299, name = "Willie Riddle"
            )
        ),
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
        uiProvider = uiProvider,
        clase = Clase(
            id = 1512,
            classDate = "2025-09-21",
            classCreate = "2025-09-21",
            qrCode = "cursus",
            horario = Horario(
                id = 4420,
                name = "ornatus",
                starttime = "veniam",
                endtime = "graece"
            ),
            grupo = null,
            materia = Materia(
                id = 7526,
                name = "Gabriela Bright"
            )
        )
    )
}
