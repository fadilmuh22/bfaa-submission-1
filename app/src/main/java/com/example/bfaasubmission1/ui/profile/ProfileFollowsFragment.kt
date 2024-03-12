package com.example.bfaasubmission1.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bfaasubmission1.R
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.ui.adapter.Section
import com.example.bfaasubmission1.ui.adapter.UserListAdapter

class ProfileFollowsFragment(
    private val section: Section,
    private val githubUser: GithubUser?,
) : Fragment() {
    private val viewModel by viewModels<ProfileFollowsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)

        return inflater.inflate(R.layout.fragment_profile_follows, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        githubUser?.let {
            viewModel.run {
                when (section) {
                    Section.FOLLOWERS -> getFollowers(it)
                    Section.FOLLOWING -> getFollowing(it)
                }
            }
        }
        showLoading(view)
        showErrorMessage()
        showFollows(view)
    }

    private fun showLoading(view: View) {
        val rvUsers: RecyclerView = view.findViewById(R.id.rvFollows)
        val progressBar: View = view.findViewById(R.id.progressBarFollows)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                rvUsers.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
                rvUsers.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(
                    activity,
                    "Error: $errorMessage\nSilahkan refresh halaman",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun showFollows(view: View) {
        val rvUsers: RecyclerView = view.findViewById(R.id.rvFollows)
        val tvNoFollows: TextView = view.findViewById(R.id.tvNoFollows)

        val followsLiveData =
            when (section) {
                Section.FOLLOWERS -> viewModel.followers
                Section.FOLLOWING -> viewModel.following
            }

        followsLiveData.observe(viewLifecycleOwner) { users ->
            users?.let {
                if (it.isEmpty()) {
                    rvUsers.visibility = View.GONE
                    tvNoFollows.visibility = View.VISIBLE
                } else {
                    rvUsers.visibility = View.VISIBLE
                    tvNoFollows.visibility = View.GONE
                }

                rvUsers.adapter = UserListAdapter(it)
                rvUsers.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                    ),
                )

                if (rvUsers.adapter != null) {
                    rvUsers.invalidate()
                }
            }
        }
    }
}
