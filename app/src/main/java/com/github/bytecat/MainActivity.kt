package com.github.bytecat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.github.bytecat.contact.CatBook
import com.github.bytecat.contact.Contact
import com.github.bytecat.ui.theme.ByteHoleTheme
import com.github.bytecat.vm.CatBookViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val catBookVM by lazy { CatBookViewModel(byteCat.catBook) }

    private val catBookCallback = object : CatBook.Callback {
        override fun onContactAdd(contact: Contact) {
            catBookVM.viewModelScope.launch {
                catBookVM.addCat(contact)
            }
        }

        override fun onContactRemove(contact: Contact) {
            catBookVM.viewModelScope.launch {
                catBookVM.removeCat(contact)
            }
        }
    }

    private val byteCat by lazy { AndroidByteCat() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteHoleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(catBookVM)
                }
            }
        }

        byteCat.catBook.registerCallback(catBookCallback)
        byteCat.startup()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy ")
        byteCat.catBook.unregisterCallback(catBookCallback)
        byteCat.shutdown()
    }
}

@Composable
fun MainView(catBookVM: CatBookViewModel) {

    LazyColumn {
        items(catBookVM.cats) {
            CatItemView(cat = it)
        }
    }
}

@Composable
fun CatItemView(cat: Contact) {
    val deviceIconRes = when(cat.deviceType) {
        "Android" -> R.drawable.ic_android
        "Mac" -> R.drawable.ic_apple_finder
        "iPhone" -> R.drawable.ic_apple
        else -> R.drawable.ic_cat
    }
    Row {
        Image(
            painter = painterResource(id = deviceIconRes),
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray),
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = cat.name,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = cat.name,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val catBook = CatBook()
    catBook.addContact("123", "Cat1", "Mac", "", 0, 1)
    catBook.addContact("2", "Cat2", "iPhone", "", 0, 1)
    catBook.addContact("3", "Cat3", "Android", "", 0, 1)
    catBook.addContact("4", "Cat4", "Android", "", 0, 1)
    MainView(catBookVM = CatBookViewModel(catBook))
}