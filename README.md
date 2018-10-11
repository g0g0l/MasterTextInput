# MasterTextInput
One text input field to handle it all: text, date, number and inlcudes custom error check

A custom view comprising an EditText encapsulated in TextInputLayout along with different attributes, so that you don't need to add properties and write functions separately

## Usage

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.g0g0l:MasterTextInput:v1.1'
	}


### Use XML
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
    app:custom_hint_color="@color/colorPrimary"
    app:custom_text_color="@color/colorPrimaryDark"
    app:custom_input_max_length="100"
    app:custom_input_min_length="3"
    app:custom_min_max_length_error="Input must be between 3 and 100 characters"
    app:custom_error="This input is not valid" />
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
floating_input_type_email
floating_input_type_normal
floating_input_type_age
floating_input_type_alphanumeric
floating_input_type_number
floating_input_type_date
floating_input_type_date_time
```
### Available properties/functions
```
isValid
isMinMaxValid (Only for integer inputs)
isMinMaxLengthValid
text
error: get()/set()
showError()
clearError()
setHint()
```

### For complete list of properties, functions and input types, please check the source code
