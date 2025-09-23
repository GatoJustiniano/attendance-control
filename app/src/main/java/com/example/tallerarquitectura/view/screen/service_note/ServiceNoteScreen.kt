package com.example.tallerarquitectura.view.screen.service_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.tallerarquitectura.dto.Service
import com.example.tallerarquitectura.dto.ServiceNote
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.model.ServiceNoteDetailModel
import com.example.tallerarquitectura.model.ServiceNoteModel
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ServiceNoteCreateRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteEditRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteShowRoute

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ServiceNoteScreen(
    serviceNotes: List<ServiceNote> = emptyList<ServiceNote>(),
    listener: ActionServiceNoteListener,
    uiProvider: UiProvider
) {
    val context = LocalContext.current
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
    }, bottomBar = {
        BottomAppBar(
            actions = {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
                ) {
                    selectedItem.value?.let {
                        Row() {
                            IconButton(
                                onClick = {
                                    MainActivity.navManager.navigateTo(ServiceNoteEditRoute(it.id))
                                }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "edit")
                            }
                            IconButton(
                                onClick = {
                                    listener.delete(it.id)
                                }) {
                                Icon(Icons.Outlined.Delete, contentDescription = "delete")
                            }
                        }
                    }


                    FloatingActionButton(
                        onClick = {
                            MainActivity.navManager.navigateTo(ServiceNoteCreateRoute)
                        },
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .align(Alignment.BottomEnd),
                        elevation = FloatingActionButtonDefaults.loweredElevation()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "add")
                    }
                }
            })
    }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn(

            ) {
                items(serviceNotes) {
                    Box(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                },
                                onLongClick = {
                                    if (selectedItem.value == it) {
                                        selectedItem.value = null
                                    } else {
                                        selectedItem.value = it
                                    }

                                },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current
                            )
                            .then(
                                if (selectedItem.value == it) {
                                    Modifier
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,

                                            )
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        ServiceNoteCard(
                            serviceNote = it,
                            showButtonDetail = true,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            MainActivity.navManager.navigateTo(ServiceNoteShowRoute(it.id))
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun ServiceNoteCard(
    serviceNote: ServiceNote,
    modifier: Modifier,
    showButtonDetail: Boolean=false,
    onSelectSaleNote: (ServiceNote) -> Unit={}
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = serviceNote.serviceDate.toString(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Code: ${serviceNote.voucherCode?:"Sin codigo"}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Attachment (Image or File)
            AttachmentDisplay(attachment = serviceNote.voucherUrlImage)

            Spacer(modifier = Modifier.height(16.dp))

            // Company
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_car_repair), // Replace with your company icon
                    contentDescription = "Company Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = serviceNote.enterprise?.name ?: "Sin taller",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Service
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_settings), // Replace with your service icon
                    contentDescription = "Service Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = serviceNote.service.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vehicle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_directions_car), // Replace with your vehicle icon
                    contentDescription = "Vehicle Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = serviceNote.car.plate,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if(showButtonDetail){
                TextButton(
                    onClick = {
                        onSelectSaleNote(serviceNote)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Ver detalles")
                }
            }

        }
    }
}


@Composable
fun AttachmentDisplay(attachment: Any?) {
    if (attachment != null) {
        when (attachment) {
            is Painter -> {
                Image(
                    painter = attachment,
                    contentDescription = "Sin archivo abjunto.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            is Int -> { // Assuming you pass an image resource id.
                Image(
                    painter = painterResource(id = attachment),
                    contentDescription = "Sin archivo abjunto.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            is String -> { // Assuming it's a file path or some other type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your file icon
                        contentDescription = "File Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "File: $attachment",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            else -> {
                Text(
                    text = "Sin archivo abjunto.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            }
        }
    } else {
        Text(
            text = "Sin archivo abjunto.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun ServiceNoteScreenPreview() {
    val data = listOf<ServiceNote>(
        ServiceNote(
            1, "2024-04-10", "2024-04-10", "54545", "",
            car = Car(
                1,
                "6565",
                "no detail",
                "no detail",
                "no detail",
                "no detail",
                "no detail",
                1323
            ),
            service = Service(1, "no name", ""),
            enterprise = null
        ),
        ServiceNote(
            2, "2024-04-10", "2024-04-10", "54545", "",
            car = Car(
                1,
                "6565",
                "no detail",
                "no detail",
                "no detail",
                "no detail",
                "no detail",
                1323
            ),
            service = Service(1, "no name", ""),
            enterprise = null
        ),
        ServiceNote(
            3, "2024-04-10", "2024-04-10", "54545", "",
            car = Car(
                1,
                "6565",
                "no detail",
                "no detail",
                "no detail",
                "no detail",
                "no detail",
                1323
            ),
            service = Service(1, "no name", ""),
            enterprise = null
        )

    )
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider(uiAppViewModel)
    ServiceNoteScreen(
        data,
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
        uiProvider = uiProvider
    )
}