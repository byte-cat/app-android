package com.github.bytecat.ui.content

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.bytecat.ByteCatManager
import com.github.bytecat.CatParcel
import com.github.bytecat.R
import com.github.bytecat.ext.iconRes
import com.github.bytecat.message.MessageDataParcel
import com.github.bytecat.vm.CatBookVM
import com.github.bytecat.vm.MessageBoxVM

private const val TAG = "Main"

@Composable
fun MainView(
    catBookVM: CatBookVM, msgBoxVM: MessageBoxVM
) {
    val highlightCat = remember {
        mutableStateOf<CatParcel?>(null)
    }

    MainContent(
        catBookVM = catBookVM,
        msgBoxVM = msgBoxVM,
        onItemClick = { cat, index -> },
        onFileClick = { cat, index -> },
        onMsgClick = { cat, index ->
            highlightCat.value = cat
        }
    )
    if (highlightCat.value != null) {
        CatItemDialog(cat = highlightCat.value!!) {
            highlightCat.value = null
        }
    }
}

@Composable
fun MainContent(
    catBookVM: CatBookVM, msgBoxVM: MessageBoxVM,
    onItemClick: (cat: CatParcel, index: Int) -> Unit,
    onFileClick: (cat: CatParcel, index: Int) -> Unit,
    onMsgClick: (cat: CatParcel, index: Int) -> Unit
) {
    val myCat = catBookVM.myCat.value
    val context = LocalContext.current

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
            Spacer(modifier = Modifier.height(16.dp))
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
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                itemsIndexed(catBookVM.cats) { index, item ->
                    CatItemView(
                        cat = item,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(corner = CornerSize(16.dp)))
                            .clickable {
                            }
                            .background(color = colorResource(id = R.color.cat_item_background))
                            .border(
                                width = 0.5.dp,
                                color = colorResource(id = R.color.cat_item_icon_tint),
                                shape = RoundedCornerShape(corner = CornerSize(16.dp))
                            ),
                        actionView = {
                            Spacer(Modifier.width(8.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_file_send_outline),
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                                    .clickable {
                                        onFileClick.invoke(item, index)
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
                                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                                    .clickable {
                                        onMsgClick.invoke(item, index)
                                    }
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
                                contentDescription = ""
                            )
                            Spacer(Modifier.width(12.dp))
                        },
                        extendView = msgBoxVM.getTextMessageOrNull(item)?.let { data ->
                            {
                                Column {
                                    Text(
                                        text = data.text,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(64.dp)
                                            .padding(all = 8.dp),
                                        color = colorResource(id = R.color.cat_item_name),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Divider()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_message_check_outline),
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(
                                                    RoundedCornerShape(16.dp)
                                                )
                                                .clickable {
                                                    msgBoxVM.markAsRead(item)
                                                }
                                                .padding(all = 4.dp),
                                            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
                                            contentDescription = ""
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_content_copy),
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(
                                                    RoundedCornerShape(16.dp)
                                                )
                                                .clickable {
                                                    val clipboard =
                                                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    clipboard.setPrimaryClip(
                                                        ClipData.newPlainText(
                                                            item.name,
                                                            data.text
                                                        )
                                                    )
                                                    msgBoxVM.markAsRead(item)
                                                    Toast.makeText(context, R.string.toast_content_copied, Toast.LENGTH_SHORT).show()
                                                }
                                                .padding(all = 4.dp),
                                            colorFilter = ColorFilter.tint(colorResource(id = R.color.cat_item_icon_tint)),
                                            contentDescription = ""
                                        )
                                        Spacer(Modifier.width(12.dp))
                                    }
                                }
                            }
                        }
                    )
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
fun MyCatView(myCat: CatParcel) {
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
fun CatItemView(
    cat: CatParcel,
    modifier: Modifier = Modifier,
    actionView: @Composable (() -> Unit)? = null,
    extendView: @Composable (() -> Unit)? = null
) {

    @Composable
    fun CatItemInner(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier,
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
            if (actionView != null) {
                actionView()
            }
        }
    }

    if (extendView == null) {
        CatItemInner(
            modifier = modifier
                .height(56.dp)
        )
    } else {
        Column(
            modifier = modifier,
        ) {
            CatItemInner(
                modifier = Modifier
                    .height(56.dp)
            )
            Divider()
            extendView()
        }
    }

}

@Composable
fun CatItemDialog(cat: CatParcel, onDismissRequest: () -> Unit) {

    val text = remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = {
        // TODO("Save draft")
        onDismissRequest.invoke()
    }) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(corner = CornerSize(16.dp)))
                .background(color = colorResource(id = R.color.cat_item_background))
        ) {
            CatItemView(
                cat = cat
            )
//            Divider(thickness = 1.dp, color = colorResource(id = R.color.cat_item_divider))
            TextField(
                value = text.value,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.hint_message_to_send),
                        color = colorResource(id = android.R.color.darker_gray),
                        fontSize = 12.sp
                    )
                },
                onValueChange = {
                    text.value = it
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.cat_item_name)
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .clip(shape = RoundedCornerShape(corner = CornerSize(size = 20.dp)))
                        .clickable {
                            onDismissRequest.invoke()
                        }
                        .padding(all = 8.dp),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .clip(shape = RoundedCornerShape(corner = CornerSize(size = 20.dp)))
                        .clickable {
                            ByteCatManager.sendMessage(cat, text.value)
                            onDismissRequest.invoke()
                        }
                        .padding(all = 8.dp),
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}