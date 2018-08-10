package org.myspecialway.android.session.jwt

data class TokenPayloadData (val id:String,
                            val username: String,
                            val role: String,
                            val firstname: String,
                            val lastname: String,
                            val issuedTime: Long,
                            val expirationTime: Long) {}
