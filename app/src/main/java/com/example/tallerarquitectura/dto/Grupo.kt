package com.example.tallerarquitectura.dto

data class Grupo(
    val id: Long,
    val name: String,
    val locationName: String?,
    val latitude: Double?,
    val longitude: Double?,
    val urlImage: String?
)
