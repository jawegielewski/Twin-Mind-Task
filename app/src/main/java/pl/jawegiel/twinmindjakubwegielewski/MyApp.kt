package pl.jawegiel.twinmindjakubwegielewski

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import pl.jawegiel.twinmindjakubwegielewski.utility.DaoSharedPrefs
import java.util.Locale

class MyApp : Application() {

    var isConnected = false

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("Locale", Locale.getDefault().toString())
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        }

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
            }
        })

        DaoSharedPrefs.init(this)
    }
}