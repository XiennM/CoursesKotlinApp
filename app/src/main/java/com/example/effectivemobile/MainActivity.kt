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

    // эмуляция менеджера авторизации
    private val authManager = object {
        fun isAuthorized(): Boolean = false // подставь реальную проверку, например токен в SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (authManager.isAuthorized()) {
                showMainUi()
            } else {
                // Показать экран входа, меню не добавляем
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, LoginFragment())
                    .commit()
            }
        }

        // ждём результат от LoginFragment
        supportFragmentManager.setFragmentResultListener("auth_result", this) { _, bundle ->
            val ok = bundle.getBoolean("success", false)
            if (ok) {
                showMainUi(clearBackStack = true)
            }
        }

        // слушаем клики нижнего меню (как делали раньше)
        supportFragmentManager.setFragmentResultListener("bottom_nav", this) { _, bundle ->
            when (bundle.getString("dest")) {
                "home"    -> switchTo(CourseListFragment())
                "favourites"  -> switchTo(FavouriteCoursesListFragment())
                "profile" -> switchTo(CourseListFragment())
            }
        }
    }

    /** Показываем основной экран и подключаем меню */
    private fun showMainUi(clearBackStack: Boolean = false) {
        if (clearBackStack) {
            // очищаем back stack, чтобы кнопка «назад» не вела на логин
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // основной экран
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, CourseListFragment())
            .commit()

        // показать контейнер и вставить фрагмент меню (если ещё не вставлен)
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
