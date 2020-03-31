package com.orange.og_lite.Frag

import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__privary_policy.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_PrivaryPolicy(val changePlace:Int) : JzFragement(R.layout.fragment_frag__setting_pager__privary_policy) {

//    companion object {
//        var changePlace=0
//    }

    override fun viewInit() {

        rootview.agree.setOnClickListener {
            if (changePlace == 0) {
            JzActivity.getControlInstance().changeFrag(
                Frag_SettingPager_Sign_in(),
                R.id.frage, "Frag_SettingPager_Sign_in", false)
            } else {
            JzActivity.getControlInstance().goBack()
            }
        }
        rootview.registration.setOnClickListener{
            android.os.Process.killProcess(android.os.Process.myPid())

        }

    }

}
