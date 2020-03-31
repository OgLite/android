package com.orange.og_lite.Frag

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Setting
import com.orange.og_lite.beans.SettingItem
import kotlinx.android.synthetic.main.fragment_frag__function__selection2.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Selection(var changePage:Int) : JzFragement(R.layout.fragment_frag__function__selection2) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    var myitem= SettingItem()
    lateinit var adapter: Ad_Setting

    override fun viewInit() {

        myitem=SettingItem()
        adapter= Ad_Setting(myitem)

        rootview.SelectButtonView.layoutManager= GridLayoutManager(activity, 1) as RecyclerView.LayoutManager?
        rootview.SelectButtonView.adapter=adapter

        if(changePage==1)
        {
            JzActivity.getControlInstance().closeDiaLog()
            myitem.add(
                R.mipmap.btn_auto_detection_n,getString(R.string.Auto_detection),"",View.GONE,
                Frag_Function_Detecting(),"Frag_Function_Detecting")
            if(JzActivity.getControlInstance().getNowPageTag().equals("Frag_Function_Selection_error"))
            {
                //JzActivity.getControlInstance().showDiaLog(R.layout.retry,true,false,"retry")

                JzActivity.getControlInstance().showDiaLog(R.layout.detecting_error,true,false,"detecting_error")

//                Thread {
//                    Thread.sleep(3000)
//                    handler.post {
//                        JzActivity.getControlInstance().closeDiaLog("retry")
//                        JzActivity.getControlInstance().closeDiaLog()
//                    }
//                }.start()
            }
        }
        myitem.add(
            R.mipmap.btn_scan_n,getString(R.string.Scan_Code),"", View.GONE,
            Frag_Function_QrcodeScanner(object :
                scanback {
                override fun callback(a: String) {
                    val contents_test = a.split("*", ":")
                    JzActivity.getControlInstance().toast(a)
                }
            }, 0),"Frag_Function_QrcodeScanner")
        myitem.add(
            R.mipmap.btn_icon_my_favourite_n,getString(R.string.Vehicle_data_selection),"",View.GONE,
            Frag_SelectMmyPage_Make(),"Frag_SelectMmyPage_Make")
        myitem.add(
            R.mipmap.btn_favourite_n,getString(R.string.app_my_favorite),"",View.GONE,
            Frag_SelectMmyPage_MyFavorite(),"Frag_SelectMmyPage_MyFavorite")
        adapter.notifyDataSetChanged()


        rootview.imageView8.setOnClickListener { JzActivity.getControlInstance().goMenu() }
    }

}
