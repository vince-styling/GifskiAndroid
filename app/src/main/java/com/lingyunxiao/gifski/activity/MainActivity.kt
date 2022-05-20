package com.lingyunxiao.gifski.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.lingyunxiao.gifski.FrameListBuilder
import com.lingyunxiao.skigifcore.SkigifJniApi
import com.lingyunxiao.skigifcore.GifskiUtil.parseGifskiResult
import com.lingyunxiao.skigifcore.ILogger
import com.lingyunxiao.skigifcore.InstanceKeeper
import com.lingyunxiao.gifski.MLog
import com.lingyunxiao.skigifcore.ProgressCallback
import com.lingyunxiao.gifski.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private val taskKey = 4234
    private val frameCount = 317
    private lateinit var outputFilePath: String
    private var isTerminated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread {
            setupGifski()
            outputFilePath = FrameListBuilder.buildOutPutLocation()
            File(outputFilePath).delete()
            val result = process()
            val resultStr = parseGifskiResult(result)
            MLog.info(TAG, "finish result means:$resultStr")
            Handler(Looper.getMainLooper()).post {
                txv_result.text = "已结束：$resultStr"
                btn_abort.visibility = View.GONE
                if (result == 0) {
                    gif_preview.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(outputFilePath)
                        .transition(withCrossFade())
                        .into(gif_preview)
                }
            }
        }.start()
        btn_abort.setOnClickListener {
            SkigifJniApi.abort(taskKey)
        }
    }

    private fun setupGifski() {
        InstanceKeeper.logger = ILogger { msg -> MLog.info(TAG, msg) }
        InstanceKeeper.progressCallback = ProgressCallback { writeCount, taskKey ->
            MLog.info(TAG, "received progress:$writeCount $taskKey")
            if (taskKey == this.taskKey) {
                Handler(Looper.getMainLooper()).post {
                    txv_progress.text = String.format("处理进度：%.2f%%", (writeCount / frameCount.toFloat()) * 100)
                }
            }
        }
    }

    private fun process(): Int {
        MLog.info(TAG, "start")
//        val targetWidth = FrameListBuilder.getTargetWidth()
//        val targetHeight = FrameListBuilder.getTargetHeight()
        val (targetWidth, targetHeight) = calculateAdjustSize(
            274,
            274,
            FrameListBuilder.getTargetWidth(),
            FrameListBuilder.getTargetHeight()
        )
        val gifskiNativeObj = SkigifJniApi.skigifNew(targetWidth, targetHeight, 100, false, 0)
        MLog.info(TAG, "new instancePtr:$gifskiNativeObj")
        if (gifskiNativeObj == 0L) return -1
        try {
            val result = SkigifJniApi.startProcess(gifskiNativeObj, outputFilePath, taskKey)
            MLog.info(TAG, "result means:${parseGifskiResult(result)}")
            if (result == 0) pushFrameList(gifskiNativeObj, targetWidth, targetHeight)
        } catch (e: Throwable) {
            Log.e(TAG, "process error", e)
        } finally {
            return SkigifJniApi.finish(gifskiNativeObj)
        }
    }

    private fun pushFrameList(gifskiNativeObj: Long, targetWidth: Int, targetHeight: Int) {
        val frameList = FrameListBuilder.build()
        MLog.info(TAG, "frame size:${frameList.size}")
        for ((index, frame) in frameList.withIndex()) {
            // Presentation timestamp (PTS) is time in seconds, since start of the file, when this frame is to be displayed
            val pts = frame.timestampUs / 1000f / 1000.toDouble()
//            val bitmap = BitmapFactory.decodeFile(frame.file.absolutePath)
//            val result = SkigifJniApi.addFrameRgba(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, pts)
            val result = SkigifJniApi.addFrameFile(gifskiNativeObj, frame.file.absolutePath, index, pts)
//            val op = BitmapFactory.Options()
//            op.inScaled = false
//            op.inPreferredConfig = Bitmap.Config.ARGB_8888
//            val bitmap = BitmapFactory.decodeFile(frame, op)
//            MLog.info(TAG, "rowBytes:${bitmap.rowBytes}")
//            val result = GifskiJniApi.addFrameRgb(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, bitmap.rowBytes, 5)
//            val result = GifskiJniApi.addFrameARgb(gifskiNativeObj, bitmap, index, targetWidth, targetHeight, bitmap.rowBytes, 5)
            if (isTerminated) return
            if (result != 0) {
                MLog.info(TAG, "push frame result means:${parseGifskiResult(result)}")
                return
            }
        }
        MLog.info(TAG, "done to push ${frameList.size} frames")
    }

    override fun onDestroy() {
        super.onDestroy()
        SkigifJniApi.abort(taskKey)
        isTerminated = true
        truncateGifski()
    }

    private fun truncateGifski() {
        InstanceKeeper.logger = null
        InstanceKeeper.progressCallback = null
        MLog.info(TAG, "truncate:${InstanceKeeper.progressCallback}")
    }

    private fun calculateAdjustSize(maxWidth: Int, maxHeight: Int, frameWidth: Int, frameHeight: Int): Pair<Int, Int> {
        var adjustWidth = Math.min(frameWidth, maxWidth)
        var adjustHeight = Math.min(frameHeight, maxHeight)
        MLog.info("VideoInfo", "max:${maxWidth}x$maxHeight frame:${frameWidth}x$frameHeight")
        val ratio = frameHeight.toFloat() / frameWidth
        if (frameHeight > frameWidth) {
            adjustWidth = (adjustHeight / ratio).toInt()
        } else {
            adjustHeight = (ratio * adjustWidth).toInt()
        }
        MLog.info("VideoInfo", "adjusted:${adjustWidth}x$adjustHeight")
        return Pair(adjustWidth, adjustHeight)
    }

    companion object {
        private const val TAG = "MainPage"
    }
}