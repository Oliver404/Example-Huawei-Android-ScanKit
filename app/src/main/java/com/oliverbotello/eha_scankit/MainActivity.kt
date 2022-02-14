package com.oliverbotello.eha_scankit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.huawei.hms.framework.common.Utils
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.oliverbotello.eha_scankit.utils.CAMERA_REQ_CODE
import com.oliverbotello.eha_scankit.utils.SCAN_REQ_CODE
import com.oliverbotello.eha_scankit.utils.requestMyPermissions
import com.oliverbotello.eha_scankit.utils.showMessage

class MainActivity : AppCompatActivity() {
    val options = HmsScanAnalyzerOptions.Creator()
        .setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null) {
            showMessage(applicationContext, "No se obtuvo nada")

            return
        }

        if (requestCode == SCAN_REQ_CODE) {
            val obj = data.getParcelableExtra(ScanUtil.RESULT) as HmsScan?

            obj?.let {
                showMessage(applicationContext, it.toString())
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode:Int, permissions: Array<out String>, grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            showMessage(applicationContext, "Permiso aceptado, intente de nuevo su accion")
    }

    // Class Methods
    private fun initView() {
        findViewById<AppCompatButton>(R.id.btn_scan).setOnClickListener {
            onScanClick()
        }
    }

    fun onScanClick() {
        if (
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        )
            scanCode()
        else
            requestMyPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQ_CODE)
    }

    private fun scanCode() {
        ScanUtil.startScan(this, SCAN_REQ_CODE, options)
    }
}