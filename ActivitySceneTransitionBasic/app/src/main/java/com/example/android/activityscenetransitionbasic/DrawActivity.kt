package com.example.android.activityscenetransitionbasic

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DrawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draw_activity)

        drawLines()
    }

    private fun drawLines() {
        val imageView = findViewById<ImageView>(R.id.iv)
        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        val bottomLeft = Pair(100f, (canvas.height / 3 * 2).toFloat())
        val topLeft = Pair((canvas.width / 3).toFloat(), (canvas.height / 3 * 1).toFloat())
        val topRight = Pair((canvas.width / 3 * 2).toFloat(), (canvas.height / 3 * 1).toFloat())
        val bottomRight = Pair((canvas.width - 100).toFloat(), (canvas.height / 3 * 2).toFloat())

        // draw (/)
        canvas.drawLine(bottomLeft.first, bottomLeft.second, topLeft.first, topLeft.second, paint)
        // draw (-)
        canvas.drawLine(topLeft.first, topLeft.second, topRight.first, topRight.second, paint)
        // draw (\)
        canvas.drawLine(topRight.first, topRight.second, bottomRight.first, bottomRight.second, paint)

        imageView.setImageBitmap(bitmap)
    }

}