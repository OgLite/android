package com.orange.og_lite.Frag

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.tool.LanguageUtil
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_LanguageUtil
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__set__languages.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Set_Languages(var changePlace:Int) : JzFragement(R.layout.fragment_frag__setting_pager__set__languages) {

    var Arealist = ArrayList<String>()
    var LanguageList = ArrayList<String>()
    var areaposition=0
    var languageposition=0

    override fun viewInit() {

        //JzActivity.getControlInstance().getPro("Languages", LOCALE_ENGLISH)
        Arealist.add("Select")
        Arealist.add("EU")
        Arealist.add("North America")
        Arealist.add("台灣")
        Arealist.add("中國大陸")
        val arrayAdapter = ArrayAdapter<String>(activity!!,
            R.layout.language_spinner, Arealist)
        rootview.Area_spinner.adapter=arrayAdapter
        LanguageList.add("Select")
        LanguageList.add("繁體中文")
        LanguageList.add("简体中文")
        LanguageList.add("Deutsche")
        LanguageList.add("English")
        LanguageList.add("Italiano")
        LanguageList.add("Slovinčina")
        LanguageList.add("DANISH")

        val lanAdapter = ArrayAdapter<String>(activity!!,
            R.layout.language_spinner, LanguageList)
        rootview.Languages_spinner.adapter=lanAdapter
        rootview.Area_spinner.setSelection(JzActivity.getControlInstance().getPro("aIndex",0),true)
        rootview.Languages_spinner.setSelection(JzActivity.getControlInstance().getPro("lIndex",0),true)
        rootview.Area_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
                areaposition=pos
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {

            }
        }

        rootview.Languages_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
                languageposition=pos
                //JzActivity.getControlInstance().changeFrag(Frag_SettingPager_Set_Languages(),R.id.frage,"Frag_SettingPager_Set_Languages",false)
                when (rootview.Languages_spinner.selectedItem.toString()) {

                    "繁體中文" -> {
                        SetLan_live(LOCALE_TAIWAIN)
                    }
                    "简体中文" -> {
                        SetLan_live(LOCALE_CHINESE)
                    }
                    "Deutsche" -> {
                        SetLan_live(LOCALE_DE)
                    }
                    "English" -> {
                        SetLan_live(LOCALE_ENGLISH)
                    }
                    "Italiano" -> {
                        SetLan_live(LOCALE_ITALIANO)
                    }
                    "Slovinčina" -> {
                        SetLan_live(LOCALE_SK)
                    }
                    "DANISH" -> {
                        SetLan_live(LOCALE_DA)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {

            }
        }

        rootview.Set_up_button.setOnClickListener {
            if (rootview.Area_spinner.selectedItem.toString().equals("Select") || rootview.Languages_spinner.selectedItem.toString().equals(
                "Select"
            )
        ) {
            return@setOnClickListener
        }
            JzActivity.getControlInstance().getPro("Area", rootview.Area_spinner.selectedItem.toString());
            JzActivity.getControlInstance().getPro("Language",  rootview.Languages_spinner.selectedItem.toString());
            Log.d("Language", rootview.Languages_spinner.selectedItem.toString())
            when (rootview.Languages_spinner.selectedItem.toString()) {
                "繁體中文" -> {
                    SetLan(LOCALE_TAIWAIN)
                }
                "简体中文" -> {
                    SetLan(LOCALE_CHINESE)
                }
                "Deutsche" -> {
                    SetLan(LOCALE_DE)
                }
                "English" -> {
                    SetLan(LOCALE_ENGLISH)
                }
                "Italiano" -> {
                    SetLan(LOCALE_ITALIANO)
                }
                "Slovinčina" -> {
                    SetLan(LOCALE_SK)
                }
                "DANISH" -> {
                    SetLan(LOCALE_DA)
                }
            }
        }
    }

    open fun SetLan_live(value:String){
        when(value){
            LOCALE_ENGLISH->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_ENGLISH);}
            LOCALE_CHINESE->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_CHINESE);}
            LOCALE_TAIWAIN->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_TAIWAIN);}
            LOCALE_ITALIANO->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_ITALIANO);}
            LOCALE_DE->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_DE);}
            LOCALE_SK->{ Util_LanguageUtil.updateLocale(activity, Util_LanguageUtil.LOCALE_SK);}
            LOCALE_DA->{Util_LanguageUtil.updateLocale(activity, Util_LanguageUtil.LOCALE_DA);}
        }
        JzActivity.getControlInstance().setPro("aIndex",rootview.Area_spinner.selectedItemPosition)
        JzActivity.getControlInstance().setPro("lIndex",rootview.Languages_spinner.selectedItemPosition)
        if(changePlace == 0)
        {
            JzActivity.getControlInstance().changeFrag(
                Frag_SettingPager_Set_Languages(
                    0
                ), R.id.frage,"Frag_SettingPager_Set_Languages_first_Live",false)
        }
        else
        {
            JzActivity.getControlInstance().changeFrag(
                Frag_SettingPager_Set_Languages(
                    1
                ), R.id.frage,"Frag_SettingPager_Set_Languages_Live",false)
        }
    }

    open fun SetLan(value:String){
        JzActivity.getControlInstance().setPro("Languages",value)
        JzActivity.getControlInstance().setPro("aIndex",rootview.Area_spinner.selectedItemPosition)
        JzActivity.getControlInstance().setPro("lIndex",rootview.Languages_spinner.selectedItemPosition)
        Laninit()
        //JzActivity.getControlInstance().changeFrag(Frag_SettingPager_Set_Languages(),R.id.frage,"Frag_SettingPager_Set_Languages",false)
        if(changePlace == 0)
        {
            JzActivity.getControlInstance().changeFrag(
                Frag_SettingPager_PrivaryPolicy(
                    0
                ), R.id.frage,"Frag_SettingPager_PrivaryPolicy_first",false)
        }
        else
        {
            JzActivity.getControlInstance().goMenu()
        }
    }

    val LOCALE_ENGLISH="en"
    val LOCALE_CHINESE="zh"
    val LOCALE_TAIWAIN="tw"
    val LOCALE_ITALIANO="it"
    val LOCALE_DE="de"
    val LOCALE_SK="sk"
    val LOCALE_DA="da"
    open fun Laninit(){
        when(JzActivity.getControlInstance().getPro("Languages",LOCALE_ENGLISH)){
            LOCALE_ENGLISH->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_ENGLISH);}
            LOCALE_CHINESE->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_CHINESE);}
            LOCALE_TAIWAIN->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_TAIWAIN);}
            LOCALE_ITALIANO->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_ITALIANO);}
            LOCALE_DE->{LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_DE);}
            LOCALE_SK->{Util_LanguageUtil.updateLocale(activity, Util_LanguageUtil.LOCALE_SK);}
            LOCALE_DA->{Util_LanguageUtil.updateLocale(activity, Util_LanguageUtil.LOCALE_DA);}
        }
    }

}
