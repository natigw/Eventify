package com.example.common.utils

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.common.R
import com.google.android.material.button.MaterialButton

fun showCustomDialog(
    context: Context,                    //

    mainText: String,                    //
    headingText: String? = null,
    positiveButtonText: String,          //
    neutralButtonText: String,           //
    negativeButtonText: String? = null,

    positiveAction: ()->Unit,            //
    negativeAction: (()->Unit)? = null,
    neutralAction: (()->Unit)? = null,

    textColor: Int? = null,
    buttonColor: Int? = null,
    buttonColorSecondary: Int? = null,
    cardColor: Int,                      //

    font: Typeface? = null,
    fontHeading: Typeface? = null,
) {

    val dialog = Dialog(context)
    dialog.setContentView(R.layout.custom_dialog_layout)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val dialogText = dialog.findViewById<TextView>(R.id.textMainDialog)
    val dialogHeading = dialog.findViewById<TextView>(R.id.textHeadingDialog)
    val buttonPositive = dialog.findViewById<MaterialButton>(R.id.buttonPositive)
    val buttonNegative = dialog.findViewById<MaterialButton>(R.id.buttonNegative)
    val buttonNeutral = dialog.findViewById<MaterialButton>(R.id.buttonNeutral)

    val cardBackgroundColor = dialog.findViewById<CardView>(R.id.cardDialog)

    cardBackgroundColor.setCardBackgroundColor(cardColor)

    if (headingText != null) {
        dialogHeading.text = headingText
        textColor?.let { color -> dialogHeading.setTextColor(color) }
        fontHeading?.let { dialogHeading.typeface = it }
    }
    else
        dialogHeading.visibility = View.GONE

    dialogText.text = mainText
    textColor?.let { dialogText.setTextColor(it) }
    font?.let { dialogText.typeface = it }

    buttonPositive.text = positiveButtonText
    buttonNeutral.text = neutralButtonText
    negativeButtonText?.let {
        buttonNegative.text = it
    } ?: run {
        buttonNegative.visibility = View.GONE
    }


    buttonColor?.let { color ->
        buttonPositive.setBackgroundColor(color)
        buttonNegative.setBackgroundColor(buttonColorSecondary ?: color)
        val strokeColor = ColorStateList.valueOf(color)
        buttonNeutral.strokeColor = strokeColor
        buttonNeutral.setTextColor(color)
    }
    buttonPositive.setTextColor(cardColor)
    buttonNegative.setTextColor(cardColor)


    buttonPositive.setOnClickListener {
        positiveAction()
        dialog.dismiss()
    }
    buttonNegative.setOnClickListener {
        if (negativeAction != null) {
            negativeAction()
            dialog.dismiss()
        }
    }
    buttonNeutral.setOnClickListener {
        if (neutralAction != null)
            neutralAction()
        dialog.dismiss()
    }


    dialog.show()
}