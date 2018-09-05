//package org.myspecialway.ui.login
//
//
//import android.content.SharedPreferences
//import junit.framework.Assert
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.koin.standalone.StandAloneContext.startKoin
//import org.koin.standalone.StandAloneContext.stopKoin
//import org.koin.standalone.inject
//import org.koin.test.KoinTest
//import org.myspecialway.di.localDataSourceModule
//
//class UserModelTest : KoinTest {
//
//    private val sp: SharedPreferences by inject()
//
//    @Before
//    fun before() {
//        startKoin(arrayListOf(localDataSourceModule))
//    }
//
//    @After
//    fun after() {
//        stopKoin()
//    }
//
//    private val mockUser: UserModel = UserModel().apply {
//        firstName = "idan"
//        lastName = "ayalon"
//        username = "username"
//        id = "12324"
//        authData = LoginAuthData("student", "Aa1234")
//        role = "student"
//    }
//
//    @Test
//    fun `storeUserModel saves user` () {
//        UserModel().storeUserModel(sp, mockUser)
//        Assert.assertNotNull(UserModel().getUser(sp))
//
//    }
//
//    @Test
//    fun getUser() {
//    }
//
//    @Test
//    fun fullName() {
//    }
//
//    @Test
//    fun map() {
//    }
//}