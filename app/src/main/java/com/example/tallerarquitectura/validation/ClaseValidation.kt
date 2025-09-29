package com.example.tallerarquitectura.validation

import com.example.tallerarquitectura.dto.Horario
import com.example.tallerarquitectura.dto.Grupo
import com.example.tallerarquitectura.dto.Materia

fun claseFormValidate(
    horario: Horario?,
    materia: Materia?
): List<String> {
    val errors = mutableListOf<String>()
    if(horario == null) {
        errors.add("Horario es requerido.")
    }
    if(materia==null){
        errors.add("Materia es requerido.")
    }
    return errors
}