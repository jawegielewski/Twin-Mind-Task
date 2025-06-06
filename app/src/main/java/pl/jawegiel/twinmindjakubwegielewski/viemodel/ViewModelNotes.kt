package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ViewModelNotes(application: Application) : AndroidViewModel(application) {

    private val _isEditModeOn = MutableLiveData<Boolean>(false)
    val isEditModeOn: LiveData<Boolean>
        get() = _isEditModeOn

    fun isEditModeEnabled(isEnabled: Boolean) {
        _isEditModeOn.value = isEnabled
    }
}