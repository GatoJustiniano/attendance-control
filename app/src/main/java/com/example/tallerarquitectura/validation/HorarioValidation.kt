package com.example.tallerarquitectura.validation

 fun horarioFormValidate(
    name: String,
    starttime: String
): List<String> {
    val errors = mutableListOf<String>()
    if (name.trim().isEmpty()) {
        errors.add("Horario nombre es requerido.")
    }
    if (starttime.trim().isEmpty()) {
        errors.add("Desde es requerido.")
    }
    
    return errors
}