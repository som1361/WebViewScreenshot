package com.example.webviewscreenshot.viewmodel

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
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
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
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
    lateinit var getContentByUrlObservable: PublishSubject<ArrayList<Content>>
    lateinit var getContentByUrlErrorObservable: PublishSubject<Exception>

   constructor(mContentRepository: ContentRepository) : this() {
        contentRepository = mContentRepository
       saveContentObservable = CompletableSubject.create()
       saveContentErrorObservable = PublishSubject.create()
       removeContentObservable = CompletableSubject.create()
       removeContentErrorObservable = PublishSubject.create()
       getContentObservable = PublishSubject.create()
       getContentErrorObservable = PublishSubject.create()
       getContentByUrlObservable = PublishSubject.create()
       getContentByUrlErrorObservable = PublishSubject.create()
   }

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

    fun getHistoryByUrl(url:String) {
        val disposable: Disposable = contentRepository.getContentsByUrl(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ArrayList<Content>>(){
                override fun onSuccess(t: ArrayList<Content>) {
                    getContentByUrlObservable.onNext(t)
                }

                override fun onError(e: Throwable) {
                    getContentByUrlErrorObservable.onNext(e as Exception)
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

    companion object{
        const val SAVE_CONTENT_OBSERVABLE = "saveContentObservable"
        const val REMOVE_CONTENT_OBSERVABLE = "removeContentObservable"
        const val SAVE_CONTENT_ERROR_OBSERVABLE = "saveContentErrorObservable"
        const val GET_CONTENT_ERROR_OBSERVABLE = "getContentErrorObservable"
        const val GET_CONTENT_BY_URL_ERROR_OBSERVABLE = "getContentByUrlErrorObservable"
        const val REMOVE_CONTENT_ERROR_OBSERVABLE = "removeContentErrorObservable"
        const val GET_CONTENT_OBSERVABLE = "getContentObservable"
        const val GET_CONTENT_BY_URL_OBSERVABLE = "getContentByUrlObservable"
    }
}