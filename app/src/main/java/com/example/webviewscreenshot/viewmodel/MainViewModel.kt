package com.example.webviewscreenshot.viewmodel

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.example.webviewscreenshot.application.ScreenshotApplication
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.repository.ContentRepository
import com.example.webviewscreenshot.utils.getCurrentTime
import com.example.webviewscreenshot.utils.loadBitmap
import com.example.webviewscreenshot.utils.saveToInternalStorage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable
import javax.inject.Inject


class MainViewModel @Inject constructor(private var contentRepository: ContentRepository) {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun captureContent(context: Context, view: View, url: String) {
        // save captured data in internal storage
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
                @RequiresApi(Build.VERSION_CODES.O)
                override fun accept(savedImageAbsPath: String?) {
                    val content = Content(savedImageAbsPath, url, getCurrentTime())
                    //save url data in database
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
                    ScreenshotApplication.getAsyncComponent().getSaveContentObservable().onComplete()
                }

                override fun onError(e: Throwable) {
                    ScreenshotApplication.getAsyncComponent().getSaveContentErrorObservable().onNext(e as Exception)
                }
            })
        compositeDisposable.add(disposable)
    }

    fun getHistory() {
        val disposable: Disposable = contentRepository.getContentList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ArrayList<Content>>() {
                override fun onSuccess(t: ArrayList<Content>) {
                    ScreenshotApplication.getAsyncComponent().getGetContentObservable().onNext(t)
                }

                override fun onError(e: Throwable) {
                    ScreenshotApplication.getAsyncComponent().getGetContentErrorObservable().onNext(e as Exception)
                }

            })
        compositeDisposable.add(disposable)
    }

    fun getHistoryByUrl(url: String) {
        val disposable: Disposable = contentRepository.getContentsByUrl(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ArrayList<Content>>() {
                override fun onSuccess(t: ArrayList<Content>) {
                    ScreenshotApplication.getAsyncComponent().getGetContentByUrlObservable().onNext(t)
                }

                override fun onError(e: Throwable) {
                    ScreenshotApplication.getAsyncComponent().getGetContentByUrlErrorObservable().onNext(e as Exception)
                }

            })
        compositeDisposable.add(disposable)
    }

    fun removeContent(content: Content) {
        val disposable = contentRepository.removeContent(content)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    ScreenshotApplication.getAsyncComponent().getRemoveContentObservable().onComplete()
                }

                override fun onError(e: Throwable) {
                    ScreenshotApplication.getAsyncComponent().getRemoveContentErrorObservable().onNext(e as Exception)
                }
            })
        compositeDisposable.add(disposable)
    }

    fun cancelDBConnection() {
        compositeDisposable.clear()
    }
}