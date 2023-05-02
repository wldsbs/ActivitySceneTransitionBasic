package com.example.android.activityscenetransitionbasic

import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.android.activityscenetransitionbasic.databinding.AckermanActivityBinding
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.exitProcess

class AckermanActivity : AppCompatActivity() {
    private lateinit var binding: AckermanActivityBinding

    private lateinit var display: DisplayMetrics
    private val deviceWidth: Int by lazy { display.widthPixels }
    private val deviceHeight: Int by lazy { display.heightPixels }

    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap

    private lateinit var carCenterPoint: PointF
    private lateinit var carLeftPoint: PointF
    private lateinit var carRightPoint: PointF
    private lateinit var circleCenterPoint: PointF

    private lateinit var rightEndPoint: PointF
    private lateinit var leftEndPoint: PointF

    private var radius: Float = 0f         // 두 앞바퀴 중심에서의 회전 반경
    private var rate = 5f                  // 비율 변환에 사용(실제 자동차 -> AVN)

    private var carTrack = 1614f            // 윤거
    private var wheelbase = 2845f           // 축거(축간 거리)
    private var kingPinDiff = 160f          // 타이어 중심과 킹핀 사이 거리
    private var carHalfLength = carTrack / 2 / rate
    private var angle = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AckermanActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bind()
        initCanvas()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = false
        if (event != null) {
            val x = event.x
//            val y = event.y

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> { result = false }
                MotionEvent.ACTION_UP -> { result = false }
                MotionEvent.ACTION_MOVE -> {
                    if(x < deviceWidth/2){
                        carLeftPoint.x = x
                        carRightPoint.x = deviceWidth-x
                    }
                    else{
                        carRightPoint.x = x
                        carLeftPoint.x = deviceWidth-x
                    }

                    if(binding.seekbar.progress > 0) drawRightRotation(binding.seekbar.progress, radius)
                    else drawLeftRotation(binding.seekbar.progress, radius)
                    result = true
                }
            }
        }
        return result
    }

    private fun bind() {
        binding.btnHome.setOnClickListener {
            finishAffinity()
            System.runFinalization()
            exitProcess(0)
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                if (progress != 0) {
                    angle = (abs(progress) * 2 * Math.PI).toFloat() / 360.0f
                    radius = wheelbase / sin(angle) * cos(angle) - carTrack / 2 + kingPinDiff
                    radius /= rate
                }

                if (progress > 0) {
                    // 오른쪽 회전
                    circleCenterPoint = PointF(carCenterPoint.x + radius, carCenterPoint.y)
                    drawRightRotation(progress, radius)

                } else if (progress < 0) {
                    // 왼쪽 회전
                    circleCenterPoint = PointF(carCenterPoint.x - radius, carCenterPoint.y)
                    drawLeftRotation(progress, radius)
                }
//                else {
//                    drawLine()
//                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

//    private fun drawLine() {
//        bitmap = Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
//        canvas = Canvas(bitmap)
//        val paint = Paint().apply {
//            color = Color.BLUE
//            style = Paint.Style.STROKE
//            strokeWidth = 6f
//            isAntiAlias = true
//        }
//
//        canvas.save()
//        canvas.setMatrix(setBitmap())
//
//        canvas.drawLine(leftEndPoint.x, leftEndPoint.y, rightEndPoint.x, rightEndPoint.y, paint)
//        canvas.drawLine(carLeftPoint.x, carLeftPoint.y, leftEndPoint.x, leftEndPoint.y, paint)
//        canvas.drawLine(carRightPoint.x, carRightPoint.y, rightEndPoint.x, rightEndPoint.y, paint)
//
//        canvas.drawLine(
//            leftEndPoint.x,
//            leftEndPoint.y + leftEndPoint.y / 3,
//            rightEndPoint.x,
//            rightEndPoint.y + rightEndPoint.y / 3,
//            paint.apply { color = Color.RED })
//
//        canvas.drawLine(
//            leftEndPoint.x,
//            leftEndPoint.y + 2 * leftEndPoint.y / 3,
//            rightEndPoint.x,
//            rightEndPoint.y + 2 * rightEndPoint.y / 3,
//            paint.apply { color = Color.YELLOW })
//
//        binding.ivGuide.setImageBitmap(bitmap)
//    }
    private fun drawRightRotation(progress: Int, radius: Float) {
        bitmap = Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        binding.tvR.setTextColor(Color.WHITE)
        binding.tvR.text = "${2 * Math.PI * radius * (angle / 360.0f)}"

        canvas.save()
        canvas.setMatrix(setBitmap())

        val leftRadius = radius + carHalfLength
        val rightRadius = radius - carHalfLength

        rightEndPoint = PointF(
            carRightPoint.x + rightRadius * (1 - cos(angle)),
            carRightPoint.y - (rightRadius * sin(angle))
        )

        leftEndPoint = PointF(
            carLeftPoint.x + leftRadius * (1 - cos(angle)),
            carLeftPoint.y - (leftRadius * sin(angle))
        )

        // top line : Blue
        canvas.drawLine(leftEndPoint.x, leftEndPoint.y, rightEndPoint.x, rightEndPoint.y, paint)

        // left line : Blue
        canvas.drawArc(
            RectF(
                carLeftPoint.x,
                carLeftPoint.y - leftRadius,
                carLeftPoint.x + leftRadius * 2,
                carLeftPoint.y + leftRadius
            ),
            180f,
            progress.toFloat(),
            false,
            paint
        )

        // right line : Blue
        canvas.drawArc(
            RectF(
                carRightPoint.x,
                carRightPoint.y - rightRadius,
                carRightPoint.x + rightRadius * 2,
                carRightPoint.y + rightRadius
            ),
            180f,
            progress.toFloat(),
            false,
            paint
        )

        // top line : Yellow (1/2)
        canvas.drawLine(
            carLeftPoint.x + leftRadius * (1 - cos(angle / 2)),
            carLeftPoint.y - (leftRadius * sin(angle / 2)),
            carRightPoint.x + rightRadius * (1 - cos(angle / 2)),
            carRightPoint.y - (rightRadius * sin(angle / 2)),
            paint.apply { color = Color.YELLOW }
        )

        // left line : Yellow
        canvas.drawArc(
            RectF(
                carLeftPoint.x,
                carLeftPoint.y - leftRadius,
                carLeftPoint.x + leftRadius * 2,
                carLeftPoint.y + leftRadius
            ),
            180f,
            progress.toFloat() / 2,
            false,
            paint
        )

        // right line : Yellow
        canvas.drawArc(
            RectF(
                carRightPoint.x,
                carRightPoint.y - rightRadius,
                carRightPoint.x + rightRadius * 2,
                carRightPoint.y + rightRadius
            ),
            180f,
            progress.toFloat() / 2,
            false,
            paint
        )

        // top  line : Blue (1/6)
        canvas.drawLine(
            carLeftPoint.x + leftRadius * (1 - cos(angle / 6)),
            carLeftPoint.y - (leftRadius * sin(angle / 6)),
            carRightPoint.x + rightRadius * (1 - cos(angle / 6)),
            carRightPoint.y - (rightRadius * sin(angle / 6)),
            paint.apply { color = Color.RED }
        )

        // left line : Blue
        canvas.drawArc(
            RectF(
                carLeftPoint.x,
                carLeftPoint.y - leftRadius,
                carLeftPoint.x + leftRadius * 2,
                carLeftPoint.y + leftRadius
            ),
            180f,
            progress.toFloat() / 5,
            false,
            paint
        )

        // right line : Blue
        canvas.drawArc(
            RectF(
                carRightPoint.x,
                carRightPoint.y - rightRadius,
                carRightPoint.x + rightRadius * 2,
                carRightPoint.y + rightRadius
            ),
            180f,
            progress.toFloat() / 5,
            false,
            paint
        )

        binding.ivGuide.setImageBitmap(bitmap)
    }

    private fun drawLeftRotation(progress: Int, radius: Float) {
        bitmap = Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        binding.tvR.setTextColor(Color.WHITE)
        binding.tvR.text = "${2 * Math.PI * radius * (angle / 360.0f)}"

        canvas.save()
        canvas.setMatrix(setBitmap())

        val leftRadius = radius - carHalfLength
        val rightRadius = radius + carHalfLength

        rightEndPoint = PointF(
            carRightPoint.x - rightRadius * (1 - cos(angle)),
            carRightPoint.y - (rightRadius * sin(angle))
        )
        leftEndPoint = PointF(
            carLeftPoint.x - leftRadius * (1 - cos(angle)),
            carLeftPoint.y - (leftRadius * sin(angle))
        )

        canvas.drawLine(leftEndPoint.x, leftEndPoint.y, rightEndPoint.x, rightEndPoint.y, paint)

        //left line
        canvas.drawArc(
            RectF(
                carLeftPoint.x - leftRadius * 2,
                carLeftPoint.y - leftRadius,
                carLeftPoint.x,
                carLeftPoint.y + leftRadius
            ),
            0f,
            progress.toFloat(),
            false,
            paint
        )

        canvas.drawArc(
            RectF(
                carRightPoint.x - rightRadius * 2,
                carRightPoint.y - rightRadius,
                carRightPoint.x,
                carRightPoint.y + rightRadius
            ),
            0f,
            progress.toFloat(),
            false,
            paint
        )

        canvas.drawLine(
            carLeftPoint.x - leftRadius * (1 - cos(angle / 2)),
            carLeftPoint.y - (leftRadius * sin(angle / 2)),
            carRightPoint.x - rightRadius * (1 - cos(angle / 2)),
            carRightPoint.y - (rightRadius * sin(angle / 2)),
            paint.apply { color = Color.YELLOW }
        )

        canvas.drawArc(
            RectF(
                carLeftPoint.x - leftRadius * 2,
                carLeftPoint.y - leftRadius,
                carLeftPoint.x,
                carLeftPoint.y + leftRadius
            ),
            0f,
            progress.toFloat() / 2,
            false,
            paint
        )

        canvas.drawArc(
            RectF(
                carRightPoint.x - rightRadius * 2,
                carRightPoint.y - rightRadius,
                carRightPoint.x,
                carRightPoint.y + rightRadius
            ),
            0f,
            progress.toFloat() / 2,
            false,
            paint
        )

        canvas.drawLine(
            carLeftPoint.x - leftRadius * (1 - cos(angle / 6)),
            carLeftPoint.y - (leftRadius * sin(angle / 6)),
            carRightPoint.x - rightRadius * (1 - cos(angle / 6)),
            carRightPoint.y - (rightRadius * sin(angle / 6)),
            paint.apply { color = Color.RED }
        )

        canvas.drawArc(
            RectF(
                carLeftPoint.x - leftRadius * 2,
                carLeftPoint.y - leftRadius,
                carLeftPoint.x,
                carLeftPoint.y + leftRadius
            ),
            0f,
            progress.toFloat() / 5,
            false,
            paint
        )
        //right line
        canvas.drawArc(
            RectF(
                carRightPoint.x - rightRadius * 2,
                carRightPoint.y - rightRadius,
                carRightPoint.x,
                carRightPoint.y + rightRadius
            ),
            0f,
            progress.toFloat() / 5,
            false,
            paint
        )

        binding.ivGuide.setImageBitmap(bitmap)
    }

    private fun initCanvas() {
        display = this.applicationContext?.resources?.displayMetrics!!

        bitmap =
            Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)

        carCenterPoint = PointF(deviceWidth.toFloat() / 2, deviceHeight.toFloat())
        carLeftPoint = PointF(carCenterPoint.x - carHalfLength, carCenterPoint.y)
        carRightPoint = PointF(carCenterPoint.x + carHalfLength, carCenterPoint.y)
    }

    private fun setBitmap(): Matrix {
        val src = floatArrayOf(
            0f, 0f,                                      // top left
            deviceWidth.toFloat(), 0f,                   // top right
            carRightPoint.x, deviceHeight.toFloat(),     // bottom right
            carLeftPoint.x, deviceHeight.toFloat()       // bottom left
        )

        val dst = floatArrayOf(
            100f, deviceHeight.toFloat() * 0.6f,                           // top left
            deviceWidth.toFloat()-100f, deviceHeight.toFloat() * 0.6f,     // top right
            deviceWidth.toFloat()-100f, deviceHeight.toFloat(),            // bottom right
            100f, deviceHeight.toFloat(),                                  // bottom left
        )

        val m = Matrix().apply {
            setPolyToPoly(src, 0, dst, 0, 4)
        }

        return m
    }
}