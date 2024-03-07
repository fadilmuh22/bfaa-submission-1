package com.example.bfaasubmission1.ui.profile

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bfaasubmission1.R
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.data.response.GithubUserDetails
import com.example.bfaasubmission1.databinding.ActivityProfileDetailsBinding
import com.example.bfaasubmission1.ui.adapter.SectionsPagerAdapter
import com.example.bfaasubmission1.ui.main.ProfileViewModel
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

    private val viewModel by viewModels<ProfileViewModel>()
    private val binding by lazy { ActivityProfileDetailsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

            viewModel.run {
                setGithubUser(it)
                getUserDetails(it)
            }
            showUserView(it)
        }

        showLoading()
        showErrorMessage()

        viewModel.githubUserDetails.observe(this) { githubUserDetails ->
            githubUserDetails?.let {
                showUserDetailsView(it)
            }
        }

        binding.viewPager.adapter = SectionsPagerAdapter(this, githubUser)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) { isLoading ->
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
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, "Error: $errorMessage\nSilahkan refresh halaman", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUserView(githubUser: GithubUser) {
        Glide.with(this)
            .load(githubUser.avatarUrl)
            .into(binding.imgProfile)

        binding.tvUsername.text = githubUser.login
    }

    private fun showUserDetailsView(githubUserDetails: GithubUserDetails) {
        if (githubUserDetails.name.isNullOrEmpty()) {
            binding.tvName.visibility = View.GONE
        }
        githubUserDetails.name?.let {
            binding.tvName.text = it
        }

        "${githubUserDetails.followers} followers · ${githubUserDetails.following} following".also {
            binding.tvFollows.text = it
        }
    }
}
