package com.mobile.healthpoint.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mobile.healthpoint.App
import com.mobile.healthpoint.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var txtWeight: TextView? = null
    private var enabledServer: Button? = null

    @Inject
    lateinit var bluetoothPresenter: BluetoothPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.inject(this)
        txtWeight = findViewById(R.id.txtWeight) as TextView?
        enabledServer = findViewById(R.id.enabledServer) as Button?
        findViewById(R.id.enabledServer).setOnClickListener(onClickListener)
    }

    override fun onStart() {
        super.onStart()
        val messageBluetooth = this.bluetoothPresenter.resultMessage().map { s -> s }
        disposable.add(messageBluetooth.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s -> txtWeight!!.text = s })

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    internal val onClickListener = View.OnClickListener {
        view -> when(view.id){
            R.id.enabledServer -> {
                launch(CommonPool){
                bluetoothPresenter.listenClient("Peso 100 Kg.")
                }

            }
        }
    }

    private fun launchListenClient(data: String){
        launch(CommonPool){
            bluetoothPresenter.listenClient(data)
        }
    }

}
