package com.bibaswann.mastertextinput

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import java.util.*

/**
Created by bibaswann on 11/10/18.
DO NOT MODIFY WITHOUT PROPER DISCUSSION
 */


class MasterTextInput : LinearLayout {
    private var mContext: Context? = null
    private var inputType = 0

    lateinit var etFloating: EditText
    lateinit var tilFloating: TextInputLayout
    lateinit var llHolder: LinearLayout

    var floatingImage: Drawable? = null
    var mHint: String? = null
    var mText: String? = null
    var mHintColor: Int = -1
    var mTextcolor: Int = -1

    var errorMessage: String? = ""
    var minMaxErrorMessage: String? = ""
    var minMaxLengthErrorMessage: String? = ""

    var min: Int = 0
    var max: Int = 0

    var minLength: Int = 0
    var maxLength: Int = 0

    var mIsEditable = true

    //Todo: When add more type add entry here
    open var isValid: Boolean = false
        get() {
            var returnValue = !Validate.isEmptyString(etFloating.text.toString())
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_email)) {
                returnValue = Validate.isEmailAddress(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_normal)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_age)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_alphanumeric)) {
                returnValue = Validate.isAlphaNumeric(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_number)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_date)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_date_time)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (!returnValue) {
                etFloating.requestFocus()
                tilFloating.error = errorMessage
            }

            return returnValue
        }

    val isMinMaxValid: Boolean
        get() {
            var returnValue = false
            //Restriction is already in place for email and age, no ned to check
            returnValue = if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_email)
                    || inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_age)) {
                true
            } else {
                getMinMaxValidation(Utils.getIntFromString(etFloating.text.toString()))
            }

            if (!returnValue) {
                val error = if (Validate.isEmptyString(minMaxErrorMessage)) errorMessage else minMaxErrorMessage
                tilFloating.error = error
                etFloating.requestFocus()
            }
            return returnValue
        }

    val isMinMaxLengthValid: Boolean
        get() {
            val returnValue = getMinMaxLengthValidation(Utils.length(etFloating))

            if (!returnValue) {
                var error: String? = if (Validate.isEmptyString(minMaxLengthErrorMessage)) minMaxErrorMessage else minMaxLengthErrorMessage
                error = if (Validate.isEmptyString(error)) errorMessage else error
                tilFloating.error = error
                etFloating.requestFocus()
            }
            return returnValue
        }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            clearError()
        }

        override fun afterTextChanged(editable: Editable) {
        }
    }

    var text: String
        get() = etFloating.text.toString()
        set(text) {
            mText = if (Validate.isEmptyString(text)) "" else text
            etFloating.setText(mText)
        }

    /**
     * returns if the edit text is focused or not
     * @return
     */
    val isEditFocused: Boolean
        get() = etFloating.isFocused

    constructor(context: Context) : super(context) {
        mContext = context
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initAttributes(attrs)
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        initAttributes(attrs)
        initLayout()
    }

    private fun initLayout() {
        val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        inflater.inflate(R.layout.layout_input, this, true)

        etFloating = findViewById(R.id.etFloating)
        tilFloating = findViewById(R.id.tilFloating)
        llHolder = findViewById(R.id.llHolder)

        initListeners()

    }

    //Todo: When add more type add entry here
    private fun initListeners() {
        etFloating.addTextChangedListener(mTextWatcher)

        when (inputType) {
            mContext!!.resources.getInteger(R.integer.floating_input_type_email) -> setupEmail()
            mContext!!.resources.getInteger(R.integer.floating_input_type_normal) -> setupNormal()
            mContext!!.resources.getInteger(R.integer.floating_input_type_age) -> setupAge()
            mContext!!.resources.getInteger(R.integer.floating_input_type_alphanumeric) -> setupDocumentNumber()
            mContext!!.resources.getInteger(R.integer.floating_input_type_number) -> setupNumber()
            mContext!!.resources.getInteger(R.integer.floating_input_type_date) -> setupDateTime(true)
            mContext!!.resources.getInteger(R.integer.floating_input_type_date_time) -> setupDateTime(false)
        }

        //For types other than email and age, restriction will be there if set explicitly from XML/code
        if (maxLength > 0) {
            val filters = arrayOfNulls<InputFilter>(1)
            filters[0] = InputFilter.LengthFilter(maxLength)
            etFloating.filters = filters
        }

        redrawData()
    }

    private fun setupDocumentNumber() {
        val editText = etFloating
        editText.filters = arrayOf(InputFilter { src, start, end, dst, dstart, dend ->
            if (src == "") { // for backspace
                return@InputFilter src
            }
            if (src.toString().matches("[a-zA-Z 0-9]+".toRegex())) {
                src
            } else ""
        })
    }

    private fun setupNormal() {
        redrawData()
    }

    private fun setupEmail() {
        etFloating.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        val filters = arrayOfNulls<InputFilter>(1)
        filters[0] = InputFilter.LengthFilter(EMAIl_LENGTH)//This restricts input more than 256 characters
        etFloating.filters = filters
    }

    private fun setupNumber() {
        etFloating.keyListener = DigitsKeyListener.getInstance("0123456789.")
        //editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    private fun setupAge() {
        etFloating.inputType = InputType.TYPE_CLASS_NUMBER
        val filters = arrayOfNulls<InputFilter>(1)
        filters[0] = InputFilter.LengthFilter(AGE_LENGTH)
        etFloating.filters = filters
    }

    private fun setupDateTime(dateOnly: Boolean) {
        etFloating.inputType = InputType.TYPE_NULL
        etFloating.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                setdateTimeToEditText(etFloating, dateOnly)
        }
    }

    private fun getMinMaxValidation(value: Int): Boolean {
        return if (min != 0 && max != 0) {
            value in min..max
        } else if (min != 0) {
            value >= min
        } else if (max != 0) {
            value <= max
        } else {
            value > 0
        }
    }

    private fun getMinMaxLengthValidation(value: Int): Boolean {
        return if (minLength != 0 && maxLength != 0) {
            value in minLength..maxLength
        } else if (minLength != 0) {
            value >= minLength
        } else if (maxLength != 0) {
            value <= maxLength
        } else {
            value > 0
        }
    }

    private fun initAttributes(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        //get the attributes specified in attrs.xml using the name we included
        val attr = mContext!!.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0)

        try {
            //get the text and colors specified using the names in attrs.xml
            floatingImage = attr.getDrawable(R.styleable.CustomEditText_custom_image)
            mText = attr.getString(R.styleable.CustomEditText_custom_text)
            mHint = attr.getString(R.styleable.CustomEditText_custom_hint)
            mTextcolor = attr.getColor(R.styleable.CustomEditText_custom_text_color,-1)
            mHintColor = attr.getColor(R.styleable.CustomEditText_custom_hint_color,-1)
            errorMessage = attr.getString(R.styleable.CustomEditText_custom_error)
            minMaxErrorMessage = attr.getString(R.styleable.CustomEditText_custom_min_max_error)
            minMaxLengthErrorMessage = attr.getString(R.styleable.CustomEditText_custom_min_max_length_error)
            inputType = attr.getInt(R.styleable.CustomEditText_custom_input_type, 0)

            min = attr.getInt(R.styleable.CustomEditText_custom_input_min, 0)
            max = attr.getInt(R.styleable.CustomEditText_custom_input_max, 0)
            minLength = attr.getInt(R.styleable.CustomEditText_custom_input_min_length, 0)
            maxLength = attr.getInt(R.styleable.CustomEditText_custom_input_max_length, 0)
            mIsEditable = attr.getBoolean(R.styleable.CustomEditText_custom_editable, true)

        } finally {
            attr.recycle()
        }
    }

    private fun redrawData() {
        //Here we apply the attributes
        if (!Validate.isEmptyString(mHint)) {
            tilFloating.hint = mHint
        }
        if (!Validate.isEmptyString(mText)) {
            etFloating.setText(mText)
        }
        if (floatingImage != null) {
            etFloating.setCompoundDrawablesWithIntrinsicBounds(null, null, floatingImage, null)
        }
        if (mTextcolor != -1) {
            etFloating.setTextColor(mTextcolor)
        }
        if (mTextcolor != -1) {
            etFloating.setHintTextColor(mHintColor)
        }
        setEditable(mIsEditable)
        etFloating.addTextChangedListener(mTextWatcher)
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        etFloating.addTextChangedListener(textWatcher)
    }

    fun clearError() {
        tilFloating.error = null
        tilFloating.isErrorEnabled = false
    }

    private fun setErrorMessageToTil(error: String) {
        errorMessage = error
        if (Validate.isEmptyString(errorMessage)) {
            clearError()
            return
        }
        tilFloating.error = error
        tilFloating.isErrorEnabled = true
    }

    fun showError(b: Boolean) {
        if (b) {
            tilFloating.error = errorMessage
            tilFloating.isErrorEnabled = true
        } else {
            clearError()
        }
    }

    var error: Any
        get() {
            return errorMessage.toString()
        }
        set(error) {
            when (error) {
                is String -> setErrorMessageToTil(error)
                is Int -> setErrorMessageToTil(mContext!!.getString(error))
            }
        }

    fun setHint(hint: String) {
        mHint = if (Validate.isEmptyString(hint)) "" else hint
        tilFloating.hint = mHint
    }

    fun setEditable(editable: Boolean) {
        mIsEditable = editable
        etFloating.isFocusable = mIsEditable
        etFloating.isEnabled = mIsEditable
        etFloating.isFocusableInTouchMode = mIsEditable
    }

    fun clearBackground() {
        etFloating.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
    }

    private fun setdateTimeToEditText(editText: EditText?, dateOnly: Boolean) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)

        val dialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            editText!!.setText(dayOfMonth.toString() + "/" + month.toString() + "/" + year.toString())
            if (!dateOnly) {
                val timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    editText!!.setText(editText!!.text.toString() + " - " + String.format("%02d:%02d", hourOfDay, minute))
                    //This will not work if this is the first view from top
                    //When you clear a focus, the focus goes to first view from the top
                    //And there is no solution for that, except, maybe using a button
                    editText.clearFocus()
                }, mHour, mMinute, true)
                timePickerDialog.show()
            } else
                editText.clearFocus()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    companion object {
        val EMAIl_LENGTH = 256
        val AGE_LENGTH = 3
        val NAME_INPUT_CHARS = "qwertyuiopasdfghjklzxcvbnm. QWERTYUIOPASDFGHJKLZXCVBNM"
    }
}