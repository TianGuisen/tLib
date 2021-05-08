package a.tlib.utils.glide

import com.bumptech.glide.request.RequestOptions
import a.tlib.R
import a.tlib.utils.glide.GlideOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * glide配置
 */
/**
 * banner 普通的
 */
val bannerCommonOptions = RequestOptions()
        .fitCenter()
        .error(R.drawable.banner_null_img)
        .placeholder(R.drawable.banner_null_img)

val goodsCommonOptions = RequestOptions()
        .centerCrop()
        .error(R.drawable.goods_null_img)
        .placeholder(R.drawable.goods_null_img)
val shopOptions = RequestOptions()
        .centerCrop()
        .error(R.drawable.ic_car_shop)
        .placeholder(R.drawable.ic_car_shop)
val userCommonOptions = RequestOptions()
        .centerCrop()
        .error(R.drawable.default_head)
        .placeholder(R.drawable.default_head)

fun roundedBannerOptions(radius: Int) = RequestOptions.bitmapTransform(CenterCropRoundCornerTransform(radius))
        .error(R.drawable.banner_null_img)
        .placeholder(R.drawable.banner_null_img)

/**
 * 直播的模糊图
 */
val blurOptions=bitmapTransform(BlurTransformation(25, 3))

/**
 * 背景图
 */
fun backgroundOptions(image: Int) = RequestOptions()
        .error(image)
        .placeholder(image)

/***
 * 圆形
 */
var circleOptions = RequestOptions.circleCropTransform()

/**
 *  圆形的头像
 */
fun circleAvatorOptions(avator: Int) = RequestOptions
        .circleCropTransform()
        .error(avator)
        .placeholder(avator)

var circleUserOptions = RequestOptions.circleCropTransform().centerCrop()
        .error(R.drawable.default_head)
        .placeholder(R.drawable.default_head)