package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositoryDatabase
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositorySharedPrefs

class ViewModelMain(application: Application) : AndroidViewModel(application) {

    private val repositorySharedPrefs by lazy { RepositorySharedPrefs() }

    private val _userPhoto = MutableLiveData<Bitmap>()
    val userPhoto: LiveData<Bitmap>
        get() = _userPhoto

    fun getUserPhoto() {
        val userPhoto = repositorySharedPrefs.getBitmap(RepositorySharedPrefs.USER_PHOTO, "")
        if (userPhoto != null) {
            _userPhoto.value = userPhoto
        }
    }
}