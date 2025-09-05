package com.example.effectivemobile

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.effectivemobile.ui.theme.EffectiveMobileTheme

class MainActivity : AppCompatActivity() {

    private val authManager = object {
        fun isAuthorized(): Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (authManager.isAuthorized()) {
                showMainUi()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, LoginFragment())
                    .commit()
            }
        }

        supportFragmentManager.setFragmentResultListener("auth_result", this) { _, bundle ->
            val ok = bundle.getBoolean("success", false)
            if (ok) {
                showMainUi(clearBackStack = true)
            }
        }

        supportFragmentManager.setFragmentResultListener("bottom_nav", this) { _, bundle ->
            when (bundle.getString("dest")) {
                "home"    -> switchTo(CourseListFragment())
                "favourites"  -> switchTo(FavouritesFragment())
                "profile" -> switchTo(ProfileFragment())
            }
        }
    }

    private fun showMainUi(clearBackStack: Boolean = false) {
        if (clearBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, CourseListFragment())
            .commit()

        findViewById<View>(R.id.bottom_container).visibility = View.VISIBLE
        if (supportFragmentManager.findFragmentById(R.id.bottom_container) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_container, BottomMenuFragment())
                .commit()
        }
    }

    private fun switchTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, fragment)
            .commit()
    }
}
