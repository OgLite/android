package com.orange.og_lite.Dialog

import android.app.Dialog
import android.view.KeyEvent
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.connectBack

class Da_Scan_ble(val connectBack:connectBack) : SetupDialog()
{
    val manager= (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager

    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
        manager.devicelist.clear()
        rootview.findViewById<RecyclerView>(R.id.re).layoutManager = LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        rootview.findViewById<RecyclerView>(R.id.re).adapter = manager.adapter
        manager.adapter.notifyDataSetChanged()
        manager.adapter.connectBack=connectBack

        rootview.findViewById<Button>(R.id.close).setOnClickListener {
            manager.Ble_Helper.stopScan()

            manager.Ble_Helper.disconnect()

            JzActivity.getControlInstance().closeDiaLog()
        }
        rootview.findViewById<Button>(R.id.rescan).setOnClickListener {
            manager.Ble_Helper.startScan()
        }
    }


}