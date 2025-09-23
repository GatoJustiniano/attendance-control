package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Enterprise
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.EnterpriseRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.enterprise.EnterpriseCreateScreen
import com.example.tallerarquitectura.view.screen.enterprise.EnterpriseEditScreen
import com.example.tallerarquitectura.view.screen.enterprise.EnterpriseScreen

class EnterpriseController(private val enterpriseModel: EnterpriseModel, private val view: View):
    ActionListener<Enterprise> {

    fun index(): @Composable () -> Unit {
        try {
            val enterprises = enterpriseModel.getAll()
            return view.render {
                EnterpriseScreen(enterprises, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("EnterpriseController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los empresas, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            EnterpriseCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(enterprise: Enterprise){
        try {
            enterpriseModel.save(enterprise)
            MainActivity.navManager.navigateCurrentTo(EnterpriseRoute)
        } catch (ex: Exception) {
            Log.d("EnterpriseController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(EnterpriseRoute)
        }
    }

    override fun update(enterprise: Enterprise){
        try {
            enterpriseModel.update(enterprise)
            MainActivity.navManager.navigateCurrentTo(EnterpriseRoute)
        } catch (ex: Exception) {
            Log.d("EnterpriseController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(EnterpriseRoute)
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val enterprise = enterpriseModel.getById(id)

            if(enterprise==null){
                return view.render {
                    NotFoundScreen("No se encontro la empresa",view.getUiProvider())
                }
            }
            return view.render {
                EnterpriseEditScreen(enterprise, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("EnterpriseController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar la empresa",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            enterpriseModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(EnterpriseRoute)
        } catch (ex: Exception) {
            Log.d("EnterpriseController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(EnterpriseRoute)
        }
    }
}
