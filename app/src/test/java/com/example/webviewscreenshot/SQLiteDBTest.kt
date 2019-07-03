package com.example.webviewscreenshot

import android.database.sqlite.SQLiteDatabase
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SQLiteDBTest {

    private var contentDao: ContentDao? = null

    @Before
    fun setUp() {
        contentDao = ContentDao(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        contentDao?.close()
    }

    @Test
    fun getContentListTest() = readableDatabaseTest {
        var contentList: ArrayList<Content> = ArrayList()
        var content = Content(
            "/data/data/screenshotapp/app_data/1",
            "https://www.google.com",
            "2019-07-01 12:30:00"
        )
        contentList.add(content)
        contentDao!!.addContent(content)
        content = Content(
            "/data/data/screenshotapp/app_data/2",
            "https://www.yahoo.com",
            "2019-07-02 12:30:00"
        )
        contentList.add(content)
        contentDao!!.addContent(content)
        assertTrue(contentDao!!.getContentList()!!.containsAll(contentList))
    }

    @Test
    fun addContentTest() = writableDatabaseTest {
        val content = Content("/data/data/screenshotapp/app_data/1", "https://www.google.com", "2019-07-01 12:30:00")
        contentDao!!.addContent(content)
        assertTrue(contentDao!!.getContentList()!!.contains(content))
    }

    @Test
    fun getContentsByUrlTest() = readableDatabaseTest {
        var content = Content(
            "/data/data/screenshotapp/app_data/7",
            "https://www.ebay.com",
            "2019-07-01 12:30:00"
        )
        contentDao!!.addContent(content)
        assertTrue(content.url?.let { contentDao!!.getContentsByUrl(it) }!!.contains(content))
    }

    @Test
    fun removeContentTest() = writableDatabaseTest {
        var content = Content(
            "/data/data/screenshotapp/app_data/4",
            "https://www.facebook.com",
            "2019-07-01 12:30:00"
        )

        contentDao!!.addContent(content)
        contentDao!!.removeContent(content)
        assertTrue(!contentDao!!.getContentList()!!.contains(content))
    }

    private fun writableDatabaseTest(f: SQLiteDatabase.() -> Unit) = contentDao!!.writableDatabase.let(f)
    private fun readableDatabaseTest(f: SQLiteDatabase.() -> Unit) = contentDao!!.readableDatabase.let(f)

}
