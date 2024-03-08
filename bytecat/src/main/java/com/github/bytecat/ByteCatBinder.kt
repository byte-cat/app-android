package com.github.bytecat

import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import java.util.LinkedList

class ByteCatBinder : IByteCatService.Stub() {

    private val byteCat by lazy { AndroidByteCat() }

    private val civetCats = LinkedList<CivetCat>()

    private val catBookCallback by lazy {
        object : CatBook.Callback {
            override fun onContactAdd(cat: Cat) {
                val civetCat = CivetCat(cat)
                civetCats.add(civetCat)
                callback?.onCatAdd(civetCat)
            }

            override fun onContactRemove(cat: Cat) {
                val civetCat = civetCats.firstOrNull {
                    it.ip == cat.ipAddress
                } ?: return
                civetCats.remove(civetCat)
                callback?.onCatRemove(civetCat)
            }

            override fun onContactUpdate(cat: Cat) {
                val civetCat = civetCats.firstOrNull {
                    it.ip == cat.ipAddress
                } ?: return
                civetCat.apply {
                    name = cat.name
                }
                callback?.onCatUpdate(civetCat)
            }
        }
    }
    private var callback: ICallback? = null

    fun startup() {
        byteCat.catBook.registerCallback(catBookCallback)
        byteCat.startup()
    }
    fun shutdown() {
        callback = null
        byteCat.catBook.unregisterCallback(catBookCallback)
        byteCat.shutdown()
    }

    override fun getCats(): MutableList<CivetCat> {
        TODO("Not yet implemented")
    }

    override fun setCallback(callback: ICallback?) {
        this.callback = callback
    }

}