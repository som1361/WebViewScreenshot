package com.example.webviewscreenshot.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.qoutecalculator.utils.hide
import com.example.qoutecalculator.utils.hideSoftKeyboard
import com.example.qoutecalculator.utils.isValidUrl
import com.example.qoutecalculator.utils.show
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.utils.showFailMessage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadView()
        respondToClicks()
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
    }

    private fun respondToClicks() {
        go_button.setOnClickListener {
            if (!isValidUrl())
                showFailMessage(this, R.string.invalid_url)
            else
                loadContent(url_edittext.text.toString())
        }

        main_layout.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                this@MainActivity.hideSoftKeyboard()
                return true
            }
        })
    }

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

    private fun isValidUrl() = url_edittext.text.toString().isValidUrl()
}
