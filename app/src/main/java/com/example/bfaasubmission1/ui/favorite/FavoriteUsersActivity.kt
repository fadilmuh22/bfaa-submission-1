package com.example.bfaasubmission1.ui.favorite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.databinding.ActivityFavoriteUsersBinding
import com.example.bfaasubmission1.ui.adapter.UserListAdapter

class FavoriteUsersActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFavoriteUsersBinding.inflate(layoutInflater)
    }

    private val viewModel: FavoriteUsersViewModel by viewModels {
        FavoriteUsersViewModelFactory.getInstance(application)
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

        setSupportActionBar(binding.topAppBarFavorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userListAdapter = UserListAdapter()

        showFavoriteUsers(userListAdapter)

        bindRecyclerView(userListAdapter)
    }

    private fun showFavoriteUsers(userListAdapter: UserListAdapter) {
        viewModel.getAllFavoriteUsers().observe(this) { favoriteUsers ->
            userListAdapter.submitList(
                favoriteUsers.map {
                    GithubUser.fromFavoriteUserEntity(it)
                },
            )
        }
    }

    private fun bindRecyclerView(userListAdapter: UserListAdapter) {
        binding.rvFavoriteUsers.apply {
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
