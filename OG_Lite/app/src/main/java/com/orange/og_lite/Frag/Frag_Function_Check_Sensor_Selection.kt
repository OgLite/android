package com.orange.og_lite.Frag

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Setting
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.SettingItem
import kotlinx.android.synthetic.main.fragment_frag__function__check__sensor__selection.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Check_Sensor_Selection : JzFragement(R.layout.fragment_frag__function__check__sensor__selection) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    var myitem= SettingItem()
    lateinit var adapter: Ad_Setting

    override fun viewInit() {
        myitem.clear()
        refresh=true
        rootview.Read_MMY_Title.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        myitem=SettingItem()
        adapter= Ad_Setting(myitem)

        rootview.SelectButtonView.layoutManager= GridLayoutManager(
            activity,
            1
        ) as RecyclerView.LayoutManager?
        rootview.SelectButtonView.adapter=adapter

        myitem.add(
            R.mipmap.btn_read_sensor_n,getString(R.string.app_sensor_info_read),"", View.GONE,
            Frag_Function_Check_Sensor_Read(),"Frag_Function_Check_Sensor_Read")
        myitem.add(
            //R.mipmap.btn_check_n,"Check Sensor\n" + "Installation\n" + "Location","",View.GONE,
            R.mipmap.btn_check_n,getString(R.string.app_sensor_info_position),"",View.GONE,
            Frag_Function_Check_Sensor_Location(),"Frag_Function_Check_Sensor_Location")
        adapter.notifyDataSetChanged()

    }

}
