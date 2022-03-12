package com.grigorenko.yourchance.auth.tablayout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.grigorenko.yourchance.auth.tablayout.fragments.SignInFragment
import com.grigorenko.yourchance.auth.tablayout.fragments.SignUpFragment
import com.grigorenko.yourchance.database.repo.FirebaseAuthRepo.Companion.firebaseAuth

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignInFragment(firebaseAuth.currentUser)
            1 -> SignUpFragment()
            else -> Fragment()
        }
    }
}