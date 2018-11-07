package com.sitong.changqin.utils.files

import com.jyall.android.common.utils.LogUtils
import java.io.File

class FilesUtils {
    companion object {
        fun getFilesAllName(path: String): ArrayList<String>? {
            val file = File(path)
            val files = file.listFiles()
            if (files == null) {
                LogUtils.e("error", "空目录")
                return null
            }
            val s = arrayListOf<String>()
            for (i in files!!.indices) {
                s.add(files!![i].getAbsolutePath())
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