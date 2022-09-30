package a.tlib.utils.abs

import android.animation.Animator
import android.view.animation.Animation

/**
 * @author 田桂森 2020/3/21
 */
abstract class AnimationLis : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation) {

    }

    override fun onAnimationEnd(animation: Animation) {

    }

    override fun onAnimationStart(animation: Animation) {

    }
}

abstract class AnimatorLis : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator) {

    }

    override fun onAnimationEnd(animation: Animator) {

    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationStart(animation: Animator) {

    }

}