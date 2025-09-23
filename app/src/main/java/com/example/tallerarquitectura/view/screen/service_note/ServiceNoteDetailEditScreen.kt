package com.example.tallerarquitectura.view.screen.service_note

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
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionServiceNoteListener
import com.example.tallerarquitectura.controller.ServiceNoteController
import com.example.tallerarquitectura.dto.Product
import com.example.tallerarquitectura.dto.ServiceNoteDetail
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.model.ServiceNoteDetailModel
import com.example.tallerarquitectura.model.ServiceNoteModel
import com.example.tallerarquitectura.validation.serviceNoteDetailFormValidate
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceNoteDetailEditScreen(
    serviceNoteDetail: ServiceNoteDetail,
    products: List<Product>,
    listener: ActionServiceNoteListener,
    uiProvider: UiProvider,
) {
    val quantity = remember { mutableStateOf(serviceNoteDetail.quantity.toString()) }
    val price = remember { mutableStateOf(serviceNoteDetail.price.toString()) }

    val selectedProduct = remember { mutableStateOf<Product?>(serviceNoteDetail.product) }

    val errors = remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de nota servicio") },
                navigationIcon = {
                    IconButton(onClick = {
                        MainActivity.navManager.popBackStack()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {

                    TextButton(onClick = {
                        val errorsForm = serviceNoteDetailFormValidate(price = price.value,quantity=quantity.value,selectedProduct.value)
                        if (errorsForm.any()) {
                            errors.value = errorsForm
                        } else {
                            errors.value = emptyList()
                            listener.updateDetail(
                                    serviceNoteDetail = ServiceNoteDetail(
                                        quantity = quantity.value.toInt(),
                                        price = price.value.toDouble(),
                                        productId = selectedProduct.value!!.id,
                                        serviceNoteId = serviceNoteDetail.serviceNoteId,
                                        product = selectedProduct.value!!
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
                    value = quantity.value,
                    onValueChange = { quantity.value = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Characters
                    )
                )

                OutlinedTextField(
                    value = price.value,
                    onValueChange = { price.value = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Characters
                    )
                )

                SelectDropdownMenuProduct(
                    products=products,
                    selectedTitle = selectedProduct.value?.name ?: ""
                ) {
                    selectedProduct.value = it
                }

            }

        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectDropdownMenuProduct(
    products: List<Product>,
    selectedTitle: String,
    onSelectItem: (Product) -> Unit
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
        products.forEach {
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
    ServiceNoteDetailEditScreen(
        serviceNoteDetail = ServiceNoteDetail(
            quantity = 8533,
            price = 2.3,
            productId = 1494,
            serviceNoteId = 2563,
            product = Product(
                id = 2212,
                name = "Maggie Heath",
                detail = "nisl",
                urlImage = "https://duckduckgo.com/?q=idque"
            )
        ),
        products = listOf(
            Product(
                id = 6046, name = "Les Griffin", detail = "leo", urlImage = "https://search.yahoo.com/search?p=invidunt"
            ),
            Product(
                id = 7453, name = "Carlo Doyle", detail = "tacimates", urlImage = "https://search.yahoo.com/search?p=audire"
            ),
            Product(
                id = 7210, name = "Bettie Butler", detail = "commodo", urlImage = "https://search.yahoo.com/search?p=reque"

            )
        ),
        listener = ServiceNoteController(
            serviceNoteModel = ServiceNoteModel(
                ServiceNoteDetailModel(
                    CarAtelierDbHelper.getInstance(
                        context
                    )
                ), CarAtelierDbHelper.getInstance(context)
            ),
            serviceModel = ServiceModel(CarAtelierDbHelper.getInstance(context)),
            carModel = CarModel(CarAtelierDbHelper.getInstance(context)),
            productModel = ProductModel(CarAtelierDbHelper.getInstance(context)),
            enterpriseModel = EnterpriseModel(CarAtelierDbHelper.getInstance(context)),
            view = View(uiProvider)
        ),
        uiProvider = uiProvider
    )
}
