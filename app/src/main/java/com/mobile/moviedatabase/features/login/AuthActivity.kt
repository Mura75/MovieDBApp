package com.mobile.moviedatabase.features.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mobile.moviedatabase.R
import com.mobile.moviedatabase.features.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.ext.android.inject

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by inject()

    //todo temp solution for progress
    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isUserExist()) {
            openMenuPage()
        } else {
            setContentView(R.layout.activity_auth)
            setData()
            setListeners()
        }
    }

    private fun setData() {
        //todo for testing
        etLogin.setText("Mura")
        etPassword.setText("L6zwZSPZmB6EfZr")
    }

    private fun setListeners() {
        buttonLogin.setOnClickListener {
            viewModel.login(
                username = etLogin.text.toString(),
                password = etPassword.text.toString()
            )
        }

        viewModel.liveData.observe(this, Observer { result ->
            when(result) {
                is AuthViewModel.State.ShowLoading -> progressDialog.show()
                is AuthViewModel.State.HideLoading -> progressDialog.dismiss()
                is AuthViewModel.State.Login -> {
                    openMenuPage()
                }
                is AuthViewModel.State.Error -> {}
            }
        })
    }

    private fun openMenuPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
