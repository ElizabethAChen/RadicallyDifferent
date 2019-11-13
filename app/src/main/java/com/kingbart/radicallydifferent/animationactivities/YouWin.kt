package com.kingbart.radicallydifferent.animationactivities

import android.animation.ObjectAnimator

class YouWin : BaseAnimationActivity(){
    override fun onStartAnimation() {
        val objectAnimator = ObjectAnimator.ofFloat(car, "translationX", screenWidth)
        objectAnimator.duration = DEFAULT_ANIMATION_DURATION
        objectAnimator.start()
    }
}