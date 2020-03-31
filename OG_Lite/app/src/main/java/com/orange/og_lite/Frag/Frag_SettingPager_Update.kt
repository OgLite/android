package com.orange.og_lite.Frag


import android.app.Dialog
import android.view.KeyEvent
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.callback.Update_C

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Update(var auto:Boolean) : JzFragement(R.layout.fragment_frag__setting_pager__update) {

    override fun viewInit() {
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
        Thread{
            Util_FileDowload.ChechUpdate(activity!!,object : Update_C {
                override fun Finish(a: Boolean) {
                    if(a){
                        val result= JzActivity.getControlInstance().checkUpdate(true)
                        handler.post{
                            JzActivity.getControlInstance().closeDiaLog()
                            if(result==null){
                                JzActivity.getControlInstance().toast(resources.getString(R.string.nointernet))
                            }else{
                                JzActivity.getControlInstance().setPro("update","nodata")
                            }
                        }
                    }
                }
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
            })
        }.start()
    }

}
