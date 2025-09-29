package com.example.tallerarquitectura.view.screen.horario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionListener
import com.example.tallerarquitectura.dto.Horario
import com.example.tallerarquitectura.validation.horarioFormValidate
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.screen.clase.DatePickerFieldToModal
import com.example.tallerarquitectura.view.screen.clase.convertMillisToDateDb


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorarioCreateScreen(
    listener: ActionListener<Horario>,
    uiProvider: UiProvider,
) {
    val name = remember { mutableStateOf("") }
    val starttime=remember { mutableStateOf("") }
    val endtime = remember { mutableStateOf("") }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vehiculo") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = horarioFormValidate(name.value, starttime.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.store(
                                    Horario(
                                        id=0,
                                        name=name.value,
                                        starttime=if (starttime.value.trim().isEmpty()) null else starttime.value,
                                        endtime=if (endtime.value.trim().isEmpty()) null else endtime.value
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

                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Placa") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Characters)
                )

                DatePickerFieldToModal{
                    starttime.value = convertMillisToDateDb(it)
                }

                DatePickerFieldToModal{
                    endtime.value = convertMillisToDateDb(it)
                }

            }

        }

    }
}


@Composable
@Preview
private fun CarCreateScreenPreview() {
    val navHostController = rememberNavController()
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    HorarioCreateScreen(
        ControllerProvider().horarioController,
        uiProvider = uiProvider
    )
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