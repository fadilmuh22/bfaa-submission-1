package com.example.bfaasubmission1.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.ui.profile.ProfileFollowersFragment
import com.example.bfaasubmission1.ui.profile.ProfileFollowingFragment

enum class Section {
    FOLLOWERS,
    FOLLOWING,
}

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    private val githubUser: GithubUser?,
) : FragmentStateAdapter(
        activity,
    ) {
    companion object {
        const val PAGER_SIZE = 2
    }

    override fun getItemCount(): Int {
        return PAGER_SIZE
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ProfileFollowersFragment(githubUser)
            1 -> fragment = ProfileFollowingFragment(githubUser)
        }
        return fragment as Fragment
    }
}
