package com.orange.og_lite

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.util.Log
import android.webkit.PermissionRequest
import androidx.core.app.ActivityCompat
import com.example.jzandroidwidget.Callback.runner
import com.example.jzandroidwidget.JzTool
import com.jianzhi.jzblehelper.BleHelper
import com.jianzhi.jzblehelper.callback.BleCallBack
import com.jianzhi.jzblehelper.models.BleBinary
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.permission_C
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Frag.Frag_Function_Check_Sensor_Read
import com.orange.og_lite.Frag.Frag_Function_OBD_ID_copy
import com.orange.og_lite.Frag.Frag_Function_Program
import com.orange.og_lite.adapter.Ad_ScanBle
import com.orange.og_lite.adapter.connectBack
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__show__read.view.*


class BleManager(var activity: Activity) : BleCallBack {
    override fun onDisconnect() {
//        handler.post {
//            Ble_Helper.startScan()
//            JzActivity.getControlInstance().showDiaLog(R.layout.activity_scan_ble, false, false,
//                Da_Scan_ble(object : connectBack {
//                    override fun connec() {
//                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.stopScan()
//                    }
//                }), "Da_Scan_ble")
//        }
    }
     var adapter: Ad_ScanBle
    var devicelist = ArrayList<BluetoothDevice>()
    var handler= Handler()
    var Ble_Helper: BleHelper

    init {
        Ble_Helper = BleHelper( activity,this)
        adapter=Ad_ScanBle(devicelist,this)
    }

    override fun onConnectFalse() {
        //當ble連線失敗時觸發

        handler.post {

        }

    }

    override fun onConnectSuccess() {
        JzTool.newInstance.runTaskTimer("onConnectSuccess",0.02, runner {
            Log.d("JzBleMessage", "藍芽連線")
            Thread{
                Thread.sleep(1000)
                Command.getState()}.start()
        })
        //當ble連線成功時觸發
//        handler.post {

//            JzActivity.getControlInstance().toast("藍芽連線成功!")
//            JzActivity.getControlInstance().closeDiaLog()}
    }

    override fun onConnecting() {
        //當ble開始連線時觸發
        JzTool.newInstance.runTaskTimer("onConnecting",0.02, runner {
            Log.d("JzBleMessage", "藍芽正在連線中")
        })

    }

    override fun needGPS() {
        JzActivity.getControlInstance().closeDiaLog()
        JzActivity.getControlInstance().toast("請打開定位以搜尋藍芽")
    }
var lastRx=""
    override fun rx(a: BleBinary) {
        //三種Format方式接收藍牙訊息
        //1.ReadUTF()
        //2.ReadHEX()
        //3.ReadBytes()
        if(lastRx==a.readHEX()){return}
        lastRx=a.readHEX()
        if(a.readHEX()=="F500000304F20A"){
            val frag=JzActivity.getControlInstance().getNowPage()
            handler.post {
                if(frag is Frag_Function_OBD_ID_copy){
                    frag.rootview.sending_data.performClick()
                }else if(frag is Frag_Function_Program){
                    frag.rootview.sending_data2.performClick()
                }else if(frag is Frag_Function_Check_Sensor_Read){
                    frag.rootview.read.performClick()
                }
            }
            Log.d("JzBleMessage", "收到trigger${a.readHEX()}")
        }else{
            Command.rx += a.readHEX().toString()
        }


        Log.d("JzBleMessage", "收到藍牙消息${Command.rx}")
        }

    override fun requestPermission(permission: ArrayList<String>) {
        //當藍牙權限不足時觸發

//        for (i in permission) {
//            Log.e("JzBleMessage", "權限不足請先請求權限${i}")
//        }
        JzActivity.getControlInstance().permissionRequest(permission.toTypedArray(),  object : permission_C{
            override fun requestFalse(a: String?) {
                Log.e("JzBleMessage_False", a)
                JzActivity.getControlInstance().closeDiaLog()
                JzActivity.getControlInstance().toast("使用藍芽需要定位權限")
            }

            override fun requestSuccess(a: String?) {
                Log.e("JzBleMessage_Success", a)

            }
        },10)
    }

    override fun scanBack(device: BluetoothDevice) {
        //當掃描到新裝置時觸發
        if (!devicelist.contains(device) && device.name != null) {
            devicelist.add(device)
            adapter.notifyDataSetChanged()
        }
        Log.d("JzBleMessage", "掃描到裝置:名稱${device.name}/地址:${device.address}")

        //當獲取到device.address即可儲存下來，藍牙連線時會使用到
    }

    override fun tx(b: BleBinary) {
        lastRx=""
        JzTool.newInstance.runTaskTimer("tx",0.02, runner {
            Command.rx=""
            Log.d("JzBleMessage", "傳送藍牙消息${b.readHEX()}")
        })
    }


}
