package com.example.webviewscreenshot.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.domain.repository.ContentDaoRepository
import com.example.webviewscreenshot.utils.*
import com.example.webviewscreenshot.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_main.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mContentAdapter: ContentAdapter
    private var itemPosition: Int = 0
    lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = MainViewModel(ContentDaoRepository(ContentDao(this)))
        loadView()
        respondToClicks()
        listenToObservables()
    }

    private fun listenToObservables() {
        mMainViewModel.getContentObservable.subscribe({
            history_progress_bar.hide()
            updateContentList(it)
        })
        mMainViewModel.getContentErrorObservable.subscribe({
            history_progress_bar.hide()
            showFailMessage(this, R.string.get_history_failed)
        })
        mMainViewModel.removeContentObservable.subscribe({
            history_progress_bar.hide()
            //update recyclerview
            mContentAdapter.removeItem(itemPosition)
            //remove image from device internal storage
            removeFromInternalStorage(imagePath)
            showSuccessMessage(this, R.string.remove_content_success)
        })

        mMainViewModel.removeContentErrorObservable.subscribe({
            showFailMessage(this, R.string.remove_content_failed)
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
                itemPosition = position
                imagePath = content.imageRef.toString()
                //remove item from DB
                mMainViewModel.removeContent(content)
            }

            override fun doWhenItemUrlIsClicked(content: Content) = gotoMainActivity(content)
            override fun doWhenItemImageIsClicked(content: Content) = gotoMainActivity(content)
        })
    }

    private fun loadView() {
        setContentView(R.layout.activity_history)
        mContentAdapter = ContentAdapter()
        mLinearLayoutManager = LinearLayoutManager(this)
        history_recyclerview.layoutManager = mLinearLayoutManager
        history_recyclerview.adapter = mContentAdapter
        history_progress_bar.hide()
        history_progress_bar.show()
        mMainViewModel.getHistory()
    }

    private fun gotoMainActivity(content: Content) {
     val bundle = Bundle()
        bundle.putString(MainActivity.Constants.URL, content.url)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        mMainViewModel.cancelDBConnection()
    }
}
