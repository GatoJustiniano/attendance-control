package com.example.tallerarquitectura.view.screen.clase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaseCreateScreen(
    horarios: List<Horario>,
    grupos: List<Grupo>,
    materias: List<Materia>,
    listener: ActionClaseListener,
    uiProvider: UiProvider,
) {
    val classDate = remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    val qrCode = remember { mutableStateOf("") }

    val selectedMateria = remember { mutableStateOf<Materia?>(null) }
    val selectedHorario = remember { mutableStateOf<Horario?>(null) }
    val selectedGrupo = remember { mutableStateOf<Grupo?>(null) }

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
                            listener.store(
                                    clase = Clase(
                                        id = 0,
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
                        Text("Guardar")
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
                "Formulario de creación",
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

                DatePickerFieldToModal{
                    classDate.value = convertMillisToDateDb(it)
                }

                OutlinedTextField(
                    value = qrCode.value,
                    onValueChange = { qrCode.value = it },
                    label = { Text("Código de comprobante") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Characters
                    )
                )

                SelectDropdownMenuMateria(
                    materias=materias,
                    selectedTitle = selectedMateria.value?.name ?: "",
                ) {
                    selectedMateria.value=it
                }

                SelectDropdownMenuGrupo(
                    grupos = grupos,
                    selectedTitle = selectedGrupo.value?.name ?: ""
                ) {
                    selectedGrupo.value = it
                }
                
                SelectDropdownMenuHorario(
                    horarios=horarios,
                    selectedTitle = selectedHorario.value?.name ?: "",
                ) {
                    selectedHorario.value=it
                }
                Text(classDate.value.toString())
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDropdownMenu(
    expanded: Boolean,
    title: String,
    selectedTitle: String,
    onCloseExpanded: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            onCloseExpanded(!expanded)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            value = selectedTitle,
            placeholder = {
                Text("Seleccione un elemento")
            },
            onValueChange = {},
            label = {
                Text(text = title)
            },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),

            )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onCloseExpanded(false)
            },
        ) {
            content()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDropdownMenuMateria(
    materias: List<Materia>,
    selectedTitle: String,
    onSelectItem: (Materia) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    ContentDropdownMenu(
        expanded = expanded.value,
        title = "Servicios",
        selectedTitle = selectedTitle,
        onCloseExpanded = {
            expanded.value = it
        }
    ) {
        materias.forEach {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDropdownMenuHorario(
    horarios: List<Horario>,
    selectedTitle: String,
    onSelectItem: (Horario) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    ContentDropdownMenu(
        expanded = expanded.value,
        title = "Servicios",
        selectedTitle = selectedTitle,
        onCloseExpanded = {
            expanded.value = it
        }
    ) {
        horarios.forEach {
            DropdownMenuItem(
                text = {
                    Text("${it.name} - ${it.endtime?:""}")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDropdownMenuGrupo(
    grupos: List<Grupo>,
    selectedTitle: String,
    onSelectItem: (Grupo) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    ContentDropdownMenu(
        expanded = expanded.value,
        title = "Talleres",
        selectedTitle = selectedTitle,
        onCloseExpanded = {
            expanded.value = it
        }
    ) {
        grupos.forEach {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }

}

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    initialDate: Long=LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
    onDateSelected: (Long) -> Unit,
) {
    var selectedDate by remember { mutableLongStateOf(initialDate) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = //selectedDate.toString(),
        convertMillisToDate(selectedDate),
        onValueChange = { },
        label = { Text("Fecha") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Seleccione Fecha")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                val dateLocal=it?: LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                selectedDate = dateLocal
                onDateSelected(dateLocal)
            },
            onDismiss = { showModal = false }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    formatter.timeZone= TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

fun convertMillisToDateDb(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone= TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

@Composable
@Preview
private fun ServiceNoteCreateScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    ClaseCreateScreen(
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
                name = "Tony Newman",
                locationName = "Chuck Marsh",
                latitude = 4.5,
                longitude = 6.7,
                urlImage = "https://search.yahoo.com/search?p=ponderum"

            ),
            Grupo(
                id = 5126,
                name = "Basil Howell",
                locationName = "Stevie Lancaster",
                latitude = 12.13,
                longitude = 14.15,
                urlImage = "https://www.google.com/#q=efficiantur"
            ),
            Grupo(
                id = 8017,
                name = "Tony Newman",
                locationName = "Chuck Marsh",
                latitude = 4.5,
                longitude = 6.7,
                urlImage = null
            )
        ),
        materias = listOf(
            Materia(
                id = 6946,
                name = "Gwen Boyle",
                urlImage = "http://www.bing.com/search?q=libero"
            ),
            Materia(
                id = 2299, name = "Willie Riddle", urlImage = "https://www.google.com/#q=ultrices"

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

        )

}
