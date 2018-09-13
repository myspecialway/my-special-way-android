package org.myspecialway.ui.login
import org.junit.Assert
import org.junit.Test
import org.myspecialway.utils.createFakeToken
import org.myspecialway.utils.fakeToken
import org.myspecialway.utils.tokenParser

import com.google.common.truth.Truth.assertThat

class TokenParserTest {

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
}