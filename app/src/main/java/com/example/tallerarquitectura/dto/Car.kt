package com.example.tallerarquitectura.dto

data class Car(
    val id: Long,
    val plate: String,
    val alias: String?,
    val model: String,
    val cylinder: String,
    val urlImage: String?,
    val mark: String,
    val year: Int
)
