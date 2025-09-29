package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Alumno
import com.example.tallerarquitectura.dto.DetalleClase

class DetalleClaseModel(
    private val connection: AttendanceControlDbHelper = AttendanceControlDbHelper.getInstance()
) {

    fun getByClaseId(claseId: Long): List<DetalleClase> {
        val db = connection.readableDatabase
        val query = db.rawQuery(
            """
                SELECT * 
                FROM ${AttendanceControlData.DetalleClase.TABLE_NAME}
                JOIN ${AttendanceControlData.Alumno.TABLE_NAME}
                ON ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ${AttendanceControlData.Alumno.COLUMN_NAME_ID}
                WHERE ${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ?
            """.trimIndent(),
            arrayOf(claseId.toString())
        )

        val detalleClaseList = mutableListOf<DetalleClase>()

        return query.use { cursor ->
            val idxClaseId = cursor.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID)
            val idxAlumnoCode = cursor.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CODE)
            val idxDetalleClaseAlumno = cursor.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID)

            val idxAlumnoId = cursor.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_ID)
            val idxAlumnoName = cursor.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_NAME)
            val idxAlumnoUrlImage = cursor.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE)

            while (cursor.moveToNext()) {
                val detalle = DetalleClase(
                    clase_id = cursor.getLong(idxClaseId),
                    alumno_id = cursor.getLong(idxDetalleClaseAlumno),
                    code = cursor.getInt(idxAlumnoCode),
                    alumno = Alumno(
                        id = cursor.getLong(idxAlumnoId),
                        name = cursor.getString(idxAlumnoName),
                        urlImage = cursor.getStringOrNull(idxAlumnoUrlImage)
                    )
                )
                detalleClaseList.add(detalle)
            }
            detalleClaseList
        }
    }

    fun getById(claseId: Long, alumnoId: Long): DetalleClase? {
        val db = connection.readableDatabase
        val query = db.rawQuery(
            """
                SELECT * 
                FROM ${AttendanceControlData.DetalleClase.TABLE_NAME}
                JOIN ${AttendanceControlData.Alumno.TABLE_NAME}
                ON ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ${AttendanceControlData.Alumno.COLUMN_NAME_ID}
                WHERE ${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ? 
                  AND ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ?
            """.trimIndent(),
            arrayOf(claseId.toString(), alumnoId.toString())
        )

        return query.use { cursor ->
            val idxClaseId = cursor.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID)
            val idxAlumnoCode = cursor.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_CODE)
            val idxDetalleClaseAlumno = cursor.getColumnIndexOrThrow(AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID)

            val idxAlumnoId = cursor.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_ID)
            val idxAlumnoName = cursor.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_NAME)
            val idxAlumnoUrlImage = cursor.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE)

            if (cursor.moveToFirst()) {
                DetalleClase(
                    clase_id = cursor.getLong(idxClaseId),
                    alumno_id = cursor.getLong(idxDetalleClaseAlumno),
                    code = cursor.getInt(idxAlumnoCode),
                    alumno = Alumno(
                        id = cursor.getLong(idxAlumnoId),
                        name = cursor.getString(idxAlumnoName),
                        urlImage = cursor.getStringOrNull(idxAlumnoUrlImage)
                    )
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
            arrayOf(detalleClase.clase_id.toString(), detalleClase.alumno_id.toString())
        )
    }

    fun delete(claseId: Long, alumnoId: Long): Int {
        val db = connection.writableDatabase
        return db.delete(
            AttendanceControlData.DetalleClase.TABLE_NAME,
            "${AttendanceControlData.DetalleClase.COLUMN_NAME_CLASE_ID} = ? AND ${AttendanceControlData.DetalleClase.COLUMN_NAME_ALUMNO_ID} = ?",
            arrayOf(claseId.toString(), alumnoId.toString())
        )
    }
}
