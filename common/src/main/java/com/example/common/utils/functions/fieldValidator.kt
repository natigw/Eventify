package com.example.common.utils.functions

import com.google.android.material.textfield.TextInputLayout

//fun validateInputField(
//    inputLayout: TextInputLayout,
//    inputText: String,
//    validator: (String) -> Boolean,
//    errorMessage: String
//): Boolean {
//    if (!validator(inputText)) {
//        inputLayout.isErrorEnabled = true
//        inputLayout.error = errorMessage
//        inputLayout.editText?.requestFocus()  //focus on the field
//        return false
//    } else {
//        inputLayout.error = null
//        inputLayout.isErrorEnabled = false
//        return true
//    }
//}

fun validateInputFieldMeet(
    inputLayout: TextInputLayout,
    conditionToMeet: Boolean,
    errorMessage: String
): Boolean {
    if (!conditionToMeet) {
        inputLayout.isErrorEnabled = true
        inputLayout.error = errorMessage
        inputLayout.editText?.requestFocus()  //focus on the field
        return false
    } else {
        inputLayout.error = null
        inputLayout.isErrorEnabled = false
        return true
    }
}

fun validateInputFieldEmpty(
    inputLayout: TextInputLayout,
    inputText: String,
    errorMessage: String
): Boolean {
    if (inputText.isBlank()) {
        inputLayout.isErrorEnabled = true
        inputLayout.error = errorMessage
        inputLayout.editText?.requestFocus()  //focus on the layout
        return false
    } else {
        inputLayout.error = null
        inputLayout.isErrorEnabled = false
        return true
    }
}