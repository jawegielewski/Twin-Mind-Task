package pl.jawegiel.twinmindjakubwegielewski.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightSeaGreen
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelAccount
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelLogin

private const val PERMISSION_ALL = 1

class ActivityLogin() : ComponentActivity() {

    private val vmLogin by lazy { ViewModelProvider(this)[ViewModelLogin::class.java] }
    private val vmAccount by lazy { ViewModelProvider(this)[ViewModelAccount::class.java] }

    private var indicatorVisibility by mutableIntStateOf(View.GONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ActivityLoginScreenLayout()
        }

        vmLogin.isPermissionsGranted.observe(this) { isPermissionsGranted ->
            if (!isPermissionsGranted) {
                ActivityCompat.requestPermissions(this, ViewModelLogin.PERMISSIONS_REQUIRED, PERMISSION_ALL)
            }
        }

        vmLogin.task.observe(this) {
            startActivity(Intent(this, ActivityMain::class.java))
            indicatorVisibility = View.GONE
        }

        vmAccount.isUserLoggedIn.observe(this) { isUserLoggedIn ->
            if (isUserLoggedIn) {
                startActivity(Intent(this, ActivityMain::class.java))
            }
        }
    }

    @Composable
    fun ActivityLoginScreenLayout() {
        Scaffold(backgroundColor = LightSeaGreen, content = { paddingValues ->
            LoginMainContent(paddingValues)
        }, bottomBar = {
            BottomBar()
        })
    }

    @Composable
    fun LoginMainContent(paddingValues: PaddingValues) {
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "TwinMind", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(40.dp))
            ButtonLogin()
            IndicatorLogin()
        }
    }

    @Composable
    fun ButtonLogin() {
        Button(onClick = {
            vmLogin.getGoogleSignInCredential(this)
            indicatorVisibility = View.VISIBLE
        }) {
            Text("Continue with Google")
        }
    }

    @Composable
    fun IndicatorLogin() {
        if (indicatorVisibility == View.VISIBLE) {
            CircularProgressIndicator(modifier = Modifier.width(64.dp))
        }
    }

    @Composable
    private fun BottomBar() {
        BottomAppBar(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ButtonPrivacyPolicy()
                Spacer(modifier = Modifier.width(50.dp))
                ButtonTermsOfService()
            }
        }
    }

    @Composable
    private fun ButtonPrivacyPolicy() {
        TextButton(onClick = {
            Toast.makeText(this@ActivityLogin, "Privacy Policy", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Privacy Policy", fontSize = 12.sp, color = Color.White)
        }
    }

    @Composable
    private fun ButtonTermsOfService() {
        TextButton(onClick = {
            Toast.makeText(this@ActivityLogin, "Terms of Service", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Terms of Service", fontSize = 12.sp, color = Color.White)
        }
    }
}

