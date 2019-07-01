package com.example.webviewscreenshot.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.domain.repository.ContentDaoRepository
import com.example.webviewscreenshot.utils.hide
import com.example.webviewscreenshot.utils.show
import com.example.webviewscreenshot.utils.showFailMessage
import com.example.webviewscreenshot.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mContentAdapter: ContentAdapter

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
                 mContentAdapter.removeItem(position)
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
        // configSearchDebounce()
        history_progress_bar.show()
        mMainViewModel.getHistory()
    }

    private fun configSearchDebounce() {

    }

    private fun gotoMainActivity(content: Content) {
     val bundle = Bundle()
        bundle.putString(MainActivity.Constants.IMAGE_REF, content.imageRef)
        bundle.putString(MainActivity.Constants.URL, content.url)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
