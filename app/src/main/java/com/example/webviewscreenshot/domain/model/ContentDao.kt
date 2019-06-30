package com.example.webviewscreenshot.domain.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DB_NAME = "Urls"
val TABLE_NAME = "UrlDetails"
val COL_URL = "url"
val COL_IMAGE_REF = "image"
val COL_DATETIME = "datetime"


class ContentDao(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME($COL_URL VARCHAR(256), $COL_IMAGE_REF VARCHAR(256), $COL_DATETIME DATETIME);"
        db?.execSQL(createTable)
        val createIndex = "CREATE INDEX ${TABLE_NAME}_${COL_URL} ON $TABLE_NAME ($COL_URL);"
        db?.execSQL(createIndex)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addContent(content: Content): Long {
        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_IMAGE_REF, content.imageRef)
        contentValues.put(COL_URL, content.url)
        contentValues.put(COL_DATETIME, content.dateTime.toString())

        return db.insert(TABLE_NAME, null, contentValues)
    }
}