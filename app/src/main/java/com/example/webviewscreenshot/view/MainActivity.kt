package com.example.webviewscreenshot.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.application.ScreenshotApplication
import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.domain.repository.ContentDaoRepository
import com.example.webviewscreenshot.utils.*
import com.example.webviewscreenshot.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject lateinit var mMainViewModel: MainViewModel
  //  private lateinit var mMainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

       // mMainViewModel = MainViewModel(ContentDaoRepository(ContentDao(this)))
        (application as ScreenshotApplication).screenShotComponent.inject(this)
        super.onCreate(savedInstanceState)
        loadView()
        respondToClicks()
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent!!.extras
        if (bundle != null) {
            updateViewContents(bundle.getString(Constants.URL))
        }
    }

    private fun updateViewContents(url: String?) {
        url_edittext.setText(url)
        loadContent(url!!)

    }

    private fun respondToClicks() {
        go_button.setOnClickListener {
            val url = url_edittext.text.toString()
            if (!url.isValidUrl())
                showFailMessage(this, R.string.invalid_url)
            else {
                val res = url.formatUrl()
                url_edittext.setText(res)
                loadContent(res)
            }
        }
        capture_button.setOnClickListener {
            listenToObservables()
            mMainViewModel.doWhenCaptureButtonIsClicked(
                applicationContext,
                content_webview,
                url_edittext.text.toString()
            )
        }

        history_button.setOnClickListener {
            goToHistoryActivity()
        }

//hide soft keyboard after clicking outside Url edittext
        main_layout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                this@MainActivity.hideSoftKeyboard()
                return true
            }
        })
    }

    private fun goToHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun listenToObservables() {
        mMainViewModel.saveContentObservable.subscribe({
            main_progress_bar.hide()
            showSuccessMessage(this, R.string.save_content_success)
        })

        mMainViewModel.saveContentErrorObservable.subscribe({
            showFailMessage(this, R.string.save_content_failed)
        })
    }

    //it loads the content into the WEBVIEW
    private fun loadContent(url: String) {
        content_webview.getSettings().setJavaScriptEnabled(true)
        content_webview.getSettings().setDomStorageEnabled(true)
        content_webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                main_progress_bar.hide()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                main_progress_bar.show()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })
        content_webview.loadUrl(url)
    }

    override fun onStop() {
        super.onStop()
        mMainViewModel.cancelDBConnection()
    }

    object Constants {
        const val URL = "url"
    }
}
