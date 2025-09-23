package com.example.tallerarquitectura.model

import android.content.ContentValues
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.Product
import com.example.tallerarquitectura.dto.ServiceNoteDetail

class ServiceNoteDetailModel(
    private val connection:CarAtelierDbHelper=CarAtelierDbHelper.getInstance()
){
    fun getByServiceNoteId(serviceNoteId: Long): List<ServiceNoteDetail> {
        val db = connection.readableDatabase
        val query=db.rawQuery(
            """
                SELECT 
                * 
                FROM ${CarAtelierContract.ServiceNoteDetail.TABLE_NAME}
                JOIN ${CarAtelierContract.Product.TABLE_NAME} ON ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID} = ${CarAtelierContract.Product.COLUMN_NAME_ID}
                WHERE ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID} = ?
                
            """.trimIndent(),arrayOf(serviceNoteId.toString())
        )
        val serviceNoteDetails = mutableListOf<ServiceNoteDetail>()
        return query.use {
            val serviceNoteId= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID)
            val productPrice= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRICE)
            val productQuantity= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_QUANTITY)
            val serviceDetailProduct= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID)

            val productId= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_ID)
            val productName= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_NAME)
            val productUrlImage= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE)
            val productDetail= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_DETAIL)

            while (it.moveToNext()) {
                val serviceNoteDetail = ServiceNoteDetail(
                    serviceNoteId = it.getLong(serviceNoteId),
                    productId = it.getLong(serviceDetailProduct),
                    price = it.getDouble(productPrice),
                    quantity = it.getInt(productQuantity),
                    product = Product(
                        id = it.getLong(productId),
                        name = it.getString(productName),
                        detail = it.getStringOrNull(productDetail),
                        urlImage = it.getStringOrNull(productUrlImage)
                    ),

                )
                serviceNoteDetails.add(serviceNoteDetail)
            }
            serviceNoteDetails
        }
    }

    fun getById(serviceNotId: Long,productId: Long): ServiceNoteDetail? {
        val db = connection.readableDatabase
        val query=db.rawQuery(
            """
                SELECT 
                * 
                FROM ${CarAtelierContract.ServiceNoteDetail.TABLE_NAME}
                JOIN ${CarAtelierContract.Product.TABLE_NAME} ON ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID} = ${CarAtelierContract.Product.COLUMN_NAME_ID}
                WHERE ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID} = ? AND
                ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID} = ?
                
            """.trimIndent(),arrayOf(serviceNotId.toString(),productId.toString())
        )
        return query.use {
            val serviceNoteId= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID)
            val productPrice= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRICE)
            val productQuantity= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_QUANTITY)
            val serviceDetailProduct= it.getColumnIndexOrThrow(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID)

            val productId= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_ID)
            val productName= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_NAME)
            val productUrlImage= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_URL_IMAGE)
            val productDetail= it.getColumnIndexOrThrow(CarAtelierContract.Product.COLUMN_NAME_DETAIL)

            if (it.moveToFirst()) {
                ServiceNoteDetail(
                    serviceNoteId = it.getLong(serviceNoteId),
                    productId = it.getLong(serviceDetailProduct),
                    price = it.getDouble(productPrice),
                    quantity = it.getInt(productQuantity),
                    product = Product(
                        id = it.getLong(productId),
                        name = it.getString(productName),
                        detail = it.getStringOrNull(productDetail),
                        urlImage = it.getStringOrNull(productUrlImage)
                    ),

                )
            } else {
                null
            }
        }
    }

    fun save(serviceNoteDetail: ServiceNoteDetail): Long {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID, serviceNoteDetail.serviceNoteId)
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID, serviceNoteDetail.productId)
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRICE, serviceNoteDetail.price)
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_QUANTITY, serviceNoteDetail.quantity)
        }
        return db.insert(CarAtelierContract.ServiceNoteDetail.TABLE_NAME, null, values)
    }

    fun update(serviceNoteDetail: ServiceNoteDetail): Int {
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID, serviceNoteDetail.serviceNoteId)
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID, serviceNoteDetail.productId)
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRICE, serviceNoteDetail.price)
            put(CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_QUANTITY, serviceNoteDetail.quantity)
        }
        return db.update(
            CarAtelierContract.ServiceNoteDetail.TABLE_NAME,
            values,
            "${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID} = ? AND ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID} = ?",
            arrayOf(serviceNoteDetail.serviceNoteId.toString(),serviceNoteDetail.productId.toString())
        )
    }

    fun delete(serviceNoteId: Long,productId: Long): Int {
        val db = connection.writableDatabase
        return db.delete(
            CarAtelierContract.ServiceNoteDetail.TABLE_NAME,
            "${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_SERVICE_NOTE_ID} = ? AND ${CarAtelierContract.ServiceNoteDetail.COLUMN_NAME_PRODUCT_ID} = ?",
            arrayOf(serviceNoteId.toString(),productId.toString())
        )
    }
}
