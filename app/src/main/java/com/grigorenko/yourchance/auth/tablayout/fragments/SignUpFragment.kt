package com.grigorenko.yourchance.auth.tablayout.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.grigorenko.yourchance.MainActivity
import com.grigorenko.yourchance.databinding.FragmentSignUpBinding
import com.grigorenko.yourchance.viewmodel.AuthViewModel
import java.util.regex.Pattern

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    private val passwordPattern = Pattern.compile(
        "^" + "(?=.*[0-9])" +     //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                // "(?=.*[a-zA-Z])" +   //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                authViewModel.addNewUser(it,
                                         binding.nameField.text.toString(),
                                         binding.phoneField.text.toString())
                updateUi(it)
            }
        }

        binding.apply {
            signUpButton.setOnClickListener {
                if (validateEmail() and validatePassword() and validatePhoneNumber())
                    authViewModel.signUpWithEmail(this.emailField.text.toString(),
                                                  this.passwordField.text.toString())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            emailField.text?.clear()
            emailContainer.error = null
            passwordField.text?.clear()
            passwordContainer.error = null
            phoneField.text?.clear()
            phoneContainer.error = null
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val mainActivity = Intent(context, MainActivity::class.java)
            user.apply {
                mainActivity.putExtra("Email", binding.emailField.text.toString())
                mainActivity.putExtra("Name", binding.nameField.text.toString())
                mainActivity.putExtra("Phone", binding.phoneField.text.toString())
                mainActivity.putExtra("Photo", this.photoUrl)
            }
            startActivity(mainActivity)
            activity?.finish()
        } else {
            Log.e("AUTH", "User is null")
        }
    }

    private fun validateEmail(): Boolean {
        val emailInput = binding.emailField.text.toString()
        return when {
            emailInput.isEmpty() -> {
                binding.emailContainer.error = "Поле не может быть пустым"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> {
                binding.emailContainer.error = "Введите правильный email адрес"
                false
            }
            else -> {
                binding.emailContainer.error = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput = binding.passwordField.text.toString()
        return when {
            passwordInput.isEmpty() -> {
                binding.passwordContainer.error = "Поле не может быть пустым"
                false
            }
            !passwordPattern.matcher(passwordInput).matches() -> {
                binding.passwordContainer.error = "Пароль слишком легкий"
                false
            }
            else -> {
                binding.passwordContainer.error = null
                true
            }
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumberInput = binding.phoneField.text.toString()
        return when {
            phoneNumberInput.isEmpty() -> {
                binding.phoneContainer.error = "Поле не может быть пустым"
                false
            }
            phoneNumberInput.first() != '0' -> {
                binding.phoneContainer.error = "Номер должен начинаться с нуля"
                false
            }
            phoneNumberInput.length != 10 -> {
                binding.phoneContainer.error = "Номер указан не полностью"
                false
            }
            else -> {
                binding.phoneContainer.error = null
                true
            }
        }
    }
}
