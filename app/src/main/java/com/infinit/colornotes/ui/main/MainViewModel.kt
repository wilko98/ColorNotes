package com.infinit.colornotes.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinit.colornotes.di.Api
import com.infinit.colornotes.di.Value
import com.infinit.colornotes.di.idModel
import com.infinit.colornotes.model.Credentials
import com.infinit.colornotes.model.LoginResponse
import com.infinit.colornotes.model.Note
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(var api: Api) : ViewModel() {
    val errorLiveData = MutableLiveData<String>()
    val resultLiveData = MutableLiveData<List<Note>>()
    val resultValueLiveData = MutableLiveData<Boolean>()
    val loginResponseLiveData = MutableLiveData<LoginResponse>()
    val registerResponseLiveData = MutableLiveData<LoginResponse>()

    fun getNotes() {
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    resultLiveData.postValue(api.getNotes().await().body())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    errorLiveData.postValue(e.localizedMessage)
                }
            }
        }
    }

    fun createNote(note: Note) {
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    resultValueLiveData.postValue(api.createNote(note).await().isSuccessful)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    errorLiveData.postValue(e.localizedMessage)
                }
            }
        }
    }

    fun deleteNote(id: Int){
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    resultValueLiveData.postValue(api.deleteNote(idModel(id)).await().isSuccessful)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    errorLiveData.postValue(e.localizedMessage)
                }
            }
        }
    }

    fun login(credentials: Credentials){
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    loginResponseLiveData.postValue(api.login(credentials).await().body())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    errorLiveData.postValue(e.localizedMessage)
                }
            }
        }
    }

    fun register(credentials: Credentials){
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    registerResponseLiveData.postValue(api.register(credentials).await().body())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    errorLiveData.postValue(e.localizedMessage)
                }
            }
        }
    }

}
