package com.github.trueddd.truetripletwitch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.github.trueddd.truetripletwitch.ui.theme.TrueTripleTwitchTheme
import com.github.trueddd.twitch.TwitchClient
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import org.koin.android.ext.android.inject
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val twitchClient by inject<TwitchClient>()

    private var authState: String = ""

    private fun updateAuthState() {
        authState = Random.Default.nextLong().toString()
    }

    private fun login() {
        updateAuthState()
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(twitchClient.getAuthLink(authState))
        }
        startActivity(intent)
    }

    private fun logout() {
        twitchClient.logout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user by twitchClient.userFlow.collectAsState(null, lifecycleScope.coroutineContext)
            TrueTripleTwitchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (user == null) {
                        Text(
                            text = "Login",
                            modifier = Modifier
                                .clickable { login() }
                        )
                    } else {
                        Text(
                            text = user?.displayName ?: "username",
                            modifier = Modifier
                                .clickable { logout() }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val twitchUri = intent?.data ?: return
        Log.d("URI new intent", twitchUri.toString())
        val fragment = twitchUri.fragment ?: return
        Log.d("URI fragment", fragment)
        val response = fragment.split("&")
            .associate { it.split("=").let { (name, value) -> name to value } }
        if (response["state"] == authState) {
            Log.d("State check", "passed")
            val accessToken = response["access_token"] ?: return
            twitchClient.login(accessToken)
                .onStart { Log.d("Login", "start") }
                .onCompletion { Log.d("Login", "finished") }
                .launchIn(lifecycleScope)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TrueTripleTwitchTheme {
        Greeting("Android")
    }
}