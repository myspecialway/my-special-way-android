package org.myspecialway.android.session

data class Token(val accessToken: String, val issued: Long, val expirationTime: Long) {

    val isValid: Boolean
        get() {
            val now = System.currentTimeMillis()
            return accessToken.isNotBlank() && now - issued > 0 && expirationTime - now > 0
        }
}
