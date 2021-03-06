package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityLoginBinding
import com.android.desafiojogos4.model.user.UserLogin
import com.android.desafiojogos4.validation.Validation
import com.android.desafiojogos4.validation.Validation.Companion.EMAIL
import com.android.desafiojogos4.validation.Validation.Companion.REQUIRED
import com.android.desafiojogos4.view.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupButtonListeners()
    }

    private fun setupButtonListeners() = with(binding) {

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, UserRegistrationActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnSignIn.setOnClickListener {

            val isFormValid = listOf<Boolean>(Validation.checkField(tilEmail, tietEmail, listOf(REQUIRED, EMAIL)),
                Validation.checkField(tilPassword, tietPassword, listOf(REQUIRED)))

            if(!isFormValid.contains(false))
                loginUser(UserLogin(tietEmail.text.toString().trim(), tietPassword.text.toString().trim()))
        }
    }

    private fun loginUser(user: UserLogin) {
        userViewModel.loginUser(user)

        userViewModel.userSucess.observe(this, {
            Toast.makeText(applicationContext, "Bem vindo de volta, ${it}!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })

        userViewModel.userFailure.observe(this, {
            Toast.makeText(applicationContext, it ?: "Falhou", Toast.LENGTH_SHORT).show()
        })
    }

}