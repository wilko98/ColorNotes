package com.infinit.colornotes.Login

import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.infinit.colornotes.R
import com.infinit.colornotes.model.Credentials
import com.infinit.colornotes.ui.main.MainActivity
import com.infinit.colornotes.ui.main.MainViewModel
import com.infinit.colornotes.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : Fragment(),AuthCallBack {

    companion object{
        fun newInstance():LoginFragment{
            return LoginFragment()
        }
    }

    private val viewModel: MainViewModel by inject()
    private val prefManager: PrefManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (FingerprintManagerCompat.from(requireContext()).hasEnrolledFingerprints()) {
            imgFinger.visibility = View.VISIBLE
        } else imgFinger.visibility = View.GONE

        imgFinger.setOnClickListener {
            showFingerprintDialog()
        }

        loginButton.setOnClickListener {
            viewModel.login(Credentials( emailAddressEditText.text.toString(),passwordEditText.text.toString()))
            loginButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        btnRegistration.setOnClickListener {
            (activity as LoginActivity).showRegistration()
        }

        viewModel.loginResponseLiveData.observe(this, Observer {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            if(it.exists == false){
                showMessage("User with given login and password doesn't exist")
            }else if (it.exists == true){
                Toast.makeText(activity,it.token, Toast.LENGTH_SHORT).show()
                prefManager.saveCredentials(Credentials(emailAddressEditText.text.toString(),passwordEditText.text.toString()))
                prefManager.saveToken(it.token?:"")
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

    fun showFingerprintDialog(){
        val fingerprintDialog = FingerprintDialog()
        fingerprintDialog.show(activity?.supportFragmentManager ?: return,"Hey")
        fingerprintDialog.authCallBack = this
    }

    fun showMessage(message:String){
        Snackbar.make(constraintLayout,message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        viewModel.login(prefManager.getCredentials())
    }

    override fun onError() {
        showMessage("Fingerprints not match")
    }
}