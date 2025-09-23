package com.example.tallerarquitectura.controller

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.dto.Car
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.view.View
import com.example.tallerarquitectura.view.navigation.CarRoute
import com.example.tallerarquitectura.view.screen.InternalErrorScreen
import com.example.tallerarquitectura.view.screen.NotFoundScreen
import com.example.tallerarquitectura.view.screen.car.CarCreateScreen
import com.example.tallerarquitectura.view.screen.car.CarEditScreen
import com.example.tallerarquitectura.view.screen.car.CarScreen

class CarController(private val carModel: CarModel, private val view: View): ActionListener<Car> {

    fun index(): @Composable () -> Unit {
        try {
            val cars = carModel.getAll()
            return view.render {
                CarScreen(cars, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("CarController.index", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al obtener los empresas, vuelva a intentarlo mas tarde.",view.getUiProvider())
            }
        }
    }

    fun create(): @Composable () -> Unit {
        return view.render {
            CarCreateScreen(this,view.getUiProvider())
        }
    }

    override fun store(car: Car){
        try {
            carModel.save(car)
            MainActivity.navManager.navigateCurrentTo(CarRoute)
        } catch (ex: Exception) {
            Log.d("CarController.store", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(CarRoute)
        }
    }

    override fun update(car: Car){
        try {
            carModel.update(car)
            MainActivity.navManager.navigateCurrentTo(CarRoute)
        } catch (ex: Exception) {
            Log.d("CarController.update", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(CarRoute)
        }
    }

    fun edit(id: Long): @Composable () -> Unit {
        try {
            val car = carModel.getById(id)

            if(car==null){
                return view.render {
                    NotFoundScreen("No se encontro la empresa",view.getUiProvider())
                }
            }
            return view.render {
                CarEditScreen(car, this,view.getUiProvider())
            }
        } catch (ex: Exception) {
            Log.d("CarController.edit", ex.message.toString())
            return view.render {
                InternalErrorScreen("Error al intentar cargar la empresa",view.getUiProvider())
            }
        }
    }

    override fun destroy(id: Long){
        try {
            carModel.delete(id)
            MainActivity.navManager.navigateCurrentTo(CarRoute)
        } catch (ex: Exception) {
            Log.d("CarController.destroy", ex.message.toString())
            MainActivity.navManager.navigateCurrentTo(CarRoute)
        }
    }
}
