package org.myspecialway.utils

import android.util.Base64
import com.google.gson.Gson
import org.myspecialway.session.jwt.TokenHeaderData
import org.myspecialway.session.jwt.TokenPayloadData

interface JWTBase64 {

    fun decode(toDecode: String): String
    fun encode(toEncode: String): String
}

class JWTParser @JvmOverloads constructor(private val base64: JWTBase64 = JWTBase64Impl()) {

    class JWTBase64Impl : JWTBase64 {

        override fun encode(toEncode: String): String {
            return Base64.encodeToString(toEncode.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        }

        override fun decode(toDecode: String): String {
            return String(Base64.decode(toDecode.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING))
        }
    }

    fun parsePayload(token: String): TokenPayloadData {

        if (token.isBlank()) throw IllegalArgumentException("token should not be empty")

        val split = token.split("\\.".toRegex())

        val payload = base64.decode(split[1])

        return Gson().fromJson(payload, TokenPayloadData::class.java)
    }

    /**
     * Used for testing
     */
    fun createJWTToken(header: TokenHeaderData, payload: TokenPayloadData): String{

        val gson = Gson()
        val encodedHeader = base64.encode(gson.toJson(header))
        val encodedPayload = base64.encode(gson.toJson(payload))
        val signature = base64.encode("not_used")

        return "$encodedHeader.$encodedPayload.$signature"
    }
}
