package com.example.tallerarquitectura.dblocal

import android.provider.BaseColumns

object CarAtelierContract {

    object MeasureUnit : BaseColumns {
        const val TABLE_NAME = "UnitMeasures"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
        const val COLUMN_NAME_SHORT = "sShort"
    }

    object Service : BaseColumns {
        const val TABLE_NAME = "Services"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
        const val COLUMN_NAME_URL_IMAGE = "sUrlImage"
    }

    object Enterprise : BaseColumns {
        const val TABLE_NAME = "Enterprises"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
        const val COLUMN_NAME_LOCATION_NAME = "sLocationName"
        const val COLUMN_NAME_LATITUDE = "nLatitude"
        const val COLUMN_NAME_LONGITUDE = "nLongitude"
        const val COLUMN_NAME_URL_IMAGE = "sUrlImage"
    }

    object Car : BaseColumns {
        const val TABLE_NAME = "Cars"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_PLATE = "sPlate"
        const val COLUMN_NAME_ALIAS = "sAlias"
        const val COLUMN_NAME_MODEL = "sModel"
        const val COLUMN_NAME_CYLINDER = "sCylinder"
        const val COLUMN_NAME_MARK = "sMark"
        const val COLUMN_NAME_YEAR = "nYear"
        const val COLUMN_NAME_URL_IMAGE = "sUrlImage"
    }

    object Product : BaseColumns {
        const val TABLE_NAME = "Products"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_NAME = "sName"
        const val COLUMN_NAME_DETAIL = "sDetail"
        const val COLUMN_NAME_URL_IMAGE = "sUrlImage"
    }

    object ServiceNote : BaseColumns {
        const val TABLE_NAME = "ServiceNotes"
        const val COLUMN_NAME_ID = "nId"
        const val COLUMN_NAME_SERVICE_DATE = "dServiceDate"
        const val COLUMN_NAME_CREATION_DATE = "dCreationDate"
        const val COLUMN_NAME_VOUCHER_CODE = "sVoucherCode"
        const val COLUMN_NAME_VOUCHER_URL_IMAGE = "sVoucherUrlImage"
        const val COLUMN_NAME_SERVICE_ID = "nServiceId"
        const val COLUMN_NAME_ENTERPRISE_ID = "nEnterpriseId"
        const val COLUMN_NAME_CAR_ID = "nCarId"
    }

    object ServiceNoteDetail : BaseColumns {
        const val TABLE_NAME = "ServiceNoteDetails"
        const val COLUMN_NAME_SERVICE_NOTE_ID = "nServiceNoteId"
        const val COLUMN_NAME_PRODUCT_ID = "nProductId"
        const val COLUMN_NAME_QUANTITY = "nQuantity"
        const val COLUMN_NAME_PRICE = "nPrice"
    }

    object ReminderNote: BaseColumns{
        const val TABLE_NAME="ReminderNotes"
        const val COLUMN_NAME_ID="nId"
        const val COLUMN_NAME_END_DATE="dEndDate"
        const val COLUMN_NAME_END_OTHER_DATA="nEndOtherData"
        const val COLUMN_NAME_SERVICE_NOTE_ID="nServiceNoteId"
        const val COLUMN_NAME_STATUS="nStatus"
        const val COLUMN_NAME_UNIT_MEASURE_ID="nUnitMeasureId"
    }


}
