package com.orange.og_lite.Frag

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__qrcode_scanner.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
public interface scanback{
    fun callback(content:String)
}
class Frag_Function_QrcodeScanner(val back: scanback, val changePlace:Int) : JzFragement(
    R.layout.fragment_frag__function__qrcode_scanner
), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    var ALL_FORMATS: ArrayList<BarcodeFormat> = ArrayList(1)
    lateinit var mMainActivity: MainActivity
    override fun viewInit() {
        RequestPermission()
        mMainActivity=activity!! as MainActivity
        mScannerView = ZXingScannerView(activity)
        ALL_FORMATS.add(BarcodeFormat.DATA_MATRIX)
        mScannerView!!.setFormats(ALL_FORMATS)
        mScannerView!!.resumeCameraPreview(this)
        mScannerView!!.setAutoFocus(true)
        mScannerView!!.setAspectTolerance(0.0f)

        rootview.frame.addView(mScannerView)
    }

    override fun handleResult(rawResult: Result?) {
        if(changePlace == 0)
        {mmy_query(rawResult!!.text.split("*")[0])}
        else
        {JzActivity.getControlInstance().goBack()
            back.callback(rawResult!!.text)}



    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    fun RequestPermission() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(activity!!)
                    .setCancelable(false)
                    .setTitle("需要相機權限")
                    .setMessage("需要相機權限才能掃描BARCODE!")
                    .setPositiveButton(
                        "確認"
                    ) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.CAMERA),
                            1
                        )
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), 1)
            }
        }
    }

    fun mmyname(): String {
        try {
            val url = URL("https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/")
            val conn = url.openConnection() as HttpURLConnection
            // val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8") as Reader?)
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
            var line: String? = null
            val strBuf = StringBuffer()
            line=reader.readLine()
            while (line != null) {
                strBuf.append(line)
                line=reader.readLine()
            }
            val arg = strBuf.toString().split("HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (a in arg) {
                if (a.contains(".db")) {
                    return a.substring(a.indexOf(">") + 1, a.indexOf("<"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "nodata"
    }

    fun mmy_query(mmy_number : String) {

        Thread {
            //資料查詢
            mMainActivity.item.create().query("select * from `Summary table` where `MMY number` = " + "'"  + mmy_number + "'", Sql_Result {
                //Callback回調，會迴圈跑到所有資料載入完
                val result1 = it.getString(0)
                val result2 = it.getString(1)
                val result3 = it.getString(2)

                handler.post {

                    PublicBeans.make = result1
                    PublicBeans.model = result2
                    PublicBeans.year = result3

                    JzActivity.getControlInstance()
                        .changeFrag(
                            Frag_Function_Relearn_Procedure(0),
                            R.id.frage, "Frag_Function_Relearn_Procedure", true)
                }

            });
        }.start()
    }

}
