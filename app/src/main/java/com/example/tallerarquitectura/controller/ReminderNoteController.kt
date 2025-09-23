package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.ReminderNoteCreate
import com.example.tallerarquitectura.dto.ReminderNoteWithMeasureUnit
import com.example.tallerarquitectura.model.MeasureUnitModel
import com.example.tallerarquitectura.model.ReminderNoteModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ReminderNoteRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteShowRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.reminder_note.ReminderNoteCreateScreen
import com.example.tallerarquitectura.view.screen.reminder_note.ReminderNoteEditScreen
import com.example.tallerarquitectura.view.screen.reminder_note.ReminderNoteScreen

class ReminderNoteController(
    private val reminderNoteModel: ReminderNoteModel,
    private val measureUnitModel: MeasureUnitModel,
    private val view: View
) : ActionListener<ReminderNoteCreate>{

    fun index(): @Composable () -> Unit {
        try {
            val reminderNotes = reminderNoteModel.getAll()
            return view.render { ReminderNoteScreen(reminderNotes,this,view.getUiProvider())}
        }catch (ex: Exception){
            Log.d("ReminderNoteController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los recordatorios, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(serviceNoteId: Long): @Composable () -> Unit {
        val measureUnits = measureUnitModel.getAll()
        return view.render {
            ReminderNoteCreateScreen(
                serviceNoteId,
                measureUnits,
                this,
                uiProvider = view.getUiProvider()
            )
        }
    }
    fun edit(serviceNoteId: Long): @Composable () -> Unit {
        try {
        var reminderNote= reminderNoteModel.getByServiceNoteId(serviceNoteId)
        if (reminderNote==null){
            return view.render {
                NotFoundScreen("No se encontro el recordatorio",view.getUiProvider())
            }
        }
        val measureUnits = measureUnitModel.getAll()
        return view.render {
            ReminderNoteEditScreen(
                reminderNote,
                serviceNoteId,
                measureUnits,
                this,
                uiProvider = view.getUiProvider()
            )
        }
        }catch (ex: Exception){
            Log.d("ReminderNoteController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener el recordatorio, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    override fun store(reminderNoteCreate: ReminderNoteCreate) {
        try{
            reminderNoteModel.save(reminderNoteCreate)
            MainActivity.navManager.navigateCurrentTo(ReminderNoteRoute)
        }catch (ex: Exception){
            Log.d("ReminderNoteController.store ", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ReminderNoteRoute)
        }
    }

    override fun update(data: ReminderNoteCreate) {
        try {
            reminderNoteModel.update(data)
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(data.serviceNoteId))
        }catch (ex: Exception){
            Log.d("ReminderNoteController.update ", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(data.serviceNoteId))
        }
    }

    override fun destroy(id: Long) {
        try {
            reminderNoteModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(ReminderNoteRoute)
        }catch (ex: Exception){
            Log.d("ReminderNoteController.destroy ", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ReminderNoteRoute)
        }
    }
}