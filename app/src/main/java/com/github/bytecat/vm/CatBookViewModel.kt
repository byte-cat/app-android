package com.github.bytecat.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.bytecat.CivetCat
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook

class CatBookViewModel : ViewModel() {

    val myCat = mutableStateOf<CivetCat?>(null)

    val cats = mutableStateListOf<CivetCat>()

    fun addCat(cat: CivetCat) {
        if (cats.contains(cat)) {
            return
        }
        cats.add(cat)
    }

    fun removeCat(cat: CivetCat) {
        cats.remove(cat)
    }

}