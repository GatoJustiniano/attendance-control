package com.example.tallerarquitectura.view.navigation

import kotlinx.serialization.Serializable

//MeasureUnit
@Serializable
object MeasureUnitRoute

@Serializable
object MeasureUnitCreateRoute

@Serializable
data class MeasureUnitEditRoute(
    val id: Long
)

//Service
@Serializable
object ServiceRoute

@Serializable
object ServiceCreateRoute

@Serializable
data class ServiceEditRoute(
    val id: Long
)

//Enterprise
@Serializable
object EnterpriseRoute

@Serializable
object EnterpriseCreateRoute

@Serializable
data class EnterpriseEditRoute(
    val id: Long
)

//Car
@Serializable
object CarRoute

@Serializable
object CarCreateRoute

@Serializable
data class CarEditRoute(
    val id: Long
)

//Product
@Serializable
object ProductRoute

@Serializable
object ProductCreateRoute

@Serializable
data class ProductEditRoute(
    val id: Long
)

//SaleNotes
@Serializable
object ServiceNoteRoute

@Serializable
object ServiceNoteCreateRoute

@Serializable
data class ServiceNoteEditRoute(
    val id: Long
)

@Serializable
data class ServiceNoteShowRoute(
    val id: Long
)
@Serializable
data class  ServiceNoteDetailCreateRoute(
    val id: Long
)
@Serializable
data class  ServiceNoteDetailEditRoute(
    val serviceNoteId: Long,
    val productId: Long
)

//ReminderNote
@Serializable
data class ReminderNoteCreateRoute(
    val serviceNoteId: Long
)

@Serializable
object ReminderNoteRoute

@Serializable
data class ReminderNoteEditRoute(
    val serviceNoteId: Long
)






