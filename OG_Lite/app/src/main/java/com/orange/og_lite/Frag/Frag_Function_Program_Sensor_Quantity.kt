package com.orange.og_lite.Frag


import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__program__sensor__quantity.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Program_Sensor_Quantity : JzFragement(R.layout.fragment_frag__function__program__sensor__quantity) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun viewInit() {
        rootview.read_MMY_Title.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        rootview.read_sensor.setOnClickListener {
                JzActivity.getControlInstance().changeFrag(
                    Frag_Function_Program(
                        if(rootview.sensor_quantity.text.isEmpty()) "4" else (rootview.sensor_quantity.text.toString())
                    ), R.id.frage,"Frag_Function_Program",true)
        }

    }

}
