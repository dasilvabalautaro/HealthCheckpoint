package com.mobile.healthpoint.dagger

import android.content.Context
import com.mobile.healthpoint.data.BluetoothServer
import com.mobile.healthpoint.domain.EnableBluetoothServer
import com.mobile.healthpoint.presentation.BluetoothPresenter
import com.mobile.healthpoint.utils.RxBroadcastReceiver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint.dagger, proyect HealthPoint in date 13/7/17
 */

@Module
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideBluetoothServer(): BluetoothServer{
        return BluetoothServer(context)
    }
    @Provides
    fun provideBroadcast(): RxBroadcastReceiver {
        return RxBroadcastReceiver()
    }

    @Provides
    fun provideEnableBluetooth(): EnableBluetoothServer {
        return EnableBluetoothServer(context)
    }

    @Provides
    fun provideBluetoothPresenter(): BluetoothPresenter {
        return BluetoothPresenter(context)
    }
}