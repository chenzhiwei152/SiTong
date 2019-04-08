package com.sevenstringedzithers.sitong.utils.files

import android.graphics.Bitmap
import android.util.Log
import com.jyall.android.common.utils.LogUtils
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import java.io.*
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
                    bean.length = ExtraUtils.getMP3FileInfo(files!![i].absolutePath!!) / 1000
                    s.add(bean)
                } else {
                    val time = SimpleDateFormat("yyyy-MM-dd")
                            .format(Date(files!![i].lastModified()))
                    var bean = FileInfo()
                    bean.lastModified = time
                    bean.name = files!![i].absolutePath
                    bean.absolutePath = files!![i].absolutePath
                    bean.length = ExtraUtils.getMP3FileInfo(files!![i].absolutePath!!) / 1000
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

        /**
         * 把Bitmap转Byte
         * @Author
         * @EditTime
         */
        fun Bitmap2Bytes(bm: Bitmap): ByteArray {
            var baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }

        /**
         * 将一个pcm文件转化为wav文件
         * @param pcmPath         pcm文件路径
         * @param destinationPath 目标文件路径(wav)
         * @param deletePcmFile   是否删除源文件
         * @return
         */
        fun makePCMFileToWAVFile(pcmPath: String, destinationPath: String, deletePcmFile: Boolean): Boolean {
            var buffer: ByteArray? = null
            var TOTAL_SIZE = 0
            val file = File(pcmPath)
            if (!file.exists()) {
                return false
            }
            TOTAL_SIZE = file.length().toInt()
            // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
            val header = WaveHeader()
            // 长度字段 = 内容的大小（TOTAL_SIZE) +
            // 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)

            header.fileLength = TOTAL_SIZE + (44 - 8)
            header.FmtHdrLeth = 16
            header.BitsPerSample = 16
            header.Channels = 2
            header.FormatTag = 0x0001
            header.SamplesPerSec = 8000
            header.BlockAlign = (header.Channels * header.BitsPerSample / 8).toShort()
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec
            header.DataHdrLeth = TOTAL_SIZE

            var h: ByteArray? = null
            try {
                h = header.getHeader()
            } catch (e1: IOException) {
                Log.e("PcmToWav", e1.message)
                return false
            }

            if (h!!.size != 44)
            // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
                return false

            // 先删除目标文件
            val destfile = File(destinationPath)
            if (destfile.exists())
                destfile.delete()

            // 合成的pcm文件的数据，写到目标文件
            try {
                buffer = ByteArray(1024 * 4) // Length of All Files, Total Size
                var inStream: InputStream? = null
                var ouStream: OutputStream? = null

                ouStream = BufferedOutputStream(FileOutputStream(
                        destinationPath))
                ouStream!!.write(h, 0, h.size)
                inStream = BufferedInputStream(FileInputStream(file))
                var size = inStream!!.read(buffer)
                while (size != -1) {
                    ouStream!!.write(buffer)
                    size = inStream!!.read(buffer)
                }
                inStream!!.close()
                ouStream!!.close()
            } catch (e: FileNotFoundException) {
                Log.e("PcmToWav", e.message)
                return false
            } catch (ioe: IOException) {
                Log.e("PcmToWav", ioe.message)
                return false
            }

            if (deletePcmFile) {
                file.delete()
            }
            Log.i("PcmToWav", "makePCMFileToWAVFile  success!" + SimpleDateFormat("yyyy-MM-dd hh:mm").format(Date()))
            return true
        }


    }
}