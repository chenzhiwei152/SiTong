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
        try {
            var file = File(getCurrentUri()+"/"+file)
            file.delete()
            var file1 = File(getCurrentUri()+"/"+"storage")
            file1.delete()
        }catch (e:Exception){}

    }

    abstract fun deletedFile(path: String)
    abstract fun getFilesByPath(path: String): ArrayList<String>
    abstract fun isExist(file: String): Boolean
    abstract fun getCurrentUri(): String
}