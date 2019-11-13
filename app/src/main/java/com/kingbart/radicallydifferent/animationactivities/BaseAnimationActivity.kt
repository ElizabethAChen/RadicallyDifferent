package com.kingbart.radicallydifferent.animationactivities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kingbart.radicallydifferent.R

abstract class BaseAnimationActivity : AppCompatActivity() {
    protected lateinit var car: View
    protected lateinit var frameLayout: View
    protected var screenWidth = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animation_base)

        car = findViewById(R.id.car)
        frameLayout = findViewById(R.id.frame)
        frameLayout.setOnClickListener{ onStartAnimation() }
    }

    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()
    }

    protected abstract fun onStartAnimation()

    companion object{
        val DEFAULT_ANIMATION_DURATION = 2500L
    }
}