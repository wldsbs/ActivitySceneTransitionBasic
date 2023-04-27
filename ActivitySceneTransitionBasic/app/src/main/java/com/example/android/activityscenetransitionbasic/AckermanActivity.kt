package com.example.android.activityscenetransitionbasic

import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.android.activityscenetransitionbasic.databinding.AckermanActivityBinding
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

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

    private var radius: Float = 0f  // 자동차의 두 앞바퀴 중심에서의 회전반경
    private var rate = 20          // 비율 변환에 사용(실제 자동차 -> AVN)
    private var carHalfLength = 50

//    private var carTrack = 1614     // 윤거
//    private var wheelbase = 2845    // 축거(축간거리)
//    private var kingPinDiff = 160   // 타이어 중심과 킹핀 사이 거리
//    private var carHalfLength = carTrack/2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AckermanActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bind()
        initCanvas()
    }

    private fun bind() {
        binding.btnHome.setOnClickListener {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                binding.tvAngle.text = progress.toString()
                if (progress != 0) {
                    val angle = (abs(progress) * 2 * Math.PI / 360.0).toFloat()
                    radius = 2845 / sin(angle) * cos(angle) - 647
                    radius /= rate
                    binding.tvR.text = "$radius"
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
                // todo: progress == 0일 경우

                binding.tvCenterPoint.text = "(${circleCenterPoint.x}, ${circleCenterPoint.y})"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun initCanvas() {
        display = this.applicationContext?.resources?.displayMetrics!!

        bitmap =
            Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)

//        carCenterPoint = PointF((canvas.width / 2).toFloat(), (canvas.height - 100).toFloat())
        carCenterPoint = PointF((deviceWidth / 2).toFloat(), deviceHeight.toFloat())
        carLeftPoint = PointF(carCenterPoint.x - 100, carCenterPoint.y)
        carRightPoint = PointF(carCenterPoint.x + 100, carCenterPoint.y)
    }
    private fun setBitmap(): Matrix {
        val src = floatArrayOf(
            0f, 0f,         // top left
            deviceWidth.toFloat(), 0f,        // top right
            deviceWidth.toFloat() * 0.6f, deviceHeight.toFloat(),     // bottom right
            deviceWidth.toFloat() * 0.4f, deviceHeight.toFloat()       // bottom left
        )
        val nsrc = floatArrayOf(
            0f, 0f,         // top left
            deviceWidth.toFloat(), 0f,        // top right
            carRightPoint.x, deviceHeight.toFloat(),     // bottom right
            carLeftPoint.x, deviceHeight.toFloat()       // bottom left
        )

        // 모양은 예쁜데 특정 구간 안그려짐
        val dst = floatArrayOf(
            deviceWidth.toFloat() * 0.3f, 0f,                       // top left
            deviceWidth.toFloat() * 0.7f, 0f,                       // top right
            deviceWidth.toFloat(), deviceHeight.toFloat(),          // bottom right
            0.0f, deviceHeight.toFloat(),                           // bottom left
        )

        // 다 잘 그려지고 애커만 적용되는데 너무 쪼끄매,,
        val ndst = floatArrayOf(
            0f, 50f,                       // top left
            deviceWidth.toFloat()-50f, 50f,                       // top right
            deviceWidth.toFloat()-50f, deviceHeight.toFloat(),          // bottom right
            0f, deviceHeight.toFloat(),                           // bottom left
        )
        val m = Matrix().apply {
            setPolyToPoly(nsrc, 0, ndst, 0, 4)
        }
        return m
    }
    private fun drawRightRotation(progress: Int, radius: Float) {
        bitmap = Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        // center
//        canvas.drawPoint(carCenterPoint.x, carCenterPoint.y, paint)
//        if (radius != null && progress != null) {
//            canvas.drawArc(
//                RectF(
//                    carCenterPoint.x,
//                    carCenterPoint.y - radius,
//                    carCenterPoint.x + radius * 2,
//                    carCenterPoint.y + radius
//                ),
//                180f,
//                progress.toFloat(),
//                false,
//                paint
//            )
//        }

        canvas.save()
        canvas.setMatrix(setBitmap())

        //left line
        val leftRadius = radius + carHalfLength
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

        //right line
        val rightRadius = radius - carHalfLength
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

        //left line
        val leftRadius = radius - carHalfLength
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

        //right line
        val rightRadius = radius + carHalfLength
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

        binding.ivGuide.setImageBitmap(bitmap)
    }
}