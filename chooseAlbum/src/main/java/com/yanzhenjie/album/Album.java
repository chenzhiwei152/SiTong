/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.album;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.album.entity.AlbumImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>入口类。</p>
 */
public class Album {

    public static String KEY_INPUT_LIMIT_COUNT = "KEY_INPUT_LIMIT_COUNT";
    public static String KEY_INPUT_TOOLBAR_COLOR = "KEY_INPUT_TOOLBAR_COLOR";
    public static String KEY_INPUT_STATUS_COLOR = "KEY_INPUT_STATUS_COLOR";
    public static String KEY_OUTPUT_IMAGE_PATH_LIST = "KEY_OUTPUT_IMAGE_PATH_LIST";
    public static String KEY_OUTPUT_ALBUM_LIST = "KEY_OUTPUT_ALBUM_LIST";

    public static String KEY_HAS_CHOOSE_LIST = "KEY_HAS_CHOOSE_LIST";
    public static String KEY_IS_CROP = "KEY_IS_CROP";
    public static String KEY_ASPECT_RATIO_X = "KEY_ASPECT_RATIO_X";
    public static String KEY_ASPECT_RATIO_Y = "KEY_ASPECT_RATIO_Y";
    public static String KEY_OUT_PUX_X = "KEY_OUT_PUX_X";


    /**
     * @param activity    接受文件的Activity。
     * @param requestCode 请求码。
     */
    public static void startAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity    接受文件的Activity。
     * @param requestCode 请求码。
     * @param limitCount  最多能选择多少图片。
     */
    public static void startAlbum(Activity activity, int requestCode, int limitCount) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, limitCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity       接受文件的Activity。
     * @param requestCode    请求码。
     * @param limitCount     最多能选择多少图片。
     * @param toolbarColor   Toolbar 颜色。
     * @param statusBarColor 状态栏颜色。
     */
    public static void startAlbum(Activity activity, ArrayList<String> hasChoose, int requestCode, int limitCount, @ColorInt int toolbarColor, @ColorInt int statusBarColor) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putStringArrayListExtra(KEY_HAS_CHOOSE_LIST, hasChoose);
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, limitCount);
        intent.putExtra(KEY_INPUT_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(KEY_INPUT_STATUS_COLOR, statusBarColor);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 需要裁剪
     *
     * @param activity
     * @param hasChoose
     * @param requestCode
     * @param toolbarColor
     * @param statusBarColor
     */
    public static void startAlbumWithCrop(Activity activity, ArrayList<String> hasChoose, int requestCode, @ColorInt int toolbarColor, @ColorInt int statusBarColor) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putStringArrayListExtra(KEY_HAS_CHOOSE_LIST, hasChoose);
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, 1);
        intent.putExtra(KEY_INPUT_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(KEY_INPUT_STATUS_COLOR, statusBarColor);
        intent.putExtra(KEY_IS_CROP, true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪 带宽高比
     * @param activity
     * @param hasChoose
     * @param requestCode
     * @param toolbarColor
     * @param statusBarColor
     * @param outPutX
     * @param scaleX
     * @param scaleY
     */
    public static void startAlbumWithCrop(Activity activity, ArrayList<String> hasChoose, int requestCode, @ColorInt int toolbarColor, @ColorInt int statusBarColor ,int outPutX, int scaleX , int scaleY) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putStringArrayListExtra(KEY_HAS_CHOOSE_LIST, hasChoose);
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, 1);
        intent.putExtra(KEY_INPUT_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(KEY_INPUT_STATUS_COLOR, statusBarColor);
        intent.putExtra(KEY_IS_CROP, true);
        intent.putExtra(KEY_OUT_PUX_X, outPutX);
        intent.putExtra(KEY_ASPECT_RATIO_X, scaleX);
        intent.putExtra(KEY_ASPECT_RATIO_Y, scaleY);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment    接受文件的Fragment。
     * @param requestCode 请求码。
     */
    public static void startAlbum(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment    接受文件的Fragment。
     * @param requestCode 请求码。
     * @param limitCount  最多能选择多少图片。
     */
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, limitCount);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment       接受文件的Fragment。
     * @param requestCode    请求码。
     * @param limitCount     最多能选择多少图片。
     * @param toolbarColor   Toolbar 颜色。
     * @param statusBarColor 状态栏颜色。
     */
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount, @ColorInt int toolbarColor, @ColorInt int statusBarColor) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, limitCount);
        intent.putExtra(KEY_INPUT_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(KEY_INPUT_STATUS_COLOR, statusBarColor);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 解析接受到的结果，开发者需要判断{@code resultCode = Activity.RESULT_OK}.
     *
     * @param intent {@code Intent} from {@code onActivityResult(int, int, Intent)}.
     * @return {@code List<String>}.
     */
    public static
    @NonNull
    List<String> parseResult(Intent intent) {
        List<String> pathList = intent.getStringArrayListExtra(KEY_OUTPUT_IMAGE_PATH_LIST);
        if (pathList == null)
            pathList = Collections.emptyList();
        return pathList;
    }

    public static
    @NonNull
    List<AlbumImage> parseResultAlbum(Intent intent) {
        List<AlbumImage> pathList = intent.getParcelableArrayListExtra(KEY_OUTPUT_ALBUM_LIST);
        if (pathList == null)
            pathList = Collections.emptyList();
        return pathList;
    }

}