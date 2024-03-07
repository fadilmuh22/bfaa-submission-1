package com.example.bfaasubmission1.ui.profile

import android.os.Bundle
import android.util.Log
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
import com.example.bfaasubmission1.ui.adapter.UserListAdapter

class ProfileFollowersFragment(
    private val githubUser: GithubUser?,
) : Fragment() {
    private val viewModel: ProfileFollowersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)

        return inflater.inflate(R.layout.fragment_profile_followers, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        githubUser?.let {
            viewModel.run {
                getFollowers(it)
            }
        }
        showLoading(view)
        showErrorMessage()
        showFollowers(view)
    }

    private fun showLoading(view: View) {
        val progressBar: View = view.findViewById(R.id.progressBarFollowers)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(activity, "Error: $errorMessage\nSilahkan refresh halaman", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showFollowers(view: View) {
        val rvUsers: RecyclerView = view.findViewById(R.id.rvUsers)
        val tvNoFollows: TextView = view.findViewById(R.id.tvNoFollows)

        viewModel.followers.observe(viewLifecycleOwner) { users ->
            Log.d("ProfileFollowersFragment", "showFollowers: $users")

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
            }
        }
    }
}
