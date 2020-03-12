package com.mobile.data.repository

import com.google.gson.JsonObject
import com.mobile.data.network.MovieApi
import com.mobile.data.storage.LocalPrefStorage
import com.mobile.data.storage.LocalPrefStorageImpl
import com.mobile.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val localPrefStorage: LocalPrefStorage
) : UserRepository {

    override fun login(requestToken: String, username: String, password: String): Single<Pair<String, Boolean>> {
        val body = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("request_token", requestToken)
        }
        return movieApi.login(body)
            .flatMap { response ->
                if (response.isSuccessful) {
                    val newRequestToken = response.body()
                        ?.getAsJsonPrimitive("request_token")
                        ?.asString ?: ""
                    val data = response.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
                    val pair = Pair(newRequestToken, data)
                    Single.just(pair)
                } else {
                    Single.error(Throwable("login error"))
                }
            }
    }

    override fun isUserExist(): Boolean {
        return localPrefStorage.getString(LocalPrefStorageImpl.REQUEST_TOKEN)?.isEmpty() == false &&
                localPrefStorage.getString(LocalPrefStorageImpl.SESSION_ID)?.isEmpty() == false
    }

    override fun createRequestToken(): Single<String> {
        return movieApi.createRequestToken()
            .flatMap { response ->
                if (response.isSuccessful) {
                    val token = response.body()
                        ?.getAsJsonPrimitive("request_token")
                        ?.asString ?: ""
                    Single.just(token)
                } else {
                    Single.error(Throwable("request token error"))
                }
            }
    }

    override fun createSession(requestToken: String): Single<String> {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        return movieApi.createSession(body)
            .flatMap { response ->
                if (response.isSuccessful) {
                    val sessionId = response.body()
                        ?.getAsJsonPrimitive("session_id")
                        ?.asString ?: ""
                    Single.just(sessionId)
                } else {
                    Single.error(Throwable("session error"))
                }
            }
    }

    override fun saveToken(token: String) {
        localPrefStorage.saveData(LocalPrefStorageImpl.REQUEST_TOKEN, token)
    }
}