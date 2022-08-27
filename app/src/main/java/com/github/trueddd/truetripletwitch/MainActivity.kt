package com.github.trueddd.truetripletwitch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.trueddd.truetripletwitch.ui.MainViewModel
import com.github.trueddd.truetripletwitch.ui.screens.main.MainScreen
import com.github.trueddd.truetripletwitch.ui.theme.TrueTripleTwitchTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private fun login() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(viewModel.getLinkForLogin())
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrueTripleTwitchTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.stateFlow.collectAsState()
                    MainScreen(
                        state = state,
                        onLoginButtonClicked = ::login,
                        onLogoutButtonClicked = { viewModel.logout() },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val twitchUri = intent?.data ?: return
        val fragment = twitchUri.fragment ?: return
        val response = fragment.split("&")
            .associate { it.split("=").let { (name, value) -> name to value } }
        if (response["state"] == viewModel.authState) {
            val accessToken = response["access_token"] ?: return
            viewModel.login(accessToken)
        }
    }
}
