package com.unsoed.elvora.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivitySetPasswordBinding
import com.unsoed.elvora.helper.ViewModelFactory
import com.unsoed.elvora.ui.verification.CompleteVerificationActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class SetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetPasswordBinding
    private val compositeDisposable = CompositeDisposable()
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra(EMAIL)

        initView()
        setupAction(email = email!!)
    }

    private fun setupAction(email: String) {
        binding.btnSetPassword.setOnClickListener {
            val password = binding.etInputPassword.editText?.text.toString().trim()
            val confirmPassword = binding.etInputPassword.editText?.text.toString().trim()

            authViewModel.setPassword(email = email, password = password, confirmationPassword = confirmPassword).observe(this) {
                it?.let { data ->
                    when(data) {
                        is ApiResult.Loading -> {

                        }

                        ApiResult.Empty -> {

                        }

                        is ApiResult.Error -> {
                            Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(this, "Set Password success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SetPasswordActivity, CompleteVerificationActivity::class.java)
                            intent.putExtra(CompleteVerificationActivity.STATE, "password")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        val passwordStream = RxTextView.textChanges(binding.ietPassword)
            .skipInitialValue()
            .map { password ->
                val containsUpperCase = password.any { it.isUpperCase() }
                val containsSymbol = password.any { !it.isLetterOrDigit() }
                val isEmpty = password.isEmpty()

                isEmpty || !containsUpperCase || !containsSymbol
            }

        val passwordSubscription = passwordStream.subscribe {
            showPasswordAlert(it)
        }

        val passwordConfirmationStream =
            RxTextView.textChanges(binding.ietConfirmPassword)
                .skipInitialValue()
                .map { passwordConfirmation->
                    passwordConfirmation.toString() != binding.ietPassword.text.toString()
                }

        val passwordConfirmationSubscription = passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            passwordStream,
            passwordConfirmationStream)
        { passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
            !passwordInvalid && !passwordConfirmationInvalid
        }

        val invalidFieldsStreamSubscription = invalidFieldsStream.subscribe { isValid ->
            binding.btnSetPassword.isEnabled = isValid
        }

        compositeDisposable.addAll(passwordSubscription, passwordConfirmationSubscription, invalidFieldsStreamSubscription)
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.etInputConfirmPassword.isEndIconVisible = true
        binding.etInputConfirmPassword.error = if (isNotValid) "Password must be same" else null
    }

    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.etInputPassword.isEndIconVisible = true
        binding.etInputPassword.error = if (isNotValid) "Please enter a valid password." else null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        const val EMAIL = "email"
    }
}