package com.looktab.echonupy


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object DesignUtil {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun ImageView.setImageUrl(url: String?) {
        url?.let {
            Glide.with(context)
                .load(it)
                .error(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_upload_img_default,
                        null
                    )
                )
                .placeholder(ColorDrawable(Color.parseColor("#DEE2E6")))
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("roundImageUrl")
    fun ImageView.setRoundImageUrl(url: String?) {
        url?.let {
            Glide.with(context)
                .load(it)
                .error(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_upload_img_default,
                        null
                    )
                )
                .circleCrop()
                .placeholder(ColorDrawable(Color.parseColor("#DEE2E6")))
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl300")
    fun ImageView.setImageUrl300(url: String?) {
        url?.let {
            Glide.with(context)
                .load(it)
                .override(300, 300)
                .thumbnail(0.1f)
                .placeholder(ColorDrawable(Color.parseColor("#DEE2E6")))
                .error(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_upload_img_default,
                        null
                    )
                )
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun View.setVisibleGone(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleInvisible")
    fun View.setVisibleInvisible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}