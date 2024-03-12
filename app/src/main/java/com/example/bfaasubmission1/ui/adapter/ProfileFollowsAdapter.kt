package com.example.bfaasubmission1.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.ui.profile.ProfileFollowsFragment

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
        return when (Section.entries[position]) {
            Section.FOLLOWERS -> ProfileFollowsFragment(Section.entries[position], githubUser)
            Section.FOLLOWING -> ProfileFollowsFragment(Section.entries[position], githubUser)
        }
    }
}
