package pl.jawegiel.twinmindjakubwegielewski.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow

class ViewModelAccount : ViewModel() {

    val auth = FirebaseAuth.getInstance()

    private val _isUserLoggedIn = isUserLoggedIn().asLiveData(viewModelScope.coroutineContext)
    val isUserLoggedIn: LiveData<Boolean>
        get() = _isUserLoggedIn

    fun isUserLoggedIn() = flow {
        emit(auth.currentUser != null)
    }
}