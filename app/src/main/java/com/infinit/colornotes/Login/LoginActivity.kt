package com.infinit.colornotes.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.infinit.colornotes.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        showLogin()
    }

    fun showMessage(message: String) {
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLogin(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance()).commit()
    }

    fun showRegistration(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, RegistrationFragment.newInstance()).commit()
    }
}