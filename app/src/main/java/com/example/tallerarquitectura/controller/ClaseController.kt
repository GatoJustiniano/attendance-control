package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Clase
import com.example.tallerarquitectura.dto.DetalleClase
import com.example.tallerarquitectura.model.HorarioModel
import com.example.tallerarquitectura.model.GrupoModel
import com.example.tallerarquitectura.model.AlumnoModel
import com.example.tallerarquitectura.model.MateriaModel
import com.example.tallerarquitectura.model.ClaseModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.ClaseRoute
import com.example.tallerarquitectura.view.navigation.ClaseShowRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.clase.ClaseCreateScreen
import com.example.tallerarquitectura.view.screen.clase.DetalleClaseCreateScreen
import com.example.tallerarquitectura.view.screen.clase.DetalleClaseEditScreen
import com.example.tallerarquitectura.view.screen.clase.ClaseEditScreen
import com.example.tallerarquitectura.view.screen.clase.ClaseScreen
import com.example.tallerarquitectura.view.screen.clase.ClaseShowScreen

class ClaseController(
    private val claseModel: ClaseModel,
    private val materiaModel: MateriaModel,
    private val horarioModel: HorarioModel,
    private val alumnoModel: AlumnoModel,
    private val grupoModel: GrupoModel,
    private val view: View
) : ActionClaseListener {

    fun index(): @Composable () -> Unit {
        try {
            val servicesNotes = claseModel.getAll()
            return view.render {
                ClaseScreen(servicesNotes, this, view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("ClaseController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Intente mas tarde.", view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        val horarios = horarioModel.getAll()
        val materias = materiaModel.getAll()
        val grupos = grupoModel.getAll()
        return view.render {
            ClaseCreateScreen(horarios, grupos, materias, this, view.getUiProvider())
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val clase = claseModel.getById(id)
            if (clase == null) {
                return view.render {
                    NotFoundScreen("No se encontro la clase", view.getUiProvider())
                }
            }
            val materias = materiaModel.getAll()
            val horarios = horarioModel.getAll()
            val grupos = grupoModel.getAll()
            return view.render {
                ClaseEditScreen(clase,horarios, grupos, materias, this, view.getUiProvider())
            }
        } catch (ex: Exception) {
            return view.render {
                InternalErrorScreen("Intente mas tarde.", view.getUiProvider())
            }
        }


    }


    override fun store(clase: Clase) {
        try {
            claseModel.save(clase)
            MainActivity.navManager.navigateCurrentTo(ClaseRoute)
        } catch (ex: Exception) {
            Log.d("ClaseController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ClaseRoute)
        }
    }

    override fun update(clase: Clase) {
        try {
            claseModel.update(clase)
            MainActivity.navManager.navigateCurrentTo(ClaseRoute)
        }catch (ex: Exception){
            Log.d("ClaseController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ClaseRoute)
        }

    }

    override fun delete(id: Long) {
        try {
            claseModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(ClaseRoute)
        }catch (ex: Exception){
            Log.d("ClaseController.delete", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ClaseRoute)
        }
    }

    fun show(id: Long): @Composable () -> Unit {
        try {
            val clase = claseModel.getById(id)

            if (clase == null) {
                return view.render {
                    NotFoundScreen(
                        "Clase no encontrado",
                        view.getUiProvider()
                    )
                }
            }
            return view.render {
                ClaseShowScreen(
                    clase,
                    this,
                    view.getUiProvider()
                )
            }
        } catch (ex: Exception) {
            Log.d("ClaseController.getById", ex.message.toString())
            return view.render {
                InternalErrorScreen(
                    "Vuelva a intentarlo mas tarde.",
                    view.getUiProvider()
                )
            }
        }
    }

    fun createDetail(clase_id: Long): @Composable (() -> Unit) {
        val alumnos = alumnoModel.getAll()
        return view.render {
            DetalleClaseCreateScreen(
                clase_id = clase_id,
                alumnos = alumnos,
                this,
                uiProvider = view.getUiProvider()
            )
        }
    }

    override fun storeDetail(detalleClase: DetalleClase) {
        try {
            claseModel.addDetail(detalleClase)
            MainActivity.navManager.navigateCurrentTo(ClaseShowRoute(detalleClase.clase_id))
        } catch (ex: Exception) {
            MainActivity.navManager.navigateCurrentTo(ClaseShowRoute(detalleClase.clase_id))
            Log.d("ClaseController.storeDetail", ex.message.toString())
        }
    }

    override fun destroyDetail(clase_id: Long, alumno_id: Long) {
        try {
            MainActivity.navManager.navigateCurrentTo(ClaseShowRoute(clase_id))
            claseModel.deleteDetail(clase_id, alumno_id)
        }catch (ex: Exception){
            Log.d("ClaseController.destroyDetail", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ClaseShowRoute(clase_id))
        }
    }

    override fun updateDetail(detalleClase: DetalleClase) {
        try {
            claseModel.updateDetail(detalleClase)
            MainActivity.navManager.navigateCurrentTo(ClaseShowRoute(detalleClase.clase_id))
        }catch (ex: Exception){
            Log.d("ClaseController.updateDetail", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(ClaseShowRoute(detalleClase.clase_id))
        }

    }

    fun editDetail(clase_id: Long,alumno_id: Long): @Composable () -> Unit{
        try {
            val detalleClase = claseModel.getDetail(clase_id,alumno_id)
            if (detalleClase == null) {
                return view.render {
                    NotFoundScreen("No se encontro el detalle de la nota de servicio", view.getUiProvider())
                }
            }
            val alumnos = alumnoModel.getAll()
            return view.render {
                DetalleClaseEditScreen(
                    detalleClase=detalleClase,
                    alumnos = alumnos,
                    this,
                    uiProvider = view.getUiProvider()
                )
            }
        } catch (ex: Exception) {
            Log.d("ClaseController.editDetail", ex.message.toString())
            return view.render {
                InternalErrorScreen("Intente mas tarde.", view.getUiProvider())
            }
        }
    }

}