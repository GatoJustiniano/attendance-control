package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.model.MeasureUnitModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.MeasureUnitRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.measure_unit.MeasureUnitCreateScreen
import com.example.tallerarquitectura.view.screen.measure_unit.MeasureUnitEditScreen
import com.example.tallerarquitectura.view.screen.measure_unit.MeasureUnitScreen
import com.example.tallerarquitectura.view.screen.service.ServiceCreateScreen

class MeasureUnitController(
    private val measureUnitModel: MeasureUnitModel, private val view: View
) : ActionListener<MeasureUnit> {
    fun index(): @Composable () -> Unit {
        try {
            val measuresUnit = measureUnitModel.getAll()
            return view.render {
                MeasureUnitScreen(measuresUnit, this, view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("MeasureUnitController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen(
                    "Error al obtener las unidades de medida, vuelva a intentarlo mas tarde",
                    view.getUiProvider()
                )
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            MeasureUnitCreateScreen(this, view.getUiProvider())
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val measureUnit = measureUnitModel.getById(id)

            if (measureUnit == null) {
                return view.render {
                    NotFoundScreen("No se encontro la unidad de medida", view.getUiProvider())
                }
            }
            return view.render {
                MeasureUnitEditScreen(measureUnit, this, view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("MeasureUnitController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen(
                    "Error al intentar cargar la medida de unidad", view.getUiProvider()
                )
            }
        }
    }

    override fun store(data: MeasureUnit) {
        try {
            measureUnitModel.save(data)
            MainActivity.navManager.navigateCurrentTo(MeasureUnitRoute)
        } catch (ex: Exception) {
            Log.d("MeasureUnitController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(MeasureUnitRoute)
        }
    }

    override fun update(data: MeasureUnit) {
        try {
            measureUnitModel.update(data)
            MainActivity.navManager.navigateCurrentTo(MeasureUnitRoute)
        } catch (ex: Exception) {
            Log.d("MeasureUnitController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(MeasureUnitRoute)
        }
    }

    override fun destroy(id: Long) {
        try {
            measureUnitModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(MeasureUnitRoute)
        } catch (ex: Exception) {
            Log.d("MeasureUnitController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(MeasureUnitRoute)
        }
    }

}