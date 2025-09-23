package com.example.tallerarquitectura.validation

import com.example.tallerarquitectura.dto.Car
import com.example.tallerarquitectura.dto.Enterprise
import com.example.tallerarquitectura.dto.Service

fun serviceNoteFormValidate(
    car: Car?,
    service: Service?
): List<String> {
    val errors = mutableListOf<String>()
    if(car == null) {
        errors.add("Vehiculo es requerido.")
    }
    if(service==null){
        errors.add("Servicio es requerido.")
    }
    return errors
}