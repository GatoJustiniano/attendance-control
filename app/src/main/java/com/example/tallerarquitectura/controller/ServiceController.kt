package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Service
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.MeasureUnitRoute
import com.example.tallerarquitectura.view.navigation.ServiceRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.service.ServiceCreateScreen
import com.example.tallerarquitectura.view.screen.service.ServiceEditScreen
import com.example.tallerarquitectura.view.screen.service.ServiceScreen

class ServiceController(private val serviceModel: ServiceModel,private val view: View):
    ActionListener<Service> {
    fun index(): @Composable () -> Unit {
        try {
            val services = serviceModel.getAll()
            return view.render {
                ServiceScreen(services, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("ServiceController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los servicios, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable (() -> Unit) {
        return view.render {
            ServiceCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(service: Service){
        try {
            serviceModel.save(service)
            MainActivity.navManager.navigateCurrentTo(ServiceRoute)
        } catch (ex: Exception) {
            Log.d("ServiceController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceRoute)
        }
    }

    override fun update(service: Service){
        try {
            serviceModel.update(service)
            MainActivity.navManager.navigateCurrentTo(ServiceRoute)
        } catch (ex: Exception) {
            Log.d("ServiceController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceRoute)
        }
    }

    fun edit(id: Long): @Composable (() -> Unit) {
        try {
            val service = serviceModel.getById(id)
            if(service==null){
                return view.render {
                    NotFoundScreen("No se encontro el servicio",view.getUiProvider())
                }
            }
            return view.render {
                ServiceEditScreen(service, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("ServiceController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar el servicio",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            serviceModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(ServiceRoute)
        } catch (ex: Exception) {
            Log.d("ServiceController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceRoute)
        }
    }
}