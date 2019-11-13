package com.kingbart.radicallydifferent.animationactivities

import android.animation.ObjectAnimator

class TryAgain : BaseAnimationActivity(){
    override fun onStartAnimation() {
        val objectAnimator = ObjectAnimator.ofFloat(car, "translationX", screenWidth/2)
        objectAnimator.duration = DEFAULT_ANIMATION_DURATION
        objectAnimator.start()
    }
}