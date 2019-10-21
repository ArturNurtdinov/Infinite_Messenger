package com.infinitevoid.easymessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.infinitevoid.easymessenger.R
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import kotlin.math.abs

class FullScreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)


        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE.or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE).or(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            ).or(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            ).or(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            ).or(
                View.SYSTEM_UI_FLAG_FULLSCREEN
            )
        val url = intent.getStringExtra("IMAGE_KEY")

        Glide.with(this).load(url).into(myImage)

        myImage.setOnViewDragListener { _, dy ->
            if (myImage.scale <= myImage.minimumScale) {
                if (abs(dy) > 100) {
                    finish()
                }
            }
        }
    }
}
