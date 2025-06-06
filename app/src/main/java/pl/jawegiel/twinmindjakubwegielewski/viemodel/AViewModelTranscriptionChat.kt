package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ai.type.GenerateContentResponse

abstract class AViewModelTranscriptionChat(application: Application) : AndroidViewModel(application) {

    val mutChatBasedResponse = MutableLiveData<String>()
    val chatBasedResponse: LiveData<String>
        get() = mutChatBasedResponse

    abstract suspend fun generateContentChatBased(text: String)

    abstract suspend fun generateContentChatBased(myTextFieldText: String,
                                                  onResponse: (GenerateContentResponse?) -> Unit)
}