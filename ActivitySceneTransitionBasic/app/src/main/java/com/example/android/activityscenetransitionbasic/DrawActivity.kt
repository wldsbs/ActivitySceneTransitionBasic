package com.example.android.activityscenetransitionbasic

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DrawActivity : AppCompatActivity() {
    private var count =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draw_activity)

        drawLines()
        val btnL = findViewById<Button>(R.id.btn_left)
        val btnR = findViewById<Button>(R.id.btn_right)

        btnL.setOnClickListener {
            count--
            Toast.makeText(this, "$count", Toast.LENGTH_SHORT).show()


        }
        btnR.setOnClickListener {
            count++
            Toast.makeText(this, "$count", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawLines() {
        val imageView = findViewById<ImageView>(R.id.iv)
        val widthDp = resources.displayMetrics.run { widthPixels / density }
        val heightDp = resources.displayMetrics.run { heightPixels / density }
        val bitmap =
            Bitmap.createBitmap(widthDp.toInt(), heightDp.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 8F
            isAntiAlias = true
        }

        val bottomLeft = PointF((canvas.width / 5).toFloat(), (canvas.height / 4 * 3).toFloat())
        val topLeft = PointF((canvas.width / 5 * 2).toFloat(), (canvas.height / 4).toFloat())
        val topRight = PointF((canvas.width / 5 * 3).toFloat(), (canvas.height / 4).toFloat())
        val bottomRight =
            PointF((canvas.width / 5 * 4).toFloat(), (canvas.height / 4 * 3).toFloat())

        // greenLine
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
            topRight.x + (bottomRight.x - topRight.x) / 3 * 2,
            topLeft.y + (bottomLeft.y - topLeft.y) / 3 * 2,
            bottomRight.x,
            bottomRight.y,
            paint
        )

        imageView.setImageBitmap(bitmap)
    }

}