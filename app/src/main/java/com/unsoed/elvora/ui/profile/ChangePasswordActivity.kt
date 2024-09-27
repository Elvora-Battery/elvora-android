package com.unsoed.elvora.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityChangePasswordBinding
import com.unsoed.elvora.helper.HomeModelFactory
import com.unsoed.elvora.ui.home.HomeViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val compositeDisposable = CompositeDisposable()
    private val homeViewModel: HomeViewModel by viewModels {
        HomeModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        binding.btnChangePassword.setOnClickListener {
            val currentPassword = binding.etInputCurrentPassword.editText?.text.toString().trim()
            val newPassword = binding.etInputNewPassword.editText?.text.toString().trim()
            val confirmPassword = binding.etInputConfirmPassword.editText?.text.toString().trim()

            homeViewModel.changePassword(currentPassword, newPassword, confirmPassword).observe(this) {
                it?.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            binding.ltLoading.visibility = View.VISIBLE
                            binding.btnChangePassword.visibility = View.GONE
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(this, response.data, Toast.LENGTH_SHORT).show()
                            onBackPressedDispatcher.onBackPressed()
                            finish()
                        }

                        is ApiResult.Error -> {
                            binding.ltLoading.visibility = View.GONE
                            binding.btnChangePassword.visibility = View.VISIBLE
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        }

                        ApiResult.Empty -> {}
                    }
                }
            }
        }
    }

    private fun initView() {
        val passwordStream = RxTextView.textChanges(binding.ietNewPassword)
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
                    passwordConfirmation.toString() != binding.ietNewPassword.text.toString()
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
            binding.btnChangePassword.isEnabled = isValid
        }

        compositeDisposable.addAll(passwordSubscription, passwordConfirmationSubscription, invalidFieldsStreamSubscription)
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.etInputConfirmPassword.isEndIconVisible = true
        binding.etInputConfirmPassword.error = if (isNotValid) "Password must be same" else null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.etInputNewPassword.isEndIconVisible = true
        binding.etInputNewPassword.error = if (isNotValid) "Please enter a valid password." else null
    }
}