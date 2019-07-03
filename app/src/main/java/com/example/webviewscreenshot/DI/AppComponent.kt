package com.example.webviewscreenshot.DI

import com.example.webviewscreenshot.view.HistoryActivity
import com.example.webviewscreenshot.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: HistoryActivity)
}