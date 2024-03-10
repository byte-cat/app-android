package com.github.bytecat

import android.util.Log
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import java.util.LinkedList

class ByteCatBinder : IByteCatService.Stub() {

    companion object {
        private const val TAG = "ByteCatBinder"
    }

    private lateinit var myCat: CivetCat

    private val catCallback by lazy {
        object : ByteCat.Callback {
            override fun onReady(myCat: Cat) {
                this@ByteCatBinder.myCat = CivetCat(myCat)
                if (callback != null) {
                    callback?.onReady(this@ByteCatBinder.myCat)
                }
            }
            override fun onCatMessage(cat: Cat, text: String) {
//                callback?
            }
        }
    }

    private val byteCat by lazy { AndroidByteCat().apply { setCallback(catCallback) } }

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

    override fun getCats(): List<CivetCat> {
        return byteCat.catBook.cats.map {
            CivetCat(it)
        }
    }

    override fun setCallback(callback: ICallback?) {
        this.callback = callback
        if (::myCat.isInitialized) {
            callback?.onReady(myCat)
        }
    }

}