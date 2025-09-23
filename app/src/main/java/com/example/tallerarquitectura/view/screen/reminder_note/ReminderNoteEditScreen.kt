package com.example.tallerarquitectura.view.screen.reminder_note

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionListener
import com.example.tallerarquitectura.controller.ReminderNoteController
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.dto.ReminderNoteCreate
import com.example.tallerarquitectura.dto.ReminderNoteWithMeasureUnit
import com.example.tallerarquitectura.model.MeasureUnitModel
import com.example.tallerarquitectura.model.ReminderNoteModel
import com.example.tallerarquitectura.validation.reminderNoteFormValidate
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import java.time.LocalDate
import java.time.ZoneId

import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderNoteEditScreen(
    reminderNote: ReminderNoteWithMeasureUnit,
    serviceNoteId: Long,
    measureUnits: List<MeasureUnit>,
    listener: ActionListener<ReminderNoteCreate>,
    uiProvider: UiProvider,
) {
    val endDate = remember {
        mutableStateOf(
            LocalDate.parse(reminderNote.endDate).format(DateTimeFormatter.ISO_LOCAL_DATE)
        )
    }
    val endOtherData = remember {
        mutableStateOf(
            if (reminderNote.endOtherData == 0) "" else reminderNote.endOtherData.toString()
        )
    }
    val selectedMeasureUnit = remember { mutableStateOf<MeasureUnit?>(reminderNote.measureUnit) }
    val state = remember { mutableIntStateOf(reminderNote.status) }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recordatorio") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = reminderNoteFormValidate(endDate.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.update(
                                ReminderNoteCreate(
                                    id = reminderNote.id,
                                    serviceNoteId = serviceNoteId,
                                    endDate = endDate.value,
                                    endOtherData = if (endOtherData.value.trim()
                                            .isEmpty()
                                    ) 0 else endOtherData.value.toInt(),
                                    measureUnitId = selectedMeasureUnit.value?.id,
                                    status =state.intValue
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
                "Formulario de edicion",
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
                    initialDate = LocalDate.parse(reminderNote.endDate)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                ) {
                    endDate.value = convertMillisToDateDb(it)
                }

                OutlinedTextField(
                    value = endOtherData.value,
                    onValueChange = { endOtherData.value = it },
                    label = { Text("Medida") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                SelectDropdownMenuMeasureUnit(
                    measureUnits = measureUnits,
                    selectedTitle = selectedMeasureUnit.value?.name ?: ""
                ) {
                    selectedMeasureUnit.value = it
                }
                SelectDropdownMenuState(
                    selectedTitle = when(state.intValue) {
                        ReminderNoteModel.TODO -> "Pendiente"
                        ReminderNoteModel.DONE -> "Hecho"
                        else -> "No defindo"
                    }
                ) {
                    state.intValue=it
                }
            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectDropdownMenuMeasureUnit(
    measureUnits: List<MeasureUnit>,
    selectedTitle: String,
    onSelectItem: (MeasureUnit) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    ContentDropdownMenu(
        expanded = expanded.value,
        title = "Unidad de medida",
        selectedTitle = selectedTitle,
        onCloseExpanded = {
            expanded.value = it
        }
    ) {
        measureUnits.forEach {
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
private fun SelectDropdownMenuState(
    selectedTitle: String,
    onSelectItem: (Int) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    ContentDropdownMenu(
        expanded = expanded.value,
        title = "Estado",
        selectedTitle = selectedTitle,
        onCloseExpanded = {
            expanded.value = it
        }
    ) {
        DropdownMenuItem(
            text = {
                Text("Pendiente")
            },
            onClick = {
                onSelectItem(ReminderNoteModel.TODO)
                expanded.value = false
            },
            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
        )
        DropdownMenuItem(
            text = {
                Text("Hecho")
            },
            onClick = {
                onSelectItem(ReminderNoteModel.DONE)
                expanded.value = false
            },
            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
        )
    }
}


@Composable
@Preview
private fun ReminderNoteEditScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    ReminderNoteEditScreen(
        reminderNote = ReminderNoteWithMeasureUnit(
            id = 1,
            serviceNoteId = 1,
            endDate = "2023-10-10",
            endOtherData = 0,
            status = ReminderNoteModel.TODO,
            measureUnit = MeasureUnit(
                id = 5555, name = "Elva Murphy", short = "placerat"
            )
        ),
        serviceNoteId = 1,
        measureUnits = listOf(
            MeasureUnit(
                id = 5555, name = "Elva Murphy", short = "placerat"

            ),
            MeasureUnit(id = 9049, name = "Amparo Nguyen", short = "libero")
        ),
        ReminderNoteController(
            ReminderNoteModel(CarAtelierDbHelper.getInstance(context)),
            MeasureUnitModel(CarAtelierDbHelper.getInstance(context)),
            View(uiProvider)
        ),
        uiProvider = uiProvider
    )
}
