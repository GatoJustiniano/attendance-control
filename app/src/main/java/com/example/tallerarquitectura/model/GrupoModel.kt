package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.AttendanceControlData
import com.example.tallerarquitectura.dblocal.AttendanceControlDbHelper
import com.example.tallerarquitectura.dto.Grupo

class GrupoModel(private val connection: AttendanceControlDbHelper=AttendanceControlDbHelper.getInstance()) {
    fun getAll(): List<Grupo> {
        val db = connection.readableDatabase
        val sortOrder = "${AttendanceControlData.Grupo.COLUMN_NAME_NAME} DESC"
        val query = db.query(
            AttendanceControlData.Grupo.TABLE_NAME, null, null, null, null, null, sortOrder
        )

        return query.use {
            val id=it.getColumnIndexOrThrow(AttendanceControlData.Grupo.COLUMN_NAME_ID)
            val name=it.getColumnIndexOrThrow(AttendanceControlData.Grupo.COLUMN_NAME_NAME)

            val data = mutableListOf<Grupo>()

            while (it.moveToNext()){
                data.add(
                    Grupo(
                        id=it.getLong(id),
                        name=it.getString(name)
                    )
                )
            }
            data
        }
    }

    fun getById(id: Long): Grupo? {
        val db = connection.readableDatabase
        val selection = "${AttendanceControlData.Grupo.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val query = db.query(
            AttendanceControlData.Grupo.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )

        return query.use {
            val id=it.getColumnIndexOrThrow(AttendanceControlData.Grupo.COLUMN_NAME_ID)
            val name=it.getColumnIndexOrThrow(AttendanceControlData.Grupo.COLUMN_NAME_NAME)

            if(it.moveToFirst()){
                Grupo(
                    id=it.getLong(id),
                    name=it.getString(name)
                )
            }else{
                null
            }
        }
    }

    fun save(grupo: Grupo): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Grupo.COLUMN_NAME_NAME, grupo.name)
        }
        return db.insert(AttendanceControlData.Grupo.TABLE_NAME, null, values)
    }

    fun update(grupo: Grupo): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(AttendanceControlData.Grupo.COLUMN_NAME_NAME, grupo.name)
        }
        val selection = "${AttendanceControlData.Grupo.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(grupo.id.toString())
        return db.update(
            AttendanceControlData.Grupo.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${AttendanceControlData.Grupo.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(AttendanceControlData.Grupo.TABLE_NAME, selection, selectionArgs)
    }
}