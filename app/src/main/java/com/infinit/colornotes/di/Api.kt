package com.infinit.colornotes.di

import com.infinit.colornotes.model.Credentials
import com.infinit.colornotes.model.LoginResponse
import com.infinit.colornotes.model.Note
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @GET("api/getNote")
    fun getNotes(): Deferred<Response<List<Note>>>

    @POST("api/createNote")
    fun createNote(@Body note: Note): Deferred<Response<Void>>

    @POST("api/deleteNoteById")
    fun deleteNote(@Body id: idModel): Deferred<Response<Void>>

    @POST("api/logIn")
    fun login(@Body credentials: Credentials): Deferred<Response<LoginResponse>>

    @POST("api/register")
    fun register(@Body credentials: Credentials): Deferred<Response<LoginResponse>>


}