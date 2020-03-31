package com.orange.og_lite

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Dialog.Da_Logout
import com.orange.og_lite.Frag.Frag_MainActivity_Home
import com.orange.og_lite.Frag.Frag_SettingPager_Set_Languages
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Update_C
import kotlinx.android.synthetic.main.page_activity.view.*
import android.R.attr.bottom
import android.R.attr.right
import android.R.attr.top
import android.R.attr.left
import android.widget.LinearLayout
import androidx.core.view.setPadding


class Page_MainActivity :JzFragement(R.layout.page_activity){

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun viewInit() {
        rootview.LeftTopButton.visibility = View.GONE
        rootview.RightTopButton.visibility = View.GONE
        rootview.MainTitle.text = ""
        rootview.RightTopButton.setOnClickListener {  }
        rootview.LeftTopButton.setOnClickListener {  }
        //JzActivity.getControlInstance().changeFrag(Frag_MainActivity_Home(),R.id.frage,"Frag_MainActivity_Home",false)
        //JzActivity.getControlInstance().setPro("admin","sam56445645")
        //val profilePreferences = getSharedPreferences("Frag_SettingPager_Setting", Context.MODE_PRIVATE)
        if (JzActivity.getControlInstance().getPro("admin", "nodata")=="nodata") {
            //rootview.frage.setBackgroundColor(resources.getColor(R.color.backgroung))
            JzActivity.getControlInstance().changeFrag(
                Frag_SettingPager_Set_Languages(
                    0
                ), R.id.frage, "Frag_SettingPager_Set_Languages_first", false)
        } else {
            rootview.frage.setBackgroundColor(resources.getColor(R.color.backgroung))
            JzActivity.getControlInstance().changeFrag(Frag_MainActivity_Home(),R.id.frage,"Frag_MainActivity_Home",false)
        }
        dataloading()
    }

    fun changePageListener(tag: String, frag: Fragment) {
        Log.e("switch", tag)

        rootview.LeftTopButton.visibility= View.VISIBLE
        rootview.RightTopButton.visibility = View.GONE
        rootview.LeftTopButton.setImageResource(R.mipmap.btn_back)
        rootview.LeftTopButton.setOnClickListener { JzActivity.getControlInstance().goBack() }

        if (mMainActivity.manager.Ble_Helper.isConnect())
        {
            rootview.RightTopButton.visibility = View.VISIBLE
            rootview.RightTopButton.setImageResource(R.mipmap.icon_o_genius_lite_link)
            rootview.RightTopButton.setOnClickListener {  }
        }

        when (tag)
        {
            "Frag_SettingPager_Set_Languages_first" ->
            {
                rootview.MainTitle.text = getString(R.string.AreaLanguage)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Set_Languages_first_Live" ->
            {
                //rootview.MainTitle.text = getString(R.string.AreaLanguage)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_PrivaryPolicy_first" ->
            {
                rootview.MainTitle.text = getString(R.string.app_setting_privacy_policy)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Sign_in" ->
            {
                rootview.MainTitle.text = getString(R.string.app_sign_in)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Registration" ->
            {
                rootview.MainTitle.text = getString(R.string.Registration)
            }

            "Frag_SettingPager_ResetPass" ->
            {
                rootview.MainTitle.text = getString(R.string.Reset_Password)
            }

            "Frag_MainActivity_Home" ->
            {
                rootview.LeftTopButton.visibility= View.GONE
                rootview.MainTitle.text = getString(R.string.app_o_genius)
                rootview.RightTopButton.visibility = View.VISIBLE
                rootview.RightTopButton.setImageResource(R.mipmap.btn_sign_out)

                rootview.RightTopButton.setOnClickListener {
                    JzActivity.getControlInstance().showDiaLog(R.layout.logout, true, false,
                        Da_Logout(),"logout")
                }
            }

            "Frag_Function_Selection" ->
            {
                when(PublicBeans.position) {
                    PublicBeans.OBD複製 -> {
                        rootview.MainTitle.text = getString(R.string.idcopyobd)
                    }
                    PublicBeans.OBD學碼 -> {
                        rootview.MainTitle.text = getString(R.string.app_home_obdii_relearn)
                    }
                    PublicBeans.燒錄 -> {
                        rootview.MainTitle.text = getString(R.string.Program)
                    }
                    PublicBeans.複製ID -> {
                        rootview.MainTitle.text = getString(R.string.ID_COPY)
                    }
                    PublicBeans.讀取 -> {
                        rootview.MainTitle.text = getString(R.string.app_home_check_sensor)
                    }
                }
            }
        }

        when (tag)
        {
            "Frag_SettingPager_Setting" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.app_setting)}

            "Frag_SettingPager_MyFavorite" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.app_my_favorite_setting)}

            "Frag_SettingPager_AddMyFavorite" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.app_my_favorite_setting)
            }

            "Frag_SettingPager_Set_Languages_Live" ->
            {
                rootview.RightTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Set_Languages" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.AreaLanguage)}

            "Frag_SettingPager_Update" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.update)}

            "Frag_SettingPager_Set_Up_Password" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.Reset_Password)}

            "Frag_SettingPager_PrivaryPolicy" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = getString(R.string.app_setting_privacy_policy)}

        }

        if(rootview.RightTopButton.visibility == View.GONE && rootview.LeftTopButton.visibility != View.GONE)
        {
            rootview.MainTitle.setPadding(-200,0,0,0)
            //rootview.MainTitle.setPadding()
//            val lp = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            lp.setMargins(100, 24, 0, 0)
//            rootview.MainTitle.setLayoutParams(lp)
        }
        else if(rootview.RightTopButton.visibility == View.GONE && rootview.LeftTopButton.visibility == View.GONE)
        {
            rootview.MainTitle.setPadding(-100,0,0,0)
        }
        else
        {
            rootview.MainTitle.setPadding(0,0,0,0)
//            val lp = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            lp.setMargins(0, 0, 0, 0)
//            rootview.MainTitle.setLayoutParams(lp)
        }
    }
    fun dataloading(){
        JzActivity.getControlInstance()
            .showDiaLog(R.layout.loading_view,false,false,object : SetupDialog(){
                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.findViewById<TextView>(R.id.tit).text="${resources.getString(R.string.app_updating)}"
                }

                override fun dismess() {

                }
            },"loading_view")
        Util_FileDowload.haveData(activity!!,object : Update_C {
            override fun Updateing(progress: Int) {
                handler.post {
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.loading_view,false,false,object : SetupDialog(){
                            override fun keyevent(event: KeyEvent): Boolean {
                                return false
                            }

                            override fun setup(rootview: Dialog) {
                                rootview.findViewById<TextView>(R.id.tit).text="${resources.getString(R.string.app_updating)}$progress%"
                            }

                            override fun dismess() {

                            }
                        },"loading_view")
                }
            }
            override fun Finish(a: Boolean) {
                handler.post{
                    if(a){
                        JzActivity.getControlInstance().closeDiaLog("loading_view")
                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).item.create()
                    }else{
                        dataloading()
                    }

                }
            }
        })
    }
}