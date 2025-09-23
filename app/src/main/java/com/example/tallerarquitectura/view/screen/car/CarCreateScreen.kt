package com.example.tallerarquitectura.view.screen.car

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
import com.example.tallerarquitectura.dto.Car
import com.example.tallerarquitectura.validation.carFormValidate
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarCreateScreen(
    listener: ActionListener<Car>,
    uiProvider: UiProvider,
) {
    val plate = remember { mutableStateOf("") }
    val alias = remember { mutableStateOf("") }
    val model = remember { mutableStateOf("") }
    val cylinder = remember { mutableStateOf("") }
    val urlImage = remember { mutableStateOf<String?>(null) }
    val mark=remember { mutableStateOf("") }
    var year=remember { mutableStateOf("") }

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
                        val errorsForm = carFormValidate(plate.value,model.value,cylinder.value,year.value,mark.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.store(
                                    Car(
                                        id=0,
                                        plate=plate.value,
                                        alias=if (alias.value.trim().isEmpty()) null else alias.value,
                                        model=model.value,
                                        cylinder=cylinder.value,
                                        urlImage.value,
                                        mark=mark.value,
                                        year=year.value.toIntOrNull()?: -1
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
                OutlinedTextField(
                    value = mark.value,
                    onValueChange = { mark.value = it },
                    label = { Text("Marca") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences)

                )



                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    OutlinedTextField(
                        value = model.value,
                        onValueChange = { model.value = it },
                        label = { Text("Modelo") },
                        modifier = Modifier.weight(0.5f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences)

                    )

                    OutlinedTextField(
                        value = year.value,
                        onValueChange = { year.value = it },
                        label = { Text("Año") },
                        modifier = Modifier.weight(0.5f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                    )
                }
                OutlinedTextField(
                    value = plate.value,
                    onValueChange = { plate.value = it },
                    label = { Text("Placa") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Characters)
                )



                OutlinedTextField(
                    value = cylinder.value,
                    onValueChange = { cylinder.value = it },
                    label = { Text("Cilindraje") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences)
                )
                OutlinedTextField(
                    value = alias.value,
                    onValueChange = { alias.value = it },
                    label = { Text("Alias") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences)

                )

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
    CarCreateScreen(
        ControllerProvider().carController,
        uiProvider = uiProvider
    )
}
