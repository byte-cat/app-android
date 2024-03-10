package com.github.bytecat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import com.github.bytecat.ext.iconRes
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
                    override fun onReady(cat: CivetCat?) {
                        Log.d(TAG, "onReady cat=$cat")
                        catBookVM.myCat.value = cat
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

}

@Composable
fun MainView(catBookVM: CatBookViewModel, onItemClick: (cat: CivetCat) -> Unit) {
    val myCat = catBookVM.myCat.value
    Column {
        if (myCat != null) {
            Spacer(modifier = Modifier.height(16.dp))
            MyCatView(myCat = myCat)
        } else {
            Log.e("MainActivity", "MainView myCat is null")
        }
        if (catBookVM.cats.isEmpty()) {
            EmptyView()
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1F, true),
                    thickness = 0.5.dp,
                    color = colorResource(
                        id = R.color.cat_item_divider
                    )
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "${catBookVM.cats.size} cats",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
                Divider(
                    modifier = Modifier.weight(1F, true),
                    thickness = 0.5.dp,
                    color = colorResource(
                        id = R.color.cat_item_divider
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(catBookVM.cats) { index, item ->
                    CatItemView(cat = item, onItemClick)
                    if (index < catBookVM.cats.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            modifier = Modifier.width(240.dp),
            painter = painterResource(id = R.drawable.img_no_cats),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            color = colorResource(id = R.color.cat_item_name),
            text = stringResource(id = R.string.empty_cats)
        )
    }
}

@Composable
fun MyCatView(myCat: CivetCat) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.cat_item_background),
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.cat_item_icon_tint),
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = myCat.platform.iconRes),
            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = myCat.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = myCat.ip,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
    }
}

@Composable
fun CatItemView(cat: CivetCat, onClick: (cat: CivetCat) -> Unit) {

    val roundedCornerShape = RoundedCornerShape(corner = CornerSize(16.dp))
    Row(
        modifier = Modifier
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .clip(shape = roundedCornerShape)
            .clickable {
                onClick.invoke(cat)
            }
            .background(color = colorResource(id = R.color.cat_item_background))
            .border(
                width = 0.5.dp,
                color = colorResource(id = R.color.cat_item_icon_tint),
                shape = roundedCornerShape
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Image(
            painter = painterResource(id = cat.platform.iconRes),
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
                fontSize = 12.sp,
                color = colorResource(id = R.color.cat_item_name),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cat.ip,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Light,
                fontSize = 10.sp
            )
        }
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_file_send_outline),
            modifier = Modifier
                .size(32.dp)
                .clip(roundedCornerShape)
                .clickable {
                    onClick.invoke(cat)
                }
                .padding(4.dp),
            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_message_fast_outline),
            modifier = Modifier
                .size(32.dp)
                .clip(roundedCornerShape)
                .clickable {
                    onClick.invoke(cat)
                }
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