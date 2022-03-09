package com.lingyunxiao.gifski.activity

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.lingyunxiao.gifski.GifskiJniApi
import com.lingyunxiao.gifski.GifskiUtil.parseGifskiResult
import com.lingyunxiao.gifski.ILogger
import com.lingyunxiao.gifski.InstanceKeeper
import com.lingyunxiao.gifski.ProgressCallback
import com.lingyunxiao.gifski.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val taskKey = 4234
    private val frameCount = 317
    private lateinit var outputFilePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (allPermissionsGranted()) {
            Thread(Runnable {
                setupGifski()
                val filesDir = getExternalFilesDir(null)!!
                outputFilePath = "$filesDir/4234_png/output.gif"
                val result = process()
                val resultStr = parseGifskiResult(result)
                MLog.info(TAG, "finish result means:$resultStr")
                Handler(Looper.getMainLooper()).post {
                    txv_result.text = "已完成：$resultStr"
                    btn_abort.visibility = View.GONE
                    if (result == 0) {
                        gif_preview.visibility = View.VISIBLE
                        Glide.with(this)
                            .load(outputFilePath)
                            .transition(withCrossFade())
                            .into(gif_preview)
                    }
                }
            }).start()

            btn_abort.setOnClickListener {
                GifskiJniApi.abort(taskKey)
            }
        } else {
            getRuntimePermissions()
        }
    }

    private fun setupGifski() {
        InstanceKeeper.logger = object : ILogger {
            override fun logit(msg: String) {
                MLog.info(TAG, msg)
            }

            override fun logit(msg: String, arg: Boolean) {
                MLog.info(TAG, msg, arg)
            }

            override fun logit(msg: String, arg: Int) {
                MLog.info(TAG, msg, arg)
            }
        }
        InstanceKeeper.progressCallback = ProgressCallback { frameNumber, taskKey ->
            MLog.info(TAG, "received progress:$frameNumber $taskKey")
            if (taskKey == this.taskKey) {
                Handler(Looper.getMainLooper()).post {
                    txv_progress.text = String.format("处理进度：%.2f%%", (frameNumber / frameCount.toFloat()) * 100)
                }
            }
        }
    }

    private fun process(): Int {
        MLog.info(TAG, "start")
        val targetWidth = 675
        val targetHeight = 1200
        val gifskiNativeObj = GifskiJniApi.gifskiNew(targetWidth, targetHeight, 90, false, true)
        MLog.info(TAG, "new instancePtr:$gifskiNativeObj")
        if (gifskiNativeObj == 0L) return -1
        try {
            val result = GifskiJniApi.startProcess(gifskiNativeObj, outputFilePath, taskKey)
            MLog.info(TAG, "result means:${parseGifskiResult(result)}")
            if (result == 0) pushFrameList(gifskiNativeObj, targetWidth, targetHeight)
        } catch (e: Throwable) {
            Log.e(TAG, "process error", e)
        } finally {
            return GifskiJniApi.finish(gifskiNativeObj)
        }
    }

    private fun pushFrameList(gifskiNativeObj: Long, targetWidth: Int, targetHeight: Int) {
        val frameList = mutableListOf<String>()
        val filesDir = getExternalFilesDir(null)!!
        for (index in 0 until frameCount) {
            val frameName = "${index.toString().padStart(6, '0')}.png"
            frameList.add("$filesDir/4234_png/$frameName")
        }
        MLog.info(TAG, "frame size:${frameList.size}")
        for ((index, frame) in frameList.withIndex()) {
            // TODO : 实现半途放弃退出页面，确认 gifski 内部会 abort
            val bitmap = BitmapFactory.decodeFile(frame)
            val result = GifskiJniApi.addFrameRgba(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, 5)
//            val op = BitmapFactory.Options()
//            op.inScaled = false
//            op.inPreferredConfig = Bitmap.Config.ARGB_8888
//            val bitmap = BitmapFactory.decodeFile(frame, op)
//            MLog.info(TAG, "rowBytes:${bitmap.rowBytes}")
//            val result = GifskiJniApi.addFrameRgb(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, bitmap.rowBytes, 5)
//            val result = GifskiJniApi.addFrameARgb(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, bitmap.rowBytes, 5)
            if (result != 0) {
                MLog.info(TAG, "push frame result means:${parseGifskiResult(result)}")
                return
            }
        }
        MLog.info(TAG, "done to push ${frameList.size} frames")
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