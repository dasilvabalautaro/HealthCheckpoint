package com.mobile.healthpoint.domain

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.IntentFilter
import com.mobile.healthpoint.App
import com.mobile.healthpoint.data.BluetoothServer
import com.mobile.healthpoint.data.BluetoothState
import com.mobile.healthpoint.utils.RxBroadcastReceiver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.io.IOException
import javax.inject.Inject


class EnableBluetoothServer(context: Context) {
    private var messageBluetooth: String = String()
    private var observableMessage: Subject<String> = PublishSubject.create()
    private var disposable: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var bluetoothServer: BluetoothServer
    @Inject
    lateinit var rxBroadcastReceiver: RxBroadcastReceiver

    init {
        App.appComponent.inject(this)
        val filter: IntentFilter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)

        observableMessage
                .subscribe { messageBluetooth }

        disposable.add(rxBroadcastReceiver.create(context, filter)
                .subscribeOn(Schedulers.newThread())
                .map {intent -> intent.extras}
                .subscribe {s ->
                    this.messageBluetooth = BluetoothState.from(s.
                            get(BluetoothAdapter.EXTRA_STATE) as Int).name
                    this.observableMessage.onNext(this.messageBluetooth)

                })

        val message = this.bluetoothServer.observableMessage.map { s -> s }

        disposable.add(message.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.messageBluetooth = s
                    this.observableMessage.onNext(this.messageBluetooth)
                })
    }
    fun listenClient(data: String){
        try {
            this.bluetoothServer.ensureDiscoverable()
            this.bluetoothServer.initServerSocket()
            this.bluetoothServer.listenerSocket(data)
        }catch (e: IOException){
            messageBluetooth = e.message!!
            observableMessage.onNext(messageBluetooth)
        }
    }

    fun connectBluetooth(){
        if (this.bluetoothServer.supportDevice()){
            this.bluetoothServer.enabledBluetooth()
        }
    }

    fun getResultMessage(): Subject<String>{
        return this.observableMessage
    }

}