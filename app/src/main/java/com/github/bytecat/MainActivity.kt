package com.github.bytecat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.bytecat.ui.theme.ByteHoleTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val byteHole by lazy { AndroidByteCat() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteHoleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }

        byteHole.startup()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy ")
        byteHole.shutdown()
    }
}

@Composable
fun MainView() {
    Log.d("MainView", "RUNNING --->")

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}