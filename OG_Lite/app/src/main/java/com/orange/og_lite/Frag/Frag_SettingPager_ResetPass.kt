package com.orange.og_lite.Frag

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_Http_Command_Function.ResetPassword
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__reset_pass.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_ResetPass : JzFragement(R.layout.fragment_frag__setting_pager__reset_pass) {

    override fun viewInit() {

        var run=false
        var change_text = false

        rootview.send_E_mail.setOnClickListener {
            if(change_text == true)
            {
                JzActivity.getControlInstance().changeFrag(
                    Frag_SettingPager_Sign_in(),
                    R.id.frage,"Frag_SettingPager_Sign_in",false)
            }

            if(run){
            return@setOnClickListener
            }

            JzActivity.getControlInstance().showDiaLog(R.layout.dataloading,false,false,"dataloading")
            var email=rootview.E_mail.text.toString()
            Thread{
                var isok= ResetPassword(email)
                handler.post {
                    run=false
                    JzActivity.getControlInstance().closeDiaLog()
                    if(isok){
                        change_text = true

                        rootview.send_E_mail.text = getString(R.string.Sign_in)
                        rootview.mail_content.text = getString(R.string.Reset_Email)
                        rootview.E_mail.visibility = View.GONE

                    }else{
                        Toast.makeText(act, R.string.nointernet, Toast.LENGTH_SHORT).show()
                    }


                }
            }.start() }

    }
}
