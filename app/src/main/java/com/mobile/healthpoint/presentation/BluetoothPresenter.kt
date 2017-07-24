package com.mobile.healthpoint.presentation

import android.content.Context
import com.mobile.healthpoint.App
import com.mobile.healthpoint.domain.EnableBluetoothServer
import io.reactivex.subjects.Subject
import javax.inject.Inject


class BluetoothPresenter(val context: Context) {

    @Inject
    lateinit var enableBluetoothServer: EnableBluetoothServer

    init {
        App.appComponent.inject(this)

        this.enableBluetoothServer.connectBluetooth()
    }

    fun resultMessage(): Subject<String> {
        return this.enableBluetoothServer.getResultMessage()
    }

    fun listenClient(data: String){
        this.enableBluetoothServer.listenClient(data)
    }

}