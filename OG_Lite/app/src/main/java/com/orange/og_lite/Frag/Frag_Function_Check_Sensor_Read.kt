package com.orange.og_lite.Frag

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jzandroidwidget.Callback.runner
import com.example.jzandroidwidget.JzTool
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.Command
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Function_Check_Sensor
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.Function_Check_Sensor_Item
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.SensorData
import kotlinx.android.synthetic.main.fragment_frag__function__show__read.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Check_Sensor_Read : JzFragement(R.layout.fragment_frag__function__show__read) {

    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    //var Data = ArrayList<TextView>()

    var myitem = Function_Check_Sensor_Item()
    lateinit var adapter: Ad_Function_Check_Sensor

    override fun viewInit() {
        //Data.clear()
        rootview.Read_MMY_Title.text =
            PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        adapter = Ad_Function_Check_Sensor(myitem)

        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        myitem.add("${getString(R.string.app_id)}", "")
        myitem.add("${SensorData.getPre()}", "")
        myitem.add("${SensorData.getTem()}", "")
        myitem.add("${getString(R.string.app_bat)}", "")
        myitem.add("${getString(R.string.app_voltage)}", "")

        adapter.notifyDataSetChanged()

        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }

        rootview.read.setOnClickListener {
            JzTool.newInstance.runTaskOnce("read", runner {
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
                    return@runner
                }
                Command.getSensorData(
                    myitem,
                    object : Command.Companion.callback {
                        override fun result(a: Boolean) {
                            adapter.notifyDataSetChanged()

                        }
                    })
            })

        }

    }

}
