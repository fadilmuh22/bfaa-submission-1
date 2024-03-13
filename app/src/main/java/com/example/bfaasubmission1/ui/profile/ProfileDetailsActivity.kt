package com.example.bfaasubmission1.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bfaasubmission1.R
import com.example.bfaasubmission1.data.Result
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.databinding.ActivityProfileDetailsBinding
import com.example.bfaasubmission1.ui.adapter.SectionsPagerAdapter
import com.example.bfaasubmission1.ui.main.ProfileDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ProfileDetailsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"

        @StringRes
        private val TAB_TITLES =
            intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2,
            )
    }

    private val viewModel: ProfileDetailsViewModel by viewModels {
        ProfileDetailsViewModelFactory.getInstance(application)
    }

    private val binding by lazy { ActivityProfileDetailsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.primaryDark)
        setSupportActionBar(binding.topAppBarProfile)

        val githubUser =
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_GITHUB_USER, GithubUser::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_GITHUB_USER)
            }

        githubUser?.let {
            setTitle("${it.login}'s details")
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            showUserView(it)
            showUserDetailsView(githubUser)
            setFavoriteFab(it)
            setShareUser(it)
        }

        binding.viewPager.adapter = SectionsPagerAdapter(this, githubUser)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.tvName.visibility = View.GONE
            binding.tvFollows.visibility = View.GONE
            binding.progressBarProfile.visibility = View.VISIBLE
        } else {
            binding.tvName.visibility = View.VISIBLE
            binding.tvFollows.visibility = View.VISIBLE
            binding.progressBarProfile.visibility = View.GONE
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(
            this,
            "${getString(R.string.error_title)}: $errorMessage\n${getString(R.string.error_refresh)}",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun showUserView(githubUser: GithubUser) {
        Glide.with(this)
            .load(githubUser.avatarUrl)
            .into(binding.imgProfile)

        binding.tvUsername.text = githubUser.login
    }

    private fun setShareUser(githubUser: GithubUser) {
        binding.btnShareUser.setOnClickListener {
            val shareText = "Check out this awesome developer @${githubUser.login}"
            val shareIntent =
                Intent.createChooser(
                    Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, githubUser.htmlUrl)
                        putExtra(Intent.EXTRA_TITLE, shareText)
                        type = "text/plain"
                    },
                    null,
                )
            startActivity(shareIntent)
        }
    }

    private fun showUserDetailsView(githubUser: GithubUser) {
        viewModel.getUserDetails(githubUser).observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    toggleLoading(false)
                    showErrorMessage(result.error)
                }

                Result.Loading -> {
                    toggleLoading(true)
                }

                is Result.Success -> {
                    toggleLoading(false)

                    if (result.data.name.isNullOrEmpty()) {
                        binding.tvName.visibility = View.GONE
                    }
                    result.data.name?.let {
                        binding.tvName.text = it
                    }

                    "${result.data.followers} followers · ${result.data.following} following".also {
                        binding.tvFollows.text = it
                    }
                }
            }
        }
    }

    private fun setFavoriteFab(githubUser: GithubUser) {
        viewModel.isFavorite(githubUser.id).observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }

            binding.fabFavorite.setOnClickListener {
                viewModel.setFavorite(githubUser, !isFavorite)
                setResult(RESULT_OK)
                Toast.makeText(
                    this,
                    getString(
                        when (isFavorite) {
                            true -> R.string.remove_favorite_success
                            false -> R.string.add_to_favorite_success
                        },
                    ),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}
