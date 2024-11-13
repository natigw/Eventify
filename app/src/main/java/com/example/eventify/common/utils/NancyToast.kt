package com.example.eventify.common.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import com.example.eventify.R

class NancyToast (context: Context) : Toast(context) {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SUCCESS, WARNING, ERROR, INFO, DEFAULT)
    annotation class LayoutType

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(LENGTH_SHORT, LENGTH_LONG)
    annotation class Duration
    companion object {
        const val SUCCESS: Int = 1
        const val WARNING: Int = 2
        const val ERROR: Int = 3
        const val INFO: Int = 4
        const val DEFAULT: Int = 5

        const val LENGTH_SHORT: Int = Toast.LENGTH_SHORT
        const val LENGTH_LONG: Int = Toast.LENGTH_LONG

        fun makeText(
            context: Context,
            message: CharSequence?,
            @Duration duration: Int,
            @LayoutType type: Int,
            androidIcon : Boolean
        ): Toast {
            val toast = Toast(context)
            toast.duration = duration
            val layout: View =
                LayoutInflater.from(context).inflate(com.shashank.sony.fancytoastlib.R.layout.fancytoast_layout, null, false)
            val l1 = layout.findViewById<TextView>(com.shashank.sony.fancytoastlib.R.id.toast_text)
            val linearLayout = layout.findViewById<LinearLayout>(com.shashank.sony.fancytoastlib.R.id.toast_type)
            val img = layout.findViewById<ImageView>(com.shashank.sony.fancytoastlib.R.id.toast_icon)
            val img1 = layout.findViewById<ImageView>(com.shashank.sony.fancytoastlib.R.id.imageView4)
            l1.text = message
            if (androidIcon)
                img1.visibility = View.VISIBLE
            else img1.visibility = View.GONE
            when (type) {
                SUCCESS -> {
                    linearLayout.setBackgroundResource(com.shashank.sony.fancytoastlib.R.drawable.success_shape)
                    img.setImageResource(R.drawable.round_check_circle_24)
                }
                WARNING -> {
                    linearLayout.setBackgroundResource(com.shashank.sony.fancytoastlib.R.drawable.warning_shape)
                    img.setImageResource(R.drawable.round_warning_24)
                }
                ERROR -> {
                    linearLayout.setBackgroundResource(com.shashank.sony.fancytoastlib.R.drawable.error_shape)
                    img.setImageResource(R.drawable.round_cancel_24)
                }
                INFO -> {
                    linearLayout.setBackgroundResource(com.shashank.sony.fancytoastlib.R.drawable.info_shape)
                    img.setImageResource(R.drawable.round_info_24)
                }
                DEFAULT -> {
                    linearLayout.setBackgroundResource(com.shashank.sony.fancytoastlib.R.drawable.default_shape)
                    img.visibility = View.GONE
                }
            }
            toast.setView(layout)
            return toast
        }
    }
}