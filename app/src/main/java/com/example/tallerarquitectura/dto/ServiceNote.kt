package com.example.tallerarquitectura.dto

data class ServiceNote (
    val id: Long,
    val serviceDate: String?,
    val creationDate: String,
    val voucherCode: String?,
    val voucherUrlImage: String?,

    val car: Car,
    val enterprise: Enterprise?,
    val service: Service,
    val reminderNote: ReminderNoteWithMeasureUnit?=null,
    val detail: List<ServiceNoteDetail> = emptyList()
)
