package org.myspecialway.utils

import org.myspecialway.session.Token
import org.myspecialway.session.jwt.TokenHeaderData
import org.myspecialway.session.jwt.TokenPayloadData
import java.util.*

val tokenParser = TokenParser(object : TokenParser.JWTBase64Impl() {
    override fun encode(toEncode: String): String = Base64.getUrlEncoder().encodeToString(toEncode.toByteArray())
    override fun decode(toDecode: String): String = String(Base64.getUrlDecoder().decode(toDecode))
})

const val TWO_HOURS_SEC = 7200

fun createFakeToken(isExpired: Boolean) : Token {
    val nowSec = System.currentTimeMillis() / 1000
    val fakeTokenHeader = TokenHeaderData("HS256", "JWT")
    val fakeTokenPayload = TokenPayloadData("id",
            "username",
            "role",
            "firstname",
            "lastname",
            "gender",
            iat = if (isExpired) nowSec - TWO_HOURS_SEC else nowSec,
            exp = if (isExpired) nowSec else nowSec + TWO_HOURS_SEC)
    val token = tokenParser.createJWTToken(fakeTokenHeader, fakeTokenPayload)

    return Token().apply {
        accessToken = token
        expirationTimeSec = fakeTokenPayload.exp
        issuedTimeSec = fakeTokenPayload.iat
    }
}

val fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjViODUxYjg1Y2FkOWE1NTgwM2ZkNzQyNyIsInVzZXJuYW1lIjoic3R1ZGVudCIsInJvbGUiOiJTVFVERU5UIiwiZmlyc3RuYW1lIjoiTXN3IiwibGFzdG5hbWUiOiJTdHVkZW50IiwiaWF0IjoxNTM1OTc1ODYzLCJleHAiOjE1MzU5ODMwNjN9.KT3bAm5ILyUh8SryWeFyGynH4e4fgRyoStIWjSyh7kQ"

