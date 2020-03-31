package com.orange.og_lite.Frag

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Setting
import com.orange.og_lite.beans.SettingItem
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__setting.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Setting : JzFragement(R.layout.fragment_frag__setting_pager__setting) {

    var myitem= SettingItem()
    lateinit var adapter: Ad_Setting

    override fun viewInit() {
        myitem=SettingItem()
        adapter= Ad_Setting(myitem)

        rootview.SettingButtonView.layoutManager= GridLayoutManager(
            activity,
            1
        ) as RecyclerView.LayoutManager?
        rootview.SettingButtonView.adapter=adapter

        myitem.add(
            R.mipmap.btn_favourite_n,getString(R.string.app_my_favorite_setting) ,"- "+getString(R.string.Add_or_remove_vehicles), View.VISIBLE,
            Frag_SettingPager_MyFavorite(),"Frag_SettingPager_MyFavorite")
        myitem.add(
            R.mipmap.btn_area_and_language,getString(R.string.AreaLanguage),"" , View.GONE,
            Frag_SettingPager_Set_Languages(1),"Frag_SettingPager_Set_Languages")
        myitem.add(
            R.mipmap.btn_updata,getString(R.string.update),"", View.GONE ,
            Frag_SettingPager_Update(false),"Frag_SettingPager_Update")
        myitem.add(
            R.mipmap.btn_set_password,getString(R.string.Reset_Password),"" , View.GONE ,
            Frag_SettingPager_Set_Up_Password(),"Frag_SettingPager_Set_Up_Password")
        myitem.add(
            R.mipmap.btn_privacy_policy,getString(R.string.app_setting_privacy_policy) ,"", View.GONE ,
            Frag_SettingPager_PrivaryPolicy(1),"Frag_SettingPager_PrivaryPolicy")
        adapter.notifyDataSetChanged()

    }
}
