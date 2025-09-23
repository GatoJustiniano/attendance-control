package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.Service

class ServiceModel(private val connection: CarAtelierDbHelper=CarAtelierDbHelper.getInstance()) {
    fun getAll(): List<Service> {
        val db = connection.readableDatabase
        val sortOrder = "${CarAtelierContract.Service.COLUMN_NAME_NAME} DESC"
        val cursor = db.query(
            CarAtelierContract.Service.TABLE_NAME, null, null, null, null, null, sortOrder
        )
        val data = mutableListOf<Service>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(CarAtelierContract.Service.COLUMN_NAME_ID))
                val name =
                    getString(getColumnIndexOrThrow(CarAtelierContract.Service.COLUMN_NAME_NAME))
                val urlImage =
                    getStringOrNull(getColumnIndexOrThrow(CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE))
                data.add(Service(id, name, urlImage))
            }
        }
        cursor.close()
        return data
    }

    fun getById(id: Long): Service? {
        val db = connection.readableDatabase
        val selection = "${CarAtelierContract.Service.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            CarAtelierContract.Service.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )
        var service: Service? = null
        with(cursor) {
            if (moveToFirst()) {
                val name =
                    getString(getColumnIndexOrThrow(CarAtelierContract.Service.COLUMN_NAME_NAME))
                val urlImage =
                    getString(getColumnIndexOrThrow(CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE))
                service = Service(id, name, urlImage)
            }
        }
        cursor.close()
        return service
    }

    fun save(service: Service): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Service.COLUMN_NAME_NAME, service.name)
            put(CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE, service.urlImage)
        }
        return db.insert(CarAtelierContract.Service.TABLE_NAME, null, values)
    }
    fun update(service: Service): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Service.COLUMN_NAME_NAME, service.name)
            put(CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE, service.urlImage)
        }
        val selection = "${CarAtelierContract.Service.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(service.id.toString())
        return db.update(
            CarAtelierContract.Service.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${CarAtelierContract.Service.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(CarAtelierContract.Service.TABLE_NAME, selection, selectionArgs)
    }
}