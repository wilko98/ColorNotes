package com.infinit.colornotes.Login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.infinit.colornotes.BuildConfig
import com.infinit.colornotes.R
import com.infinit.colornotes.base.OnlineUsersWebSocketListener
import com.infinit.colornotes.base.OnlineUsersWebSocketView
import com.infinit.colornotes.model.Credentials
import com.infinit.colornotes.MainScreen.MainActivity
import com.infinit.colornotes.MainScreen.MainViewModel
import com.infinit.colornotes.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.koin.android.ext.android.inject

class LoginFragment : Fragment(),AuthCallBack,OnlineUsersWebSocketView {

    companion object{
        fun newInstance():LoginFragment{
            return LoginFragment()
        }
    }

    private val viewModel: MainViewModel by inject()
    private val prefManager: PrefManager by inject()
    private val client: OkHttpClient by inject()

    private lateinit var echoWebSocketListener :OnlineUsersWebSocketListener
    private lateinit var ws: WebSocket

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        echoWebSocketListener = OnlineUsersWebSocketListener(this)
        startWebSocket()

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

        webSocketButton.setOnClickListener {
            ws.send(socketET.text.toString())
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

    fun startWebSocket(){
        val request = Request.Builder().url("${BuildConfig.API_URL}api/usersOnline").build()
        ws = client.newWebSocket(request, echoWebSocketListener)
        client.dispatcher.executorService.shutdown()
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

    override fun showOnlineUsers(text: String) {
        activity?.runOnUiThread {
            onlineUsersTextView.text = text
        }
    }
}