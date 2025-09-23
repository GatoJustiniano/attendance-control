package com.example.tallerarquitectura.dto


data class ReminderNoteWithMeasureUnit(
    val id: Long,
    val endDate: String,
    val endOtherData: Int,
    val status: Int,
    val serviceNoteId: Long,
    val measureUnit: MeasureUnit?
)

data class ReminderNoteCreate(
    val id: Long,
    val endDate: String,
    val endOtherData: Int,
    val status: Int,
    val serviceNoteId: Long,
    val measureUnitId: Long?,
)