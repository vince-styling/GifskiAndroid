package com.hzy.face.morphme.activity

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hzy.face.morphme.R
import com.lingyunxiao.gifski.GifskiJniApi
import com.lingyunxiao.gifski.GifskiUtil.parseGifskiResult
import com.lingyunxiao.gifski.MLog
import java.util.*

class MainActivity : AppCompatActivity() {
    private val taskKey = 4234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (allPermissionsGranted()) {
            Thread(Runnable {
                process()
                Handler(Looper.getMainLooper()).post {
                    finish()
                }
            }).start()

            findViewById<View>(R.id.btn_discard).setOnClickListener {
                GifskiJniApi.abort(taskKey)
            }
        } else {
            getRuntimePermissions()
        }
    }

    private fun process() {
        Log.i("gifski", "start")
        val targetWidth = 675
        val targetHeight = 1200
        val gifskiNativeObj = GifskiJniApi.gifskiNew(targetWidth, targetHeight, 90, false, true)
        Log.i("gifski", "new instancePtr:$gifskiNativeObj")
        if (gifskiNativeObj == 0L) return
        try {
            val filesDir = getExternalFilesDir(null)!!
            val result = GifskiJniApi.startProcess(gifskiNativeObj, "$filesDir/4234_png/output.gif", taskKey)
            Log.i("gifski", "result means:${parseGifskiResult(result)}")
            if (result == 0) pushFrameList(gifskiNativeObj, targetWidth, targetHeight)
        } catch (e: Throwable) {
            Log.e("gifski", "process error", e)
        } finally {
            val result = GifskiJniApi.finish(gifskiNativeObj)
            Log.i("gifski", "finish result means:${parseGifskiResult(result)}")
        }
        Log.i("gifski", "end")
    }

    private fun pushFrameList(gifskiNativeObj: Long, targetWidth: Int, targetHeight: Int) {
        val frameList = mutableListOf<String>()
        val filesDir = getExternalFilesDir(null)!!
        for (index in 0..316) {
            val frameName = "${index.toString().padStart(6, '0')}.png"
            frameList.add("$filesDir/4234_png/$frameName")
        }
        Log.i("gifski", "frame size:${frameList.size}")
        for ((index, frame) in frameList.withIndex()) {
            // TODO : 测试半途放弃，看 gifski 内部的线程处理机制
            val bitmap = BitmapFactory.decodeFile(frame)
            val result = GifskiJniApi.addFrameRgba(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, 5)
//            val op = BitmapFactory.Options()
//            op.inScaled = false
//            op.inPreferredConfig = Bitmap.Config.ARGB_8888
//            val bitmap = BitmapFactory.decodeFile(frame, op)
//            Log.i("gifski", "rowBytes:${bitmap.rowBytes}")
//            val result = GifskiJniApi.addFrameRgb(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, bitmap.rowBytes, 5)
//            val result = GifskiJniApi.addFrameARgb(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, bitmap.rowBytes, 5)
            if (result != 0) {
                Log.i("gifski", "push frame result means:${parseGifskiResult(result)}")
                return
            }
        }
        Log.i("gifski", "done to push ${frameList.size} frames")
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
        private const val TAG = "MainPage"
        private const val PERMISSION_REQUESTS = 1
    }
}