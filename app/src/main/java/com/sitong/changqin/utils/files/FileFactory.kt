package com.sitong.changqin.utils.files

import android.content.Context

abstract class FileFactory(mContext: Context) {

    private var mContext: Context? = null

    companion object {
//        fun getDownLoadUtils(): ArrayList<String> {
//            return getUri()
//        }
    }

    init {
        this.mContext = mContext
    }

    fun getUri(path: String): ArrayList<String>? {
        return FilesUtils.getFilesAllName(mContext?.getExternalFilesDir(path)!!.absolutePath)
    }



    fun deleteFiles(file: String) {
        mContext?.deleteFile(file)
    }

    abstract fun deletedFile(path: String)
    abstract fun getFilesByPath(path: String): ArrayList<String>
    abstract fun isExist(file: String): Boolean
    abstract fun getCurrentUri():String
}