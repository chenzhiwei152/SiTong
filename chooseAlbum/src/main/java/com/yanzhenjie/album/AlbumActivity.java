/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.adapter.AlbumContentAdapter;
import com.yanzhenjie.album.dialog.AlbumFolderBottomDialog;
import com.yanzhenjie.album.dialog.AlbumFolderDialog;
import com.yanzhenjie.album.dialog.AlbumImageErrorDialog;
import com.yanzhenjie.album.dialog.AlbumPreviewDialog;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.impl.OnCompatCompoundCheckListener;
import com.yanzhenjie.album.impl.OnCompatItemClickListener;
import com.yanzhenjie.album.task.AlbumScanner;
import com.yanzhenjie.album.task.Poster;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>全局相册，选择图片入口。</p>
 */
public class AlbumActivity extends AppCompatActivity implements AlbumFolderBottomDialog.DialogDismiss {

    private static final int PERMISSION_REQUEST_STORAGE = 200;
    private static final int PERMISSION_REQUEST_CAMERA = 201;

    private static final int ACTIVITY_REQUEST_CAMERA = 200;

    private static final String INSTANCE_CAMERA_FILE_PATH = "INSTANCE_CAMERA_FILE_PATH";

    private static ExecutorService sRunnableExecutor = Executors.newSingleThreadExecutor();

    private FrameLayout mToolbar;
    private Button mBtnPreview;
    private Button mBtnSwitchFolder;
    private RecyclerView mRvContentList;
    private GridLayoutManager mGridLayoutManager;
    private AlbumContentAdapter mAlbumContentAdapter;

    private int mToolBarColor;
    private int mAllowSelectCount;
    private int mCheckFolderIndex;

    private List<AlbumFolder> mAlbumFolders;
    private ArrayList<AlbumImage> mCheckedImages = new ArrayList<>(1);//选中的
    private List<AlbumImage> mTempCheckedImages;

    private AlbumFolderDialog mAlbumFolderSelectedDialog;
    private AlbumPreviewDialog mAlbumPreviewDialog;
    private AlbumFolderBottomDialog albumFolderBottomDialog;

    private String mCameraFilePath;
    List<String> hasChoosePath;

    boolean isCrop;//是否需要裁剪
    boolean isFolderShow = false;//文件夹弹窗是否显示了

    TextView tv_title;
    TextView tv_right;

    public static int spanCount = 3;
    public static int gridViewDriverHeight = 7;


    private int outPutX = 300;
    private int scaleX = 1;
    private int scaleY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.initScreen(this);
        setContentView(R.layout.album_activity_album);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        if (savedInstanceState != null) {
            mCameraFilePath = savedInstanceState.getString(INSTANCE_CAMERA_FILE_PATH);
        }

        Intent intent = getIntent();
        mToolBarColor = intent.getIntExtra(Album.KEY_INPUT_TOOLBAR_COLOR, ResourcesCompat.getColor(getResources(), R.color.albumColorPrimary, null));
        int statusColor = intent.getIntExtra(Album.KEY_INPUT_STATUS_COLOR, ResourcesCompat.getColor(getResources(), R.color.albumColorPrimaryDark, null));
        mAllowSelectCount = intent.getIntExtra(Album.KEY_INPUT_LIMIT_COUNT, Integer.MAX_VALUE);
        outPutX = intent.getIntExtra(Album.KEY_OUT_PUX_X, 300);
        scaleX = intent.getIntExtra(Album.KEY_ASPECT_RATIO_X, 1);
        scaleY = intent.getIntExtra(Album.KEY_ASPECT_RATIO_Y, 1);


        hasChoosePath = intent.getStringArrayListExtra(Album.KEY_HAS_CHOOSE_LIST);
        if (mAllowSelectCount == 1) {
            tv_right.setVisibility(View.GONE);
        }

        if (hasChoosePath == null) {
            hasChoosePath = Collections.emptyList();
        }
        final int hasChoosSize = hasChoosePath.size();
        for (int i = 0; i < hasChoosSize; i++) {
            String path = hasChoosePath.get(i);
            AlbumImage hasChooseAlbunImage = new AlbumImage(path);
            if (!mCheckedImages.contains(hasChooseAlbunImage)) {
                hasChooseAlbunImage.setChecked(true);
                mCheckedImages.add(hasChooseAlbunImage);
            }
        }
        initRightTextStatus();
        isCrop = intent.getBooleanExtra(Album.KEY_IS_CROP, false);
        initializeMain(statusColor);
        int normalColor = ContextCompat.getColor(this, R.color.albumWhiteGray);
        initializeContent(normalColor);
        setPreviewCount(mCheckedImages.size());
        scanImages();
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toResult(false);
            }
        });
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFolderDialog();
            }
        });
    }

    /**
     * Initialize up.
     */
    private void initializeMain(int statusColor) {
        mToolbar = (FrameLayout) findViewById(R.id.toolbar);
        mBtnPreview = (Button) findViewById(R.id.btn_preview);
        mBtnSwitchFolder = (Button) findViewById(R.id.btn_switch_dir);
        mRvContentList = (RecyclerView) findViewById(R.id.rv_content_list);
        mBtnPreview.setOnClickListener(mPreviewClick);
        mBtnSwitchFolder.setOnClickListener(mSwitchDirClick);
        setStatusBarColor(statusColor);
        mToolbar.setBackgroundColor(mToolBarColor);
    }

    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                window.setNavigationBarColor(ContextCompat.getColor(this, R.color.albumPrimaryBlack));
            }
        }
    }


    /**
     * Initialize content.
     */
    private void initializeContent(int normalColor) {
        mRvContentList.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(this, spanCount);
        mRvContentList.setLayoutManager(mGridLayoutManager);
        mRvContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                if (position % spanCount == 0)
                    outRect.set(gridViewDriverHeight, gridViewDriverHeight, gridViewDriverHeight, 0);
                else {
                    outRect.set(0, gridViewDriverHeight, gridViewDriverHeight, 0);
                }
            }
        });

        mAlbumContentAdapter = new AlbumContentAdapter(this, normalColor, mToolBarColor);
        mAlbumContentAdapter.setAddPhotoClickListener(mAddPhotoListener);
        mAlbumContentAdapter.setHasChoosePath(hasChoosePath);

        mAlbumContentAdapter.setItemClickListener(new OnCompatItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArrayList<AlbumImage> albumImages = mAlbumFolders.get(mCheckFolderIndex).getPhotos();
                if (mAllowSelectCount == 1 && isCrop) {
                    //去裁剪
                    getoCrop(albumImages.get(position).getPath());

                } else {
                    dismissPreviewDialog();
                    mAlbumPreviewDialog = new AlbumPreviewDialog(AlbumActivity.this, mToolBarColor, albumImages, mPreviewFolderCheckListener, position, contentHeight);
                    mAlbumPreviewDialog.show();
                }
            }
        });
        mAlbumContentAdapter.setOnCheckListener(mContentCheckListener);
        mRvContentList.setAdapter(mAlbumContentAdapter);
        mAlbumContentAdapter.setMaxChooseSize(mAllowSelectCount);
    }

    File cropFile;

    /**
     * 去裁减
     *
     * @param path
     */
    public void getoCrop(String path) {
        //去裁剪
        File file = new File(path);
        if (file.length() == 0) {
            showErrorImageDialog();
            return;
        }
        Uri uri = Uri.fromFile(file);

        File foder = new File(Environment.getExternalStorageDirectory(), PIC_STORE_PATH);
        if (!foder.exists()) {
            foder.mkdir();
        }
        cropFile = new File(foder, System.currentTimeMillis() + ".jpg");

        if (!cropFile.exists()) {
            try {
                cropFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri imageCropUri = Uri.fromFile(cropFile);
        cropImageUri(AlbumActivity.this, uri, 300, 300, CHOOSE_CROP, imageCropUri);
    }

    public static final int CHOOSE_CROP = 1004;
    public static final String PIC_STORE_PATH = "header";//

    /**
     * 裁剪图片
     *
     * @param activity
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public void cropImageUri(Activity activity, Uri uri, int outputX, int outputY, int requestCode, Uri toUri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1

        intent.putExtra("aspectX", scaleX);
        intent.putExtra("aspectY", scaleY);
        // 裁剪后输出图片的尺寸大小
        if (scaleX != scaleY) {
            intent.putExtra("outputX", outPutX);
            intent.putExtra("outputY", (outPutX * scaleY) / scaleX);
        } else {
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
        }


        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, toUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 扫描有照片的文件夹。
     */
    private void scanImages() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                sRunnableExecutor.execute(scanner);
            } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            }
        } else {
            sRunnableExecutor.execute(scanner);
        }
    }

    /**
     * 拍照点击监听。
     */
    private View.OnClickListener mAddPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                int permissionResult = ContextCompat.checkSelfPermission(AlbumActivity.this, Manifest.permission.CAMERA);
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(AlbumActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                }
            } else {
                startCamera();
            }
        }
    };

    /**
     * 启动相机拍照。
     */
    private void startCamera() {
        String outFileFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String outFilePath = AlbumUtils.getNowDateTime("yyyyMMdd_HHmmssSSS") + ".jpg";
        File file = new File(outFileFolder, outFilePath);
        mCameraFilePath = file.getAbsolutePath();
        AlbumUtils.startCamera(this, ACTIVITY_REQUEST_CAMERA, file);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(INSTANCE_CAMERA_FILE_PATH, mCameraFilePath);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (mAllowSelectCount == 1 && isCrop) {
                        //去裁减
                        getoCrop(mCameraFilePath);

                    } else {
                        setResult(mCameraFilePath);
                    }
                }
                break;
            case CHOOSE_CROP:
                if (resultCode == RESULT_OK) {
                    if (/*data != null&&*/cropFile != null) {
                     /*   Uri fileUri = data.getData();
                        String uriString = fileUri.getPath();*/
                        setResult(cropFile.getAbsolutePath());
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setResult(String path) {
        Intent intent = new Intent();
        ArrayList<String> pathList = new ArrayList<>();

        for (AlbumImage albumImage : mCheckedImages) {
            pathList.add(albumImage.getPath());
        }
//        intent.putParcelableArrayListExtra(Album.KEY_OUTPUT_ALBUM_LIST, mCheckedImages);
//        intent.putStringArrayListExtra(Album.KEY_OUTPUT_IMAGE_PATH_LIST, pathList);
//        setResult(RESULT_OK, intent);
        int size = pathList.size();
        if (size == 3) {
            pathList.remove(size - 1);
        }
        pathList.add(path);
        intent.putStringArrayListExtra(Album.KEY_OUTPUT_IMAGE_PATH_LIST, pathList);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    sRunnableExecutor.execute(scanner);
                } else {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_storage_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    toResult(true);
                                }
                            })
                            .show();
                }
                break;
            }
            case PERMISSION_REQUEST_CAMERA: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_camera_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 预览文件夹时某个item的选中监听。
     */
    private OnCompatCompoundCheckListener mPreviewFolderCheckListener = new OnCompatCompoundCheckListener() {
        @Override
        public void onCheck(CompoundButton compoundButton, int position, boolean isChecked) {
            mContentCheckListener.onCheck(compoundButton, position, isChecked);
        }
    };

    /**
     * 预览已选中的大图的监听。
     */
    private OnCompatCompoundCheckListener mPreviewCheckedImageCheckListener = new OnCompatCompoundCheckListener() {
        @Override
        public void onCheck(CompoundButton compoundButton, int position, boolean isChecked) {
            AlbumImage albumImage = mTempCheckedImages.get(position);
            albumImage.setChecked(isChecked);
            int i = mAlbumFolders.get(mCheckFolderIndex).getPhotos().indexOf(albumImage);
            if (i != -1) mAlbumContentAdapter.notifyItemChangedCompat(i);
            if (!isChecked) {
                mCheckedImages.remove(albumImage);
            }
            initRightTextStatus();
        }
    };


    public void showErrorImageDialog() {
        AlbumImageErrorDialog albumImageErrorDialog = new AlbumImageErrorDialog(AlbumActivity.this);
        albumImageErrorDialog.show();
    }

    /**
     * 选中监听。
     */
    private OnCompatCompoundCheckListener mContentCheckListener = new OnCompatCompoundCheckListener() {
        @Override
        public void onCheck(CompoundButton compoundButton, int position, boolean isChecked) {
            AlbumImage albumImage = mAlbumFolders.get(mCheckFolderIndex).getPhotos().get(position);
            if (isChecked) {
                File file = new File(albumImage.getPath());
                if (file.length() == 0) {
                    compoundButton.setChecked(false);
                    albumImage.setChecked(false);
                    showErrorImageDialog();
//                Toast.makeText(AlbumActivity.this, "无法加载该图片", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            albumImage.setChecked(isChecked);
            if (isChecked) {
                if (!mCheckedImages.contains(albumImage))
                    mCheckedImages.add(albumImage);
            } else {
                mCheckedImages.remove(albumImage);
            }
            initRightTextStatus();
            int hasCheckSize = mCheckedImages.size();
            if (hasCheckSize > mAllowSelectCount) {
                String hint = getString(R.string.album_check_limit);
                Toast.makeText(AlbumActivity.this, String.format(Locale.getDefault(), hint, mAllowSelectCount), Toast.LENGTH_LONG).show();
                mCheckedImages.remove(albumImage);
                compoundButton.setChecked(false);
                albumImage.setChecked(false);
            } else {
                setPreviewCount(hasCheckSize);
            }
            updataAdapterItem(position);
        }
    };


    /**
     * 更新adapter的item
     *
     * @param position
     */
    public void updataAdapterItem(final int position) {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mAlbumContentAdapter.notifyItemChangedCompat(position);
            }
        };
        handler.post(r);
    }


    /**
     * 设置选中的图片数。
     *
     * @param count 数字。
     */
    public void setPreviewCount(int count) {
        mBtnPreview.setText(" (" + count + ")");
        if (count != 0) {
            tv_right.setText("确定(" + count + ")");
        } else {
            tv_right.setText("确定");
        }
//        mToolbar.setSubtitle(count + "/" + mAllowSelectCount);
    }


    /**
     * 切换文件夹按钮被点击。
     */
    private View.OnClickListener mSwitchDirClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showFolderDialog();
        }
    };

    public void showFolderDialog() {
        if (null == albumFolderBottomDialog) {
            albumFolderBottomDialog = new AlbumFolderBottomDialog(AlbumActivity.this, mAlbumFolders, new OnCompatItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    showAlbum(position);
                }
            });
            if (mAllowSelectCount == 1) {
                albumFolderBottomDialog.setRightVisivle(View.GONE);
            }
            albumFolderBottomDialog.setAlbumDismissListener(this);
        }
        if (!isFolderShow) {
            albumFolderBottomDialog.show();
            setTitleRightIcon(R.drawable.title_up);
            isFolderShow = true;
        } else {
            setTitleRightIcon(R.drawable.title_down);
            albumFolderBottomDialog.dismiss();
            isFolderShow = false;
        }


    }

    /**
     * 预览按钮被点击。
     */
    private View.OnClickListener mPreviewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCheckedImages.size() == 0)
                return;
            mTempCheckedImages = new ArrayList<>(mCheckedImages);
            Collections.copy(mTempCheckedImages, mCheckedImages);

            dismissPreviewDialog();

            mAlbumPreviewDialog = new AlbumPreviewDialog(AlbumActivity.this, mToolBarColor, mTempCheckedImages, mPreviewCheckedImageCheckListener, 0, contentHeight);
            mAlbumPreviewDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mTempCheckedImages != null) {
                        mTempCheckedImages.clear();
                        mTempCheckedImages = null;
                    }
                }
            });
            mAlbumPreviewDialog.show();
        }
    };

    /**
     * 关闭预览窗口。
     */

    private void dismissPreviewDialog() {
        if (mAlbumPreviewDialog != null && mAlbumPreviewDialog.isShowing())
            mAlbumPreviewDialog.dismiss();
    }

    /**
     * 显示某个文件夹的图片。
     *
     * @param index 选中的文件夹的item。
     */
    private void showAlbum(int index) {
        mCheckFolderIndex = index;
        AlbumFolder albumFolder = mAlbumFolders.get(index);
        mBtnSwitchFolder.setText(albumFolder.getName());
        tv_title.setText(albumFolder.getName());
        mAlbumContentAdapter.notifyDataSetChanged(albumFolder.getPhotos());
        mGridLayoutManager.scrollToPosition(0);
    }


    /**
     * 设置相册弹出后dialog的标题
     *
     * @param tv_Dialogtitle
     * @param tv_Dialogright
     */
    public void setDialogTitle(TextView tv_Dialogtitle, TextView tv_Dialogright) {
        tv_Dialogtitle.setText(tv_title.getText());
        tv_Dialogright.setText(tv_right.getText());
    }

    public int choosePosition;

    public void setDialogChoosePosition(int choosePosition) {
        this.choosePosition = choosePosition;
    }


    /**
     * 选择完成或者取消。
     */
    public void toResult(boolean cancel) {
        if (cancel) {
            setResult(RESULT_CANCELED);
            super.finish();
        } else {
            int allSize = mAlbumFolders.get(0).getPhotos().size();
            int checkSize = mCheckedImages.size();
            if (allSize > 0 && checkSize == 0) {
                Toast.makeText(this, R.string.album_check_little, Toast.LENGTH_LONG).show();
            } else if (checkSize == 0) {
                setResult(RESULT_CANCELED);
                super.finish();
            } else {
                Intent intent = new Intent();
                ArrayList<String> pathList = new ArrayList<>();
                for (AlbumImage albumImage : mCheckedImages) {
                    pathList.add(albumImage.getPath());
                }
                intent.putParcelableArrayListExtra(Album.KEY_OUTPUT_ALBUM_LIST, mCheckedImages);
                intent.putStringArrayListExtra(Album.KEY_OUTPUT_IMAGE_PATH_LIST, pathList);
                setResult(RESULT_OK, intent);
                super.finish();
            }
        }
    }

    /**
     * 拿到已经选择的大小。
     *
     * @return 大小。
     */
    public int getCheckedImagesSize() {
        return mCheckedImages.size();
    }

    /**
     * 拿到允许选择的数量。
     *
     * @return 数量int。
     */
    public int getAllowCheckCount() {
        return mAllowSelectCount;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_album, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == android.R.id.home) {
//            toResult(true);
//        } else if (itemId == R.id.menu_gallery_finish) {
//            toResult(false);
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        toResult(true);
    }

    /**
     * Init ui.
     */
    private Runnable initialize = new Runnable() {
        @Override
        public void run() {
            if (!AlbumActivity.this.isFinishing()) {
                showAlbum(0);
            } else {
                mAlbumFolders.clear();
                mAlbumFolders = null;
            }
        }
    };

    /**
     * Scan image.
     */
    private Runnable scanner = new Runnable() {
        @Override
        public void run() {
            mAlbumFolders = AlbumScanner.getInstance().getPhotoAlbum(AlbumActivity.this, mCheckedImages);
            Poster.getInstance().post(initialize);
        }
    };

    private boolean initializeHeight;
    private int contentHeight;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initializeHeight) {
            initializeHeight = true;
            contentHeight = DisplayUtils.screenHeight;
        }
    }

    @Override
    protected void onDestroy() {
       /* if (mAlbumFolderSelectedDialog != null && mAlbumFolderSelectedDialog.isShowing())
            mAlbumFolderSelectedDialog.behaviorHide();*/
        dismissPreviewDialog();
        super.onDestroy();
    }

    /**
     * 文件夹弹窗消失监听
     */
    @Override
    public void albumDialogDismiss() {
        setTitleRightIcon(R.drawable.title_down);
        isFolderShow = false;
    }

    /**
     * @param id icon资源id  设置标题右侧的图片
     */
    private void setTitleRightIcon(int id) {
        Drawable nav_up = getResources().getDrawable(id);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tv_title.setCompoundDrawables(null, null, nav_up, null);
    }

    /**
     * 初始化 右边按钮的状态 是否是 可点击的
     */
    public void initRightTextStatus() {
        if (mCheckedImages.size() != 0) {
            tv_right.setEnabled(true);
//            mRight_text.setTextColor(getResources().getColor(R.color.color_333333));
        } else {
            tv_right.setEnabled(false);
//            mRight_text.setTextColor(getResources().getColor(R.color.color_666666));
        }

    }


}