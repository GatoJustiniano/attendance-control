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
        errors.add("Año es requerido.")
    }
    if(starttime.trim().isNotEmpty()){
        val yearInt = starttime.toIntOrNull()
        if (yearInt == null || yearInt < 1900 || yearInt > 2100) {
            errors.add("Año no es válido.")
        }
    }
    return errors
}