package com.bibaswann.mastertextinput

import android.util.Patterns
import android.widget.EditText
import java.util.regex.Pattern

/**
Created by bibaswann on 11/10/18.
DO NOT MODIFY WITHOUT PROPER DISCUSSION
 */


object Validate {
    fun isEmptyString(s: String?): Boolean {
        return s == null || s.trim().isEmpty()
    }

    fun isEmailAddress(email: String): Boolean {
        if (isEmptyString(email)) {
            return false
        }

        val matcher = Patterns.EMAIL_ADDRESS.matcher(email)
        return matcher.matches()
    }

    fun isEqualIgnoreCase(lhs: String?, rhs: String?): Boolean {
        return if (lhs == null || rhs == null) false else lhs.equals(rhs, ignoreCase = true)
    }

    fun isEquals(lhs: String?, rhs: String?): Boolean {
        return if (lhs == null || rhs == null) false else lhs.equals(rhs, ignoreCase = true)
    }

    fun isEmptyList(list: List<*>?): Boolean {
        return list == null || list.size <= 0
    }

    fun isAlphaNumeric(alphaNumeric: EditText): Boolean {
        return isAlphaNumeric(alphaNumeric.text.toString())
    }

    fun isAlphaNumeric(alphaNumeric: String): Boolean {
        if (Validate.isEmptyString(alphaNumeric)) {
            return false
        }
        val pattern = Pattern.compile("[0-9a-zA-Z]+")
        val matcher = pattern.matcher(alphaNumeric)
        return matcher.matches()
    }

    fun isEmpty(editText: MasterTextInput?): Boolean {
        return if (editText == null) true else isEmptyString(editText!!.text)
    }
}