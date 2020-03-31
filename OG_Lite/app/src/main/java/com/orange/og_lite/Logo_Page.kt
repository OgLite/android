package com.orange.og_lite

import com.example.customerlibrary.AdminBeans
import com.example.customerlibrary.ChatPage
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.permission_C
import com.orange.og_lite.Page_MainActivity
import com.orange.og_lite.R

class Logo_Page : JzFragement(R.layout.activity_main)
{
    override fun viewInit() {
        requestPermission()
    }
fun requestPermission(){
    JzActivity.getControlInstance().permissionRequest(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
        object :permission_C{
            var siz=0
            override fun requestFalse(a: String) {
                JzActivity.getControlInstance().toast("必須啟用權限進行檔案存取!")
                requestPermission()
            }

            override fun requestSuccess(a: String) {
                siz+=1
                if(siz==2){
                    Thread{
                        Thread.sleep(2000)
                        handler.post{
//                            AdminBeans.setMessageInstance("sam38125","0",
//                               object :AdminBeans.Companion.result{
//                                   override fun result(a: Boolean) {
//                                       if(a){
//                                           JzActivity.getControlInstance().setHome(ChatPage("sam38124","客服","192.168.3.136"),"ChatPage")
//                                       }else{
//                                           JzActivity.getControlInstance().toast("註冊失敗")
//                                           JzActivity.getControlInstance().setHome(ChatPage("sam38124","客服","192.168.3.136"),"ChatPage")
//                                       }
//                                   }
//                               }
//                            )

                            JzActivity.getControlInstance().setHome(Page_MainActivity(),"Page_MainActivity")
                        }
                    }.start()
                }
            }
        },10)
}
}