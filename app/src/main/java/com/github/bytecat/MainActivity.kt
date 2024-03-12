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
import com.github.bytecat.contact.CatBook
import com.github.bytecat.message.MessageDataParcel
import com.github.bytecat.message.MessageParcel
import com.github.bytecat.ui.content.MainView
import com.github.bytecat.ui.theme.ByteCatTheme
import com.github.bytecat.vm.CatBookVM
import com.github.bytecat.vm.MessageBoxVM

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val catBookVM by lazy { CatBookVM() }
    private val messageBoxVM by lazy { MessageBoxVM() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteCatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(catBookVM, messageBoxVM)
                }
            }
        }

        ByteCatManager.catCallbackRegistry.register(catBookVM)
        ByteCatManager.catCallbackRegistry.register(messageBoxVM)
        ByteCatManager.messageCallbackRegistry.register(messageBoxVM)
        ByteCatManager.connect(this, object : ByteCatManager.ConnectCallback {
            override fun onConnected(catService: IByteCatService) {
            }

            override fun onDisconnected() {
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        ByteCatManager.catCallbackRegistry.unregister(catBookVM)
        ByteCatManager.catCallbackRegistry.unregister(messageBoxVM)
        ByteCatManager.messageCallbackRegistry.unregister(messageBoxVM)
        ByteCatManager.disconnect()
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val catBook = CatBook()
    catBook.addCat("Cat1", "Mac", "1234", 0, 1)
    catBook.addCat("Cat2", "iPhone", "2345", 0, 1)
    catBook.addCat("Cat3", "Android", "3456", 0, 1)
    catBook.addCat("Cat4", "Android", "4567", 0, 1)
    MainView(
        catBookVM = CatBookVM(), msgBoxVM = MessageBoxVM()
    )
}