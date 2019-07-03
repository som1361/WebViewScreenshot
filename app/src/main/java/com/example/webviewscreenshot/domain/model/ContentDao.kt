package com.example.webviewscreenshot.domain.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.inject.Inject
import javax.inject.Singleton

val DB_NAME = "Urls"
val TABLE_NAME = "UrlDetails"
val COL_URL = "url"
val COL_IMAGE_REF = "image"
val COL_TIMESTAMP = "timestamp"

@Singleton
class ContentDao @Inject constructor(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME($COL_URL VARCHAR(256), $COL_IMAGE_REF VARCHAR(256), $COL_TIMESTAMP VARCHAR(256));"
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
        contentValues.put(COL_TIMESTAMP, content.timestamp)
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getContentList(): ArrayList<Content>? {
        var contentList: ArrayList<Content> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME;"
        val result = db.rawQuery(query, null)
        if (result.count > 0) {
            result.moveToFirst()
            do {
                var content = Content()
                content.imageRef = result.getString(result.getColumnIndex(COL_IMAGE_REF))
                content.url = result.getString(result.getColumnIndex(COL_URL))
                content.timestamp = result.getString(result.getColumnIndex(COL_TIMESTAMP))

                contentList.add(content)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return contentList
    }

    fun getContentsByUrl(url: String): ArrayList<Content>? {
        var contentList: ArrayList<Content> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_URL = '$url';"
        val result = db.rawQuery(query, null)
        if (result.count > 0) {
            result.moveToFirst()
            do {
                var content = Content()
                content.imageRef = result.getString(result.getColumnIndex(COL_IMAGE_REF))
                content.url = result.getString(result.getColumnIndex(COL_URL))
                content.timestamp = result.getString(result.getColumnIndex(COL_TIMESTAMP))

                contentList.add(content)
            } while (result.moveToNext())
        }

        result?.close()
        db.close()

        return contentList
    }

    fun removeContent(content: Content): Int? {
        val db = this.writableDatabase
        return db.delete(
            TABLE_NAME,
            COL_URL + "=? AND " + COL_TIMESTAMP + "=?",
            arrayOf(content.url, content.timestamp)
        )
    }
}