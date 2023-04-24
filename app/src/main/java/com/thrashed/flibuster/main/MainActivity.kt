package com.thrashed.flibuster.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.thrashed.flibuster.R
import com.thrashed.flibuster.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val model: MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.findNavController()

        model.showOnboarding.observe(this) {
            if (it) {
                navController.setGraph(R.navigation.main_graph)
            } else {
                navController.setGraph(R.navigation.onboarding_graph)
            }
        }

        binding.bnvMain.apply {

            binding.bnvMain.setupWithNavController(navController)

            setOnItemReselectedListener { menuItem ->
                if (menuItem.itemId == navController.currentDestination?.id) {
                    model.setUpCommand()
                } else {
                    navController.popBackStack(menuItem.itemId, false)
                }
            }

        }

        lifecycleScope.launchWhenResumed {
            model.bottomNavVisibility.observe(this@MainActivity) {
                binding.bnvMain.isVisible = it
            }
        }

        model.initOnboarding()
    }
}