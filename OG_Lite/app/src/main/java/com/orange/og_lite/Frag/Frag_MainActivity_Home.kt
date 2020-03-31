package com.orange.og_lite.Frag

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jzsql.lib.mmySql.InitCaller
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Home
import com.orange.og_lite.beans.HomeItem
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.check_update.*
import kotlinx.android.synthetic.main.fragment_frag_home.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
class Frag_MainActivity_Home : JzFragement(R.layout.fragment_frag_home) {
    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity
    var myitem= HomeItem()
    lateinit var adapter: Ad_Home
    var havemmy=false

    override fun viewInit() {
        if(JzActivity.getControlInstance().getPro("update","nodata") != "nodata"){
            JzActivity.getControlInstance().showDiaLog(R.layout.check_update, false, false, object :
                SetupDialog(){
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    JzActivity.getControlInstance().closeDiaLog()
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.content.text=JzActivity.getControlInstance().getPro("update","nodata")
                    rootview.cancel.setOnClickListener { JzActivity.getControlInstance().closeDiaLog()}
                    rootview.yes.setOnClickListener {
                        JzActivity.getControlInstance().closeDiaLog()
                        JzActivity.getControlInstance().changeFrag(Frag_SettingPager_Update(true),R.id.frage,"Frag_SettingPager_Update",true)
                    }
                    if(JzActivity.getControlInstance().getPro("update","nodata").trim().isEmpty()){
                        rootview.content.visibility= View.GONE
                    }
                }
            },"check_update")
        }
        refresh=true
        myitem=HomeItem()
        adapter= Ad_Home(myitem)

        rootview.HomeButtonView.layoutManager= GridLayoutManager(
            activity,
            2
        ) as RecyclerView.LayoutManager?
        rootview.HomeButtonView.adapter=adapter

        myitem.add(
            R.mipmap.btn_id_copy_by_obd_n, getString(R.string.idcopyobd),1f,
            Frag_Function_Selection(1),"Frag_Function_Selection",PublicBeans.OBD複製)
        myitem.add(
            R.mipmap.btn_obdii_relearn_n, getString(R.string.app_home_obdii_relearn),1f,
            Frag_Function_Selection(1),"Frag_Function_Selection",PublicBeans.OBD學碼)
        myitem.add(
            R.mipmap.btn_program_n, getString(R.string.Program),1f,
            Frag_Function_Selection(0),"Frag_Function_Selection",PublicBeans.燒錄)
        myitem.add(
            R.mipmap.btn_id_copy_n, getString(R.string.ID_COPY),1f,
            Frag_Function_Selection(0),"Frag_Function_Selection",PublicBeans.複製ID)
        myitem.add(
            R.mipmap.btn_check_sensor_n, getString(R.string.app_home_check_sensor),1f,
            Frag_Function_Selection(0),"Frag_Function_Selection",PublicBeans.讀取)
        myitem.add(R.mipmap.btn_online_shopping_n, getString(R.string.app_home_online_shopping),1f, Frag_MainActivity_Home(),"Frag_MainActivity_Home",PublicBeans.線上訂購)
        myitem.add(R.mipmap.btn_users_manual_p, getString(R.string.app_user_manual),0.5f, null,"",0)
        myitem.add(
            R.mipmap.btn_setting_n, getString(R.string.app_setting),1f,
            Frag_SettingPager_Setting(),"Frag_SettingPager_Setting",0)
        adapter.notifyDataSetChanged()
//        if(!havemmy){db_download()}
    }

}
