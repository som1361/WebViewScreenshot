package com.example.webviewscreenshot.view

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

    private fun updateContentList(it: List<Content>?) {
        if (it != null) {
            mContentAdapter.updateList(it)
            mContentAdapter.notifyDataSetChanged()

        }
    }

    private fun respondToClicks() {

    }

    private fun loadView() {
        setContentView(R.layout.activity_history)
        mContentAdapter = ContentAdapter(ContentListener { recipeId ->
        })
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
}
