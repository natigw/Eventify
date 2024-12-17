package com.example.common.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import com.example.common.R

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
                LayoutInflater.from(context).inflate(R.layout.nancy_toast_layout, null, false)
            val l1 = layout.findViewById<TextView>(R.id.toast_text)
            val linearLayout = layout.findViewById<LinearLayout>(R.id.toast_type)
            val img = layout.findViewById<ImageView>(R.id.toast_icon)
            val img1 = layout.findViewById<ImageView>(R.id.imageView4)
            l1.text = message
            if (androidIcon)
                img1.visibility = View.VISIBLE
            else img1.visibility = View.GONE
            when (type) {
                SUCCESS -> {
                    linearLayout.setBackgroundResource(R.drawable.shape_success)
                    img.setImageResource(R.drawable.ic_success)
                }
                WARNING -> {
                    linearLayout.setBackgroundResource(R.drawable.shape_warning)
                    img.setImageResource(R.drawable.ic_warning)
                }
                ERROR -> {
                    linearLayout.setBackgroundResource(R.drawable.shape_error)
                    img.setImageResource(R.drawable.ic_cancel)
                }
                INFO -> {
                    linearLayout.setBackgroundResource(R.drawable.shape_info)
                    img.setImageResource(R.drawable.ic_info)
                }
                DEFAULT -> {
                    linearLayout.setBackgroundResource(R.drawable.shape_default)
                    img.visibility = View.GONE
                }
            }
            toast.view = layout
            return toast
        }
    }
}