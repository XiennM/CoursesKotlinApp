package com.example.effectivemobile

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomMenuFragment : Fragment(R.layout.fragment_bottom_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bottom = view.findViewById<BottomNavigationView>(R.id.bottom_menu)

        // Выставим дефолтный пункт
        if (savedInstanceState == null) bottom.selectedItemId = R.id.menu_home

        bottom.setOnItemSelectedListener { item ->
            val dest = when (item.itemId) {
                R.id.menu_home -> "home"
                R.id.menu_favourites-> "favourites"
                R.id.menu_profile -> "profile"
                else -> return@setOnItemSelectedListener false
            }
            parentFragmentManager.setFragmentResult(
                "bottom_nav",
                bundleOf("dest" to dest)
            )
            true
        }
    }
}
