package com.orange.og_lite.Frag

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__relearn__procedure.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Relearn_Procedure(val changeFinal : Int) : JzFragement(R.layout.fragment_frag__function__relearn__procedure) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity
    var text_content_String : String =""

    override fun viewInit() {

        rootview.Read_MMY_Title.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        query_Relearn_Procedure()

        if(changeFinal == 0)
        {
            rootview.Read_ID_form_Vehice.setOnClickListener {

                Log.e("changePlace",PublicBeans.position.toString())

                when(PublicBeans.position) {
                    PublicBeans.OBD複製 -> {
                        JzActivity.getControlInstance().changeFrag(
                            Frag_Function_OBD_ID_copy(),
                            R.id.frage, "Frag_Function_OBD_ID_copy", true)
                    }
                    PublicBeans.OBD學碼 -> {
                        JzActivity.getControlInstance().changeFrag(
                            Frag_Function_OBD_ID_copy(),
                            R.id.frage, "Frag_Function_OBD_relearn", true)
                    }
                    PublicBeans.燒錄 -> {
                        JzActivity.getControlInstance().changeFrag(
                            Frag_Function_Program_Sensor_Quantity(),
                            R.id.frage, "Frag_Function_Program_Sensor_Quantity", true)
                    }
                    PublicBeans.複製ID -> {
                        JzActivity.getControlInstance().changeFrag(
                            Frag_Function_OBD_ID_copy(),
                            R.id.frage, "Frag_Function_ID_copy", true)
                    }
                    PublicBeans.讀取 -> {
                        JzActivity.getControlInstance().changeFrag(
                            Frag_Function_Check_Sensor_Selection(),
                            R.id.frage, "Frag_Function_Check_Sensor_Selection", true)
                    }

                }
            }
        }
        else
        {
            rootview.Read_ID_form_Vehice.text = getString(R.string.MENU)
            rootview.Read_ID_form_Vehice.setOnClickListener {
                JzActivity.getControlInstance().goMenu()
            }
        }

    }

    fun query_Relearn_Procedure()
    {
        var a = JzActivity.getControlInstance().getPro("Languages","")
        var colname = "English"

        Log.e("colname",a)

        val English="en"
        val 简体中文="zh"
        val 繁體中文="tw"
        val Italiano="it"
        val Deutsche="de"
        val Slovinčina="sk"
        val DANISH="da"

        when(a){
            繁體中文->{ colname="`Relearn Procedure (Traditional Chinese)`"}
            简体中文->{ colname="`Relearn Procedure (Jane)`"}
            Deutsche->{ colname="`Relearn Procedure (German)`"}
            English->{ colname="`Relearn Procedure (English)`"}
            Italiano->{ colname="`Relearn Procedure (Italian)`"}
            Slovinčina->{ colname="`Relearn Procedure (English)`"}
            DANISH->{ colname="`Relearn Procedure (English)`"}
        }

            mMainActivity.item
                //.query("select distinct Relearn Procedure (English) from `Summary table` where Make = '$PublicBeans.make' and Model = '$PublicBeans.model'  and Year = '$PublicBeans.year'", Sql_Result { result ->
                .query("select distinct " + colname + " from `Summary table` where `Make` = '" + PublicBeans.make + "'" + "and `Model` = '" + PublicBeans.model + "'" + "and `Year` = '" + PublicBeans.year + "' limit 0,1" , Sql_Result { result ->
                    //.query("select distinct `Model` from `Summary table`", Sql_Result { result ->
                    //val module = Util_MmySql_module()
                    //val data = result.getString(0)
                    //module.make=result.getString(0)
                    //module.modele=result.getString(0)
                    //name.add(module)
                    //Log.e("database",PublicBeans.year)
                    text_content_String=result.getString(0)
                    rootview.text_content.text = text_content_String
                })
    }

}
