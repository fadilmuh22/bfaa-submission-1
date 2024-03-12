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
import com.example.bfaasubmission1.data.Result
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.ui.adapter.Section
import com.example.bfaasubmission1.ui.adapter.UserListAdapter

class ProfileFollowsFragment(
    private val section: Section,
    private val githubUser: GithubUser?,
) : Fragment() {
    private val viewModel: ProfileFollowsViewModel by viewModels {
        ProfileFollowsViewModelFactory.getInstance()
    }

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

        val userListAdapter = UserListAdapter()

        githubUser?.let {
            viewModel.run {
                showFollows(view, it, userListAdapter)
            }
        }

        bindRecyclerView(view, userListAdapter)
    }

    private fun toggleLoading(
        view: View,
        isLoading: Boolean,
    ) {
        val rvUsers: RecyclerView = view.findViewById(R.id.rvFollows)
        val progressBar: View = view.findViewById(R.id.progressBarFollows)
        if (isLoading) {
            rvUsers.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            rvUsers.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(
            requireContext(),
            "${getString(R.string.error_title)}: $errorMessage\n${getString(R.string.error_refresh)}",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun toggleEmptyList(
        view: View,
        isEmpty: Boolean,
    ) {
        val rvUsers: RecyclerView = view.findViewById(R.id.rvFollows)
        val tvNoFollows: TextView = view.findViewById(R.id.tvNoFollows)

        if (isEmpty) {
            rvUsers.visibility = View.GONE
            tvNoFollows.visibility = View.VISIBLE
        } else {
            rvUsers.visibility = View.VISIBLE
            tvNoFollows.visibility = View.GONE
        }
    }

    private fun showFollows(
        view: View,
        githubUser: GithubUser?,
        userListAdapter: UserListAdapter,
    ) {
        val followsLiveData =
            when (section) {
                Section.FOLLOWERS -> viewModel.getFollowers(githubUser)
                Section.FOLLOWING -> viewModel.getFollowing(githubUser)
            }

        followsLiveData.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is Result.Error -> {
                        toggleLoading(view, false)
                        showErrorMessage(it.error)
                    }
                    Result.Loading -> {
                        toggleLoading(view, true)
                    }
                    is Result.Success -> {
                        toggleLoading(view, false)
                        toggleEmptyList(view, it.data.isEmpty())
                        if (it.data.isNotEmpty()) {
                            userListAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun bindRecyclerView(
        view: View,
        userListAdapter: UserListAdapter,
    ) {
        val rvUsers: RecyclerView = view.findViewById(R.id.rvFollows)

        rvUsers.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                ),
            )
            adapter = userListAdapter
        }
    }
}
