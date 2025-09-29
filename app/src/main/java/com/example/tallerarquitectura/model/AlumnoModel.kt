package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Alumno

class AlumnoModel(private val connection: AttendanceControlDbHelper=AttendanceControlDbHelper.getInstance()) {
    fun getAll(): List<Alumno> {
        val db = connection.readableDatabase
        val sortOrder = "${AttendanceControlData.Alumno.COLUMN_NAME_NAME} DESC"
        val query = db.query(
            AttendanceControlData.Alumno.TABLE_NAME, null, null, null, null, null, sortOrder
        )
        val data = mutableListOf<Alumno>()
        return query.use {
            val id = it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_ID)
            val name = it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_NAME)
            val urlImage = it.getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE)

            while (it.moveToNext()) {
                data.add(
                    Alumno(
                        id = it.getLong(id),
                        name = it.getString(name),
                        urlImage = it.getStringOrNull(urlImage)
                    )
                )
            }
            data
        }
    }

    fun getById(id: Long): Alumno? {
        val db = connection.readableDatabase
        val selection = "${AttendanceControlData.Alumno.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            AttendanceControlData.Alumno.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        var alumno: Alumno? = null
        with(cursor) {
            if (moveToFirst()) {
                val name =
                    getString(getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_NAME))
                val urlImage=
                    getStringOrNull(getColumnIndexOrThrow(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE))
                alumno = Alumno(id, name, urlImage)
            }
        }
        cursor.close()
        return alumno
    }

    fun save(alumno: Alumno): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Alumno.COLUMN_NAME_NAME, alumno.name)
            put(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE, alumno.urlImage)
        }
        return db.insert(AttendanceControlData.Alumno.TABLE_NAME, null, values)
    }

    fun update(alumno: Alumno): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Alumno.COLUMN_NAME_NAME, alumno.name)
            put(AttendanceControlData.Alumno.COLUMN_NAME_URL_IMAGE, alumno.urlImage)
        }
        val selection = "${AttendanceControlData.Alumno.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(alumno.id.toString())
        return db.update(
            AttendanceControlData.Alumno.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${AttendanceControlData.Alumno.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(AttendanceControlData.Alumno.TABLE_NAME, selection, selectionArgs)
    }
}