package com.example.tallerarquitectura.dto

data class ServiceNoteDetail(
    val quantity: Int,
    val price: Double,
    val productId: Long,
    val serviceNoteId: Long,
    val product: Product
)
