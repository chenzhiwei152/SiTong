package com.sitong.changqin.utils.files

import android.content.Context

class DownLoadFilesUtils(mContext: Context) : FileFactory(mContext) {

    private var mContext: Context? = null
    private var mLoadUri = "Downloads"
    private var mRecordUri = "Records"

    init {
        this.mContext = mContext
//        mContext.getExternalFilesDir(mLoadUri)
//        mContext.getExternalFilesDir(mRecordUri)
    }

    companion object {
        @Volatile
        private var instance_: DownLoadFilesUtils? = null

        fun getInstance(mContext: Context): DownLoadFilesUtils? {
            if (null == instance_) {
                synchronized(DownLoadFilesUtils::class.java) {
                    if (null == instance_) {
                        instance_ = DownLoadFilesUtils(mContext)
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
        return getUri(mLoadUri)!!
    }

    override fun isExist(file: String): Boolean {
        return FilesUtils.fileIsExists(getCurrentUri() + "/" + file)
    }


    override fun getCurrentUri(): String {
        return mContext?.getExternalFilesDir(mLoadUri)!!.absolutePath
    }
}