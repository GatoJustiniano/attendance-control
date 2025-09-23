package com.example.tallerarquitectura.view.screen.service_note

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
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionServiceNoteListener
import com.example.tallerarquitectura.controller.ServiceNoteController
import com.example.tallerarquitectura.dto.Car
import com.example.tallerarquitectura.dto.Enterprise
import com.example.tallerarquitectura.dto.Service
import com.example.tallerarquitectura.dto.ServiceNote
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.model.ServiceNoteDetailModel
import com.example.tallerarquitectura.model.ServiceNoteModel
import com.example.tallerarquitectura.validation.serviceNoteFormValidate
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.screen.reminder_note.DatePickerFieldToModal
import com.example.tallerarquitectura.view.screen.reminder_note.convertMillisToDateDb
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceNoteCreateScreen(
    cars: List<Car>,
    enterprises: List<Enterprise>,
    services: List<Service>,
    listener: ActionServiceNoteListener,
    uiProvider: UiProvider,
) {
    val serviceDate = remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    val voucherCode = remember { mutableStateOf("") }
    val selectedService = remember { mutableStateOf<Service?>(null) }
    val selectedCar = remember { mutableStateOf<Car?>(null) }
    val selectedEnterprise = remember { mutableStateOf<Enterprise?>(null) }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nota de servicio") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = serviceNoteFormValidate(selectedCar.value,selectedService.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.store(
                                    serviceNote = ServiceNote(
                                        id = 0,
                                        serviceDate = if(serviceDate.value.trim().isEmpty()) null else serviceDate.value,
                                        creationDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                        voucherCode = if (voucherCode.value.trim().isEmpty()) null else voucherCode.value,
                                        voucherUrlImage = null,
                                        car = selectedCar.value!!,
                                        enterprise = selectedEnterprise.value,
                                        service = selectedService.value!!
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
                    serviceDate.value = convertMillisToDateDb(it)
                }

                OutlinedTextField(
                    value = voucherCode.value,
                    onValueChange = { voucherCode.value = it },
                    label = { Text("Codigo de coprobante") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Characters
                    )
                )

                SelectDropdownMenuEnterprise(
                    enterprises = enterprises,
                    selectedTitle = selectedEnterprise.value?.name ?: ""
                ) {
                    selectedEnterprise.value = it
                }
                SelectDropdownMenuService(
                    services=services,
                    selectedTitle = selectedService.value?.name ?: "",

                ) {
                    selectedService.value=it
                }
                SelectDropdownMenuCar(
                    cars=cars,
                    selectedTitle = selectedCar.value?.plate ?: "",
                ) {
                    selectedCar.value=it
                }
                Text(serviceDate.value.toString())
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
fun SelectDropdownMenuService(
    services: List<Service>,
    selectedTitle: String,
    onSelectItem: (Service) -> Unit
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
        services.forEach {
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
fun SelectDropdownMenuCar(
    cars: List<Car>,
    selectedTitle: String,
    onSelectItem: (Car) -> Unit
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
        cars.forEach {
            DropdownMenuItem(
                text = {
                    Text("${it.plate} - ${it.alias?:""}")
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
fun SelectDropdownMenuEnterprise(
    enterprises: List<Enterprise>,
    selectedTitle: String,
    onSelectItem: (Enterprise) -> Unit
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
        enterprises.forEach {
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
@Preview
private fun ServiceNoteCreateScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    ServiceNoteCreateScreen(
        cars = listOf(
            Car(
                id = 9266,
                plate = "maximus",
                alias = "mediocrem",
                model = "maluisset",
                cylinder = "turpis",
                urlImage = "https://search.yahoo.com/search?p=scelerisque",
                mark = "dicunt",
                year = 2022

            ),
            Car(
                id = 5144,
                plate = "ante",
                alias = "cetero",
                model = "in",
                cylinder = "tortor",
                urlImage = "https://duckduckgo.com/?q=congue",
                mark = "epicuri",
                year = 1991

            )
        ),
        enterprises = listOf(
            Enterprise(
                id = 8017,
                name = "Tony Newman",
                locationName = "Chuck Marsh",
                latitude = 4.5,
                longitude = 6.7,
                urlImage = "https://search.yahoo.com/search?p=ponderum"

            ),
            Enterprise(
                id = 5126,
                name = "Basil Howell",
                locationName = "Stevie Lancaster",
                latitude = 12.13,
                longitude = 14.15,
                urlImage = "https://www.google.com/#q=efficiantur"
            ),
            Enterprise(
                id = 8017,
                name = "Tony Newman",
                locationName = "Chuck Marsh",
                latitude = 4.5,
                longitude = 6.7,
                urlImage = null
            )
        ),
        services = listOf(
            Service(
                id = 6946,
                name = "Gwen Boyle",
                urlImage = "http://www.bing.com/search?q=libero"
            ),
            Service(
                id = 2299, name = "Willie Riddle", urlImage = "https://www.google.com/#q=ultrices"

            )
        ),
        listener = ServiceNoteController(
            serviceNoteModel = ServiceNoteModel(ServiceNoteDetailModel(
                CarAtelierDbHelper.getInstance(
                    context
                )
            ),CarAtelierDbHelper.getInstance(context)),
            serviceModel = ServiceModel(CarAtelierDbHelper.getInstance(context)),
            carModel = CarModel(CarAtelierDbHelper.getInstance(context)),
            productModel = ProductModel(CarAtelierDbHelper.getInstance(context)),
            enterpriseModel = EnterpriseModel(CarAtelierDbHelper.getInstance(context)),
            view = View(uiProvider)
        ),
        uiProvider = uiProvider,

        )

}
