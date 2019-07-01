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
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

class MainViewModel() {
    private lateinit var contentRepository: ContentRepository
    lateinit var saveContentObservable: CompletableSubject
    lateinit var saveContentErrorObservable: PublishSubject<Exception>
    lateinit var removeContentObservable: CompletableSubject
    lateinit var removeContentErrorObservable: PublishSubject<Exception>
    lateinit var getContentObservable: PublishSubject<ArrayList<Content>>
    lateinit var getContentErrorObservable: PublishSubject<Exception>

    constructor(mContentRepository: ContentRepository) : this() {
        contentRepository = mContentRepository
        saveContentObservable = CompletableSubject.create()
        saveContentErrorObservable = PublishSubject.create()
        removeContentObservable = CompletableSubject.create()
        removeContentErrorObservable = PublishSubject.create()
        getContentObservable = PublishSubject.create()
        getContentErrorObservable = PublishSubject.create()
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
                @RequiresApi(Build.VERSION_CODES.O)
                override fun accept(savedImageAbsPath: String?) {
                    val content = Content(savedImageAbsPath, url, LocalDateTime.now().toString())
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

    fun getHistory() {
        val disposable: Disposable = contentRepository.getContentList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ArrayList<Content>>(){
                override fun onSuccess(t: ArrayList<Content>) {
                    getContentObservable.onNext(t)
                }

                override fun onError(e: Throwable) {
                    getContentErrorObservable.onNext(e as Exception)
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
                    removeContentObservable.onComplete()
                }

                override fun onError(e: Throwable) {
                    removeContentErrorObservable.onNext(e as Exception)
                }
            })
        compositeDisposable.add(disposable)
    }

    fun cancelDBConnection() {
        compositeDisposable.clear()
    }
}