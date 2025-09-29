package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Grupo
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.GrupoRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.grupo.GrupoCreateScreen
import com.example.tallerarquitectura.view.screen.grupo.GrupoEditScreen
import com.example.tallerarquitectura.view.screen.grupo.GrupoScreen

class GrupoController(private val grupoModel: GrupoModel, private val view: View):
    ActionListener<Grupo> {

    fun index(): @Composable () -> Unit {
        try {
            val grupos = grupoModel.getAll()
            return view.render {
                GrupoScreen(grupos, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("GrupoController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los empresas, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            GrupoCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(grupo: Grupo){
        try {
            grupoModel.save(grupo)
            MainActivity.navManager.navigateCurrentTo(GrupoRoute)
        } catch (ex: Exception) {
            Log.d("GrupoController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(GrupoRoute)
        }
    }

    override fun update(grupo: Grupo){
        try {
            grupoModel.update(grupo)
            MainActivity.navManager.navigateCurrentTo(GrupoRoute)
        } catch (ex: Exception) {
            Log.d("GrupoController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(GrupoRoute)
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val grupo = grupoModel.getById(id)

            if(grupo==null){
                return view.render {
                    NotFoundScreen("No se encontró el grupo",view.getUiProvider())
                }
            }
            return view.render {
                GrupoEditScreen(grupo, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("GrupoController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar el grupo",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            grupoModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(GrupoRoute)
        } catch (ex: Exception) {
            Log.d("GrupoController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(GrupoRoute)
        }
    }
}
