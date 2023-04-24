package com.example.android.activityscenetransitionbasic

import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DrawActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var subImageView: ImageView
    private lateinit var display: DisplayMetrics
    private val deviceWidth: Int by lazy { display.widthPixels }
    private val deviceHeight: Int by lazy { display.heightPixels }

    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap

    private lateinit var topLPoint: PointF
    private lateinit var topRPoint: PointF
    private lateinit var bottomLPoint: PointF
    private lateinit var bottomRPoint: PointF

    private var radioButton = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draw_activity)

        // 앱 종료
        findViewById<Button>(R.id.btn_home).setOnClickListener {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }

        initCanvas()

        imageView = findViewById(R.id.iv_guide)
        subImageView = findViewById(R.id.iv_move)

        findViewById<RadioGroup>(R.id.rg_lines).setOnCheckedChangeListener { _, id ->
            radioButton = when (id) {
                R.id.rb_bl -> 1
                R.id.rb_br -> 2
                R.id.rb_tl -> 3
                R.id.rb_tr -> 4
                R.id.rb_reset -> 5
                else -> 0
            }
        }
        myDrawLine(null, null)
    }

    private fun initCanvas() {
        display = this.applicationContext?.resources?.displayMetrics!!

        bitmap =
            Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)

        topLPoint = PointF((canvas.width / 5 * 2).toFloat(), (canvas.height / 4).toFloat())
        topRPoint = PointF((canvas.width / 5 * 3).toFloat(), (canvas.height / 4).toFloat())
        bottomLPoint = PointF((canvas.width / 5).toFloat(), (canvas.height / 4 * 3).toFloat())
        bottomRPoint = PointF((canvas.width / 5 * 4).toFloat(), (canvas.height / 4 * 3).toFloat())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = false
        if (event != null) {
            val x = event.x
            val y = event.y

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    result = false
                }
                MotionEvent.ACTION_UP -> {
                    result = false
                }
                MotionEvent.ACTION_MOVE -> {
                    myDrawLine(x, y)
                    result = true
                }
            }


        }
        return result
    }

    private fun drawLines(x: Float?, y: Float?) {
        // display 크기 구하기
        val display = this.applicationContext?.resources?.displayMetrics
        val deviceWidth = display?.widthPixels
        val deviceHeight = display?.heightPixels

        val bitmap =
            Bitmap.createBitmap(deviceWidth!!, deviceHeight!!, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        val bottomLeft = PointF((canvas.width / 5).toFloat(), (canvas.height / 4 * 3).toFloat())
        val topLeft = PointF()
        val topRight = PointF()
        if (x != null && y != null) {
            topLeft.x = x - 250
            topLeft.y = y
            topRight.x = x + 250
            topRight.y = y
        } else {
            topLeft.x = (canvas.width / 5 * 2).toFloat()
            topLeft.y = (canvas.height / 4).toFloat()
            topRight.x = (canvas.width / 5 * 3).toFloat()
            topRight.y = (canvas.height / 4).toFloat()
        }
        val bottomRight =
            PointF((canvas.width / 5 * 4).toFloat(), (canvas.height / 4 * 3).toFloat())

        // blueLine
        canvas.drawLine(bottomLeft.x, bottomLeft.y, topLeft.x, topLeft.y, paint)
        canvas.drawLine(topLeft.x, topLeft.y, topRight.x, topRight.y, paint)
        canvas.drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y, paint)

        // yellowLine
        canvas.drawLine(
            bottomLeft.x,
            bottomLeft.y,
            topLeft.x - (topLeft.x - bottomLeft.x) / 3,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3,
            paint.apply { color = Color.YELLOW })
        canvas.drawLine(
            topLeft.x - (topLeft.x - bottomLeft.x) / 3,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3,
            topRight.x + (bottomRight.x - topRight.x) / 3,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3,
            paint
        )
        canvas.drawLine(
            topRight.x + (bottomRight.x - topRight.x) / 3,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3,
            bottomRight.x,
            bottomRight.y,
            paint
        )

        // redLine
        canvas.drawLine(
            bottomLeft.x,
            bottomLeft.y,
            topLeft.x - (topLeft.x - bottomLeft.x) / 3 * 2,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3 * 2,
            paint.apply { color = Color.RED })
        canvas.drawLine(
            topLeft.x - (topLeft.x - bottomLeft.x) / 3 * 2,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3 * 2,
            topRight.x + (bottomRight.x - topRight.x) / 3 * 2,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3 * 2,
            paint
        )
        canvas.drawLine(
            topRight.x + (bottomRight.x - topRight.x) / 3 * 2,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3 * 2,
            bottomRight.x,
            bottomRight.y,
            paint
        )

        imageView.setImageBitmap(bitmap)
    }

    private fun myDrawLine(x: Float?, y: Float?) {
        bitmap = Bitmap.createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        if (x != null && y != null) {
            when (radioButton) {
                0 -> {
                    topLPoint.x = x - 250
                    topLPoint.y = y
                    topRPoint.x = x + 250
                    topRPoint.y = y
                }
                // bottom | left
                1 -> {
                    bottomLPoint.x = x
                    bottomLPoint.y = y
                }
                // bottom | right
                2 -> {
                    bottomRPoint.x = x
                    bottomRPoint.y = y
                }
                // top | left
                3 -> {
                    topLPoint.x = x
                    topLPoint.y = y
                }
                // top | right
                4 -> {
                    topRPoint.x = x
                    topRPoint.y = y
                }
                5 -> {
                    topLPoint =
                        PointF((canvas.width / 5 * 2).toFloat(), (canvas.height / 4).toFloat())
                    topRPoint =
                        PointF((canvas.width / 5 * 3).toFloat(), (canvas.height / 4).toFloat())
                    bottomLPoint =
                        PointF((canvas.width / 5).toFloat(), (canvas.height / 4 * 3).toFloat())
                    bottomRPoint =
                        PointF((canvas.width / 5 * 4).toFloat(), (canvas.height / 4 * 3).toFloat())
                }
            }
        }

        // blueLine
        canvas.drawLine(bottomLPoint.x, bottomLPoint.y, topLPoint.x, topLPoint.y, paint)
        canvas.drawLine(topLPoint.x, topLPoint.y, topRPoint.x, topRPoint.y, paint)
        canvas.drawLine(topRPoint.x, topRPoint.y, bottomRPoint.x, bottomRPoint.y, paint)

        // yellowLine
        canvas.drawLine(
            bottomLPoint.x,
            bottomLPoint.y,
            topLPoint.x - (topLPoint.x - bottomLPoint.x) / 3,
            topLPoint.y + (bottomLPoint.y - topLPoint.y) / 3,
            paint.apply { color = Color.YELLOW })
        canvas.drawLine(
            topLPoint.x - (topLPoint.x - bottomLPoint.x) / 3,
            topLPoint.y + (bottomLPoint.y - topLPoint.y) / 3,
            topRPoint.x + (bottomRPoint.x - topRPoint.x) / 3,
            topRPoint.y + (bottomRPoint.y - topRPoint.y) / 3,
            paint
        )
        canvas.drawLine(
            topRPoint.x + (bottomRPoint.x - topRPoint.x) / 3,
            topRPoint.y + (bottomRPoint.y - topRPoint.y) / 3,
            bottomRPoint.x,
            bottomRPoint.y,
            paint
        )

        // redLine
        canvas.drawLine(
            bottomLPoint.x,
            bottomLPoint.y,
            topLPoint.x - (topLPoint.x - bottomLPoint.x) / 3 * 2,
            topLPoint.y + (bottomLPoint.y - topLPoint.y) / 3 * 2,
            paint.apply { color = Color.RED })
        canvas.drawLine(
            topLPoint.x - (topLPoint.x - bottomLPoint.x) / 3 * 2,
            topLPoint.y + (bottomLPoint.y - topLPoint.y) / 3 * 2,
            topRPoint.x + (bottomRPoint.x - topRPoint.x) / 3 * 2,
            topRPoint.y + (bottomRPoint.y - topRPoint.y) / 3 * 2,
            paint
        )
        canvas.drawLine(
            topRPoint.x + (bottomRPoint.x - topRPoint.x) / 3 * 2,
            topRPoint.y + (bottomRPoint.y - topRPoint.y) / 3 * 2,
            bottomRPoint.x,
            bottomRPoint.y,
            paint
        )

        val textView = findViewById<TextView>(R.id.tv_lineInfo)
        textView.text = getString(
            R.string.point,
            topLPoint.x.toString(),
            topLPoint.y.toString(),
            topRPoint.x.toString(),
            topRPoint.y.toString(),
            bottomLPoint.x.toString(),
            bottomLPoint.y.toString(),
            bottomRPoint.x.toString(),
            bottomRPoint.y.toString()
        )
        imageView.setImageBitmap(bitmap)
    }

}