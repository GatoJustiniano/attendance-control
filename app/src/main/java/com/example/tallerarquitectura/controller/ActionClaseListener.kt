package com.example.tallerarquitectura.controller

import com.example.tallerarquitectura.dto.Clase
import com.example.tallerarquitectura.dto.DetalleClase

interface ActionClaseListener {
    fun store(clase: Clase)
    fun update(clase: Clase)
    fun delete(id: Long)
    fun storeDetail(detalleClase: DetalleClase)
    fun destroyDetail(clase_id: Long, alumno_id: Long)
    fun updateDetail(detalleClase: DetalleClase)
}