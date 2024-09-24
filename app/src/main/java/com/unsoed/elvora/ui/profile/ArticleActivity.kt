package com.unsoed.elvora.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        binding.btnLike.setOnClickListener {
            Toast.makeText(this, "Thank you for your interest in this article.", Toast.LENGTH_SHORT).show()
        }

        binding.btnDislike.setOnClickListener {
            Toast.makeText(this, "Thank you for your feedback in this article.", Toast.LENGTH_SHORT).show()
        }

        binding.btnRelatedArticle1.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }

        binding.btnRelatedArticle2.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }

        binding.btnRelatedArticle3.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
    }
}