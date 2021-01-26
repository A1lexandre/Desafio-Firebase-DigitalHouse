package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityLoginBinding
import com.android.desafiojogos4.validation.Validation
import com.android.desafiojogos4.validation.Validation.Companion.EMAIL
import com.android.desafiojogos4.validation.Validation.Companion.REQUIRED
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
    }

    private fun setupButtonListeners() = with(binding) {

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, UserRegistrationActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnSignIn.setOnClickListener {
            checkField(tilEmail, tietEmail, listOf(REQUIRED, EMAIL))
            checkField(tilPassword, tietPassword, listOf(REQUIRED))
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