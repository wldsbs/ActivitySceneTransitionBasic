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
    private var rate = 5          // 비율 변환에 사용(실제 자동차 -> AVN)
    private var carHalfLength = 200

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

        carCenterPoint = PointF((canvas.width / 2).toFloat(), (canvas.height - 100).toFloat())
        carLeftPoint = PointF(carCenterPoint.x - 300, carCenterPoint.y)
        carRightPoint = PointF(carCenterPoint.x + 300, carCenterPoint.y)
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