package org.myspecialway.session

import android.content.SharedPreferences
import com.google.gson.Gson
import org.myspecialway.ui.agenda.TOKEN_MODEL
import org.myspecialway.utils.TokenParser

data class Token(var accessToken: String? = null, var issuedTimeSec: Long? = null, var expirationTimeSec: Long? = null) {

    fun storeAccessToken(sp: SharedPreferences, token: Token) {
        sp.edit().apply {
            putString(TOKEN_MODEL, Gson().toJson(token))
        }.apply()
    }

    fun getToken(preferences: SharedPreferences): Token {
        if (preferences.contains(TOKEN_MODEL)) {
            return Gson().fromJson<Token>(preferences.getString(TOKEN_MODEL, ""), Token::class.java)
        }
        return Token()
    }

    fun map(token: String) = apply {
        accessToken = token
        issuedTimeSec = TokenParser().parsePayload(token).iat
        expirationTimeSec = TokenParser().parsePayload(token).exp
    }



    fun map(token: String, tokenParser: TokenParser) = apply {
        accessToken = token
        issuedTimeSec = tokenParser.parsePayload(token).iat
        expirationTimeSec = tokenParser.parsePayload(token).exp
    }

    val isValid: Boolean
        get() {
            val now = System.currentTimeMillis()
            return accessToken!!.isNotBlank() && now - (issuedTimeSec!! * 1000) > 0 && (expirationTimeSec!! * 1000) - now > 0
        }
}
