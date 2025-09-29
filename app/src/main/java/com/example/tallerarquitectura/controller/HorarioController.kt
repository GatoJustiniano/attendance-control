package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Horario
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.HorarioRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.horario.HorarioCreateScreen
import com.example.tallerarquitectura.view.screen.horario.HorarioEditScreen
import com.example.tallerarquitectura.view.screen.horario.HorarioScreen

class HorarioController(private val horarioModel: HorarioModel, private val view: View): ActionListener<Horario> {

    fun index(): @Composable () -> Unit {
        try {
            val horarios = horarioModel.getAll()
            return view.render {
                HorarioScreen(horarios, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("HorarioController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los empresas, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            HorarioCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(horario: Horario){
        try {
            horarioModel.save(horario)
            MainActivity.navManager.navigateCurrentTo(HorarioRoute)
        } catch (ex: Exception) {
            Log.d("HorarioController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(HorarioRoute)
        }
    }

    override fun update(horario: Horario){
        try {
            horarioModel.update(horario)
            MainActivity.navManager.navigateCurrentTo(HorarioRoute)
        } catch (ex: Exception) {
            Log.d("HorarioController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(HorarioRoute)
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val horario = horarioModel.getById(id)

            if(horario==null){
                return view.render {
                    NotFoundScreen("No se encontro la empresa",view.getUiProvider())
                }
            }
            return view.render {
                HorarioEditScreen(horario, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("HorarioController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar la empresa",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            horarioModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(HorarioRoute)
        } catch (ex: Exception) {
            Log.d("HorarioController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(HorarioRoute)
        }
    }
}
