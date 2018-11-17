package com.sevenstringedzithers.sitong.utils.files

import com.jyall.android.common.utils.LogUtils
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FilesUtils {
    companion object {
        fun getFilesAllName(path: String, isJustName: Boolean = false): ArrayList<String>? {
            val file = File(path)
            val files = file.listFiles()
            if (files == null) {
                LogUtils.e("error", "空目录")
                return null
            }
            val s = arrayListOf<String>()
            for (i in files!!.indices) {
                if (isJustName) {
                    s.add(getFileName(files!![i].absolutePath))
                    val time = SimpleDateFormat("yyyy-MM-dd")
                            .format(Date(files!![i].lastModified()))
                } else {
                    s.add(files!![i].absolutePath)
                }
            }
            return s
        }

        fun getFilesAllInfo(path: String, isJustName: Boolean = false): ArrayList<FileInfo>? {
            val file = File(path)
            val files = file.listFiles()
            if (files == null) {
                LogUtils.e("error", "空目录")
                return null
            }
            val s = arrayListOf<FileInfo>()
            for (i in files!!.indices) {
                if (isJustName) {
                    val time = SimpleDateFormat("yyyy-MM-dd")
                            .format(Date(files!![i].lastModified()))
                    var bean = FileInfo()
                    bean.lastModified = time
                    bean.name = getFileName(files!![i].absolutePath)
                    bean.absolutePath = files!![i].absolutePath
                    s.add(bean)
                } else {
                    val time = SimpleDateFormat("yyyy-MM-dd")
                            .format(Date(files!![i].lastModified()))
                    var bean = FileInfo()
                    bean.lastModified = time
                    bean.name = files!![i].absolutePath
                    bean.absolutePath = files!![i].absolutePath
                    s.add(bean)
                }
            }
            return s
        }

        //判断文件是否存在
        fun fileIsExists(strFile: String): Boolean {
            try {
                val f = File(strFile)
                if (!f.exists()) {
                    return false
                }

            } catch (e: Exception) {
                return false
            }

            return true
        }

        fun getFileName(name: String): String {
            if (name.isNullOrEmpty()) {
                return ""
            }
            val split = name.split("/")
            return split[split.size - 1]
        }

    }
}