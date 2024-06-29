package com.myapp.dreamystory.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.myapp.dreamystory.databinding.ActivitySignupBinding
import com.myapp.dreamystory.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.getSignupUser(name, email, password)
            } else {
                Toast.makeText(this, "Please fill the data completely", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.responseSuccess.observe(this) {isSuccessful ->
            isSuccessful?.let {
                if (it) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Your account registered successfully. Now, login to start!")
                        setPositiveButton("OK") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    viewModel.responseMessage.observe(this) { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameBox = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailBox = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordBox = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val nameTogether = AnimatorSet().apply {
            playTogether(nameTextView, nameBox)
        }

        val emailTogether = AnimatorSet().apply {
            playTogether(emailTextView, emailBox)
        }

        val passwordTogether = AnimatorSet().apply {
            playTogether(passwordTextView, passwordBox)
        }

        AnimatorSet().apply {
            playSequentially(title, nameTogether, emailTogether, passwordTogether, signup)
            start()
        }
    }
}