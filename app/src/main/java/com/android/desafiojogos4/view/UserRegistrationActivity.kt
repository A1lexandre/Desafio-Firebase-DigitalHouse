package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityUserRegistrationBinding
import com.android.desafiojogos4.model.user.UserRegistration
import com.android.desafiojogos4.validation.Validation
import com.android.desafiojogos4.validation.Validation.Companion.EMAIL
import com.android.desafiojogos4.validation.Validation.Companion.EQUAL
import com.android.desafiojogos4.validation.Validation.Companion.MIN_LENGTH
import com.android.desafiojogos4.validation.Validation.Companion.REQUIRED
import com.android.desafiojogos4.view.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout

class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegistrationBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupButtonListeners()

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setupButtonListeners() = with(binding) {

        btnCreateAccount.setOnClickListener {

            val isFormValid = listOf(checkField(tilName, tietName, listOf(REQUIRED)),
                    checkField(tilEmail, tietEmail, listOf(REQUIRED, EMAIL)),
                    checkField(tilPassword, tietPassword, listOf(REQUIRED, MIN_LENGTH), 6),
                    checkField(tilRepeatPassword, tietRepeatPassword, listOf(REQUIRED, EQUAL), 0, tietPassword))

            if (!isFormValid.contains(false))
                saveUser(
                    UserRegistration(tietName.text.toString(), tietEmail.text.toString(),
                        tietPassword.text.toString())
                )

        }
    }

    private fun saveUser(user: UserRegistration) {
        userViewModel.createUser(user)

        userViewModel.userSucess.observe(this, {
            Toast.makeText(applicationContext, "Bem vindo, $it!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })

        userViewModel.userFailure.observe(this, {
            Toast.makeText(applicationContext, it ?: "Falhou", Toast.LENGTH_SHORT).show()
        })

    }

    private fun checkField(container: TextInputLayout, field: EditText, errors: List<String>, min_length: Int = 0, field2: EditText? = null): Boolean {
        val errorsList = Validation.checkValidation(field, errors, min_length, field2)
        field.doOnTextChanged { _, _, _, _ ->
            if (container.isErrorEnabled)
                container.error = null
        }
        if (errorsList.isNotEmpty()) {
            container.error = Validation.getErrorMessage(errorsList, min_length)
            return false
        }

        return true
    }

}