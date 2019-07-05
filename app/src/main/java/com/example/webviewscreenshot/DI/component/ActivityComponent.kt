package com.example.webviewscreenshot.DI.component

import com.example.webviewscreenshot.DI.Scope.ActivityScope
import com.example.webviewscreenshot.DI.Scope.AppScope
import com.example.webviewscreenshot.DI.module.ActivityModule
import com.example.webviewscreenshot.view.HistoryActivity
import com.example.webviewscreenshot.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@ActivityScope
@Component(modules = [ActivityModule::class] , dependencies = [AppComponent ::class])
interface ActivityComponent {
    fun inject(target: MainActivity)
    fun inject(target: HistoryActivity)
}