package com.unsoed.elvora

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.MaterialShapeDrawable
import com.unsoed.elvora.databinding.ActivityMainBinding
import com.unsoed.elvora.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, -20)
            insets
        }
        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.fabMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        val topEdge = BottomAppBarTopEdgeTreatment(
            binding.bottomAppBar.fabCradleMargin,
            binding.bottomAppBar.fabCradleRoundedCornerRadius,
            binding.bottomAppBar.cradleVerticalOffset
        )
        val background = binding.bottomAppBar.background as MaterialShapeDrawable
        background.shapeAppearanceModel = background.shapeAppearanceModel.toBuilder().setTopEdge(topEdge).build()

        navView.setupWithNavController(navController)
        navView.background = null
    }
}