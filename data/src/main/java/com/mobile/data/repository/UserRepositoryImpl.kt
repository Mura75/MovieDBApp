package com.mobile.data.repository

import com.google.gson.JsonObject
import com.mobile.data.network.MovieApi
import com.mobile.data.storage.LocalPrefStorage
import com.mobile.data.storage.LocalPrefStorageImpl
import com.mobile.domain.repository.UserRepository
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
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
        localPrefStorage.saveData(LocalPrefStorageImpl.REQUEST_TOKEN, requestToken)
        val sessionId = createSession(requestToken)
        localPrefStorage.saveData(LocalPrefStorageImpl.SESSION_ID, sessionId)
        return loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
    }

    override fun isUserExist(): Boolean {
        return localPrefStorage.getString(LocalPrefStorageImpl.REQUEST_TOKEN)?.isEmpty() == false &&
                localPrefStorage.getString(LocalPrefStorageImpl.SESSION_ID)?.isEmpty() == false
    }

    override suspend fun createRequestToken(): String {
        return movieApi.createRequestToken().await()
            .body()
            ?.getAsJsonPrimitive("request_token")
            ?.asString ?: ""
    }

    override suspend fun createSession(requestToken: String): String {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        return movieApi.createSession(body)
            .await()
            .body()
            ?.getAsJsonPrimitive("session_id")
            ?.asString ?: ""
    }

    suspend fun createSession2(requestToken: String): Deferred<Response<JsonObject>> {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        return movieApi.createSession(body)
    }
}