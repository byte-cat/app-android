package com.github.bytecat.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.github.bytecat.contact.CatBook
import com.github.bytecat.contact.Contact

class CatBookViewModel(catBook: CatBook) : ViewModel() {

    val cats = mutableStateListOf<Contact>()

    init {
        cats.addAll(catBook.cats)
    }

    fun addCat(cat: Contact) {
        if (cats.contains(cat)) {
            return
        }
        cats.add(cat)
    }

    fun removeCat(cat: Contact) {
        cats.remove(cat)
    }

}