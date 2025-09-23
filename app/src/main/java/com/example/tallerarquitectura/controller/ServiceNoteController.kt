package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.ServiceNote
import com.example.tallerarquitectura.dto.ServiceNoteDetail
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.model.ServiceNoteModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ServiceNoteRoute
import com.example.tallerarquitectura.view.navigation.ServiceNoteShowRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.service_note.ServiceNoteCreateScreen
import com.example.tallerarquitectura.view.screen.service_note.ServiceNoteDetailCreateScreen
import com.example.tallerarquitectura.view.screen.service_note.ServiceNoteDetailEditScreen
import com.example.tallerarquitectura.view.screen.service_note.ServiceNoteEditScreen
import com.example.tallerarquitectura.view.screen.service_note.ServiceNoteScreen
import com.example.tallerarquitectura.view.screen.service_note.ServiceNoteShowScreen

class ServiceNoteController(
    private val serviceNoteModel: ServiceNoteModel,
    private val serviceModel: ServiceModel,
    private val carModel: CarModel,
    private val productModel: ProductModel,
    private val enterpriseModel: EnterpriseModel,
    private val view: View
) : ActionServiceNoteListener {

    fun index(): @Composable () -> Unit {
        try {
            val servicesNotes = serviceNoteModel.getAll()
            return view.render {
                ServiceNoteScreen(servicesNotes, this, view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("ServiceNoteController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Intente mas tarde.", view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        val cars = carModel.getAll()
        val services = serviceModel.getAll()
        val enterprises = enterpriseModel.getAll()
        return view.render {
            ServiceNoteCreateScreen(cars, enterprises, services, this, view.getUiProvider())
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val serviceNote = serviceNoteModel.getById(id)
            if (serviceNote == null) {
                return view.render {
                    NotFoundScreen("No se encontro la nota de servicio", view.getUiProvider())
                }
            }
            val services = serviceModel.getAll()
            val cars = carModel.getAll()
            val enterprises = enterpriseModel.getAll()
            return view.render {
                ServiceNoteEditScreen(serviceNote,cars, enterprises, services, this, view.getUiProvider())
            }
        } catch (ex: Exception) {
            return view.render {
                InternalErrorScreen("Intente mas tarde.", view.getUiProvider())
            }
        }


    }


    override fun store(serviceNote: ServiceNote) {
        try {
            serviceNoteModel.save(serviceNote)
            MainActivity.navManager.navigateCurrentTo(ServiceNoteRoute)
        } catch (ex: Exception) {
            Log.d("ServiceNoteController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceNoteRoute)
        }
    }

    override fun update(serviceNote: ServiceNote) {
        try {
            serviceNoteModel.update(serviceNote)
            MainActivity.navManager.navigateCurrentTo(ServiceNoteRoute)
        }catch (ex: Exception){
            Log.d("ServiceNoteController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceNoteRoute)
        }

    }

    override fun delete(id: Long) {
        try {
            serviceNoteModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(ServiceNoteRoute)
        }catch (ex: Exception){
            Log.d("ServiceNoteController.delete", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceNoteRoute)
        }
    }

    fun show(id: Long): @Composable () -> Unit {
        try {
            val serviceNote = serviceNoteModel.getById(id)

            if (serviceNote == null) {
                return view.render {
                    NotFoundScreen(
                        "Nota de servicio no encontrado",
                        view.getUiProvider()
                    )
                }
            }
            return view.render {
                ServiceNoteShowScreen(
                    serviceNote,
                    this,
                    view.getUiProvider()
                )
            }
        } catch (ex: Exception) {
            Log.d("ServiceNoteController.getById", ex.message.toString())
            return view.render {
                InternalErrorScreen(
                    "Vuelva a intentarlo mas tarde.",
                    view.getUiProvider()
                )
            }
        }
    }

    fun createDetail(serviceNoteId: Long): @Composable (() -> Unit) {
        val products = productModel.getAll()
        return view.render {
            ServiceNoteDetailCreateScreen(
                serviceNoteId = serviceNoteId,
                products = products,
                this,
                uiProvider = view.getUiProvider()
            )
        }
    }

    override fun storeDetail(serviceNoteDetail: ServiceNoteDetail) {
        try {
            serviceNoteModel.addDetail(serviceNoteDetail)
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(serviceNoteDetail.serviceNoteId))
        } catch (ex: Exception) {
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(serviceNoteDetail.serviceNoteId))
            Log.d("ServiceNoteController.storeDetail", ex.message.toString())
        }
    }

    override fun destroyDetail(serviceNoteId: Long, productId: Long) {
        try {
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(serviceNoteId))
            serviceNoteModel.deleteDetail(serviceNoteId, productId)
        }catch (ex: Exception){
            Log.d("ServiceNoteController.destroyDetail", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(serviceNoteId))
        }
    }

    override fun updateDetail(serviceNoteDetail: ServiceNoteDetail) {
        try {
            serviceNoteModel.updateDetail(serviceNoteDetail)
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(serviceNoteDetail.serviceNoteId))
        }catch (ex: Exception){
            Log.d("ServiceNoteController.updateDetail", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ServiceNoteShowRoute(serviceNoteDetail.serviceNoteId))
        }

    }

    fun editDetail(serviceNoteId: Long,productId: Long): @Composable () -> Unit{
        try {
            val serviceNoteDetail = serviceNoteModel.getDetail(serviceNoteId,productId)
            if (serviceNoteDetail == null) {
                return view.render {
                    NotFoundScreen("No se encontro el detalle de la nota de servicio", view.getUiProvider())
                }
            }
            val products = productModel.getAll()
            return view.render {
                ServiceNoteDetailEditScreen(
                    serviceNoteDetail=serviceNoteDetail,
                    products = products,
                    this,
                    uiProvider = view.getUiProvider()
                )
            }
        } catch (ex: Exception) {
            Log.d("ServiceNoteController.editDetail", ex.message.toString())
            return view.render {
                InternalErrorScreen("Intente mas tarde.", view.getUiProvider())
            }
        }
    }

}