package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Materia
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.MateriaRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.materia.MateriaCreateScreen
import com.example.tallerarquitectura.view.screen.materia.MateriaEditScreen
import com.example.tallerarquitectura.view.screen.materia.MateriaScreen

class MateriaController(private val materiaModel: MateriaModel, private val view: View):
    ActionListener<Materia> {
    fun index(): @Composable () -> Unit {
        try {
            val materias = materiaModel.getAll()
            return view.render {
                MateriaScreen(materias, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("MateriaController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener las materias, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable (() -> Unit) {
        return view.render {
            MateriaCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(materia: Materia){
        try {
            materiaModel.save(materia)
            MainActivity.navManager.navigateCurrentTo(MateriaRoute)
        } catch (ex: Exception) {
            Log.d("MateriaController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(MateriaRoute)
        }
    }

    override fun update(materia: Materia){
        try {
            materiaModel.update(materia)
            MainActivity.navManager.navigateCurrentTo(MateriaRoute)
        } catch (ex: Exception) {
            Log.d("MateriaController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(MateriaRoute)
        }
    }

    fun edit(id: Long): @Composable (() -> Unit) {
        try {
            val materia = materiaModel.getById(id)
            if(materia==null){
                return view.render {
                    NotFoundScreen("No se encontró la materia",view.getUiProvider())
                }
            }
            return view.render {
                MateriaEditScreen(materia, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("MateriaController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar materia",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            materiaModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(MateriaRoute)
        } catch (ex: Exception) {
            Log.d("MateriaController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(MateriaRoute)
        }
    }
}