package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Alumno
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.AlumnoRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.alumno.AlumnoCreateScreen
import com.example.tallerarquitectura.view.screen.alumno.AlumnoEditScreen
import com.example.tallerarquitectura.view.screen.alumno.AlumnoScreen

class AlumnoController(private val alumnoModel: AlumnoModel, private val view: View):
    ActionListener<Alumno> {
    fun index(): @Composable () -> Unit {
        try {
            val alumnos = alumnoModel.getAll()
            return view.render {
                AlumnoScreen(alumnos, this,view.getUiProvider())
            }

        } catch (ex: Exception) {
            Log.d("AlumnoController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los empresas, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            AlumnoCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(alumno: Alumno){
        try {
            alumnoModel.save(alumno)
            MainActivity.navManager.navigateCurrentTo(AlumnoRoute)
        } catch (ex: Exception) {
            Log.d("AlumnoController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(AlumnoRoute)
        }
    }

    override fun update(alumno: Alumno){
        try {
            alumnoModel.update(alumno)
            MainActivity.navManager.navigateCurrentTo(AlumnoRoute)
        } catch (ex: Exception) {
            Log.d("AlumnoController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(AlumnoRoute)
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val alumno = alumnoModel.getById(id)
            if(alumno==null){
                return view.render {
                    NotFoundScreen("No se encontro la empresa",view.getUiProvider())
                }
            }
            return view.render {
                AlumnoEditScreen(alumno, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("AlumnoController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar el alumno.",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            alumnoModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(AlumnoRoute)
        } catch (ex: Exception) {
            Log.d("AlumnoController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(AlumnoRoute)
        }
    }
}