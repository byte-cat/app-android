package com.github.bytecat.ui.content

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Shape
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
import com.github.bytecat.CivetCat
import com.github.bytecat.R
import com.github.bytecat.ext.iconRes
import com.github.bytecat.vm.CatBookViewModel

private const val TAG = "Main"

@Composable
fun MainView(
    catBookVM: CatBookViewModel
) {
    val highlightCat = remember {
        mutableStateOf<CivetCat?>(null)
    }

    MainContent(
        catBookVM = catBookVM,
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
    catBookVM: CatBookViewModel,
    onItemClick: (cat: CivetCat, index: Int) -> Unit,
    onFileClick: (cat: CivetCat, index: Int) -> Unit,
    onMsgClick: (cat: CivetCat, index: Int) -> Unit
) {
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
                    CatItemView(
                        cat = item,
                        modifier = Modifier
                            .height(56.dp)
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
                        actionViews = {
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
fun CatItemView(
    cat: CivetCat,
    modifier: Modifier = Modifier,
    actionViews: @Composable (() -> Unit)? = null
) {

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
        if (actionViews != null) {
            actionViews()
        }
    }
}

@Composable
fun CatItemDialog(cat: CivetCat, onDismissRequest: () -> Unit) {

    var text = remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(corner = CornerSize(16.dp)))
                .background(color = colorResource(id = R.color.cat_item_background))
        ) {
            CatItemView(
                cat = cat,
                modifier = Modifier
                    .height(56.dp)
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