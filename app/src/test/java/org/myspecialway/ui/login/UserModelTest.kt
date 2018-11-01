package org.myspecialway.ui.login

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.myspecialway.common.toJson
import org.myspecialway.ui.agenda.USER_MODEL

class UserModelTest{

    @Mock lateinit var sp: SharedPreferences

    @Before
    fun before(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `getUser firstTime returnsEmptyObject`() {
        Mockito.`when`(sp.contains(any())).doReturn(false)
        val user = UserModel().getUser(sp)

        assertEquals(user.id, null)
    }

    @Test
    fun `getUser userExists returnsFullObject`() {
        val user = UserModel().apply {  firstName = "Msw"
            lastName = "Student"
            id = "5b90f6b48314238c344ad06c"
            username = "null"
            photo = "null"
            role = "null"
            authData = LoginAuthData("student", "Aa123456")
        }
        val userJson = user.toJson()

        Mockito.`when`(sp.contains(any())).doReturn(true)
        Mockito.`when`(sp.getString(USER_MODEL, "")).doReturn(userJson)

        val userModel = UserModel().getUser(sp)

        assertThat(userModel).isEqualTo(user)
    }

    @Test
    fun `fullName returnsFullName`() {
        val user = UserModel().apply {  firstName = "Msw"
            lastName = "Student"
            id = "5b90f6b48314238c344ad06c"
            username = "null"
            photo = "null"
            role = "null"
            authData = LoginAuthData("student", "Aa123456")
        }
        assertEquals("Msw Student", user.fullName())
    }
}
