package com.orange.og_lite.Frag

import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_Http_Command_Function
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__registration.view.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Registration : JzFragement(R.layout.fragment_frag__setting_pager__registration) {

    var isrunn = false

    var Arealist1 = ArrayList<String>()
    var Arealist2 = ArrayList<String>()
    var Arealist3 = ArrayList<String>()

    override fun viewInit() {

        Arealist1.clear()
        Arealist2.clear()
        Arealist3.clear()

        Arealist1.add("Select")
        Arealist1.add("EU")
        Arealist1.add("North America")
        Arealist1.add("台灣")
        Arealist1.add("中國大陸")
        val arrayAdapter1 = ArrayAdapter<String>(activity!!,
            R.layout.area_spinner, Arealist1)
        rootview.country_spinner.adapter = arrayAdapter1

        rootview.cancel.setOnClickListener { JzActivity.getControlInstance().goBack() }

        rootview.next.setOnClickListener {
            if (isrunn) {
                return@setOnClickListener
            }

            var email = rootview.email.text.toString()
            var password = rootview.password.text.toString()
            var repeatpassword = rootview.repeatpassword.text.toString()
            var serialnumber =  rootview.serialnumber.text.toString()
            var firstname =  rootview.firstname.text.toString()
            var lastname =  rootview.lastname.text.toString()
            var company =  rootview.company.text.toString()
            var phone =  rootview.phone.text.toString()
            var country = rootview.country_spinner.selectedItem.toString()
            var streat =  rootview.streat.text.toString()
            var city = rootview.city.text.toString()
            var state = rootview.state.text.toString()
            var zpcode =  rootview.zpcode.text.toString()

            JzActivity.getControlInstance().showDiaLog(R.layout.dataloading,false,false,"dataloading")

            if (!password.equals(repeatpassword)) {
                Toast.makeText(act, resources.getString(R.string.confirm_password), Toast.LENGTH_SHORT).show()
            }
            Thread {
                isrunn = true
                var a = 0
                a = Util_Http_Command_Function.Register(email, password, serialnumber, company, firstname, lastname, phone, state, country, city, streat, zpcode)

                handler.post {
                    JzActivity.getControlInstance().closeDiaLog()
                    if (a == -1) {
                        Toast.makeText(act, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
                    } else if (a == 1) {
                        Toast.makeText(act, resources.getString(R.string.be_register), Toast.LENGTH_SHORT).show()
                    } else {
                        //val profilePreferences = act.getSharedPreferences("Frag_SettingPager_Setting", Context.MODE_PRIVATE)
                        JzActivity.getControlInstance().setPro("admin", email)
                        JzActivity.getControlInstance().setPro("password", password)
                        JzActivity.getControlInstance().changeFrag(
                            Frag_MainActivity_Home(),
                            R.id.frage, "Frag_MainActivity_Home", false)
                    }
                }
                isrunn = false
            }.start()

        }

    }

}
