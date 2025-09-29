package com.example.tallerarquitectura.dto

data class DetalleClase(
    val code: Int,
    val alumno_id: Long,
    val clase_id: Long,
    val alumno: Alumno
)
