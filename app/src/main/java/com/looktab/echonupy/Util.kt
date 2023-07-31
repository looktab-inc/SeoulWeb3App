package com.looktab.echonupy

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.looktab.echonupy.databinding.ToastLayoutBinding

object Util {

    fun createToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun createSolanaToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val binding: ToastLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_layout, null, false)

        binding.text.text = message

        Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 16.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}