package com.orange.og_lite.Frag

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jzandroidwidget.Callback.runner
import com.example.jzandroidwidget.JzTool
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Command
import com.orange.og_lite.Command.*
import com.orange.og_lite.Command.Companion.getObd
import com.orange.og_lite.Dialog.Da_Function_Enter_Sensor_ID
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_obd
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Dia_SelectBaclk
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.*
import kotlinx.android.synthetic.main.retry.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_OBD_ID_copy : JzFragement(R.layout.fragment_frag__function__obd__id_copy) {
    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity
    var myitem = ObdBeans()
    lateinit var adapter: Ad_obd
    override fun viewInit() {
        Log.e("取得obd","data:${PublicBeans.getOBD1()}")
        myitem.rowcount=PublicBeans.wheelCount()
        myitem.Tire_img.add(rootview.LF_image)
        myitem.Tire_img.add(rootview.RF_image)
        myitem.Tire_img.add(rootview.RR_image)
        myitem.Tire_img.add(rootview.LR_image)
        if(myitem.rowcount == 5) {
            //myitem.Tire_img.add(rootview.SP_image)
            myitem.Tire_img.add(rootview.Car_image)}
        rootview.read_MMY_Title.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year
        adapter= Ad_obd(myitem,this)
        rootview.ShowReadView.layoutManager= LinearLayoutManager(context)
        rootview.ShowReadView.adapter=adapter
        myitem.add("","",ObdBeans.PROGRAM_WAIT)
        myitem.add("","",ObdBeans.PROGRAM_WAIT)
        myitem.add("","",ObdBeans.PROGRAM_WAIT)
        myitem.add("","",ObdBeans.PROGRAM_WAIT)
        if(myitem.rowcount == 5) {
            //R.mipmap.icon_check_sensor_ok
            myitem.add("","",ObdBeans.PROGRAM_WAIT)
            rootview.Car_image.setBackgroundResource(R.mipmap.img_car_five_tire)
        }
        adapter.notifyDataSetChanged()
        JzActivity.getControlInstance().showDiaLog(R.layout.da_function_enter_sensor_id,false,false,
            Da_Function_Enter_Sensor_ID(
                Dia_SelectBaclk {
                    adapter = Ad_obd(myitem,this)
                    rootview.ShowReadView.adapter = adapter
                }
            ),"Da_Function_Enter_Sensor_ID")

        rootview.sending_data.setText(R.string.Next)
        rootview.sending_data.setOnClickListener {
            JzTool.newInstance.runTaskOnce("sending_data", runner {
                if(myitem.readable.contains(true)&&myitem.needPosition){
                    myitem.needPosition=false
                    rootview.sending_data.text=resources.getString(R.string.app_sensor_info_read)
                    if(PublicBeans.position==PublicBeans.OBD學碼){insertObd()}
                    adapter.notifyDataSetChanged()
                }else if(myitem.readable.contains(true)&&!myitem.needPosition){
                    if(checkComplete()){
                        write()
                    }else{
                        read()
                    }
                }else{
                    JzActivity.getControlInstance().toast("請先選擇燒錄位置")
                }
            })
        }
    }
    fun insertObd(){
        for(i in 0 until  myitem.rowcount){
            if(myitem.NewSensor[i].isEmpty()&&!myitem.readable[i]){
                myitem.NewSensor[i]=myitem.OldSemsor[i]
            }
        }
    }

    fun read(){
        if( !(JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.isConnect()){
            Command.handler.post {
                (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                JzActivity.getControlInstance()
                    .showDiaLog(R.layout.activity_scan_ble, false, false,
                        Da_Scan_ble(object : connectBack {
                            override fun connec() {
                                (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.stopScan()
                            }
                        }), "Da_Scan_ble"
                    )
            }
            return
        }
        Command.readId(object : Command.Companion.idback{
            override fun result(a: String) {
                insert(a)
            }
        })
    }
    fun  write(){
        if( !(JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.isConnect()){
            Command.handler.post {
                (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                JzActivity.getControlInstance()
                    .showDiaLog(R.layout.activity_scan_ble, false, false,
                        Da_Scan_ble(object : connectBack {
                            override fun connec() {
                                (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.stopScan()
                            }
                        }), "Da_Scan_ble"
                    )
            }
            return
        }
        when(PublicBeans.position){
            PublicBeans.OBD複製->{
                Command.writeID(myitem,object : Command.Companion.notify{
                    override fun result() {
                        adapter.notifyDataSetChanged()
                    }
                })
            }
            PublicBeans.OBD學碼->{
                getObd(myitem, object : Companion.callback {
                    override fun result(a: Boolean) {
                        if (a) {
                            adapter.notifyDataSetChanged()
                            Command.writeOBD(myitem,object : Command.Companion.notify{
                                override fun result() {

                                    adapter.notifyDataSetChanged()
                                }
                            })
                        } else {

                            JzActivity.getControlInstance().toast("讀取車上id失敗")
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.retry,
                                false,
                                false,
                                object : SetupDialog() {
                                    override fun dismess() {

                                    }

                                    override fun keyevent(event: KeyEvent): Boolean {
                                        return false
                                    }

                                    override fun setup(rootview: Dialog) {
                                        rootview.cancel.setOnClickListener {
                                            JzActivity.getControlInstance()
                                                .goBack("Frag_Function_Selection")
                                            JzActivity.getControlInstance()
                                                .closeDiaLog()
                                        }
                                    }
                                },
                                "retry"
                            )
                        }
                    }
                })
            }
            PublicBeans.複製ID->{
                Command.writeID(myitem,object : Command.Companion.notify{
                    override fun result() {

                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }

    }

    fun insert(id:String){
        var readold=true
        if(PublicBeans.position!=PublicBeans.OBD學碼){
            for(i in 0 until myitem.rowcount){
                if(myitem.readable[i]&&myitem.OldSemsor[i].isEmpty()){
                    if(myitem.OldSemsor.contains(id)){
                        JzActivity.getControlInstance().toast("id repeat")
                        return
                    }
                    myitem.OldSemsor[i]=id
                    readold=false
                    break
                }
            }
        }
        if(readold){
            for(i in 0 until myitem.rowcount){
                if(myitem.readable[i]&&myitem.NewSensor[i].isEmpty()){
                    if(myitem.NewSensor.contains(id)){
                        JzActivity.getControlInstance().toast("id repeat")
                        return
                    }
                    myitem.NewSensor[i]=id
                    break
                }
            }
        }
        checkComplete()
        adapter.notifyDataSetChanged()
    }

    fun checkComplete():Boolean{
        if(PublicBeans.position==PublicBeans.OBD學碼){
            for(i in 0 until myitem.rowcount){
                if((myitem.readable[i]&&myitem.NewSensor[i].isEmpty())){
                    return false
                }
            }
        }else{
            for(i in 0 until myitem.rowcount){
                if((myitem.readable[i]&&myitem.NewSensor[i].isEmpty())||(myitem.readable[i]&&myitem.OldSemsor[i].isEmpty())){
                    return false
                }
            }
        }
            when(PublicBeans.position){
                PublicBeans.OBD學碼->{
                    rootview.sending_data.text=resources.getString(R.string.transfer)
                }
                PublicBeans.OBD複製->{
                    rootview.sending_data.text=resources.getString(R.string.Program)
                }
                PublicBeans.複製ID->{
                    rootview.sending_data.text=resources.getString(R.string.Program)
                }
            }
        return true
    }
}
