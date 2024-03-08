package com.github.bytecat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import com.github.bytecat.ui.theme.ByteHoleTheme
import com.github.bytecat.vm.CatBookViewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val catBookVM by lazy { CatBookViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteHoleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(catBookVM) {
//                        byteCat.sendMessage(it, "Message from android")
                    }
                }
            }
        }

        ByteCatManager.connect(this, object : ByteCatManager.ConnectCallback {
            override fun onConnected(catService: IByteCatService) {
                catService.setCallback(object : CallbackImpl() {
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

}

@Composable
fun MainView(catBookVM: CatBookViewModel, onItemClick: (cat: CivetCat) -> Unit) {
    LazyColumn {
        itemsIndexed(catBookVM.cats) { index, item ->
            CatItemView(cat = item, onItemClick)
            if (index < catBookVM.cats.lastIndex) {
                Divider(color = colorResource(id = R.color.cat_item_divider))
            }
        }
    }
}

@Composable
fun CatItemView(cat: CivetCat, onClick: (cat: CivetCat) -> Unit) {
    val deviceIconRes = when(cat.platform) {
        Platform.Android -> R.drawable.ic_android
        Platform.Mac -> R.drawable.ic_apple_finder
        Platform.IPhone -> R.drawable.ic_apple
        Platform.PC -> R.drawable.ic_microsoft_windows
        else -> R.drawable.ic_cat
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .background(colorResource(id = R.color.cat_item_background))
            .clickable(onClick = {
                onClick.invoke(cat)
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Image(
            painter = painterResource(id = deviceIconRes),
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = cat.name,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.cat_item_name)
            )
            Text(
                text = cat.ip,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_file_send_outline),
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp),
            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_message_fast_outline),
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp),
            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
            contentDescription = ""
        )
        Spacer(Modifier.width(12.dp))
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
    MainView(catBookVM = CatBookViewModel()) {

    }
}