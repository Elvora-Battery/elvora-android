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
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityRegisterBinding
import com.unsoed.elvora.helper.ViewModelFactory
import com.unsoed.elvora.ui.help.TnCActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val compositeDisposable = CompositeDisposable()
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRegister()
        initView()
    }

    private fun initView() {
        val emailStream = RxTextView.textChanges(binding.ietRegisterEmail)
            .skipInitialValue()
            .map { email ->
                email.isEmpty() || !email.contains("@")
            }

        val emailSubscription = emailStream.subscribe {
            showEmailAlert(it)
        }

        val fullNameStream = RxTextView.textChanges(binding.ietRegisterName)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }

        val fullNameSubscription = fullNameStream.subscribe {
            showFullNameAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            fullNameStream
        ) { emailInvalid: Boolean, fullNameInvalid: Boolean ->
            !emailInvalid && !fullNameInvalid
        }

        val invalidFieldsStreamSubscription =invalidFieldsStream.subscribe { isValid ->
            binding.btnRegister.isEnabled = isValid
        }

        compositeDisposable.addAll(emailSubscription, fullNameSubscription, invalidFieldsStreamSubscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun showFullNameAlert(isNotValid: Boolean) {
        binding.etInputName.error = if (isNotValid) "Please enter a value for the name field." else null
    }

    private fun showEmailAlert(isNotValid: Boolean) {
        binding.etInputRegisterEmail.error = if (isNotValid) "Please enter a valid email address." else null
    }

    private fun setupRegister() {
        binding.tvTnc.setOnClickListener {
            val intent = Intent(this@RegisterActivity, TnCActivity::class.java)
            startActivity(intent)
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show()
        }

        binding.btnRegister.setOnClickListener {
            if(binding.cbTerms.isChecked) {
                val email = binding.etInputRegisterEmail.editText?.text.toString().trim()
                val fullName = binding.etInputName.editText?.text.toString().trim()

                authViewModel.createAccount(email, fullName).observe(this) {
                    it?.let { data ->
                        when(data) {
                            is ApiResult.Loading -> {
                                binding.btnRegister.visibility = View.GONE
                                binding.ltLoading.visibility = View.VISIBLE
                            }

                            ApiResult.Empty -> {

                            }

                            is ApiResult.Error -> {
                                binding.btnRegister.visibility = View.VISIBLE
                                binding.ltLoading.visibility = View.GONE
                                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                            }

                            is ApiResult.Success -> {
                                Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@RegisterActivity, VerifyEmailActivity::class.java)
                                intent.putExtra(VerifyEmailActivity.EMAIL_REGISTER, data.data.email)
                                startActivity(intent)

                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Accept the Terms of Service and the Privacy Policy", Toast.LENGTH_SHORT).show()
            }
        }
    }
}