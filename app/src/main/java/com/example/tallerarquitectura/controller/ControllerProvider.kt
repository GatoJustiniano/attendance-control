package com.example.tallerarquitectura.controller

import android.content.Context
import com.example.tallerarquitectura.MainActivity
import com.example.tallerarquitectura.model.CarModel
import com.example.tallerarquitectura.model.EnterpriseModel
import com.example.tallerarquitectura.model.MeasureUnitModel
import com.example.tallerarquitectura.model.ProductModel
import com.example.tallerarquitectura.model.ReminderNoteModel
import com.example.tallerarquitectura.model.ServiceModel
import com.example.tallerarquitectura.model.ServiceNoteModel
import com.example.tallerarquitectura.view.View

class ControllerProvider(
    private val context: Context = MainActivity.Companion.appContext
) {
    val measureUnitController= MeasureUnitController(
        measureUnitModel = MeasureUnitModel(),
        view = View()
    )

    val serviceController = ServiceController(
        serviceModel = ServiceModel(),
        view = View()
    )
    val enterpriseController = EnterpriseController(
        enterpriseModel = EnterpriseModel(),
        view = View()
    )
    val carController = CarController(
        carModel = CarModel(),
        view = View()
    )
    val productController= ProductController(
        productModel = ProductModel(),
        view = View()
    )
    val serviceNoteController= ServiceNoteController(
        serviceNoteModel = ServiceNoteModel(),
        serviceModel = ServiceModel(),
        carModel = CarModel(),
        enterpriseModel = EnterpriseModel(),
        view = View(),
        productModel = ProductModel(),
    )
    val reminderNoteController= ReminderNoteController(
        reminderNoteModel = ReminderNoteModel(),
        measureUnitModel = MeasureUnitModel(),
        view = View()
    )
}