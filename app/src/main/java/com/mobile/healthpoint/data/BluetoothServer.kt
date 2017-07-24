package com.mobile.healthpoint.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint.data, proyect HealthPoint in date 13/7/17
 */

class BluetoothServer(val context: Context): BluetoothServerInterface {
    var bluetoothAdapter: BluetoothAdapter? = null
    var serverSocket: BluetoothServerSocket? = null
    var messageBluetooth: String = String()
    var observableMessage: Subject<String> = PublishSubject.create()
    var inputData: InputStream? = null
    var outputData: OutputStream? = null

    val DEVICE_NOT_SUPPORT = "Device does not support Bluetooth"
    val DURATION_DETECTION = 300
    val PROTOCOL_SCHEME_RFCOMM = "PROTOCOL_SCHEME_RFCOMM"
    val uuid: UUID = UUID.fromString("57ef2400-5d26-11e7-9598-0800200c9a66")
    val STATE_ON = "STATE_ON"
    val STATE_CLOSE_LISTEN = "STATE_CLOSE_LISTEN"

    init {
        observableMessage
                .subscribe{messageBluetooth}
    }

    override fun supportDevice(): Boolean {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null){
            this.messageBluetooth = DEVICE_NOT_SUPPORT
            this.observableMessage.onNext(this.messageBluetooth)
            return false
        }
        return true

    }

    override fun enabledBluetooth() {
        if (!bluetoothAdapter!!.isEnabled){
            startRequestDiscoverable(context, DURATION_DETECTION)

        }else{
            this.messageBluetooth = STATE_ON
            this.observableMessage.onNext(this.messageBluetooth)

        }

    }

    private fun startRequestDiscoverable(context: Context, duration: Int){
        val enableIntent: Intent =
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        enableIntent.putExtra(BluetoothAdapter.
                EXTRA_DISCOVERABLE_DURATION, duration)
        enableIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivity(context, enableIntent, null)
    }

    override fun ensureDiscoverable() {
        if (bluetoothAdapter!!.scanMode !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            startRequestDiscoverable(context, DURATION_DETECTION)
        }
    }

    override fun initServerSocket() {
        serverSocket = bluetoothAdapter!!.
                listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, uuid)?:
                throw IOException()

    }

    override fun listenerSocket(data: String) {
        var socket: BluetoothSocket?
        this.messageBluetooth = STATE_ON
        this.observableMessage.onNext(this.messageBluetooth)

        while (true){

            socket = serverSocket!!.accept()

            if (socket != null){

                sendDataToClient(socket, data)
                serverSocket!!.close()
                break
            }

        }

    }

    override fun cancelConnect() {
        serverSocket?.close()
    }

    private fun initStream(bluetoothSocket: BluetoothSocket){
        inputData = bluetoothSocket.inputStream?: throw IOException()
        outputData = bluetoothSocket.outputStream?: throw IOException()
    }

    private fun writeData(data: ByteArray){
        outputData!!.write(data)
    }

    private fun sendDataToClient(bluetoothSocket: BluetoothSocket,
                                 dataToSend: String){
        launch(CommonPool) {
            initStream(bluetoothSocket)
            val sendData: ByteArray = dataToSend.toByteArray()
            writeData(sendData)
            messageBluetooth = STATE_CLOSE_LISTEN
            observableMessage.onNext(messageBluetooth)
            bluetoothSocket.close()

        }
    }
}