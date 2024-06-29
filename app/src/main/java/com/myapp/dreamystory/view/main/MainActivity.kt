package com.myapp.dreamystory.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.dreamystory.R
import com.myapp.dreamystory.databinding.ActivityMainBinding
import com.myapp.dreamystory.view.ViewModelFactory
import com.myapp.dreamystory.view.adapter.ListStoryAdapter
import com.myapp.dreamystory.view.adapter.LoadingStateAdapter
import com.myapp.dreamystory.view.addstory.AddStoryActivity
import com.myapp.dreamystory.view.maps.MapsActivity
import com.myapp.dreamystory.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        setListStory()

        setupView()
        setupAction()
    }

    private fun setListStory() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager

        val adapter = ListStoryAdapter()

        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.getDataSession().observe(this) {user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getListStory().observe(this) {
                    showLoading()
                    adapter.submitData(lifecycle, it)
                }
            }
        }

        viewModel.getListStory().observe(this) { storiesData ->
            if (storiesData == null) {
                Toast.makeText(this, "Failed fetch stories", Toast.LENGTH_SHORT).show()
            }
        }
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
    }

    private fun setupAction() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.userLogout()
                true
            }

            R.id.action_open_maps -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                true
            }
            else -> true
        }
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}