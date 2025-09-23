package com.example.tallerarquitectura.view.screen.product

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
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionListener
import com.example.tallerarquitectura.dto.Product
import com.example.tallerarquitectura.validation.productFormValidate
import com.example.tallerarquitectura.controller.ControllerProvider
import com.example.tallerarquitectura.ui.UiAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    product: Product,
    listener: ActionListener<Product>,
    uiProvider: UiProvider,
) {
    val name = remember { mutableStateOf(product.name) }
    val detail = remember { mutableStateOf(product.detail?:"") }
    val urlImage = remember { mutableStateOf<String?>(null) }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Producto") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = productFormValidate(name.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.update(
                                    Product(
                                        id=product.id,
                                        name=name.value,
                                        detail=if (detail.value.trim().isEmpty()) null else detail.value,
                                        urlImage.value,
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
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences)

                )

                OutlinedTextField(
                    value = detail.value,
                    onValueChange = { detail.value = it },
                    label = { Text("Detalle") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences)

                )

            }

        }

    }
}


@Composable
@Preview
private fun ProductEditScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel= UiAppViewModel()
    val uiProvider= UiProvider(uiAppViewModel)
    ProductEditScreen(
        product = Product(
            id = 2465,
            name = "Shelby Valentine",
            detail = "labores",
            urlImage = "https://search.yahoo.com/search?p=moderatius"
        ),
        ControllerProvider().productController,
        uiProvider = uiProvider
    )
}
