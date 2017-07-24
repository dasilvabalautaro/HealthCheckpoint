package com.mobile.healthpoint.data

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint.data, proyect HealthPoint in date 13/7/17
 */
enum class BluetoothState(val state: Int) {
    STATE_TURNING_ON(11),
    STATE_ON(12),
    STATE_TURNING_OFF(13),
    STATE_OFF(10),
    STATE_CLOSE_LISTEN(14);

    companion object{
        fun from(findValue: Int):
                BluetoothState = BluetoothState.values()
                .first { it.state == findValue }
    }

}