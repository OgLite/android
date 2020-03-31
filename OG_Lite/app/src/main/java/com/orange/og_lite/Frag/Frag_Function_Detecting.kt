package com.orange.og_lite.Frag

import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__detecting.*
import kotlinx.android.synthetic.main.fragment_frag__function__detecting.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__detecting.view.detecting_img

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Detecting : JzFragement(R.layout.fragment_frag__function__detecting) {

//    var true_page = JzActivity.getControlInstance().changeFrag(Frag_Function_Relearn_Procedure(0),R.id.frage,"Frag_Function_Relearn_Procedure",false)
//    var false_page = JzActivity.getControlInstance().goBack("Frag_Function_Selection_error")

    override fun viewInit() {
        //refresh = true

        Thread{
            Thread.sleep(3000)
            handler.post {

                rootview.textView38.text = "Detecting....."

                PublicBeans.make =  "Toyota"
                PublicBeans.model = "Avensis"
                PublicBeans.year = "2014-2018"

                //change_page()
                //arrayListOf
                arrayOf(JzActivity.getControlInstance().changeFrag(
                    Frag_Function_Relearn_Procedure(
                        0
                    ), R.id.frage,"Frag_Function_Relearn_Procedure",false),
                          JzActivity.getControlInstance().goBack("Frag_Function_Selection_error")).random()

                //JzActivity.getControlInstance().goBack("Frag_Function_Selection_error")

                //JzActivity.getControlInstance().changeFrag(Frag_Function_Selection(1),R.id.frage,"Frag_Function_Selection_error",false)
            }

        }.start()

    }

    fun  change_page(){

        //var rdm : Random
        //val rdm = (0..1).random()
//        var true_page = JzActivity.getControlInstance().changeFrag(Frag_Function_Relearn_Procedure(0),R.id.frage,"Frag_Function_Relearn_Procedure",false)
//        var false_page = JzActivity.getControlInstance().goBack("Frag_Function_Selection_error")
        //var random_page = arrayOf(true_page,false_page).random()

        //return random_page
//        if(rdm ==1)
//        {
//            true_page
//            JzActivity.getControlInstance().toast(rdm.toString())
//        }
//        else
//        {
//            false_page
//            JzActivity.getControlInstance().toast(rdm.toString())
//        }

        //JzActivity.getControlInstance().toast(rdm.toString())
    }

}
