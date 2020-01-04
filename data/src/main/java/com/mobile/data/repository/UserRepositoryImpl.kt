package com.mobile.data.repository

import com.google.gson.JsonObject
import com.mobile.data.network.MovieApi
import com.mobile.data.storage.LocalPrefStorage
import com.mobile.domain.repository.UserRepository

class UserRepositoryImpl(
    private val movieApi: MovieApi,
    private val localPrefStorage: LocalPrefStorage
) : UserRepository {

    override suspend fun login(username: String, password: String): Boolean {
        var requestToken =  createRequestToken()
        val body = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("request_token", requestToken)
        }
        val loginResponse = movieApi.login(body).await()
        requestToken = loginResponse.body()?.getAsJsonPrimitive("request_token")?.asString ?: ""
        localPrefStorage.saveData(LocalPrefStorage.REQUEST_TOKEN, requestToken)
        val sessionId = createSession(requestToken)
        localPrefStorage.saveData(LocalPrefStorage.SESSION_ID, sessionId)
        return loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
    }

    override fun isUserExist(): Boolean {
        return localPrefStorage.getString(LocalPrefStorage.REQUEST_TOKEN)?.isEmpty() == false &&
                localPrefStorage.getString(LocalPrefStorage.SESSION_ID)?.isEmpty() == false
    }

    private suspend fun createRequestToken(): String {
        return movieApi.createRequestToken().await()
            .body()
            ?.getAsJsonPrimitive("request_token")
            ?.asString ?: ""
    }

    private suspend fun createSession(requestToken: String): String {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        val result = movieApi.createSession(body).await().body()
        return result?.getAsJsonPrimitive("session_id")?.asString ?: ""
    }
}