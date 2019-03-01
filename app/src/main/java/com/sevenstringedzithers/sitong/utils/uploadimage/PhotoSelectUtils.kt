package com.sevenstringedzithers.sitong.utils.uploadimage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.extension.toast
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.PictureFileUtils
import com.sevenstringedzithers.sitong.ui.listerner.ProgressCallback
import com.sevenstringedzithers.sitong.utils.UploadImageUtils
import com.sevenstringedzithers.sitong.view.ImageDialog
import java.util.*

/**
 * 图片上传工具类
 */
class PhotoSelectUtils(val activity: Activity) {

    private var images = ArrayList<ImageBean>()
    private var images_album = ArrayList<LocalMedia>()
    private var tempList: MutableList<LocalMedia>? = null
    private var imageUrlList: ArrayList<String>? = null
    private var chooseMode = PictureMimeType.ofImage()
    private var maxSelectNum = 9
    private var aspect_ratio_x: Int = 1
    private var aspect_ratio_y: Int = 1
    private var mDialog: ImageDialog? = null
    private var type = 0
    fun setType(type: Int) {
        this.type = type
    }

    init {
        tempList = arrayListOf()
        imageUrlList = arrayListOf()
    }

    /**
     *
     *
     * @showType 控制需要显示的按钮 0 拍照，相册中选择   1 拍照，相册中选择，保存图片到相册  2 保存图片到相册
     * */
    fun showPhotoChooseDialog(
            tempList: MutableList<LocalMedia>?,
            enableRectangleCrop: Boolean = false,
            enableCircleCrop: Boolean = false,
            maxNum: Int = maxSelectNum,
            requestCode: Int = PictureConfig.CHOOSE_REQUEST
    ) {
        this.tempList = tempList

        if (mDialog == null) {

            mDialog = ImageDialog(activity)
        }
        mDialog?.setLeftTitleListerner(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                openAlbums(
                        selectedList = tempList,
                        enableRectangleCrop = enableRectangleCrop,
                        enableCircleCrop = enableCircleCrop,
                        maxNum = maxNum,
                        requestCode = requestCode
                )
            }

        })
        mDialog?.setRightTitleListerner(object : View.OnClickListener {
            override fun onClick(v: View?) {
                openCamera(
                        selectedList = tempList,
                        enableRectangleCrop = enableRectangleCrop,
                        enableCircleCrop = enableCircleCrop,
                        maxNum = maxNum,
                        requestCode = requestCode
                )
            }
        })
        mDialog?.show()


    }

    /**
     * 在Activity的onActivityResult方法中调用该方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
            images.clear()
            images_album.clear()
            images_album = PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>

            for (localUrl in images_album) {
                var position = if (images.size > 0) (images.size - 1) else 0
                when {
                    localUrl.isCompressed -> images.add(
                            position,
                            ImageBean(localUrl.compressPath, IMAGE_TYPE_LOCAL)
                    )
                    localUrl.isCut -> images.add(
                            position,
                            ImageBean(localUrl.cutPath, IMAGE_TYPE_LOCAL)
                    )
                    else -> images.add(
                            position,
                            ImageBean(localUrl.path, IMAGE_TYPE_LOCAL)
                    )
                }

            }
            if (images?.size > 0) {
                startUpload()
            }
        }
    }

    private var onUploadImagesListener: OnUploadImagesListener? = null
    /**
     * 发起上传
     */
    fun startUpload() {
        if (images.isEmpty() || images!![0].url.isNullOrEmpty()) {
            return
        }
        onUploadImagesListener?.uploadStart()

        UploadImageUtils.uploadImage(activity, type, images!![0].url!!, object : ProgressCallback {
            override fun onProgressCallback(progress: Double) {
                LogUtils.e("progress:" + progress)
            }

            override fun onProgressFailed() {
//                dismissLoading()
//                toast_msg("上传图片失败")
                onUploadImagesListener?.uploadError("")
            }

            override fun onProgressSuccess() {
//                mPresenter?.getUserInfo()
//                runOnUiThread {
//                    dismissLoading()
//                }
//                ExtraUtils.toasts("图片上传成功")
                onUploadImagesListener?.uploadComplete(arrayListOf())
                images_album.clear()
                //上传成功以后清除本地裁剪，压缩的缓存文件
                PictureFileUtils.deleteCacheDirFile(activity)
            }

        })
    }

    fun openCamera(
            enableCircleCrop: Boolean = false,
            enableRectangleCrop: Boolean = false,
            isZip: Boolean = true,
            maxNum: Int = maxSelectNum,
            selectedList: MutableList<LocalMedia>? = arrayListOf<LocalMedia>(),
            requestCode: Int = PictureConfig.CHOOSE_REQUEST
    ) {
        // 单独拍照
        PictureSelector.create(activity)
                .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .maxSelectNum(maxNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(if (maxNum == 1) PictureConfig.SINGLE else PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(enableCircleCrop || enableRectangleCrop)// 是否裁剪
                .compress(isZip)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(enableCircleCrop)// 是否圆形裁剪
                .showCropFrame(enableRectangleCrop)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectedList)// 是否传入已选图片
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(requestCode)//结果回调onActivityResult code
    }

    fun openAlbums(
            enableCircleCrop: Boolean = false,
            enableRectangleCrop: Boolean = false,
            isZip: Boolean = true,
            maxNum: Int = maxSelectNum,
            selectedList: MutableList<LocalMedia>? = arrayListOf<LocalMedia>(),
            requestCode: Int = PictureConfig.CHOOSE_REQUEST

    ) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//            .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(if (maxNum == 1) PictureConfig.SINGLE else PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(false)// 是否显示拍照按钮
                .enableCrop(enableCircleCrop || enableRectangleCrop)// 是否裁剪
                .compress(isZip)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(enableCircleCrop)// 是否圆形裁剪
                .showCropFrame(enableRectangleCrop)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectedList)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(requestCode)//结果回调onActivityResult code

    }

    /**
     *
     * 保存图片到本地
     * */
    private fun saveImage(bitmap: Bitmap?) {
        let {
            try {
                if (bitmap != null) {
                    val fileName = System.currentTimeMillis().toString() + ".jpg"
                    MediaStore.Images.Media.insertImage(
                            activity.contentResolver,
                            bitmap,
                            fileName,
                            ""
                    )
                    MediaScannerConnection.scanFile(
                            activity,
                            arrayOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/" + fileName),
                            null,
                            null
                    )
                    activity.toast("保存成功")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置图片上传监听器
     *
     * @param onUploadImagesListener
     */
    fun setOnUploadImagesListener(onUploadImagesListener: OnUploadImagesListener) {
        this.onUploadImagesListener = onUploadImagesListener
    }

    companion object {


        /**
         * ICON
         */
        val IMAGE_TYPE_ICON = 1

        /**
         * 网络图片
         */
        val IMAGE_TYPE_NET = 2

        /**
         * 本地图片
         */
        val IMAGE_TYPE_LOCAL = 3
    }

    interface OnUploadImagesListener {
        fun uploadComplete(result: List<String>)

        fun uploadError(msg: String) {//noting to do
        }

        fun uploadStart() {//noting to do
        }
    }
}


