package com.bibaswann.mastertextinput

import android.annotation.SuppressLint
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
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
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
    var backgroundImage: Drawable? = null

    var mHint: String? = null
    var mText: String? = null
    var mHintColor: Int = -1
    var mTextcolor: Int = -1
    var mValueTextSize: Int = -1
    var errorMessage: String? = ""
    var minMaxErrorMessage: String? = ""
    var minMaxLengthErrorMessage: String? = ""

    var min: Int = 0
    var max: Int = 0

    var minLength: Int = 0
    var maxLength: Int = 0

    private var isEditable = true
    private var isSingleLine = false
    private var isAllCaps = false
    private var hasBorder = false

    //Todo: When add more type add entry here
    var isValid: Boolean = false
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
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_password)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_text_name)) {
                returnValue = !Validate.isEmptyString(etFloating.text.toString())
            }
            if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_phone)) {
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
            mContext!!.resources.getInteger(R.integer.floating_input_type_password) -> setupPassword()
            mContext!!.resources.getInteger(R.integer.floating_input_type_text_name) -> setupName()
            mContext!!.resources.getInteger(R.integer.floating_input_type_phone) -> setupPhone()
        }

        //For types other than email and age, restriction will be there if set explicitly from XML/code
        if (maxLength > 0) {
            etFloating.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }

        redrawData()
    }

    private fun setupDocumentNumber() {
        //Todo there is a bug I think, sometimes the everything is deleted on special characters press, test later
        etFloating.filters = arrayOf(InputFilter { src, start, end, dst, dstart, dend ->
            if (src == "") { // for backspace
                return@InputFilter src
            }
            if (src.toString().matches("[a-zA-Z 0-9]+".toRegex())) {
                src
            } else ""
        })
    }

    private fun setupNormal() {
        //Todo Not required, delete later
        redrawData()
    }

    private fun setupEmail() {
        etFloating.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        //This restricts input more than 256 characters
        etFloating.filters = arrayOf(InputFilter.LengthFilter(EMAIl_LENGTH))
    }

    private fun setupNumber() {
        etFloating.keyListener = DigitsKeyListener.getInstance("0123456789.")
        //editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    private fun setupPhone() {
        etFloating.inputType = InputType.TYPE_CLASS_PHONE
    }

    private fun setupName() {
        //Todo there is a bug I think, sometimes the everything is deleted on special characters press, test later
        etFloating.filters = arrayOf(InputFilter { src, start, end, dst, dstart, dend ->
            if (src == "") { // for backspace
                return@InputFilter src
            }
            if (src.toString().matches("[a-zA-Z ]+".toRegex())) {
                src
            } else ""
        })
    }

    private fun setupPassword() {
        etFloating.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        etFloating.setSelection(etFloating.text.length)
        tilFloating.isPasswordVisibilityToggleEnabled = true
    }

    private fun setupAge() {
        etFloating.inputType = InputType.TYPE_CLASS_NUMBER
        etFloating.filters = arrayOf(InputFilter.LengthFilter(AGE_LENGTH))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDateTime(dateOnly: Boolean) {
        etFloating.isEnabled = false
        etFloating.inputType = InputType.TYPE_NULL
        etFloating.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_date_range_24, 0)

        //Currently not allowing manual input, comment below lines and change input type above to allow
        etFloating.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                Utils.hideKeyboard(mContext!!, etFloating)
        }

        etFloating.setOnTouchListener(OnTouchListener { _, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etFloating.right - etFloating.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    setdateTimeToEditText(etFloating, dateOnly)
                    return@OnTouchListener true
                }
            }
            false
        })
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

    //Todo if add more attribute, change here
    private fun initAttributes(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        //get the attributes specified in attrs.xml using the name we included
        val attr = mContext!!.theme.obtainStyledAttributes(attrs, R.styleable.MasterTextInput, 0, 0)

        try {
            //get the text and colors specified using the names in attrs.xml
            floatingImage = attr.getDrawable(R.styleable.MasterTextInput_custom_image)
            backgroundImage = attr.getDrawable(R.styleable.MasterTextInput_custom_background)
            mHintColor = attr.getColor(R.styleable.MasterTextInput_custom_hint_color, -1)
            mText = attr.getString(R.styleable.MasterTextInput_custom_text)
            mHint = attr.getString(R.styleable.MasterTextInput_custom_hint)
            mTextcolor = attr.getColor(R.styleable.MasterTextInput_custom_text_color, -1)
            mHintColor = attr.getColor(R.styleable.MasterTextInput_custom_hint_color, -1)
            errorMessage = attr.getString(R.styleable.MasterTextInput_custom_error)
            minMaxErrorMessage = attr.getString(R.styleable.MasterTextInput_custom_min_max_error)
            minMaxLengthErrorMessage = attr.getString(R.styleable.MasterTextInput_custom_min_max_length_error)
            inputType = attr.getInt(R.styleable.MasterTextInput_custom_input_type, 0)
            isSingleLine = attr.getBoolean(R.styleable.MasterTextInput_custom_single_line, true)
            isAllCaps = attr.getBoolean(R.styleable.MasterTextInput_custom_all_caps, false)
            min = attr.getInt(R.styleable.MasterTextInput_custom_input_min, 0)
            max = attr.getInt(R.styleable.MasterTextInput_custom_input_max, 0)
            minLength = attr.getInt(R.styleable.MasterTextInput_custom_input_min_length, 0)
            maxLength = attr.getInt(R.styleable.MasterTextInput_custom_input_max_length, 0)
            isEditable = attr.getBoolean(R.styleable.MasterTextInput_custom_editable, true)
            hasBorder = attr.getBoolean(R.styleable.MasterTextInput_custom_border, false)
            mValueTextSize = attr.getDimensionPixelSize(R.styleable.MasterTextInput_custom_text_size, -1)

        } finally {
            attr.recycle()
        }
    }

    //Todo if add more attribute, change here
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
        if (backgroundImage != null) {
            val scale = resources.displayMetrics.density
            val dpAsPixels = (8 * scale + 0.5f).toInt()
            etFloating.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                etFloating.setBackgroundDrawable(backgroundImage)
            } else {
                etFloating.background = backgroundImage
            }
        }
        if (mTextcolor != -1) {
            etFloating.setTextColor(mTextcolor)
        }
        if (mHintColor != -1) {
            etFloating.setHintTextColor(mHintColor)
        }
        if (mValueTextSize != -1) {
            etFloating.textSize = mValueTextSize.toFloat()
        }
        if (hasBorder) {
            var eightDp = Utils.getDpFromPixel(context, 8)
            etFloating.setPadding(eightDp, eightDp, eightDp, eightDp)
            if (isEditable)
                etFloating.setBackgroundResource(R.drawable.edittext_box_active)
            else
                etFloating.setBackgroundResource(R.drawable.edittext_box_inactive)
            hideHint()
        }
        if (isAllCaps) {
            etFloating.filters = arrayOf(InputFilter.AllCaps())
        }

        setEditable(isEditable)
        etFloating.addTextChangedListener(mTextWatcher)
        //Single line is not applicable to all input types
        if (inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_normal)
                || inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_alphanumeric)
                || inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_number)
                || inputType == mContext!!.resources.getInteger(R.integer.floating_input_type_text_name))
            setSingleLine(isSingleLine)
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        etFloating.addTextChangedListener(textWatcher)
    }

    fun hideHint() {
        tilFloating.isHintAnimationEnabled = false
        etFloating.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!Validate.isEmptyString(etFloating.text.toString()) || hasFocus)
                tilFloating.hint = ""
            else
                tilFloating.hint = mHint
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setImageClickListener(onClickListener: OnClickListener) {
        if (floatingImage != null) {
            etFloating.setOnTouchListener(OnTouchListener { _, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= etFloating.right - etFloating.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        onClickListener.onClick(etFloating)
                        return@OnTouchListener true
                    }
                }
                false
            })
        }
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

    //Todo will think about converting the following to properties

    fun setHint(hint: String) {
        mHint = if (Validate.isEmptyString(hint)) "" else hint
        tilFloating.hint = mHint
    }

    fun setEditable(editable: Boolean) {
        isEditable = editable
        etFloating.isEnabled = isEditable
        etFloating.isFocusable = isEditable
        etFloating.isFocusableInTouchMode = isEditable
    }

    fun setSingleLine(singleLine: Boolean) {
        etFloating.setSingleLine(singleLine)
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
                }, mHour, mMinute, true)
                timePickerDialog.show()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    //Todo add get integer part and string part function

    companion object {
        val EMAIl_LENGTH = 256
        val AGE_LENGTH = 3
        val NAME_INPUT_CHARS = "qwertyuiopasdfghjklzxcvbnm QWERTYUIOPASDFGHJKLZXCVBNM"
    }
}
