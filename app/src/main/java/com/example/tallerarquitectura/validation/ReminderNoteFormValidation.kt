package com.example.tallerarquitectura.validation

fun reminderNoteFormValidate(
    endDate: String,
): List<String> {
    val errors = mutableListOf<String>()
    if (endDate.trim().isEmpty()) {
        errors.add("Fecha final es requerido.")
    }
    return errors
}