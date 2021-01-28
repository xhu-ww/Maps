package cn.xhu.www.map

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animDrawable1 = findViewById<AppCompatImageView>(R.id.image1).drawable
        val animDrawable2 = findViewById<AppCompatImageView>(R.id.image2).drawable
        if (animDrawable1 is AnimatedVectorDrawable) {
            animDrawable1.start()
        }
        if (animDrawable2 is AnimatedVectorDrawable) {
            animDrawable2.start()
        }
    }
}