package com.mobile.healthpoint

import android.app.Application
import com.mobile.healthpoint.dagger.AppComponent
import com.mobile.healthpoint.dagger.AppModule
import com.mobile.healthpoint.dagger.DaggerAppComponent

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint, proyect HealthPoint in date 13/7/17
 */
class App: Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}