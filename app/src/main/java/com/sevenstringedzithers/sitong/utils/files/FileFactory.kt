package com.sevenstringedzithers.sitong.utils.files

import android.content.Context
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo

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

    fun getUri(path: String, isJustName: Boolean = false): ArrayList<String>? {
        return FilesUtils.getFilesAllName(mContext?.getExternalFilesDir(path)!!.absolutePath, isJustName)
    }

    fun getFilesInfo(path: String, isJustName: Boolean = false): ArrayList<FileInfo>? {
        return FilesUtils.getFilesAllInfo(mContext?.getExternalFilesDir(path)!!.absolutePath, isJustName)
    }


    fun deleteFiles(file: String) {
        mContext?.deleteFile(file)
    }

    abstract fun deletedFile(path: String)
    abstract fun getFilesByPath(path: String): ArrayList<String>
    abstract fun isExist(file: String): Boolean
    abstract fun getCurrentUri(): String
}