package com.sevenstringedzithers.sitong.utils.files

import com.jyall.bbzf.base.BaseContext
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo
import java.io.File

abstract class FileFactory() {

//    private var mContext: Context? = null

    companion object {
//        fun getDownLoadUtils(): ArrayList<String> {
//            return getUri()
//        }
    }

    init {
//        this.mContext = mContext
    }

    fun getUri(path: String, isJustName: Boolean = false): ArrayList<String>? {
        return FilesUtils.getFilesAllName(BaseContext.instance?.getExternalFilesDir(path)!!.absolutePath, isJustName)
    }

    fun getFilesInfo(path: String, isJustName: Boolean = false): ArrayList<FileInfo>? {
        return FilesUtils.getFilesAllInfo(BaseContext.instance?.getExternalFilesDir(path)!!.absolutePath, isJustName)
    }


    fun deleteFiles(file: String) {
        var file = File(getCurrentUri()+"/"+file)
        file.delete()
//        mContext?.deleteFile(file)
    }

    abstract fun deletedFile(path: String)
    abstract fun getFilesByPath(path: String): ArrayList<String>
    abstract fun isExist(file: String): Boolean
    abstract fun getCurrentUri(): String
}