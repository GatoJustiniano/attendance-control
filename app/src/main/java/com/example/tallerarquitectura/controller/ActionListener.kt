package com.example.tallerarquitectura.controller

interface ActionListener<T> {
    fun store(data:T)
    fun update(data:T)
    fun destroy(id: Long)
}