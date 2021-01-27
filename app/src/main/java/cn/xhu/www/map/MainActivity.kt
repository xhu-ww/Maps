package cn.xhu.www.map

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animDrawable = findViewById<AppCompatImageView>(R.id.image).drawable
        if (animDrawable is AnimatedVectorDrawable) {
            animDrawable.start()
        }
    }
}