package com.lingyunxiao.gifski.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lingyunxiao.gifski.MLog
import com.lingyunxiao.gifski.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initView()
        getRuntimePermissions()
    }

    private fun initView() {
        if (allPermissionsGranted()) {
            txv_permgrant.text = "已授权"
            btn_opt.text = "去操作"
            btn_opt.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else {
            txv_permgrant.text = "需要授予存储权限"
            btn_opt.text = "去授权"
            btn_opt.setOnClickListener {
                getRuntimePermissions()
            }
        }
    }

    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    allNeededPermissions.add(permission)
                }
            }
        }

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            MLog.info(TAG, "Permission granted:$permission")
            return true
        }
        MLog.info(TAG, "Permission NOT granted:$permission")
        return false
    }

    companion object {
        private const val TAG = "SplashPage"
        private const val PERMISSION_REQUESTS = 1
    }
}