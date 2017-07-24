package com.mobile.healthpoint.data

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint.data, proyect HealthPoint in date 13/7/17
 */
interface BluetoothServerInterface {
    fun supportDevice(): Boolean
    fun enabledBluetooth()
    fun initServerSocket()
    fun listenerSocket(data: String)
    fun cancelConnect()
    fun ensureDiscoverable()
}