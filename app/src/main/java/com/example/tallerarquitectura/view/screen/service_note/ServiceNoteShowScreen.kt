package com.example.tallerarquitectura.view.screen.service_note

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.R
import com.example.tallerarquitectura.ui.UiProvider
import com.example.tallerarquitectura.controller.ActionServiceNoteListener
import com.example.tallerarquitectura.controller.ServiceNoteController
import com.example.tallerarquitectura.dto.Car
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.dto.Product
import com.example.tallerarquitectura.dto.ReminderNoteWithMeasureUnit
import com.example.tallerarquitectura.dto.Service
import com.example.tallerarquitectura.dto.ServiceNote
import com.example.tallerarquitectura.dto.ServiceNoteDetail
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.model.ReminderNoteModel
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.model.ServiceNoteDetailModel
import com.example.tallerarquitectura.model.ServiceNoteModel
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ReminderNoteCreateRoute
import com.example.tallerarquitectura.view.navigation.ReminderNoteEditRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteDetailCreateRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteDetailEditRoute
import com.example.tallerarquitectura.view.screen.reminder_note.colorState
import com.example.tallerarquitectura.view.screen.reminder_note.timeDifference
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceNoteShowScreen(
    serviceNote: ServiceNote,
    listener: ActionServiceNoteListener,
    uiProvider: UiProvider
) {
    val coroutineScope = rememberCoroutineScope()

    val selectedItem = remember {
        mutableStateOf<ServiceNote?>(null)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Nota de servicio") },
            navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        uiProvider.getUiAppViewModel().getDrawerState().apply {
                            if (isClosed) {
                                open()
                            } else {
                                close()
                            }
                        }
                    }
                }) {
                    Icon(
//                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        imageVector = Icons.Filled.Menu, contentDescription = "Back"
                    )
                }
            },
        )
    }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    "Detalle de nota de servicio",
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
                )
                ServiceNoteCard(
                    serviceNote = serviceNote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Productos")
                    TextButton(
                        onClick = {
                            MainActivity.navManager.navigateTo(ServiceNoteDetailCreateRoute(serviceNote.id))
                        },
                    ) {
                        Text("Agregar")
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(serviceNote.detail) { item ->
                        ServiceNoteItemCard(item,
                            onDeleteDetail = {
                                coroutineScope.launch {
                                    listener.destroyDetail(
                                        serviceNoteId = serviceNote.id,
                                        productId = it.productId
                                    )
                                }
                            },
                            onSelectDetail = {
                                MainActivity.navManager.navigateTo(
                                    ServiceNoteDetailEditRoute(
                                        serviceNoteId = serviceNote.id,
                                        productId = it.productId
                                    )
                                )
                            }
                        )
                    }
                }
                Text("Recodatorio", modifier = Modifier.padding(16.dp))
                if (serviceNote.reminderNote == null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(100.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("No tiene recordatorio.")
                                IconButton(
                                    onClick = {
                                        MainActivity.navManager.navigateTo(
                                            ReminderNoteCreateRoute(serviceNoteId = serviceNote.id)
                                        )
                                    }) {
                                    Icon(Icons.Default.Add, contentDescription = "add")
                                }
                            }
                        }
                    }
                } else {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {


                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Siguiente servicio: ${timeDifference(serviceNote.reminderNote.endDate)}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    if (ReminderNoteModel.TODO == serviceNote.reminderNote.status) "Pendiente" else "Hecho",
                                    modifier = Modifier
                                        .background(
                                            color = colorState(serviceNote.reminderNote.status),
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(4.dp),
                                    style = MaterialTheme.typography.bodySmall
                                )

                            }

                            if (serviceNote.reminderNote.measureUnit != null) {
                                Text("O su equivalencia en : ${serviceNote.reminderNote.endOtherData} ${serviceNote.reminderNote.measureUnit.short} (${serviceNote.reminderNote.measureUnit.name} )")
                            } else {
                                Text("Sin otra medida.")
                            }
                            Text("Codigo : ${serviceNote.reminderNote.id}")

                        }
                        TextButton(
                            onClick = {
                                MainActivity.navManager.navigateTo(ReminderNoteEditRoute(serviceNote.id))
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 8.dp, bottom = 8.dp)
                        ) {
                            Text("Editar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewServiceNoteShowScreen() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider(uiAppViewModel)
    ServiceNoteShowScreen(

        serviceNote = ServiceNote(
            id = 2591,
            serviceDate = "varius",
            creationDate = "varius",
            voucherCode = "suscipit",
            voucherUrlImage = "https://www.google.com/#q=prompta",
            car = Car(
                id = 9551,
                plate = "tation",
                alias = "purus",
                model = "invenire",
                cylinder = "sententiae",
                urlImage = "http://www.bing.com/search?q=quot",
                mark = "pretium",
                year = 1999

            ),
            enterprise = null,
            service = Service(
                id = 1657, name = "Pat Fletcher", urlImage = "http://www.bing.com/search?q=porta"
            ),
            detail = listOf(
                ServiceNoteDetail(
                    quantity = 1463,
                    productId = 4669,
                    serviceNoteId = 1,
                    price = 2.3,
                    product = Product(
                        id = 4669,
                        name = "Kathrine Kemp",
                        detail = "id",
                        urlImage = "http://www.bing.com/search?q=scripserit"
                    )
                ), ServiceNoteDetail(
                    quantity = 1463,
                    productId = 4669,
                    serviceNoteId = 1,
                    price = 2.3,
                    product = Product(
                        id = 4669,
                        name = "Kathrine Kemp",
                        detail = "id",
                        urlImage = "http://www.bing.com/search?q=scripserit"
                    )
                ), ServiceNoteDetail(
                    quantity = 1463,
                    productId = 4669,
                    serviceNoteId = 1,
                    price = 2.3,
                    product = Product(
                        id = 4669,
                        name = "Kathrine Kemp",
                        detail = "id",
                        urlImage = "http://www.bing.com/search?q=scripserit"
                    )
                ), ServiceNoteDetail(
                    quantity = 1463,
                    productId = 4669,
                    serviceNoteId = 1,
                    price = 2.3,
                    product = Product(
                        id = 4669,
                        name = "Kathrine Kemp",
                        detail = "id",
                        urlImage = "http://www.bing.com/search?q=scripserit"
                    )
                ), ServiceNoteDetail(
                    quantity = 1463,
                    productId = 4669,
                    serviceNoteId = 1,
                    price = 2.3,
                    product = Product(
                        id = 4669,
                        name = "Kathrine Kemp",
                        detail = "id",
                        urlImage = "http://www.bing.com/search?q=scripserit"
                    )
                )

            ),
            reminderNote = ReminderNoteWithMeasureUnit(
                id = 9062,
                endDate = "2025-10-24",
                endOtherData = 2683,
                status = 7197,
                measureUnit = MeasureUnit(id = 0, name = "Kilometris", short = "Km"),
                serviceNoteId = 1
            ),

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
        uiProvider = uiProvider,
    )
}

@Composable
fun ServiceNoteItemCard(
    serviceNoteDetail: ServiceNoteDetail,
    onDeleteDetail: (ServiceNoteDetail) -> Unit,
    onSelectDetail:(ServiceNoteDetail) -> Unit
) {
    val context=LocalContext.current
    OutlinedCard(
        modifier = Modifier.width(110.dp).
        clickable(onClick = {
            onSelectDetail(serviceNoteDetail)
        })
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.no_img),
                contentDescription = "product_img",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(90.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onDeleteDetail(serviceNoteDetail)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Outlined.Delete, contentDescription = "delete",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Un.${serviceNoteDetail.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = serviceNoteDetail.product.name, style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                "Bs. ${serviceNoteDetail.price}", style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
