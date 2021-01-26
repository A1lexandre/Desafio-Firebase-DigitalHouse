package com.android.desafiojogos4.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityUserRegistrationBinding
import com.android.desafiojogos4.validation.Validation
import com.android.desafiojogos4.validation.Validation.Companion.EMAIL
import com.android.desafiojogos4.validation.Validation.Companion.EQUAL
import com.android.desafiojogos4.validation.Validation.Companion.MIN_LENGTH
import com.android.desafiojogos4.validation.Validation.Companion.REQUIRED
import com.google.android.material.textfield.TextInputLayout

class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setupButtonListeners() = with(binding) {

        btnCreateAccount.setOnClickListener {
            checkField(tilName, tietName, listOf(REQUIRED))
            checkField(tilEmail, tietEmail, listOf(REQUIRED, EMAIL))
            checkField(tilPassword, tietPassword, listOf(REQUIRED, MIN_LENGTH), 6)
            if (!Validation.isEqual(tietRepeatPassword, tietPassword))
                checkField(tilRepeatPassword, tietRepeatPassword, listOf(REQUIRED, EQUAL))
            else
                checkField(tilRepeatPassword, tietRepeatPassword, listOf(REQUIRED))
        }
    }

    private fun checkField(container: TextInputLayout, field: EditText, errors: List<String>, min_length: Int = 0) {
        val errorsList = Validation.checkValidation(field, errors, min_length)
        if (errorsList.isNotEmpty())
            container.error = Validation.getErrorMessage(errorsList, min_length)
        field.doOnTextChanged { _, _, _, _ ->
            if (container.isErrorEnabled)
                container.error = null
        }
    }

}