package a.tlib.utils.abs

import android.animation.Animator
import android.view.animation.Animation
import com.bumptech.glide.request.transition.ViewPropertyTransition

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

abstract class AnimatorLis : ViewPropertyTransition.Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: ViewPropertyTransition.Animator) {

    }

    override fun onAnimationEnd(animation: ViewPropertyTransition.Animator) {

    }

    override fun onAnimationCancel(animation: ViewPropertyTransition.Animator) {

    }

    override fun onAnimationStart(animation: ViewPropertyTransition.Animator) {

    }

}