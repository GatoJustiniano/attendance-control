package com.example.tallerarquitectura.view.screen.reminder_note

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.tallerarquitectura.model.MeasureUnitModel
import com.example.tallerarquitectura.model.ReminderNoteModel
import com.example.tallerarquitectura.validation.reminderNoteFormValidate
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import java.text.SimpleDateFormat
import java.time.LocalDate

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderNoteCreateScreen(
    serviceNoteId: Long,
    measureUnits: List<MeasureUnit>,
    listener: ActionListener<ReminderNoteCreate>,
    uiProvider: UiProvider,
) {
    val endDate = remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    val endOtherData = remember { mutableStateOf("") }
    val selectedMeasureUnit = remember { mutableStateOf<MeasureUnit?>(null) }

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
                            listener.store(
                                ReminderNoteCreate(
                                    id = 0,
                                    serviceNoteId = serviceNoteId,
                                    endDate = endDate.value,
                                    endOtherData = if(endOtherData.value.trim().isEmpty()) 0 else endOtherData.value.toInt(),
                                    measureUnitId = selectedMeasureUnit.value?.id,
                                    status = ReminderNoteModel.TODO
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
                "Formulario de creacion",
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
@Preview
private fun ReminderNoteCreateScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    ReminderNoteCreateScreen(
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
