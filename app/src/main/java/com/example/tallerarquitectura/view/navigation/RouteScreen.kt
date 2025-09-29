package com.example.tallerarquitectura.view.navigation

import kotlinx.serialization.Serializable

//Materia
@Serializable
object MateriaRoute

@Serializable
object MateriaCreateRoute

@Serializable
data class MateriaEditRoute(
    val id: Long
)

//Grupo
@Serializable
object GrupoRoute

@Serializable
object GrupoCreateRoute

@Serializable
data class GrupoEditRoute(
    val id: Long
)

//Horario
@Serializable
object HorarioRoute

@Serializable
object HorarioCreateRoute

@Serializable
data class HorarioEditRoute(
    val id: Long
)

//Alumno
@Serializable
object AlumnoRoute

@Serializable
object AlumnoCreateRoute

@Serializable
data class AlumnoEditRoute(
    val id: Long
)

//SaleNotes
@Serializable
object ClaseRoute

@Serializable
object ClaseCreateRoute

@Serializable
data class ClaseEditRoute(
    val id: Long
)

@Serializable
data class ClaseShowRoute(
    val id: Long
)
@Serializable
data class  DetalleClaseCreateRoute(
    val id: Long
)
@Serializable
data class  DetalleClaseEditRoute(
    val clase_id: Long,
    val alumno_id: Long
)






