package com.orange.og_lite

import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import com.jzsql.lib.mmySql.ItemDAO
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.jzchi.jzframework.tool.LanguageUtil
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.Util.Util_LanguageUtil
import com.orange.og_lite.callback.Update_C

class MainActivity : JzActivity() {
    override fun savedInstanceAble(): Boolean {
        return false
    }

    lateinit var item:ItemDAO
    lateinit var item_favorite:ItemDAO
    lateinit var item_File:ItemDAO
    lateinit var manager:BleManager
    val LOCALE_ENGLISH="en"
    lateinit var sqlmemnory:ItemDAO
    val LOCALE_CHINESE="zh"
    val LOCALE_TAIWAIN="tw"
    val LOCALE_ITALIANO="it"
    val LOCALE_DE="de"
    val LOCALE_SK="sk"
    val LOCALE_DA="da"
    var MyFavorite_Memory = com.orange.og_lite.beans.MyFavorite_Memory()

    override fun changePageListener(tag: String, frag: Fragment) {
        if (getControlInstance().findFragByTag("Page_MainActivity") is Page_MainActivity) {
            (getControlInstance().findFragByTag("Page_MainActivity") as Page_MainActivity).changePageListener(
                tag,
                frag
            )
        }

    }

    override fun keyEventListener(event: KeyEvent): Boolean {
        return true
    }

    override fun viewInit(rootview: View) {
        FirebaseMessaging.getInstance().subscribeToTopic("ogliteupdate")
            .addOnCompleteListener {
//                             Toast("註冊成功")
            };
        sqlmemnory=ItemDAO(this,"errormemory.db").create()
        manager=BleManager(this)
        when(getControlInstance().getPro("Languages",LOCALE_CHINESE)){
            LOCALE_ENGLISH->{
                LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ENGLISH);}
            LOCALE_CHINESE->{
                LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_CHINESE);}
            LOCALE_TAIWAIN->{
                LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_TAIWAIN);}
            LOCALE_ITALIANO->{
                LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ITALIANO);}
            LOCALE_DE->{
                LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_DE);}
            LOCALE_SK->{
                Util_LanguageUtil.updateLocale(this, Util_LanguageUtil.LOCALE_SK);}
            LOCALE_DA->{
                Util_LanguageUtil.updateLocale(this, Util_LanguageUtil.LOCALE_DA);}

        }
        item=ItemDAO( this, "mmy.db")
        item_favorite=ItemDAO( this, "favorite.db").create()
        item_File=ItemDAO( this, "file.db").create()
        item_File.exsql("CREATE TABLE IF NOT EXISTS `file` (\n" +
                "    ditectfit VARCHAR PRIMARY KEY,\n" +
                "    data      VARCHAR\n" +
                ");\n")
        getControlInstance().changePage(Logo_Page(),"Logo_Page",false)
    }

    override fun onDestroy() {
        super.onDestroy()
        item.close()
        item_favorite.close()
        item_File.close()
    }

}
