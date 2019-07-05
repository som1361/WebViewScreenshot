package com.example.webviewscreenshot.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.example.webviewscreenshot.DI.component.ActivityComponent
import com.example.webviewscreenshot.DI.component.DaggerActivityComponent
import com.example.webviewscreenshot.DI.module.ActivityModule
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.application.ScreenshotApplication
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.utils.*
import com.example.webviewscreenshot.viewmodel.MainViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_history.*
import javax.inject.Inject

class HistoryActivity : AppCompatActivity() {
    @Inject lateinit var mMainViewModel: MainViewModel
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mContentAdapter: ContentAdapter
    private var itemPosition: Int = 0
    lateinit var imagePath: String
    lateinit var activityComponent: ActivityComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = DaggerActivityComponent
            .builder()
            .appComponent((application as ScreenshotApplication).screenShotComponent)
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.inject(this)

        loadView()
        respondToClicks()
        listenToObservables()
    }

    private fun listenToObservables() {
        ScreenshotApplication.getAsyncComponent().getGetContentObservable().subscribe({
            history_progress_bar.hide()
            if (it.size == 0)
                showSuccessMessage(this, R.string.empty_list)
            updateContentList(it)
        })
        ScreenshotApplication.getAsyncComponent().getGetContentErrorObservable().subscribe({
            history_progress_bar.hide()
            showFailMessage(this, R.string.get_history_failed)
        })
        ScreenshotApplication.getAsyncComponent().getGetContentByUrlObservable().subscribe({
            history_progress_bar.hide()
            if (it.size == 0)
                showSuccessMessage(this, R.string.empty_list)
            updateContentList(it)
        })
        ScreenshotApplication.getAsyncComponent().getGetContentByUrlErrorObservable().subscribe({
            history_progress_bar.hide()
            showFailMessage(this, R.string.get_history_failed)
        })
    }

    private fun updateContentList(it: ArrayList<Content>?) {
        if (it != null) {
            mContentAdapter.updateList(it)
            mContentAdapter.notifyDataSetChanged()

        }
    }

    private fun respondToClicks() {
        mContentAdapter.setItemClickListener(object : AdapterActionListener {
            override fun doWhenDeleteItemIsClicked(
                position: Int,
                content: Content
            ) {
                listentoListObservebles()
                itemPosition = position
                imagePath = content.imageRef.toString()
                //remove item from DB
                mMainViewModel.removeContent(content)
            }

            override fun doWhenItemUrlIsClicked(content: Content) = gotoMainActivity(content)
            override fun doWhenItemImageIsClicked(content: Content) = gotoMainActivity(content)
        })

        search_imageView.setOnClickListener {
            val url = url_search_editText.text.toString()
            if (!url.isValidUrl())
                showFailMessage(this, R.string.invalid_url)
            else {
                val res = url.formatUrl()
                url_search_editText.setText(res)
                mMainViewModel.getHistoryByUrl(res)
            }
        }

        history_layout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                this@HistoryActivity.hideSoftKeyboard()
                return true
            }
        })
    }

    private fun loadView() {
        setContentView(R.layout.activity_history)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        mContentAdapter = ContentAdapter()
        mLinearLayoutManager = LinearLayoutManager(this)
        history_recyclerview.layoutManager = mLinearLayoutManager
        history_recyclerview.adapter = mContentAdapter
        history_progress_bar.hide()
        history_progress_bar.show()
        mMainViewModel.getHistory()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        onBackPressed()
        return true
    }

    private fun gotoMainActivity(content: Content) {
        val bundle = Bundle()
        bundle.putString(MainActivity.Constants.URL, content.url)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        mMainViewModel.cancelDBConnection()
    }

    fun listentoListObservebles(){
        ScreenshotApplication.getAsyncComponent().getRemoveContentObservable().subscribe({
            history_progress_bar.hide()
            //update recyclerview
            mContentAdapter.removeItem(itemPosition)
            //remove image from device internal storage
            removeFromInternalStorage(imagePath)
            showSuccessMessage(this, R.string.remove_content_success)
        })
        ScreenshotApplication.getAsyncComponent().getRemoveContentErrorObservable().subscribe({
            showFailMessage(this, R.string.remove_content_failed)
        })
    }
}
