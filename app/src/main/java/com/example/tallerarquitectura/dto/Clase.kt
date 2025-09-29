package com.example.tallerarquitectura.dto

data class Clase (
    val id: Long,
    val classDate: String?,
    val classCreate: String,
    val qrCode: String?,

    val horario: Horario,
    val grupo: Grupo?,
    val materia: Materia,
    val detail: List<DetalleClase> = emptyList()
)
