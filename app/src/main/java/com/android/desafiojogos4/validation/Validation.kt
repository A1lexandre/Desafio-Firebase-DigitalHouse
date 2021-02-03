package com.android.desafiojogos4.validation

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

class Validation {

    companion object {
        private fun checkValidation(field: EditText, errors: List<String>, min_length: Int, field2: EditText?): ArrayList<String> {
            val catchedErrors = arrayListOf<String>()
            for (error in errors) {
                when(error) {
                    REQUIRED -> {
                        if (field.text.toString().trim().isEmpty())
                            catchedErrors.add(REQUIRED)
                    }
                    EMAIL -> {
                        if (!field.text.toString().trim().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
                            catchedErrors.add(EMAIL)
                    }
                    MIN_LENGTH -> {
                        if (field.text.toString().trim().length < min_length)
                            catchedErrors.add(MIN_LENGTH)
                    }
                    EQUAL -> {
                        val pass: String = field2?.text?.toString()?.trim() ?: ""
                        val cpass: String = field.text.toString().trim()
                        if (!pass.equals(cpass))
                            catchedErrors.add(EQUAL)
                    }
                }
            }
            return catchedErrors
        }

        private fun getErrorMessage(errors: ArrayList<String>, min_length: Int): String {
            for (error in errors) {
                when(error) {
                    REQUIRED ->
                        return "Este campo é obrigatório"
                    EMAIL ->
                        return "Este endereção de email não é válido"
                    MIN_LENGTH ->
                        return "Este campo deve ter no mínimo ${min_length} caracteres"
                    EQUAL ->
                        return "O valor deste campo deve corresponder ao valor do campo acima"
                }
            }
            return ""
        }

        fun checkField(container: TextInputLayout, field: EditText, errors: List<String>, min_length: Int = 0, field2: EditText? = null): Boolean {
            val errorsList = checkValidation(field, errors, min_length, field2)
            field.doOnTextChanged { _, _, _, _ ->
                if (container.isErrorEnabled)
                    container.error = null
            }
            if (errorsList.isNotEmpty()) {
                container.error = getErrorMessage(errorsList, min_length)
                return false
            }

            return true
        }

        const val REQUIRED = "required"
        const val EMAIL = "email"
        const val MIN_LENGTH = "min_length"
        const val EQUAL = "equal"
    }

}