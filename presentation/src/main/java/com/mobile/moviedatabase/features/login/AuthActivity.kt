package com.mobile.moviedatabase.features.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mobile.moviedatabase.R
import com.mobile.moviedatabase.features.MainActivity
import dagger.android.DispatchingAndroidInjector
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)
    }

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

   // override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

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
