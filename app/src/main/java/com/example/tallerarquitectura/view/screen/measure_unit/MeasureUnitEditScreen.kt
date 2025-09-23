package com.example.tallerarquitectura.view.screen.measure_unit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionListener
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.validation.measureUniteFormValidate
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureUnitEditScreen(
    measureUnit: MeasureUnit,
    listener: ActionListener<MeasureUnit>,
    uiProvider: UiProvider
) {
    val name= remember { mutableStateOf(measureUnit.name) }
    val short=remember { mutableStateOf(measureUnit.short) }
    val errors=remember { mutableStateOf<List<String>>(emptyList()) }
    val context=LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unidad de Medida") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm= measureUniteFormValidate(name.value,short.value)
                        if(errorsForm.any()){
                            errors.value=errorsForm
                        }else{
                            errors.value=emptyList()
                            listener.update(MeasureUnit(measureUnit.id, name.value, short.value))

                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
        ) {


            Text("Formulario de actualizacion",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if(errors.value.any()){
                Text(errors.value.joinToString("\n", transform = { "*$it" }),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp).padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value=it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = short.value,
                    onValueChange = { short.value = it },
                    label = { Text("Abreviatura") }
                    , modifier = Modifier.fillMaxWidth()
                )

            }

        }

    }
}
@Composable
@Preview
private fun MeasureUnitEditScreenPreview(){
    val context=LocalContext.current
    val navHostController=rememberNavController()
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    MeasureUnitEditScreen(MeasureUnit(1,"No name","Corto"),ControllerProvider().measureUnitController,uiProvider)
}
