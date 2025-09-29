package com.example.tallerarquitectura.validation

import com.example.tallerarquitectura.dto.Alumno


fun detalleClaseFormValidate(
    code: String,
    alumno: Alumno?
): List<String> {
    val errors = mutableListOf<String>()
    if (code.trim().isEmpty() ){
        errors.add("Code es requerido.")
    }
    if (alumno==null){
        errors.add("Alumno es requerido.")
    }
    return errors
}