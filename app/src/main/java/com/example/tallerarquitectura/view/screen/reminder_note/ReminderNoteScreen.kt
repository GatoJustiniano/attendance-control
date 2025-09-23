package com.example.tallerarquitectura.view.screen.reminder_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.tallerarquitectura.ui.UiAppViewModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ServiceNoteShowRoute
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReminderNoteScreen(
    reminderNotes: List<ReminderNoteWithMeasureUnit> =
        emptyList<ReminderNoteWithMeasureUnit>(),
    listener: ActionListener<ReminderNoteCreate>,
    uiProvider: UiProvider,
) {

    val selectedItem = remember {
        mutableStateOf<ReminderNoteWithMeasureUnit?>(null)
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recordatorios") },
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
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        selectedItem.value?.let {
                            Row {
                                IconButton(
                                    onClick = {
                                        //MainActivity.navManager.navigateTo(ReminderNoteEditRoute(it.id))
                                    }
                                ) {
                                    Icon(Icons.Outlined.Edit, contentDescription = "edit")
                                }

                            }
                        }

                    }
                }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn(
            ) {
                items(reminderNotes) {
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
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
                                        "Siguiente servicio: ${timeDifference(it.endDate)}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        if(ReminderNoteModel.TODO == it.status) "Pendiente" else "Hecho", modifier = Modifier
                                            .background(
                                                color = colorState(it.status),
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .padding(4.dp),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                if (it.measureUnit != null) {
                                    Text("O su equivalencia en : ${it.endOtherData} ${it.measureUnit.short} (${it.measureUnit.name} )")
                                } else {
                                    Text("Sin otra medida.")
                                }
                                Text("Codigo : ${it.id}")

                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(
                                    onClick = {
                                        MainActivity.navManager.navigateTo(ServiceNoteShowRoute(it.serviceNoteId))
                                    }) {
                                    Text("Ver")
                                }
                                IconButton(
                                    onClick = {
                                        listener.destroy(it.id)
                                    }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = "delete")
                                }
                            }
                        }


//                        Column(
//                            modifier = Modifier
//                                .padding(horizontal = 16.dp, vertical = 8.dp)
//                                .fillMaxWidth()
//
//
//                        ) {
//                            Text(it.status.toString(), style = MaterialTheme.typography.titleSmall)
//                            Text(it.endDate)
//                        }
                    }
                }
            }
        }

    }
}

fun timeDifference(date: String): String {
    val format = DateTimeFormatter.ISO_LOCAL_DATE
    val dateParse = LocalDate.parse(date, format)
    val hoy = LocalDate.now()

    val period = Period.between(hoy, dateParse)

    return when {
        period.years != 0 -> if (period.years < 0) "hace ${period.years*-1} año(s)" else "en ${period.years} año(s)"
        period.months != 0 -> if (period.months < 0) "hace ${period.months*-1} mes(es)" else "en ${period.months} mes(es)"
        period.days != 0 -> if (period.days < 0) "hace ${period.days*-1} día(s)" else "en ${period.days} día(s)"
        else -> "Es hoy"
    }
}

fun colorState(state: Int): Color {
    return when (state) {
        ReminderNoteModel.TODO -> {
            return Color(0xFFFFC107)
        }

        ReminderNoteModel.DONE -> {
            return Color(0xFF4CAF50)
        }

        else -> Color.Transparent
    }
}

@Composable
@Preview(showBackground = true)
private fun ReminderNoteScreenPreview() {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val uiAppViewModel = UiAppViewModel()
    val uiProvider = UiProvider( uiAppViewModel)
    ReminderNoteScreen(
        listOf(
            ReminderNoteWithMeasureUnit(
                id = 9247,
                endDate = "2025-04-21",
                endOtherData = 9873,
                status = 1,
                serviceNoteId = 1,
                measureUnit = MeasureUnit(
                    id = 1,
                    name = "Kilometros",
                    short = "Km"
                )
            ),
            ReminderNoteWithMeasureUnit(
                id = 6587,
                endDate = "2025-04-21",
                endOtherData = 9144,
                status = 2,
                serviceNoteId = 2,
                measureUnit = null
            )
        ), ReminderNoteController(
            reminderNoteModel = ReminderNoteModel(CarAtelierDbHelper.getInstance(context)),
            measureUnitModel = MeasureUnitModel(CarAtelierDbHelper.getInstance(context)),
            view = View(uiProvider)
        ), uiProvider
    )
}