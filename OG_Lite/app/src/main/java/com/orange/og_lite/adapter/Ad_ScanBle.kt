package com.orange.og_lite.adapter

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.BleManager
import com.orange.og_lite.Command
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.adapter_ad_select_ble.view.*
import java.util.ArrayList
interface  connectBack{
    fun  connec()
}
class Ad_ScanBle (val Ble: ArrayList<BluetoothDevice>,var manager: BleManager) : JzAdapter(R.layout.adapter_ad_select_ble)
{
    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity
lateinit var connectBack:connectBack
    override fun sizeInit(): Int {
        return Ble.size
    }

    //lateinit var act: RootActivity
    var RxChannel = "00008D81-0000-1000-8000-00805F9B34FB"
    var TxChannel = "00008D82-0000-1000-8000-00805F9B34FB"

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //mainPeace=activity!! as MainPeace

        if(Ble[position].name != null){holder.mView.Name.text = Ble[position].name.toString()}else{
            holder.mView.Name.text="UNKNOWN"
        }
        holder.mView.Address.text = Ble[position].address.toString()

        //holder.mView.setOnTouchListener(mOnTouchListener)
        //holder.mView.setOnScrollChangeListener { view, i, i2, i3, i4 -> Log.e("","") }

        holder.mView.setOnTouchListener{ v, event ->

            if(event.action == MotionEvent.ACTION_POINTER_DOWN)
            {
                Log.e("觸控事件：","ACTION_POINTER_DOWN")
            }

            if(event.action == MotionEvent.ACTION_POINTER_UP)
            {
                Log.e("觸控事件：","ACTION_POINTER_UP")
            }

            if(event.action == MotionEvent.ACTION_HOVER_ENTER)
            {
                Log.e("觸控事件：","ACTION_HOVER_ENTER")
            }

            if(event.action == MotionEvent.ACTION_MOVE)
            {
                //Log.e("觸控事件：","ACTION_MOVE")
                holder.mView.setBackgroundResource(R.color.white)
                holder.mView.Name.setTextColor(mMainActivity.resources.getColor(R.color.buttoncolor))
                holder.mView.Address.setTextColor(mMainActivity.resources.getColor(R.color.buttoncolor))
            }
            else
            {
                //Log.e("觸控事件：",event.action.toString())
                //holder.mView.setBackgroundResource(R.color.white)
                holder.mView.background = null
                holder.mView.Name.setTextColor(mMainActivity.resources.getColor(R.color.white))
                holder.mView.Address.setTextColor(mMainActivity.resources.getColor(R.color.white))

            }

            if(event.action == MotionEvent.ACTION_UP)
            {
                JzActivity.getControlInstance().showDiaLog(R.layout.dataloading, false, true,"dataloading")
                manager.Ble_Helper.stopScan()
                manager.Ble_Helper.connect(Ble[position].address,10, ConnectResult{

                    JzActivity.getControlInstance().closeDiaLog("dataloading")
                    if(it){JzActivity.getControlInstance().closeDiaLog()
                    if(::connectBack.isInitialized){
                        connectBack.connec()
                    }
                    }else{ JzActivity.getControlInstance().toast("連線失敗")}
                })

            }

        true
        }

    }

}