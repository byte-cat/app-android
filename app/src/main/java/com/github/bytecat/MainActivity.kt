package com.github.bytecat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.github.bytecat.contact.CatBook
import com.github.bytecat.ui.content.MainView
import com.github.bytecat.ui.theme.ByteCatTheme
import com.github.bytecat.vm.CatBookViewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val catBookVM by lazy { CatBookViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteCatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(catBookVM)
                }
            }
        }

        ByteCatManager.connect(this, object : ByteCatManager.ConnectCallback {
            override fun onConnected(catService: IByteCatService) {
                catService.setCallback(object : CallbackImpl() {
                    override fun onReady(cat: CivetCat?) {
                        Log.d(TAG, "onReady cat=$cat")
                        catBookVM.myCat.value = cat
                        catBookVM.cats.addAll(catService.cats)
                    }

                    override fun onCatAdd(cat: CivetCat?) {
                        cat ?: return
                        catBookVM.addCat(cat)
                    }

                    override fun onCatRemove(cat: CivetCat?) {
                        cat ?: return
                        catBookVM.removeCat(cat)
                    }

                    override fun onCatUpdate(cat: CivetCat?) {
                        cat ?: return
                    }
                })
            }

            override fun onDisconnected() {
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        ByteCatManager.disconnect()
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val catBook = CatBook()
    catBook.addContact("Cat1", "Mac", "1234", 0, 1)
    catBook.addContact("Cat2", "iPhone", "2345", 0, 1)
    catBook.addContact("Cat3", "Android", "3456", 0, 1)
    catBook.addContact("Cat4", "Android", "4567", 0, 1)
    MainView(
        catBookVM = CatBookViewModel()
    )
}