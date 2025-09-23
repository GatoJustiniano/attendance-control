package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.Enterprise

class EnterpriseModel(private val connection: CarAtelierDbHelper=CarAtelierDbHelper.getInstance()) {
    fun getAll(): List<Enterprise> {
        val db = connection.readableDatabase
        val sortOrder = "${CarAtelierContract.Enterprise.COLUMN_NAME_NAME} DESC"
        val query = db.query(
            CarAtelierContract.Enterprise.TABLE_NAME, null, null, null, null, null, sortOrder
        )

        return query.use {
            val id=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_ID)
            val name=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_NAME)
            val locationName=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME)
            val latitude=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE)
            val longitude=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE)
            val urlImage=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE)

            val data = mutableListOf<Enterprise>()

            while (it.moveToNext()){
                data.add(
                    Enterprise(
                        id=it.getLong(id),
                        name=it.getString(name),
                        locationName=it.getStringOrNull(locationName),
                        latitude=it.getDoubleOrNull(latitude),
                        longitude=it.getDoubleOrNull(longitude),
                        urlImage=it.getStringOrNull(urlImage)
                    )
                )
            }
            data
        }
    }

    fun getById(id: Long): Enterprise? {
        val db = connection.readableDatabase
        val selection = "${CarAtelierContract.Enterprise.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val query = db.query(
            CarAtelierContract.Enterprise.TABLE_NAME, null, selection, selectionArgs, null, null, null
        )

        return query.use {
            val id=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_ID)
            val name=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_NAME)
            val locationName=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME)
            val latitude=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE)
            val longitude=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE)
            val urlImage=it.getColumnIndexOrThrow(CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE)

            if(it.moveToFirst()){
                Enterprise(
                    id=it.getLong(id),
                    name=it.getString(name),
                    locationName=it.getStringOrNull(locationName),
                    latitude=it.getDoubleOrNull(latitude),
                    longitude=it.getDoubleOrNull(longitude),
                    urlImage=it.getStringOrNull(urlImage)
                )
            }else{
                null
            }
        }
    }

    fun save(enterprise: Enterprise): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Enterprise.COLUMN_NAME_NAME, enterprise.name)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME, enterprise.locationName)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE, enterprise.latitude)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE, enterprise.longitude)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE, enterprise.urlImage)
        }
        return db.insert(CarAtelierContract.Enterprise.TABLE_NAME, null, values)
    }

    fun update(enterprise: Enterprise): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.Enterprise.COLUMN_NAME_NAME, enterprise.name)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME, enterprise.locationName)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE, enterprise.latitude)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE, enterprise.longitude)
            put(CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE, enterprise.urlImage)
        }
        val selection = "${CarAtelierContract.Enterprise.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(enterprise.id.toString())
        return db.update(
            CarAtelierContract.Enterprise.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        val selection = "${CarAtelierContract.Enterprise.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(CarAtelierContract.Enterprise.TABLE_NAME, selection, selectionArgs)
    }
}