package org.myspecialway.ui.login

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.myspecialway.session.Token
import org.myspecialway.utils.createFakeToken
import org.myspecialway.utils.fakeToken
import org.myspecialway.utils.tokenParser

class TokenParserTest {

    @Mock
    lateinit var sp: SharedPreferences

    @Before
    fun before(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `check token parse username`(){
        val result = tokenParser.parsePayload(fakeToken)
        assertThat(result.username).isEqualTo("student")
    }

    @Test
    fun `check token parsing - first name`() {
        val result = tokenParser.parsePayload(fakeToken)
        Assert.assertEquals("Msw", result.firstname)
    }


    @Test
    fun `check token parsing - last name`() {
        val result = tokenParser.parsePayload(fakeToken)
        Assert.assertEquals("Student", result.lastname)
    }

    @Test
    fun `token is valid check - not expired`() {
        val notExpiredToken = createFakeToken(false)
        Assert.assertTrue(notExpiredToken.isValid)
    }

    @Test
    fun `token is valid check - expired`() {
        val notExpiredToken = createFakeToken(true)
        Assert.assertFalse(notExpiredToken.isValid)
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