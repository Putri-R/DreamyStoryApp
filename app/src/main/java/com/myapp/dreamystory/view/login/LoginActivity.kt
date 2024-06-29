package com.myapp.dreamystory.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.myapp.dreamystory.view.ViewModelFactory
import com.myapp.dreamystory.data.pref.UserModel
import com.myapp.dreamystory.databinding.ActivityLoginBinding
import com.myapp.dreamystory.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.getLoginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill the data completely", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.responseSuccess.observe(this) { isSuccessful ->
            isSuccessful?.let {
                if (it) {
                    viewModel.apply {
                        responseLogin.observe(this@LoginActivity) { token ->
                            if (token != null) {
                                saveDataSession(UserModel(email, token, true))
                            }
                        }
                    }
                    AlertDialog.Builder(this).apply {
                        setTitle("Welcome!")
                        setMessage("Login Success")
                        setPositiveButton("OK") { _, _ ->
                            val intentStory =
                                Intent(context, MainActivity::class.java)
                            intentStory.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intentStory)
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    viewModel.responseMessage.observe(this@LoginActivity) { message ->
                        message?.let {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailBox = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordBox = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        val emailTogether = AnimatorSet().apply {
            playTogether(emailTextView, emailBox)
        }

        val passwordTogether = AnimatorSet().apply {
            playTogether(passwordTextView, passwordBox)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, emailTogether, passwordTogether, login)
            start()
        }
    }
}