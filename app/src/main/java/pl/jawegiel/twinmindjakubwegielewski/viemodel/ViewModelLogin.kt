package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.credentials.GetCredentialException
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositorySharedPrefs
import java.net.URL

private const val TAG = "ViewModelLogin"
private const val SERVER_CLIENT_ID = "885584477912-0jhpcplsufr6no48hn0mphvlu4pf0ode.apps.googleusercontent.com"

class ViewModelLogin(application: Application) : AndroidViewModel(application) {

    companion object {
        val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
        )
    }

    private val auth = FirebaseAuth.getInstance()
    private val repositorySharedPrefs by lazy { RepositorySharedPrefs() }

    private val _task: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    val task: LiveData<Task<AuthResult>>
        get() = _task

    private val _isPermissionsGranted = hasPermissions(*PERMISSIONS_REQUIRED).asLiveData(viewModelScope.coroutineContext)
    val isPermissionsGranted: LiveData<Boolean>
        get() = _isPermissionsGranted

    fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                if (repositorySharedPrefs.getBitmap(RepositorySharedPrefs.USER_PHOTO, "") == null) {
                    val photoUri = auth.currentUser!!.photoUrl
                    saveUserPhoto(photoUri, result)
                } else {
                    _task.postValue(result)
                }
            }
        }
    }

    private fun saveUserPhoto(photoUri: Uri?, result: Task<AuthResult>) {
        if (photoUri != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val url = URL("https://${photoUri.host}${photoUri.path}").openStream()
                val userPhoto = BitmapFactory.decodeStream(url)
                viewModelScope.launch {
                    repositorySharedPrefs.setBitmap(RepositorySharedPrefs.USER_PHOTO, userPhoto)
                    _task.postValue(result)
                }
            }
        }
    }

    fun getGoogleSignInCredential(context: Context) {
        val googleIdOption = GetGoogleIdOption.Builder().setServerClientId(SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credentialManager = CredentialManager.create(context)
        viewModelScope.launch {
            var result: GetCredentialResponse? = null
            try {
                result = credentialManager.getCredential(context = context, request = request)
                handleSignIn(result.credential)
            } catch (ex: Exception) {
                Log.e(TAG, "An exception occurred during getting Google credentials: ${ex.message}")
            }
        }
    }

    fun hasPermissions(vararg permissions: String) = flow {
        emit(permissions.all {
            ActivityCompat.checkSelfPermission(application, it) == PackageManager.PERMISSION_GRANTED
        })
    }
}