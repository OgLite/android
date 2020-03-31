package com.orange.og_lite.Frag


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jzandroidwidget.Callback.runner
import com.example.jzandroidwidget.JzTool
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.Command
import com.orange.og_lite.Dialog.Da_Function_Enter_Sensor_ID
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Function_Program
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.Function_Program_Item
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Dia_SelectBaclk
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.ShowReadView

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Program(val quantity:String) : JzFragement(R.layout.fragment_frag__function__program) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    var myitem = Function_Program_Item()
    lateinit var adapter: Ad_Function_Program

    override fun viewInit() {
        PublicBeans.programNumber=quantity.toInt()
        rootview.read_MMY_Title2.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        myitem= Function_Program_Item()
        adapter= Ad_Function_Program(myitem)

        rootview.ShowReadView.layoutManager= GridLayoutManager(
            activity,
            1
        ) as RecyclerView.LayoutManager?
        rootview.ShowReadView.adapter=adapter

        for(i in 0 until quantity.toInt()) {
            myitem.add( "",Function_Program_Item.PROGRAM_WAIT)
        }

        adapter.notifyDataSetChanged()

        myitem.rowcount = quantity.toInt()
        adapter.notifyDataSetChanged()

        JzActivity.getControlInstance().showDiaLog(R.layout.da_function_enter_sensor_id,false,false,
            Da_Function_Enter_Sensor_ID(
                Dia_SelectBaclk {
                    adapter = Ad_Function_Program(myitem)
                    rootview.ShowReadView.adapter = adapter

                }
            ),"Da_Function_Enter_Sensor_ID")

        rootview.sending_data2.setOnClickListener {
            JzTool.newInstance.runTaskOnce("sending_data2", runner {
                if(checkComplete()){
                    Log.e("write","write")
                    write()
                }else{
                    Log.e("read","read")
                    read()
                }
            })
        }

    }

    fun read()
    {
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
        when(PublicBeans.selectWay){
            PublicBeans.KEY_IN->{
                adapter.notifyDataSetChanged()
            }
            PublicBeans.READ->{
                Command.getPrid(object :
                    Command.Companion.idback {
                    override fun result(a: String) {
                        insert(a)
                    }
                })
            }
            PublicBeans.SCAN->{
                PublicBeans.getSensor(object : PublicBeans.sensorBack {
                    override fun callback(content: String) {
                        insert(content)
                    }
                })
            }
        }
    }

    fun write()
    {
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
        Command.Program(myitem,
            object : Command.Companion.notify {
                override fun result() {

                    adapter.notifyDataSetChanged()
                }
            })
    }

    fun insert(id:String){
        if(myitem.programId.contains(id)){
            JzActivity.getControlInstance().toast("id repeat")
            return
        }

        var readold=true

        for(i in 0 until myitem.rowcount){
            if(myitem.programId[i].isEmpty()){
                myitem.programId[i]=id
                readold=false
                break
            }
        }

        if(checkComplete()){
            rootview.sending_data2.text=resources.getString(R.string.Program)

        }

        adapter.notifyDataSetChanged()
    }

    fun checkComplete():Boolean{

        for(i in 0 until myitem.rowcount){

            var exit = ArrayList<String>()
            exit.clear()
            for(j in 0 until myitem.rowcount)
            {
                if(i != j)
                {
                    exit.add(myitem.programId[j])
                }
            }

            if(myitem.programId[i].isEmpty() || exit.contains(myitem.programId[i])){
                return false
            }
        }
        return true
    }

}
