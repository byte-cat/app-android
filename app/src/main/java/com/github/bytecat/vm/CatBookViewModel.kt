package com.github.bytecat.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.github.bytecat.CivetCat
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook

class CatBookViewModel : ViewModel() {

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