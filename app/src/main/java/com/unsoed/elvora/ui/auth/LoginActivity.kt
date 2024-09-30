package com.unsoed.elvora.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.unsoed.elvora.MainActivity
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityLoginBinding
import com.unsoed.elvora.helper.ViewModelFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val compositeDisposable = CompositeDisposable()
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etInputEmail.editText?.text.toString().trim()
            val password = binding.etInputPassword.editText?.text.toString().trim()

            authViewModel.login(email, password).observe(this) {
                it?.let { data ->
                    when(data) {
                        is ApiResult.Loading -> {
                            binding.ltLoading.visibility = View.VISIBLE
                            binding.btnLogin.visibility = View.GONE
                        }

                        ApiResult.Empty -> {

                        }

                        is ApiResult.Error -> {
                            binding.ltLoading.visibility = View.GONE
                            binding.btnLogin.visibility = View.VISIBLE
                            Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(this, data.data, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        binding.btnDirectRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        val emailStream = RxTextView.textChanges(binding.ietLoginEmail)
            .skipInitialValue()
            .map { email ->
                email.isEmpty() || !email.contains("@")
            }

        val emailSubscription = emailStream.subscribe {
            showEmailAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.ietLoginPassword)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }

        val passwordSubscription = passwordStream.subscribe {
            showPasswordAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream
        ) { emailInvalid: Boolean, passwordInvalid: Boolean ->
            !emailInvalid && !passwordInvalid
        }

        val invalidFieldsStreamSubscription =invalidFieldsStream.subscribe { isValid ->
            binding.btnLogin.isEnabled = isValid
        }

        compositeDisposable.addAll(emailSubscription, passwordSubscription, invalidFieldsStreamSubscription)
    }

    private fun showEmailAlert(isNotValid: Boolean) {
        binding.etInputEmail.error = if (isNotValid) "Please enter a valid email address." else null
    }

    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.etInputPassword.error = if (isNotValid) "Please enter a valid password." else null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}