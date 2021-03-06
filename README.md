# MasterTextInput

[![](https://jitpack.io/v/g0g0l/MasterTextInput.svg)](https://jitpack.io/#g0g0l/MasterTextInput)

One text input field to handle it all: email, string, date, number and also inlcudes custom error check

A custom view comprising an EditText encapsulated in TextInputLayout along with different attributes, so that you don't need to add properties and write functions separately

## Usage
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.g0g0l:MasterTextInput:1.2.3'
	}


### In XML
```
<com.bibaswann.mastertextinput.MasterTextInput
    android:id="@+id/inputName"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingTop="8dp"
    app:custom_hint="Your Name"
    app:custom_input_type="@integer/floating_input_type_normal"
    app:custom_text="John Doe"
    app:custom_editable="true"
    app:custom_single_line="true"
    app:custom_hint_color="@color/colorPrimary"
    app:custom_text_color="@color/colorPrimaryDark"
    app:custom_input_max_length="100"
    app:custom_input_min_length="3"
    app:custom_min_max_length_error="Input must be between 3 and 100 characters"
    app:custom_error="This input is not valid"
    app:custom_background="@drawable/sample_box_design"
    app:custom_image="@drawable/sample_image"
    app:custom_border="true"
    app:custom_all_caps="false"
    app:custom_text_size="14sp" />
```
### In Kotlin File
```
val masterTextInput: MasterTextInput = MasterTextInput(this)
//isValid will automatically show specified error message in case of error
if (!masterTextInput.isValid)
	btnSubmit.isEnabled = false
```
### Available input types are
```
floating_input_type_normal
floating_input_type_email
floating_input_type_password
floating_input_type_age
floating_input_type_alphanumeric
floating_input_type_number
floating_input_type_date
floating_input_type_date_time
floating_input_type_text_name
floating_input_type_phone
```
### Available properties/functions
```
isValid(): Checks email/number and if input is empty
isMinMaxValid(): Whether input is in given range (Only for integer inputs)
isMinMaxLengthValid()
text: get()/set()
error: get()/set()
hint: get()/set()
showError()
clearError()
setTextWatcher(TextWatcher)
setImageClickListener(View.OnClickListener): Only if you set custom_image via XML
setSingleLine(Boolean)
setEditable(Boolean)
hideHint(): Hides hint on focus
```

### For complete list of properties, functions and input types, please check the source code
