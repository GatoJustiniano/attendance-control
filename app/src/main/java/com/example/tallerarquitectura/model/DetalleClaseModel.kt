package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Alumno
import com.example.tallerarquitectura.dto.DetalleClase

class DetalleClaseModel(
    private val connection:AttendanceControlDbHelper=AttendanceControlDbHelper.getInstance()
){
    fun getByClaseId(clase_id: Long): List<DetalleClase> {
        val db = connection.readableDatabase
        val query=db.rawQuery(
            """
                SELECT 
                * 
                FROM ${AttendanceControlData.DetalleClase.TABLE_NAME}
                JOIN ${AttendanceControlData.Alumno.TABLE_NAME} ON ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ${AttendanceControlData.Alumno.COLUMN_NAME_ID}
                WHERE ${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ?
                
            """.trimIndent(),arrayOf(clase_id.toString())
        )
        val serviceNoteDetails = mutableListOf<DetalleClase>()
        return query.use {
            val clase_id= it.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID)
            val alumno_code= it.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CODE)
            val detallaClaseAlumno= it.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID)

            val alumno_id= it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_ID)
            val alumno_name= it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_NAME)
            val alumno_urlImage= it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE)

            while (it.moveToNext()) {
                val detalleClase = DetalleClase(
                    clase_id = it.getLong(clase_id),
                    alumno_id = it.getLong(detallaClaseAlumno),
                    code = it.getInt(alumno_code),
                    alumno = Alumno(
                        id = it.getLong(alumno_id),
                        name = it.getString(alumno_name),
                        urlImage = it.getStringOrNull(alumno_urlImage)
                    ),

                )
                serviceNoteDetails.add(detalleClase)
            }
            serviceNoteDetails
        }
    }

    fun getById(serviceNotId: Long,alumno_id: Long): DetalleClase? {
        val db = connection.readableDatabase
        val query=db.rawQuery(
            """
                SELECT 
                * 
                FROM ${AttendanceControlData.DetalleClase.TABLE_NAME}
                JOIN ${AttendanceControlData.Alumno.TABLE_NAME} ON ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ${AttendanceControlData.Alumno.COLUMN_NAME_ID}
                WHERE ${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ? AND
                ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ?
                
            """.trimIndent(),arrayOf(serviceNotId.toString(),alumno_id.toString())
        )
        return query.use {
            val clase_id= it.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID)
            val alumno_code= it.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CODE)
            val detallaClaseAlumno= it.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID)

            val alumno_id= it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_ID)
            val alumno_name= it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_NAME)
            val alumno_urlImage= it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE)

            if (it.moveToFirst()) {
                DetalleClase(
                    clase_id = it.getLong(clase_id),
                    alumno_id = it.getLong(detallaClaseAlumno),
                    code = it.getInt(alumno_code),
                    alumno = Alumno(
                        id = it.getLong(alumno_id),
                        name = it.getString(alumno_name),
                        urlImage = it.getStringOrNull(alumno_urlImage)
                    ),

                )
            } else {
                null
            }
        }
    }

    fun save(detalleClase: DetalleClase): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID, detalleClase.clase_id)
            put(AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID, detalleClase.alumno_id)
            put(AttendanceControlData.DetalleClase.COLUMN_NAME_CODE, detalleClase.code)
        }
        return db.insert(AttendanceControlData.DetalleClase.TABLE_NAME, null, values)
    }

    fun update(detalleClase: DetalleClase): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID, detalleClase.clase_id)
            put(AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID, detalleClase.alumno_id)
            put(AttendanceControlData.DetalleClase.COLUMN_NAME_CODE, detalleClase.code)
        }
        return db.update(
            AttendanceControlData.DetalleClase.TABLE_NAME,
            values,
            "${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ? AND ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ?",
            arrayOf(detalleClase.clase_id.toString(),detalleClase.alumno_id.toString())
        )
    }

    fun delete(clase_id: Long,alumno_id: Long): Int {
        val db = connection.writableDatabase
        return db.delete(
            AttendanceControlData.DetalleClase.TABLE_NAME,
            "${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ? AND ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ?",
            arrayOf(clase_id.toString(),alumno_id.toString())
        )
    }
}
