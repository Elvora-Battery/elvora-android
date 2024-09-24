package com.unsoed.elvora.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.UserVerify
import com.unsoed.elvora.databinding.ActivityProfileBinding
import com.unsoed.elvora.helper.HomeModelFactory
import com.unsoed.elvora.ui.auth.LoginActivity
import com.unsoed.elvora.ui.home.HomeViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val homeViewModel: HomeViewModel by viewModels {
        HomeModelFactory.getInstance(this)
    }
    private var userModel: UserModel? = null
    private var userShippingModel: UserShippingModel? = null
    private var userVerify: UserVerify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnAbout.setOnClickListener {
            val intent = Intent(this@ProfileActivity, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnAccInfo.setOnClickListener {
            val intent = Intent(this@ProfileActivity, AccountInformationActivity::class.java)
            intent.putExtra(AccountInformationActivity.EXTRA_DATA_CONTACT, userModel)
            intent.putExtra(AccountInformationActivity.EXTRA_DATA_PERSONAL, userShippingModel)
            intent.putExtra(AccountInformationActivity.EXTRA_DATA_VERIFY, userVerify)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            homeViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        homeViewModel.getUserSession().observe(this) {
            it?.let { data ->
                userModel = data
            }
        }

        homeViewModel.getUserShipping().observe(this) {
            it?.let { data ->
                userShippingModel = data
            }
        }

        homeViewModel.getUserVerify().observe(this) {
            it?.let { data ->
                userVerify = data
            }
        }

        val dataProfile = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, UserModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        dataProfile?.let {
            binding.tvProfileName.text = dataProfile.fullName
        }

        binding.shapeableImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.avatar3))
        binding.tvTierMember.text = "Premium Member"
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}