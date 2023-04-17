package com.example.android.activityscenetransitionbasic

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DrawActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draw_activity)

        imageView = findViewById(R.id.iv)
        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val canvas1 = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

//        val topLeft = Pair((canvas1.width / 3).toFloat(), (canvas1.height / 3 * 1).toFloat())
//        val topRight = Pair((canvas1.width / 3 * 2).toFloat(), (canvas1.height / 3 * 1).toFloat())
//        val bottomLeft = Pair(100f, (canvas1.height / 3 * 2).toFloat())
//        val bottomRight = Pair((canvas1.width - 100).toFloat(), (canvas1.height / 3 * 2).toFloat())

        // draw (/)
        canvas1.drawLine(
            100f,
            (canvas1.height / 3 * 2).toFloat(),
            (canvas1.width / 3).toFloat(),
            (canvas1.height / 3 * 1).toFloat(),
            paint
        )
        // draw (-)
        canvas1.drawLine(
            (canvas1.width / 3).toFloat(),
            (canvas1.height / 3 * 1).toFloat(),
            (canvas1.width / 3 * 2).toFloat(),
            (canvas1.height / 3 * 1).toFloat(),
            paint
        )
        // draw (\)
        canvas1.drawLine(
            (canvas1.width / 3 * 2).toFloat(),
            (canvas1.height / 3 * 1).toFloat(),
            (canvas1.width - 100).toFloat(),
            (canvas1.height / 3 * 2).toFloat(),
            paint
        )
//        val path = Path()
//        path.move
//        canvas.drawPath()
        imageView.setImageBitmap(bitmap)
    }

}