package com.example.bfaasubmission1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaasubmission1.R
import com.example.bfaasubmission1.data.Result
import com.example.bfaasubmission1.data.repository.dataStore
import com.example.bfaasubmission1.databinding.ActivityMainBinding
import com.example.bfaasubmission1.ui.adapter.UserListAdapter
import com.example.bfaasubmission1.ui.favorite.FavoriteUsersActivity
import com.example.bfaasubmission1.utils.Validator

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(application.dataStore)
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userListAdapter = UserListAdapter()

        setSearchBar(userListAdapter)

        setDarkModeMenu()

        showMostFollowedUsers(userListAdapter)

        setRecyclerView(userListAdapter)

        setOptionMenu()
    }

    private fun setSearchBar(userListAdapter: UserListAdapter) {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    val searchText = textView.text.toString().trim()
                    searchBar.setText(searchText)
                    searchView.hide()

                    if (!Validator.isSearchTextValid(searchText)) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.search_validation),
                            Toast.LENGTH_SHORT,
                        ).show()
                        return@setOnEditorActionListener false
                    }

                    showSearchUsers(searchText, userListAdapter)
                    false
                }
        }
    }

    private fun setDarkModeMenu() {
        viewModel.getThemeSettings().observe(this) {
            if (it) {
                binding.topAppBarMain.menu.findItem(R.id.action_theme)
                    .setIcon(R.drawable.baseline_dark_mode_24)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                binding.topAppBarMain.menu.findItem(R.id.action_theme)
                    .setIcon(R.drawable.baseline_brightness_high_24)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setOptionMenu() {
        binding.topAppBarMain.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favorite -> {
                    val intent = Intent(this, FavoriteUsersActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.action_theme -> {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        viewModel.saveThemeSetting(false)
                    } else {
                        viewModel.saveThemeSetting(true)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun toggleLoading(visible: Int) {
        binding.progressBarMain.visibility = visible
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(
            this,
            "${getString(R.string.error_title)}: $errorMessage\n${getString(R.string.error_refresh)}",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun showMostFollowedUsers(userListAdapter: UserListAdapter) {
        viewModel.getMostFollowedGithubUsers().observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    toggleLoading(View.GONE)
                    showErrorMessage(result.error)
                }

                Result.Loading -> {
                    toggleLoading(View.VISIBLE)
                }

                is Result.Success -> {
                    toggleLoading(View.GONE)
                    userListAdapter.submitList(result.data.items)
                }
            }
        }
    }

    private fun showSearchUsers(
        searchText: String,
        userListAdapter: UserListAdapter,
    ) {
        viewModel.searchGithubUsers(searchText).observe(this) { result ->
            when (result) {
                Result.Loading -> {
                    toggleLoading(View.VISIBLE)
                }

                is Result.Success -> {
                    toggleLoading(View.GONE)
                    userListAdapter.submitList(result.data.items)
                }

                is Result.Error -> {
                    toggleLoading(View.GONE)
                    showErrorMessage(result.error)
                }
            }
        }
    }

    private fun setRecyclerView(userListAdapter: UserListAdapter) {
        binding.rvPopularUsers.apply {
            addItemDecoration(
                DividerItemDecoration(
                    baseContext,
                    LinearLayoutManager.VERTICAL,
                ),
            )
            adapter = userListAdapter
        }
    }
}
