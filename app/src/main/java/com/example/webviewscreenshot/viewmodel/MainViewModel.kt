package com.example.webviewscreenshot.viewmodel

import android.content.Context
import android.view.View
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.repository.HistoryRepository
import com.example.webviewscreenshot.utils.loadBitmap
import com.example.webviewscreenshot.utils.saveToInternalStorage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable

class MainViewModel() {
    private lateinit var historyRepository: HistoryRepository

    constructor(mHistoryRepository: HistoryRepository) : this() {
        historyRepository = mHistoryRepository
    }

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun doWhenCaptureButtonIsClicked(context: Context, view: View, url: String) {

        val disposable = Observable.fromCallable(object : Callable<String> {
            override fun call(): String {
                return saveToInternalStorage(
                    context,
                    view.loadBitmap(),
                    context.applicationInfo.loadLabel(context.packageManager).toString(), "SC_${Date()}"
                )
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<String> {
                override fun accept(savedImageAbsPath: String?) {
                    val content = Content(savedImageAbsPath, url)
                    saveContent(content)
                }

            });
        compositeDisposable.add(disposable)
    }

    private fun saveContent(content: Content) {

    }
}