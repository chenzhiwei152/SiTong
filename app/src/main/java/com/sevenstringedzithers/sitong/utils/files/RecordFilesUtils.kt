package com.sevenstringedzithers.sitong.utils.files

import android.content.Context
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo

class RecordFilesUtils(mContext: Context) : FileFactory(mContext) {

    private var mContext: Context? = null
    //    private var mLoadUri = "Downloads"
    private var mRecordUri = "Records"

    init {
        this.mContext = mContext
//        mContext.getExternalFilesDir(mLoadUri)
//        mContext.getExternalFilesDir(mRecordUri)
    }

    companion object {
        @Volatile
        private var instance_: RecordFilesUtils? = null

        fun getInstance(mContext: Context): RecordFilesUtils? {
            if (null == instance_) {
                synchronized(RecordFilesUtils::class.java) {
                    if (null == instance_) {
                        instance_ = RecordFilesUtils(mContext)
                    }
                }
            }
            return instance_
        }
    }

    override fun deletedFile(path: String) {
        deleteFiles(path)
    }

    override fun getFilesByPath(path: String): ArrayList<String> {
        return getUri(mRecordUri, true)!!
    }

    fun getFilesInfoByPath(path: String): ArrayList<FileInfo> {
        return getFilesInfo(mRecordUri, true)!!
    }

    override fun isExist(file: String): Boolean {
        return FilesUtils.fileIsExists(getCurrentUri() + "/" + file)
    }


    override fun getCurrentUri(): String {
        return mContext?.getExternalFilesDir(mRecordUri)!!.absolutePath
    }
}