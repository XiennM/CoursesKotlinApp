package com.example.effectivemobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etEmail  = view.findViewById<EditText>(R.id.et_email)
        val etPass   = view.findViewById<EditText>(R.id.et_password)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnVK = view.findViewById<ShapeableImageView>(R.id.btn_vk)
        val btnOK = view.findViewById<ShapeableImageView>(R.id.btn_ok)

        etEmail.doAfterTextChanged { viewModel.onEmailChanged(it?.toString().orEmpty()) }
        etPass.doAfterTextChanged  { viewModel.onPassChanged(it?.toString().orEmpty()) }

        etPass.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onLoginClick()
                true
            } else false
        }

        btnLogin.setOnClickListener { viewModel.onLoginClick() }

        btnVK.setOnClickListener {
            openWebsite( "https://vk.com/")
        }

        btnOK.setOnClickListener {
            openWebsite("https://ok.ru/")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { s ->
                    btnLogin.isEnabled = s.isButtonEnabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is LoginEvent.Success -> {
                            parentFragmentManager.setFragmentResult(
                                "auth_result",
                                bundleOf("success" to true)
                            )
                        }
                    }
                }
            }
        }
    }

    fun onVkClick() {

    }

    private fun openWebsite(url: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}