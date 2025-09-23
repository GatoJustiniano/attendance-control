package com.example.tallerarquitectura.controller

import com.example.tallerarquitectura.dto.ServiceNote
import com.example.tallerarquitectura.dto.ServiceNoteDetail

interface ActionServiceNoteListener {
    fun store(serviceNote: ServiceNote)
    fun update(serviceNote: ServiceNote)
    fun delete(id: Long)
    fun storeDetail(serviceNoteDetail: ServiceNoteDetail)
    fun destroyDetail(serviceNoteId: Long, productId: Long)
    fun updateDetail(serviceNoteDetail: ServiceNoteDetail)
}