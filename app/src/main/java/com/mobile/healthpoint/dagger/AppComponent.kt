package com.mobile.healthpoint.dagger

import com.mobile.healthpoint.domain.EnableBluetoothServer
import com.mobile.healthpoint.presentation.BluetoothPresenter
import com.mobile.healthpoint.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint.dagger, proyect HealthPoint in date 13/7/17
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(enableBluetoothServer: EnableBluetoothServer)
    fun inject(bluetoothPresenter: BluetoothPresenter)
    fun inject(mainActivity: MainActivity)
}