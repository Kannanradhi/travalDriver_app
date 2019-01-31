package com.cabily.cabilydriver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cabily.cabilydriver.Pojo.RegistrationInfoModel;
import com.cabily.cabilydriver.widgets.SnackFlashBar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationLoginDetailsActivity extends FragmentActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "imageupload";
    private static final String IMAGE_DIRECTORY_NAME = "Cabily_image";
    private static final int REQUEST_TAKE_PHOTO = 101;
    private static final int REQUEST_GALLERY_PHOTO = 102;

    private View headerView;
    private RelativeLayout headerBackRl, mainRl;
    private SnackFlashBar snackFlashBar;
    private CollapsingToolbarLayout headerCollapseTL;

    private TextView loginDetailsNextTv;
    private EditText driverNameEt, driverEmailEt, passwordEt, confrmPasswordEt;
    private ImageView driverImageAddIv;
    private CircularImageView driverImageIv;
    //    private RegistrationInfoModel registrationInfoModel;
    //Image upload
    private Dialog photo_dialog;
    private Uri camera_FileUri;
    private Bitmap selectedBitmap;
    private byte[] byteArray;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finish();
            CloseKeyBoard();
            overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    chooseimage();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    final String picturePath = camera_FileUri.getPath();
                    File curFile = new File(picturePath);
                    Uri picUri = Uri.fromFile(curFile);

                    UCrop.Options Uoptions = new UCrop.Options();
                    Uoptions.setStatusBarColor(getResources().getColor(R.color.app_color));
                    Uoptions.setToolbarColor(getResources().getColor(R.color.app_color));
                    Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.app_color));
                    Uoptions.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                    Uoptions.setCompressionQuality(70);


                    UCrop.of(picUri, picUri)
                            .withMaxResultSize(8000, 8000)
                            .withOptions(Uoptions)
                            .start(RegistrationLoginDetailsActivity.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                try {
                    Uri selectedImage = data.getData();
                    if (selectedImage.toString().startsWith("content://com.sec.android.gallery3d.provider")) {
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        final String picturePath = c.getString(columnIndex);
                        c.close();
                        File curFile = new File(picturePath);

                        Uri picUri = Uri.fromFile(curFile);

                        UCrop.Options Uoptions = new UCrop.Options();
                        Uoptions.setStatusBarColor(getResources().getColor(R.color.app_color));
                        Uoptions.setToolbarColor(getResources().getColor(R.color.app_color));
                        Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.app_color));
                        Uoptions.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        Uoptions.setCompressionQuality(70);

                        UCrop.of(picUri, picUri)
                                .withMaxResultSize(8000, 8000)
                                .withOptions(Uoptions)
                                .start(RegistrationLoginDetailsActivity.this);

                    } else {
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        final String picturePath = c.getString(columnIndex);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        File curFile = new File(picturePath);

                        Uri picUri = Uri.fromFile(curFile);
                        UCrop.Options Uoptions = new UCrop.Options();
                        Uoptions.setStatusBarColor(getResources().getColor(R.color.app_color));
                        Uoptions.setToolbarColor(getResources().getColor(R.color.app_color));
                        Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.app_color));


                        UCrop.of(picUri, picUri)
                                .withMaxResultSize(8000, 8000)
                                .withOptions(Uoptions)
                                .start(RegistrationLoginDetailsActivity.this);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == UCrop.REQUEST_CROP) {

                final Uri resultUri = UCrop.getOutput(data);
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
                driverImageIv.setImageBitmap(selectedBitmap);
                driverImageIv.setImageURI(resultUri);
//                UploadDriverImage(Iconstant.Edit_profile_image_url);

            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            System.out.println("========muruga cropError===========" + cropError);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_login_details);
//for android.os.FileUriExposedException target version>24
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Init();
        headerBackRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CloseKeyBoard();
                overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
            }
        });
        driverImageAddIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    if (!checkAccessFineLocationPermission() || !checkAccessCoarseLocationPermission() || !checkWriteExternalStoragePermission()) {
                        requestPermission();
                    } else {
                        chooseimage();
                    }
                } else {
                    chooseimage();
                }

            }
        });

        loginDetailsNextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (driverNameEt.getText().toString().trim().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.login_details_name_empty_error), mainRl, "error");
                } else if (driverEmailEt.getText().toString().trim().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.login_details_email_empty_error), mainRl, "error");
                } else if (!isValidEmail(driverEmailEt.getText().toString().replace(" ", ""))) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.login_details_email_invalid_error), mainRl, "error");
                } else if (passwordEt.getText().toString().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.login_details_password_empty_error), mainRl, "error");

                } else if (!confrmPasswordEt.getText().toString().equalsIgnoreCase(passwordEt.getText().toString())) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.login_details_confirm_password_mismatch_error), mainRl, "error");
                } else {

                    RegistrationInfoModel registrationInfoModel = new RegistrationInfoModel();
                    registrationInfoModel.setLoginDetailsAttr(driverNameEt.getText().toString(), driverEmailEt.getText().toString(), passwordEt.getText().toString());

                    Intent nextIntent = new Intent(RegistrationLoginDetailsActivity.this, RegistrationDriverDetailsActivity.class);
                    nextIntent.putExtra("modelObject", registrationInfoModel);
                    CloseKeyBoard();
                    startActivity(nextIntent);
                    overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
                }

            }
        });
    }

    private void Init() {

        headerView = findViewById(R.id.header_registration);
        headerBackRl = (RelativeLayout) headerView.findViewById(R.id.back_layout);
        snackFlashBar = new SnackFlashBar(RegistrationLoginDetailsActivity.this);
        mainRl = (RelativeLayout) findViewById(R.id.main_layout);
        headerCollapseTL = (CollapsingToolbarLayout) headerView.findViewById(R.id.collapsing_toolbar);
        headerCollapseTL.setTitleEnabled(true);
        headerCollapseTL.setCollapsedTitleTextColor(getResources().getColor(R.color.white_color));
        headerCollapseTL.setTitle(getResources().getString(R.string.login_details_header_label));
        headerCollapseTL.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        headerCollapseTL.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);


        loginDetailsNextTv = (TextView) findViewById(R.id.registration_login_details_next_tv);
        driverNameEt = (EditText) findViewById(R.id.registration_login_name_editText);
        driverEmailEt = (EditText) findViewById(R.id.registration_login_email_editText);
        passwordEt = (EditText) findViewById(R.id.registration_login_password_editText);
        confrmPasswordEt = (EditText) findViewById(R.id.registration_login_confrm_passwrd_editText);
        driverImageAddIv = (ImageView) findViewById(R.id.registration_login_iv_add_profile);
        driverImageIv = (CircularImageView) findViewById(R.id.registration_login_iv_profile);
//        Intent in = getIntent();
//        registrationInfoModel = (RegistrationInfoModel) in.getSerializableExtra("modelObject");

        passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confrmPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    // --------------------Method for choose image to edit profileimage--------------------
    private void chooseimage() {
        photo_dialog = new Dialog(RegistrationLoginDetailsActivity.this);
        photo_dialog.getWindow();
        photo_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photo_dialog.setContentView(R.layout.image_upload_dialog);
        photo_dialog.setCanceledOnTouchOutside(true);
        photo_dialog.getWindow().getAttributes().windowAnimations = R.style.Animations_photo_Picker;
        photo_dialog.show();
        photo_dialog.getWindow().setGravity(Gravity.CENTER);

        RelativeLayout camera = (RelativeLayout) photo_dialog
                .findViewById(R.id.profilelayout_takephotofromcamera);
        RelativeLayout gallery = (RelativeLayout) photo_dialog
                .findViewById(R.id.profilelayout_takephotofromgallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                photo_dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                photo_dialog.dismiss();
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_FileUri = getOutputMediaFileUri(1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, camera_FileUri);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_PHOTO);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void CloseKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    //-------------------------code to Check Email Validation-----------------------
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private boolean checkAccessFineLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAccessCoarseLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWriteExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


}
