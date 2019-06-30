package com.example.webviewscreenshot.viewmodel

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.repository.ContentRepository
import com.example.webviewscreenshot.utils.loadBitmap
import com.example.webviewscreenshot.utils.saveToInternalStorage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable

class MainViewModel() {
    private lateinit var contentRepository: ContentRepository
    lateinit var saveContentObservable: CompletableSubject
    lateinit var saveContentErrorObservable: PublishSubject<Exception>

    constructor(mContentRepository: ContentRepository) : this() {
        contentRepository = mContentRepository
        saveContentObservable = CompletableSubject.create()
        saveContentErrorObservable = PublishSubject.create()
    }

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun doWhenCaptureButtonIsClicked(context: Context, view: View, url: String) {

        val disposable = Observable.fromCallable(object : Callable<String> {
            @RequiresApi(Build.VERSION_CODES.DONUT)
            override fun call(): String {
                return saveToInternalStorage(
                    context,
                    view.loadBitmap(),
                    context.applicationInfo.loadLabel(context.packageManager).toString(), "SC_${Date()}"
                )
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<String> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun accept(savedImageAbsPath: String?) {
                    val content = Content(savedImageAbsPath, url, LocalDateTime.now())
                    saveContent(content)
                }

            });
        compositeDisposable.add(disposable)
    }

    private fun saveContent(content: Content) {
        val disposable = contentRepository.addContent(content)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    saveContentObservable.onComplete()
                }

                override fun onError(e: Throwable) {
                    saveContentErrorObservable.onNext(e as Exception)
                }
            })
        compositeDisposable.add(disposable)
    }
}