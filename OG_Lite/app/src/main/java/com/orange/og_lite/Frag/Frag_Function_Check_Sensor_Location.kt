package com.orange.og_lite.Frag


import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Command
import com.orange.og_lite.Command.Companion.getSensorID
import com.orange.og_lite.R
import com.orange.og_lite.beans.CheckBeans
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__check__sensor__location.view.*
import kotlinx.android.synthetic.main.retry.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Check_Sensor_Location : JzFragement(R.layout.fragment_frag__function__check__sensor__location) {

    var Tire_text = ArrayList<TextView>()
    var Tire_img = ArrayList<TextView>()
    var check = ArrayList<String>()

    override fun viewInit() {
        Tire_text.clear()
        Tire_img.clear()
        check.clear()

        rootview.mmy_text.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year
        //rootview.Spt.getLayoutParams().width = rootview.Lrt.getLayoutParams().width

        Tire_text.add(rootview.Lft)
        Tire_text.add(rootview.Rft)
        Tire_text.add(rootview.Rrt)
        Tire_text.add(rootview.Lrt)
        if(PublicBeans.wheelCount() == 5)
        {
            Tire_text.add(rootview.Spt)
            rootview.Spt.visibility = View.VISIBLE
        }
        else
        {
            rootview.Spt.visibility = View.GONE
        }

        Tire_img.add(rootview.Lf)
        Tire_img.add(rootview.Rf)
        Tire_img.add(rootview.Rr)
        Tire_img.add(rootview.Lr)
        if(PublicBeans.wheelCount() == 5)
        {
            rootview.pad.setBackgroundResource(R.mipmap.img_car_five_tire)
            Tire_img.add(rootview.pad)
        }

        getSensorID(Tire_text, object : Command.Companion.callback {
            override fun result(a: Boolean) {
                if(a){

                }else{
                    JzActivity.getControlInstance().toast("讀取車上id失敗")
                    JzActivity.getControlInstance().showDiaLog(R.layout.retry,false,false,object:
                        SetupDialog(){
                        override fun dismess() {

                        }
                        override fun keyevent(event: KeyEvent): Boolean {
                            return false
                        }
                        override fun setup(rootview: Dialog) {
                            rootview.cancel.setOnClickListener {
                                JzActivity.getControlInstance().goBack("Frag_Function_Selection")
                                JzActivity.getControlInstance().closeDiaLog("retry")
                            }

                        }
                    },"retry")
                }
            }
        })

        rootview.read_sensor.setOnClickListener {

            var Check_size= check.size

            if(!Check_size.equals(PublicBeans.wheelCount()))
            {
                Command.checkID(
                    Tire_text,
                    Tire_img,
                    object : Command.Companion.notify {
                        override fun result() {
                            //adapter.notifyDataSetChanged()

                            when (CheckBeans.CHECK) {
                                CheckBeans.CHECK_WAIT -> {
                                    Tire_text[Check_size].setBackgroundResource(R.mipmap.img_input_box_locked)
                                    if (Check_size == 4) {
                                        Tire_img[Check_size].setBackgroundResource(R.mipmap.img_car_five_tire)
                                    } else {
                                        Tire_img[Check_size].setBackgroundResource(R.mipmap.icon_tire_cancel)
                                    }
                                }

                                CheckBeans.CHECK_SUCCESS -> {
                                    Tire_text[Check_size].setBackgroundResource(R.mipmap.img_input_box_ok)
                                    if (Check_size == 4) {
                                        Tire_img[Check_size].setBackgroundResource(R.mipmap.img_car_five_tire)
                                    } else {
                                        Tire_img[Check_size].setBackgroundResource(R.mipmap.img_tirel_ok)
                                    }
                                    check.add("")
                                }
                                CheckBeans.CHECK_FALSE -> {
                                    Tire_text[Check_size].setBackgroundResource(R.mipmap.img_input_box_fail)
                                    if (Check_size == 4) {
                                        Tire_img[Check_size].setBackgroundResource(R.mipmap.img_car_five_tire)
                                    } else {
                                        Tire_img[Check_size].setBackgroundResource(R.mipmap.img_wheel_fail)
                                    }
                                }
                            }
                        }
                    })
            }

//            PublicBeans.getSensor(object : PublicBeans.sensorBack {
//                override fun callback(content: String) {
//                insert(content)
//                }
//            })
        }


    }

    fun insert(id:String){
        if(Tire_text.toString().contains(id)){
            JzActivity.getControlInstance().toast("id repeat")
            return
        }
        else if(Tire_text.isEmpty())
        {
            JzActivity.getControlInstance().toast("id null")
            return
        }

        for(i in 0 until ObdBeans().rowcount){
            Tire_text[i].text=id
        }

//        if(checkComplete()){
//                    rootview.sending_data.text=resources.getString(R.string.transfer)
//            }
//        }

    }

}
