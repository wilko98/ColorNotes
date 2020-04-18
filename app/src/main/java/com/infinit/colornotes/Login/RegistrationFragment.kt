package com.infinit.colornotes.Login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.infinit.colornotes.R
import com.infinit.colornotes.model.Credentials
import com.infinit.colornotes.MainScreen.MainActivity
import com.infinit.colornotes.MainScreen.MainViewModel
import com.infinit.colornotes.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_login.btnRegistration
import kotlinx.android.synthetic.main.fragment_login.emailAddressEditText
import kotlinx.android.synthetic.main.fragment_registration.*
import org.koin.android.ext.android.inject

class RegistrationFragment : Fragment() {

    val viewModel: MainViewModel by inject()
    val prefManager:PrefManager by inject()

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_registration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnRegistration.setOnClickListener {
            if (password1EditText.text.toString().equals(password2EditText.text.toString())) {
                viewModel.register(
                    Credentials(
                        emailAddressEditText.text.toString(),
                        password1EditText.text.toString()
                    )
                )
            } else {
                password1EditText.error = "passwords don't match"
            }
        }
        viewModel.registerResponseLiveData.observe(this, Observer {
            prefManager.saveToken(it.token?:"")
            if(!it.token.isNullOrEmpty()){
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }else {
                showMessage("User already exists")
            }
        })
    }

    fun showMessage(message:String){
        Snackbar.make(parentLayout,message, Snackbar.LENGTH_SHORT).show()
    }
}