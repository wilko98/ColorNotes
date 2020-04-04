package com.infinit.colornotes.utils

import android.content.Context
import android.content.SharedPreferences
import com.infinit.colornotes.model.Credentials

class PrefManager(private val context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SHdSdjR3yuud34D", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        sharedPreferences.edit().putString("Token",token).apply()
    }

    fun getToken():String{
        return sharedPreferences.getString("Token","")?:""
    }

    fun saveCredentials(credentials: Credentials){
        sharedPreferences.edit().putString("CredentialsLogin", credentials.login).apply()
        sharedPreferences.edit().putString("CredentialsPassword", credentials.password).apply()
    }

    fun getCredentials(): Credentials{
        var credentials = Credentials(sharedPreferences.getString("CredentialsLogin",""),
            sharedPreferences.getString("CredentialsPassword",""))
        return credentials
    }

}