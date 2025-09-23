package com.example.tallerarquitectura.model

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.example.tallerarquitectura.dblocal.CarAtelierContract
import com.example.tallerarquitectura.dblocal.CarAtelierDbHelper
import com.example.tallerarquitectura.dto.Car
import com.example.tallerarquitectura.dto.Enterprise
import com.example.tallerarquitectura.dto.MeasureUnit
import com.example.tallerarquitectura.dto.ReminderNoteWithMeasureUnit
import com.example.tallerarquitectura.dto.Service
import com.example.tallerarquitectura.dto.ServiceNote
import com.example.tallerarquitectura.dto.ServiceNoteDetail

class ServiceNoteModel(private val serviceNoteDetailModel: ServiceNoteDetailModel= ServiceNoteDetailModel(), private val connection: SQLiteOpenHelper= CarAtelierDbHelper.getInstance()) {
    fun getAll(): List<ServiceNote> {
        val db = connection.readableDatabase
        val query = db.rawQuery(
            """
            SELECT
                sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ID},
                sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE},
                sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE},
                sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_CREATION_DATE},
                sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE},
                sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID} AS ServiceNoteEnterpriseId,

                s.${CarAtelierContract.Service.COLUMN_NAME_ID} AS ServiceId,
                s.${CarAtelierContract.Service.COLUMN_NAME_NAME} AS ServiceName,
                s.${CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE} AS ServiceUrlImage,

                c.${CarAtelierContract.Car.COLUMN_NAME_ID} AS CarId,
                c.${CarAtelierContract.Car.COLUMN_NAME_PLATE} AS CarPlate,
                c.${CarAtelierContract.Car.COLUMN_NAME_ALIAS} AS CarAlias,
                c.${CarAtelierContract.Car.COLUMN_NAME_MODEL} AS CarModel,
                c.${CarAtelierContract.Car.COLUMN_NAME_CYLINDER} AS CarCylinder,
                c.${CarAtelierContract.Car.COLUMN_NAME_MARK} AS CarMark,
                c.${CarAtelierContract.Car.COLUMN_NAME_YEAR} AS CarYear,
                c.${CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE} AS CarUrlImage,

                e.${CarAtelierContract.Enterprise.COLUMN_NAME_ID} AS EnterpriseId,
                e.${CarAtelierContract.Enterprise.COLUMN_NAME_NAME} AS EnterpriseName,
                e.${CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME} AS EnterpriseLocationName,
                e.${CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE} AS EnterpriseLatitude,
                e.${CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE} AS EnterpriseLongitude,
                e.${CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE} AS EnterpriseUrlImage
            FROM ${CarAtelierContract.ServiceNote.TABLE_NAME} sn   
            JOIN ${CarAtelierContract.Service.TABLE_NAME} s 
                ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_ID} = s.${CarAtelierContract.Service.COLUMN_NAME_ID}
            JOIN ${CarAtelierContract.Car.TABLE_NAME} c
                ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_CAR_ID} = c.${CarAtelierContract.Car.COLUMN_NAME_ID}
            LEFT JOIN ${CarAtelierContract.Enterprise.TABLE_NAME} e
                ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID} = e.${CarAtelierContract.Enterprise.COLUMN_NAME_ID}
        """.trimIndent(), null
        )

        val data = mutableListOf<ServiceNote>()

        query.use {
            val id = it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_ID)
            val serviceDate = it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE)
            val creationDate = it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_CREATION_DATE)
            val voucherCode = it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE)
            val voucherUrlImage = it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE)
            val serviceNoteEnterpriseId = it.getColumnIndexOrThrow("ServiceNoteEnterpriseId")

            val serviceId = it.getColumnIndexOrThrow("ServiceId")
            val serviceName = it.getColumnIndexOrThrow("ServiceName")
            val serviceUrlImage = it.getColumnIndexOrThrow("ServiceUrlImage")

            val carId = it.getColumnIndexOrThrow("CarId")
            val carPlate = it.getColumnIndexOrThrow("CarPlate")
            val carAlias = it.getColumnIndexOrThrow("CarAlias")
            val carModel = it.getColumnIndexOrThrow("CarModel")
            val carCylinder = it.getColumnIndexOrThrow("CarCylinder")
            val carMark = it.getColumnIndexOrThrow("CarMark")
            val carYear = it.getColumnIndexOrThrow("CarYear")
            val carUrlImage = it.getColumnIndexOrThrow("CarUrlImage")

            val enterpriseId = it.getColumnIndexOrThrow("EnterpriseId")
            val enterpriseName = it.getColumnIndexOrThrow("EnterpriseName")
            val enterpriseLocationName = it.getColumnIndexOrThrow("EnterpriseLocationName")
            val enterpriseLatitude = it.getColumnIndexOrThrow("EnterpriseLatitude")
            val enterpriseLongitude = it.getColumnIndexOrThrow("EnterpriseLongitude")
            val enterpriseUrlImage = it.getColumnIndexOrThrow("EnterpriseUrlImage")

            while (it.moveToNext()) {
                val enterprise = if (it.isNull(serviceNoteEnterpriseId)) {
                    null
                } else {
                    Enterprise(
                        id = it.getLong(enterpriseId),
                        name = it.getString(enterpriseName),
                        locationName = it.getStringOrNull(enterpriseLocationName),
                        latitude = it.getDouble(enterpriseLatitude),
                        longitude = it.getDouble(enterpriseLongitude),
                        urlImage = it.getStringOrNull(enterpriseUrlImage)
                    )
                }

                val car = Car(
                    id = it.getLong(carId),
                    plate = it.getString(carPlate),
                    alias = it.getStringOrNull(carAlias),
                    model = it.getString(carModel),
                    cylinder = it.getString(carCylinder),
                    mark = it.getString(carMark),
                    year = it.getInt(carYear),
                    urlImage = it.getStringOrNull(carUrlImage)
                )

                val service = Service(
                    id = it.getLong(serviceId),
                    name = it.getString(serviceName),
                    urlImage = it.getStringOrNull(serviceUrlImage)
                )

                data.add(
                    ServiceNote(
                        id = it.getLong(id),
                        serviceDate = it.getString(serviceDate),
                        creationDate = it.getString(creationDate),
                        voucherCode = it.getString(voucherCode),
                        voucherUrlImage = it.getStringOrNull(voucherUrlImage),
                        enterprise = enterprise,
                        car = car,
                        service = service
                    )
                )
            }
        }

        return data
    }

    fun getById(id: Long): ServiceNote? {
        val db = connection.readableDatabase

        val query = db.rawQuery(
            """
        SELECT
            sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ID},
            sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE},
            sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE},
            sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_CREATION_DATE},
            sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE},
            sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID} AS ServiceNoteEnterpriseId,
            
            s.${CarAtelierContract.Service.COLUMN_NAME_ID} AS ServiceId,
            s.${CarAtelierContract.Service.COLUMN_NAME_NAME} AS ServiceName,
            s.${CarAtelierContract.Service.COLUMN_NAME_URL_IMAGE} AS ServiceUrlImage,
            
            c.${CarAtelierContract.Car.COLUMN_NAME_ID} AS CarId,
            c.${CarAtelierContract.Car.COLUMN_NAME_PLATE} AS CarPlate,
            c.${CarAtelierContract.Car.COLUMN_NAME_ALIAS} AS CarAlias,
            c.${CarAtelierContract.Car.COLUMN_NAME_MODEL} AS CarModel,
            c.${CarAtelierContract.Car.COLUMN_NAME_CYLINDER} AS CarCylinder,
            c.${CarAtelierContract.Car.COLUMN_NAME_MARK} AS CarMark,
            c.${CarAtelierContract.Car.COLUMN_NAME_YEAR} AS CarYear,
            c.${CarAtelierContract.Car.COLUMN_NAME_URL_IMAGE} AS CarUrlImage,
            
            e.${CarAtelierContract.Enterprise.COLUMN_NAME_ID} AS EnterpriseId,
            e.${CarAtelierContract.Enterprise.COLUMN_NAME_NAME} AS EnterpriseName,
            e.${CarAtelierContract.Enterprise.COLUMN_NAME_LOCATION_NAME} AS EnterpriseLocationName,
            e.${CarAtelierContract.Enterprise.COLUMN_NAME_LATITUDE} AS EnterpriseLatitude,
            e.${CarAtelierContract.Enterprise.COLUMN_NAME_LONGITUDE} AS EnterpriseLongitude,
            e.${CarAtelierContract.Enterprise.COLUMN_NAME_URL_IMAGE} AS EnterpriseUrlImage,
            
            rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_ID} AS ReminderNoteId,
            rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_STATUS} AS ReminderNoteStatus,
            rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_END_DATE} AS ReminderNoteEndDate,
            rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_END_OTHER_DATA} AS ReminderNoteEndOtherData,
            rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID} AS ServiceNoteId,
                
            mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID} AS MeasureUnitId,
            mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_NAME} AS MeasureUnitName,
            mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_SHORT}  AS MeasureUnitShort
           

        FROM ${CarAtelierContract.ServiceNote.TABLE_NAME} sn
        JOIN ${CarAtelierContract.Service.TABLE_NAME} s
            ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_ID} = s.${CarAtelierContract.Service.COLUMN_NAME_ID}
        JOIN ${CarAtelierContract.Car.TABLE_NAME} c
            ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_CAR_ID} = c.${CarAtelierContract.Car.COLUMN_NAME_ID}
        LEFT JOIN ${CarAtelierContract.Enterprise.TABLE_NAME} e
            ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID} = e.${CarAtelierContract.Enterprise.COLUMN_NAME_ID}
        LEFT JOIN ${CarAtelierContract.ReminderNote.TABLE_NAME} rn
            ON sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ID} = rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_SERVICE_NOTE_ID}
        LEFT JOIN ${CarAtelierContract.MeasureUnit.TABLE_NAME} mu
             ON rn.${CarAtelierContract.ReminderNote.COLUMN_NAME_UNIT_MEASURE_ID} = mu.${CarAtelierContract.MeasureUnit.COLUMN_NAME_ID}
        WHERE sn.${CarAtelierContract.ServiceNote.COLUMN_NAME_ID} = ?
        """.trimIndent(),
            arrayOf(id.toString())
        )

        return query.use {
            if (!it.moveToFirst()) return null

            val enterprise = buildEnterprise(it)
            val car = buildCar(it)
            val service = buildService(it)
            val reminderNote=buildReminderNote(it)
            val serviceNoteDetail= serviceNoteDetailModel.getByServiceNoteId(id)

            ServiceNote(
                id = it.getLong(it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_ID)),
                serviceDate = it.getString(it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE)),
                creationDate = it.getString(it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_CREATION_DATE)),
                voucherCode = it.getString(it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE)),
                voucherUrlImage = it.getStringOrNull(it.getColumnIndexOrThrow(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE)),
                enterprise = enterprise,
                car = car,
                service = service,
                reminderNote = reminderNote,
                detail = serviceNoteDetail

            )
        }
    }

    fun addDetail(serviceNoteDetail: ServiceNoteDetail): Long {
        return serviceNoteDetailModel.save(serviceNoteDetail)
    }
    fun deleteDetail(serviceNoteId: Long, productId: Long): Int {
        return serviceNoteDetailModel.delete(serviceNoteId, productId)
    }
    fun updateDetail(serviceNoteDetail: ServiceNoteDetail): Int {
        return serviceNoteDetailModel.update(serviceNoteDetail)
    }
    fun getDetail(serviceNoteId: Long, productId: Long): ServiceNoteDetail? {
        return serviceNoteDetailModel.getById(serviceNoteId, productId)
    }


    fun save(serviceNote: ServiceNote): Long{
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE, serviceNote.serviceDate)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE, serviceNote.voucherCode)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE, serviceNote.voucherUrlImage)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_ID, serviceNote.service.id)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID, serviceNote.enterprise?.id)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_CREATION_DATE, serviceNote.creationDate)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_CAR_ID, serviceNote.car.id)

        }
        return db.insert(CarAtelierContract.ServiceNote.TABLE_NAME, null, values)
    }

    fun update(serviceNote: ServiceNote): Int{
        val db = connection.writableDatabase
        val values = ContentValues().apply {
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_DATE, serviceNote.serviceDate)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_CODE, serviceNote.voucherCode)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_VOUCHER_URL_IMAGE, serviceNote.voucherUrlImage)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_SERVICE_ID, serviceNote.service.id)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_ENTERPRISE_ID, serviceNote.enterprise?.id)
            put(CarAtelierContract.ServiceNote.COLUMN_NAME_CAR_ID, serviceNote.car.id)
        }
        return db.update(
            CarAtelierContract.ServiceNote.TABLE_NAME,
            values,
            "${CarAtelierContract.ServiceNote.COLUMN_NAME_ID}=?",
            arrayOf(serviceNote.id.toString())
        )
    }

    fun delete(id: Long): Int {
        val db = connection.writableDatabase
        return db.delete(
            CarAtelierContract.ServiceNote.TABLE_NAME,
            "${CarAtelierContract.ServiceNote.COLUMN_NAME_ID}=?",
            arrayOf(id.toString())
        )
    }
    private fun buildEnterprise(cursor: Cursor): Enterprise? {
        val enterpriseIdCol = "ServiceNoteEnterpriseId"
        return if (cursor.isNull(cursor.getColumnIndexOrThrow(enterpriseIdCol))) {
            null
        } else {
            Enterprise(
                id = cursor.getLongByName("EnterpriseId"),
                name = cursor.getStringByName("EnterpriseName"),
                locationName = cursor.getStringOrNullByName("EnterpriseLocationName"),
                latitude = cursor.getDoubleByName("EnterpriseLatitude"),
                longitude = cursor.getDoubleByName("EnterpriseLongitude"),
                urlImage = cursor.getStringOrNullByName("EnterpriseUrlImage")
            )
        }
    }

    private fun buildCar(cursor: Cursor): Car {
        return Car(
            id = cursor.getLongByName("CarId"),
            plate = cursor.getStringByName("CarPlate"),
            alias = cursor.getStringOrNullByName("CarAlias"),
            model = cursor.getStringByName("CarModel"),
            cylinder = cursor.getStringByName("CarCylinder"),
            mark = cursor.getStringByName("CarMark"),
            year = cursor.getIntByName("CarYear"),
            urlImage = cursor.getStringOrNullByName("CarUrlImage")
        )
    }

    private fun buildService(cursor: Cursor): Service {
        return Service(
            id = cursor.getLongByName("ServiceId"),
            name = cursor.getStringByName("ServiceName"),
            urlImage = cursor.getStringOrNullByName("ServiceUrlImage")
        )
    }
    private fun buildReminderNote(cursor: Cursor): ReminderNoteWithMeasureUnit? {
        val reminderNoteId = cursor.getLongOrNull(cursor.getColumnIndexOrThrow("ReminderNoteId"))
        return if (reminderNoteId == null) {
            null
        } else {
            val measureUnitId = cursor.getLongOrNull(cursor.getColumnIndexOrThrow("MeasureUnitId"))
            val measureUnit = if (measureUnitId != null) {
                MeasureUnit(
                    id =measureUnitId,
                    name = cursor.getStringOrNullByName("MeasureUnitName") ?: "",
                    short = cursor.getStringOrNullByName("MeasureUnitShort") ?: ""
                )
            } else {
                null
            }
            ReminderNoteWithMeasureUnit(
                id = cursor.getLongByName("ReminderNoteId"),
                status = cursor.getIntByName("ReminderNoteStatus"),
                endDate = cursor.getStringByName("ReminderNoteEndDate"),
                endOtherData = cursor.getIntByName("ReminderNoteEndOtherData"),
                serviceNoteId = cursor.getLongByName("ServiceNoteId"),
                measureUnit = measureUnit
            )

        }
    }

    private fun Cursor.getStringByName(name: String) = getString(getColumnIndexOrThrow(name))
    private fun Cursor.getStringOrNullByName(name: String): String? =
        getColumnIndexOrNull(name)?.let { if (isNull(it)) null else getString(it) }

    private fun Cursor.getLongByName(name: String) = getLong(getColumnIndexOrThrow(name))
    private fun Cursor.getIntByName(name: String) = getInt(getColumnIndexOrThrow(name))
    private fun Cursor.getDoubleByName(name: String) = getDouble(getColumnIndexOrThrow(name))
    private fun Cursor.getColumnIndexOrNull(name: String): Int? =
        try {
            getColumnIndexOrThrow(name)
        } catch (e: Exception) {
            null
        }
}