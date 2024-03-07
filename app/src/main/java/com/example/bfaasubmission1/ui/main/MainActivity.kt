package com.example.bfaasubmission1.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaasubmission1.R
import com.example.bfaasubmission1.databinding.ActivityMainBinding
import com.example.bfaasubmission1.ui.adapter.UserListAdapter

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.searchGithubUsers(textView.text.toString())
                    false
                }
        }

        showLoading()
        showMostFollowedUsers()
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, "Error: $errorMessage\nSilahkan refresh halaman", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMostFollowedUsers() {
        viewModel.githubUsers.observe(this) { users ->
            users?.let {
                binding.rvUsers.adapter = UserListAdapter(it)
                binding.rvUsers.addItemDecoration(
                    DividerItemDecoration(
                        baseContext,
                        LinearLayoutManager.VERTICAL,
                    ),
                )
            }
        }
    }
}
