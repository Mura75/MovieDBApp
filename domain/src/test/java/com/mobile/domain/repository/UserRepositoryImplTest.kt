package com.mobile.domain.repository

import io.reactivex.Single

class UserRepositoryImplTest: UserRepository {

    override fun login(
        requestToken: String,
        username: String,
        password: String
    ): Single<Pair<String, Boolean>> {
        return Single.just(Pair("request_token", true))
    }

    override fun isUserExist(): Boolean {
        return true
    }

    override fun createRequestToken(): Single<String> {
        return Single.just("request_token")
    }

    override fun createSession(requestToken: String): Single<String> {
        return Single.just("session_id")
    }

    override fun saveToken(token: String) {

    }

}