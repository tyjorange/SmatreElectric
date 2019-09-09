package com.rejuvee.smartelectric.family.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2018/3/23.
 */
public class ImageUtil {
    public static final String CROP_CACHE_FILE_NAME = "/temp.jpg";
    public static final int REQUEST_GALLERY = 0xa0;
    public static final int REQUEST_CAMERA = 0xa1;
    public static final int RE_GALLERY = 127;
    public static final int RE_CAMERA = 128;
    private Context context;
    private Uri imgUri;

    private ImageUtil(Context context) {
        this.context = context;
    }

    private static ImageUtil instance;

    public static ImageUtil getCropHelperInstance(Context context) {
        if (instance == null) {
            instance = new ImageUtil(context);
        }
        return instance;
    }


    public void sethandleResultListerner(CropHandler handler, int requestCode,
                                         int resultCode, Intent data, Uri tempUri) {
        if (handler == null)
            return;
        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
        } else if (resultCode == Activity.RESULT_OK) {
            Bitmap photo;
            switch (requestCode) {
                case RE_CAMERA:
                    if (data == null || data.getExtras() == null) {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                        return;
                    }
//                    photo = data.getExtras().getParcelable("data");
//                    try {
//                        photo.compress(Bitmap.CompressFormat.JPEG, 30,
//                                new FileOutputStream(getCachedCropFile()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                  Bitmap bm = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                    Bitmap bitmap = BitmapFactory.decodeFile(tempUri.getPath());
                    handler.onPhotoCropped(bitmap, requestCode);
                    break;
                case RE_GALLERY:
//                    if (data == null || data.getExtras() == null)
                    if (data == null) {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                        return;
                    }
//                    photo = data.getExtras().getParcelable("data");
//                    try {
//                      photo.compress(Bitmap.CompressFormat.JPEG, 30,
//                           new FileOutputStream(getFile(buildUri(context))));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    Bitmap bitmapca = BitmapFactory.decodeFile(tempUri.getPath());
                    handler.onPhotoCropped(bitmapca, requestCode);
                    break;
                case REQUEST_CAMERA:
                    Intent intent = buildCropIntent(tempUri, tempUri);
                    if (handler.getContext() != null) {
                        handler.getContext().startActivityForResult(intent, RE_CAMERA);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    imgUri = tempUri;
                    break;
                case REQUEST_GALLERY:
                    if (data == null) {
                        handler.onCropFailed("Data MUST NOT be null!");
                        return;
                    }
                    Intent intent2 = buildCropIntent(data.getData(), tempUri);
                    if (handler.getContext() != null) {
                        handler.getContext().startActivityForResult(intent2, RE_GALLERY);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    imgUri = tempUri;
                    break;
            }
        }
    }

    public Intent buildGalleryIntent() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        return galleryIntent;
    }

    public Intent buildCameraIntent() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSdcard()) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        }
        return cameraIntent;
    }

    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private Intent buildCropIntent(Uri uri, Uri tempUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 300);
        cropIntent.putExtra("outputY", 300);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);//图像输出
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //不启用人脸识别
        cropIntent.putExtra("noFaceDetection", false);
        return cropIntent;
    }

    public interface CropHandler {

        void onPhotoCropped(Bitmap imgUrl, int requesCode);

        void onCropCancel();

        void onCropFailed(String message);

        Activity getContext();
    }
}