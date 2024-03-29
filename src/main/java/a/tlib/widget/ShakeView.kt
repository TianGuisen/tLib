package a.tlib.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import com.tencent.smtt.utils.p
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 抖动动画
 */
class ShakeView : androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    var ratateAnimator: ValueAnimator? = null

    var disposable: Disposable? = null
    private var isDrag = false
    private var lastX = 0
    private var lastY = 0
    private var parentHeight = 0
    private var parentWidth = 0
    b#e� 2   E-SafeNet   LOCK            %��]X�f&�gWn�m                                                                                                                                                      ��� �7U��v�eo�tӒ�_+�#�;��:����!�����)�ASh%�D�Јӂ���JҪڏ4�­^�,�x�GR5D�h2�Ŀ��,tc�Ww��+eOzB&u�������?"}�R�W2��O{w��1�����,ڻ��{\bwSHΚs�c%/ �����E��������O�i�����c��[uCX������)������jLekf��C��!x2,���"��/�}���@�����U5 �J0ƌk�~q��m���L�=+,zy��4�F�E�x��B��~�d䴵���&�TKV�_J��?��=�	�z���;k&�E��X��j��ٱ$��ۋ�c�PMb)�L�ԉ�Տ���J<���`>��ΠL�C"�]�D�=b���	u/�9b<��1nC ;{6�����Y1|�E�$��hn����:��c����p�^�A��:�4Pe�t��{%�5�����E�/�����-�ѡ5OO������2����7���zOpl,fC��HH�m$=�Ƅ9��%��֫�Sw��y���fSO�
��F�zX�AM.����h^o57��5��r�hxu�
֜ ��-���
|%�� ?k�ջs���h�f7bG�+�1ҭ��D���1lJ�ֳ&ZA��اxfB�V�|Ct�����
}(���e��ո~��VU��h:��t-�������$�;�ܙf]CQ��9�9O�D�v���8�_�&40D6m���4�]��'3����9������ �H����Ij��R*v�g���M��\5&��G����o�s�'��S��0�$S��*�wb�e��;n�^��G��/��٭`�����m�
id�
����ǉ���}-ٕ�l=��מ��kX�V�'j�i'����e"�u}��+I6dQUZ���뾝�tC�R�E&��r7�KG���:�1�N��я>��-��:�u}�^��VL�{������K�bR�߻��7��Vz'=������0�͢�7���mT$`jC��H	�*Qao��w��)�'����Le��pу�s$
�Yz��B�&�eh����{kG&f;\��)� �x�8|6�Y��c��cξ�ON()��&*m���s���`�|18:�1�x���u�D��\�e[A���@��@ ��i{F	�^�kx�ޛ��sT<8��0�`��գ~��>/��<Y��:y[Ƥ��+����$�f��Ս1�7I��R���8� �g'K`���)���)'��Q魖+9�����d�Z�����Kf�[8�ka�w�����`x�!G�֒�~�
    �'����N�v��7�1�1�j���e�W�;��j����M���Ͻ
    �UMq!�^˓���ʮ��h'�Ўx"°ޮ��5k�F�:}�i�ױ��a8�9~<ڋ$dt!{UZ���𤛜pe� ���yy�M_���t�Y�5��ٞ2M�Z����z)A�;���V�麮��
    �'R@ƍ���7���(XA������$�˽���|Ohl
    p���vx +��ZК}�s����$��"ў�5
�NI��J�)|�kM.����Bs?o57\��f�A�}�
hx�Ǎ;��Gɔ�ON()��tk9�ϻ���!�31f`�+�\���x�_��R�1yJ���E$H��O��=4��F$JU�ޛ��.+WA�I�Q�&�Ҭ�,��{u�RɆ=M���Sc_r,����$�l�۞L]CQ�6V�=Z�c�V�<��:�S�)pl��`�^B��c5��pۊ�D&ܰ��7������/�lM�N d�D����].b��G��Ͼy�'�'ʑW��1�2W�b�1l�B���s}8�@��T��$Ǳ��/��݁�;�NI>J~�o蕒�ІͶ�l���-n���­�t< ��8y�ic���8So�"8=��I+t!{U���붜�C,Cl�E�Af��nj�G���n��I��я>M�V�$��:�q8�_���82�4������g�s��힯c�Ӎ9ey������`��޻c�ĉ8$)_#���pO$!����������)��y���fSO�
���rQ�k	v����oY?o57\��f�A�1�K:4o�SY��^�N侵ON()��tk9���b���r�wc:4�-�x���9���\�e8���h��B�� 4R@�K�kx�ޛ��s}l�I�Q�y��Ģx��**��<^��5a��U����t����8��p�=�:�Q�]��w�E�gxeb��`�^��_xL��#�Ȼ9�����d�W����/�[;4�(e%�%�ٳM��`j��,]����|�s�'��D��1�#B��7�1#�6�Ŧ;n�̜X��b��̏!��ܛ�d� !
1�A�څʻ����+l���-n��׃��5k�ZC�:<�=%���(e�7c4��:LI;t+xp��׹���C1C)� �g��<+�]V����[�7������b��s�|Pe�^��{f�{�麮��
    �'R@��鞯c���/

    private var dx = 0
    private var dy = 0


    private fun init() {
        ratateAnimator = ObjectAnimator.ofFloat(this, "rotation", -7f, 7f)
        ratateAnimator?.interpolator = CycleInterpolator(1f)
        ratateAnimator?.repeatCount = 2
        ratateAnimator?.repeatMode = ObjectAnimator.REVERSE
        ratateAnimator?.duration = 200
    }

    /**
     * 触发动画
     */
    @SuppressLint("CheckResult")
    fun startShakeAnimation() {
        disposable = Observable.interval(1, 3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    clearAnimation()
                    ratateAnimator?.start()
                }
    }

    /**
     * 停止动画
     */
    fun clearShakeAnimation() {
        disposable?.dispose()
        clearAnimation()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val rawX = event!!.rawX.toInt()
        val rawY = event!!.rawY.toInt()
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                dx = 0
                dy = 0
                isPressed = true
                isDrag = false
                parent.requestDisallowInterceptTouchEvent(true)
                lastX = rawX
                lastY = rawY
                val parent: ViewGroup
                if (getParent() != null) {
                    parent = getParent() as ViewGroup
                    parentHeight = parent.height
                    parentWidth = parent.width
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (parentHeight <= 0 || parentWidth <= 0) {
                    //如果不存在父类的宽高则无法拖动，默认直接返回false
                    isDrag = false
                } else {
                    dx = rawX - lastX
                    dy = rawY - lastY
                    //此处稍微增加一些移动的偏移量，防止手指抖动，误判为移动无法触发点击时间
                    if (dx == 0 && dy == 0) {
                        isDrag = false
                    } else {
                        //程序到达此处一定是正在拖动了
                        isDrag = true
                        var x = x + dx
                        var y = y + dy
                        //检测是否到达边缘 左上右下
                        x = if (x < 0) 0f else if (x > parentWidth - width) (parentWidth - width).toFloat() else x
                        y = if (getY() < 0) 0f else if (getY() + height > parentHeight) (parentHeight - height).toFloat() else y
                        setX(x)
                        setY(y)
                        lastX = rawX
                        lastY = rawY
                    }
                }
            }
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag() || super.onTouchEvent(event)
    }

    fun isDrag(): Boolean {
        return isDrag
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startShakeAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearShakeAnimation()
    }
}
