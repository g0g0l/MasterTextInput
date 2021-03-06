package com.bibaswann.mastertextinput

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
Created by bibaswann on 11/10/18.
DO NOT MODIFY WITHOUT PROPER DISCUSSION
 */


object Utils {
    fun getIntFromString(amount: String): Int {
        var amount = amount
        if (Validate.isEmptyString(amount)) {
            return 0
        }
        amount = amount.replace("[^0-9]".toRegex(), "")
        var returnValue = 0
        try {
            returnValue = Integer.parseInt(amount)
        } catch (e: Exception) {
//            Log.e("Exception", Log.getStackTraceString(e))
        }

        return returnValue
    }

    fun length(etFloating: EditText): Int {
        return etFloating.text.toString().length
    }

    fun getDpFromPixel(context: Context, pixel: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (pixel * scale + 0.5f).toInt()
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}