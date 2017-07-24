package com.mobile.healthpoint.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable
import io.reactivex.functions.Cancellable

/**
 * Created by arturosilva on 13/7/17. Name of the package com.mobile.healthpoint.utils, proyect HealthPoint in date 13/7/17
 */
class RxBroadcastReceiver {
    fun create(context: Context,
               intentFilter: IntentFilter): Observable<Intent> {
        return Observable.create { subscriber ->
            val receiver = broadcastReceiver({ context, intent ->
                subscriber.onNext(intent!!) })
            context.registerReceiver(receiver, intentFilter)

            subscriber.setCancellable { Cancellable{
                    receiver.abortBroadcast()
                }
            }

        }
    }

    private fun broadcastReceiver(init: (Context, Intent?) -> Unit):
            BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                init(context, intent)
            }
        }
    }
}