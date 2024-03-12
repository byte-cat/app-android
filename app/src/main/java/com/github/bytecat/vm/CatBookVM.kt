package com.github.bytecat.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.bytecat.ByteCatManager
import com.github.bytecat.CatParcel

class CatBookVM : ViewModel(), ByteCatManager.CatCallback {

    val myCat = mutableStateOf<CatParcel?>(null)

    val cats = mutableStateListOf<CatParcel>()

    private fun addCat(cat: CatParcel) {
        if (cats.contains(cat)) {
            return
        }
        cats.add(cat)
    }

    private fun removeCat(cat: CatParcel) {
        cats.remove(cat)
    }

    override fun onMyCatReady(cat: CatParcel) {
        myCat.value = cat
    }

    override fun onCatAdd(cat: CatParcel) {
        addCat(cat)
    }

    override fun onCatRemove(cat: CatParcel) {
        removeCat(cat)
    }

    override fun onCatUpdate(cat: CatParcel) {
    }

}