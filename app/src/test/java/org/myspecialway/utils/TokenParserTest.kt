package org.myspecialway.utils

import io.reactivex.Observable
import org.junit.Assert
import org.junit.Test
import org.myspecialway.session.Token


class TokenParserTest {


    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjViODUxYjg1Y2FkOWE1NTgwM2ZkNzQyNyIsInVzZXJuYW1lIjoic3R1ZGVudCIsInJvbGUiOiJTVFVERU5UIiwiZmlyc3RuYW1lIjoiTXN3IiwibGFzdG5hbWUiOiJTdHVkZW50IiwiaWF0IjoxNTM1OTc1ODYzLCJleHAiOjE1MzU5ODMwNjN9.KT3bAm5ILyUh8SryWeFyGynH4e4fgRyoStIWjSyh7kQ"

    @Test
    fun `check token parse username`() {
        val result = tokenParser.parsePayload(token)
        Assert.assertEquals("student", result.username)
    }

    @Test
    fun `check token parsing - first name`() {
        val result = tokenParser.parsePayload(token)
        Assert.assertEquals("Msw", result.firstname)
    }


    @Test
    fun `check token parsing - last name`() {
        val result = tokenParser.parsePayload(token)
        Assert.assertEquals("Student", result.lastname)
    }

    @Test
    fun `fail parse with bad token`() {
        Observable.fromCallable { tokenParser.parsePayload("a_b_c_d") }
                .test()
                .assertError { true }
    }

    @Test
    fun `mapToken - check map returns valid token`() {
        val fakeTokenGenerated = createFakeToken(false).accessToken
        val mappedToken = Token().map(fakeTokenGenerated!!, tokenParser)
        Assert.assertNotNull(mappedToken.accessToken)
        Assert.assertNotNull(mappedToken.issuedTimeSec)
        Assert.assertNotNull(mappedToken.expirationTimeSec)
        Assert.assertTrue(mappedToken.isValid)
    }
}