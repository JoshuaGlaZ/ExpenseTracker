package com.example.expensetracker.viewmodel

import SessionManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.expensetracker.model.ExpenseDatabase
import com.example.expensetracker.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SignViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    val signUpStatus = MutableLiveData<String>()
    val signInStatus = MutableLiveData<String>()
    val updatePasswordStatus = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val dao = ExpenseDatabase.getInstance(getApplication()).expenseDao()

    fun signIn(username: String, password: String) {
        launch {
            isLoading.postValue(true)
            val user = dao.loginUser(username, password)
            if (user != null) {
                SessionManager.saveLoginSession(user.id)
                signInStatus.postValue("success")
            } else {
                signInStatus.postValue("error")
            }
            isLoading.postValue(false)
        }
    }

    fun signUp(username: String, password: String, firstname: String, lastName: String) {
        launch {
            isLoading.postValue(true)
            val existing = dao.selectUserByUsername(username)
            if (existing != null) {
                signUpStatus.postValue("exists")
            } else {
                val newUser = User(
                    username = username,
                    password = password,
                    firstname = firstname,
                    lastname = lastName
                )
                dao.insertUser(newUser)
                val savedUser = dao.selectUserByUsername(username)
                if (savedUser != null) {
                    SessionManager.saveLoginSession(savedUser.id)
                    signUpStatus.postValue("success")
                } else {
                    signUpStatus.postValue("error")
                }
            }
            isLoading.postValue(false)
        }
    }

    fun signOut() {
        SessionManager.clearSession()
    }

    fun getCurrentUser(): User? {
        val userId = SessionManager.getUserId()
        return if (userId != -1) {
            dao.selectUser(userId)
        } else null
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        launch {
            isLoading.postValue(true)
            try {
                val user = getCurrentUser()
                if (user != null) {
                    if (user.password == oldPassword) {
                        user.password = newPassword
                        dao.updateUser(user)
                        updatePasswordStatus.postValue("success")
                    } else {
                        updatePasswordStatus.postValue("incorrect_old_password")
                    }
                } else {
                    updatePasswordStatus.postValue("not_found")
                }
            } catch (e: Exception) {
                updatePasswordStatus.postValue("error")
            } finally {
                isLoading.postValue(false)
            }
        }
    }
}