package com.mobile.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun login(requestToken: String, username: String, password: String): Single<Pair<String, Boolean>>
    fun isUserExist(): Boolean
    fun createRequestToken(): Single<String>
    fun createSession(requestToken: String): Single<String>
    fun saveToken(token: String)
}