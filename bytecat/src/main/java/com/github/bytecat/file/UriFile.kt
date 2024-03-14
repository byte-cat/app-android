package com.github.bytecat.file

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class UriFile(private val context: Context, private val uri: Uri) : IFile {
    override fun getName(): String {
        return uri.toString().substringAfterLast(File.separatorChar)
    }

    override fun length(): Long {
        val descriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val size = descriptor?.statSize
        descriptor?.close()
        return size ?: 0L
    }

    override fun openReadStream(): InputStream {
        return context.contentResolver.openInputStream(uri)!!
    }

    override fun openWriteStream(): OutputStream {
        return context.contentResolver.openOutputStream(uri)!!
    }
}