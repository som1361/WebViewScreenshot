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
    private var contentList: ArrayList<Content> = ArrayList()
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME($COL_URL VARCHAR(256), $COL_IMAGE_REF VARCHAR(256), $COL_DATETIME VARCHAR(256));"
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
        contentValues.put(COL_DATETIME, content.dateTime)

        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getContentList(): ArrayList<Content>? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME;"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        do{
            var content = Content()
            content.imageRef = result.getString(result.getColumnIndex(COL_IMAGE_REF))
            content.url = result.getString(result.getColumnIndex(COL_URL))
            content.dateTime = result.getString(result.getColumnIndex(COL_DATETIME))

            contentList.add(content)
        }while(result.moveToNext())

        result.close()
        db.close()

        return contentList
    }

    fun removeContent(content: Content): Int? {
        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_IMAGE_REF, content.imageRef)
        contentValues.put(COL_URL, content.url)
        contentValues.put(COL_DATETIME, content.dateTime)

        return db.delete(TABLE_NAME, "$COL_URL = ${content.url}  and $COL_DATETIME = ${content.url}" , null)
    }
}