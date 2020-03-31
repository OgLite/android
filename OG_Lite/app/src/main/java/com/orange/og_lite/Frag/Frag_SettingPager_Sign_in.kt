package com.orange.og_lite.Frag

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_Http_Command_Function
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__sign_in.view.*

class Frag_SettingPager_Sign_in : JzFragement(R.layout.fragment_frag__setting_pager__sign_in) {
    override fun viewInit() {
        //JzActivity.getControlInstance().setPro("admin","sam56445645")

        rootview.two_forgot_button.setOnClickListener{ JzActivity.getControlInstance().changeFrag(
            Frag_SettingPager_ResetPass(),
            R.id.frage,"Frag_SettingPager_ResetPass",true) }

        rootview.registration.setOnClickListener { JzActivity.getControlInstance().changeFrag(
            Frag_SettingPager_Registration(),
            R.id.frage,"Frag_SettingPager_Registration",true) }

        rootview.agree.setOnClickListener {
            val admin=rootview.admin.text.toString()
            val password=rootview.password.text.toString()
            JzActivity.getControlInstance().showDiaLog(R.layout.dataloading,false,false,"dataloading")
            Thread{
                val a= Util_Http_Command_Function.ValidateUser(admin,password)
                //run=false
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog()
                    if(a){
                        JzActivity.getControlInstance().setPro("admin",admin)
                        JzActivity.getControlInstance().changeFrag(
                            Frag_MainActivity_Home(),
                            R.id.frage,"Frag_MainActivity_Home",false)
                    }else{
                        Toast.makeText(act, R.string.signfall, Toast.LENGTH_SHORT).show()
                    }
                }
            }.start() }


    }
}
