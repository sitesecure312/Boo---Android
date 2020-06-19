package com.boo.app.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.api.Response;
import com.boo.app.api.request.PostManage;
import com.boo.app.api.response.PostManageResponse;
import com.boo.app.api.response.ServerResponse;
import com.boo.app.model.User;
import com.boo.app.ui.base.BaseActivity;
import com.boo.app.ui.utils.BitmapUtils;
import com.boo.app.ui.utils.DialogUtils;
import com.boo.app.ui.utils.ImageUtils;
import com.boo.app.ui.utils.KeyboardUtils;
import com.boo.app.ui.utils.PicassoUtils;
import com.boo.app.ui.utils.TintUtils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class PostToBooActivity extends BaseActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private static final int READ_STORAGE_REQUEST_CODE = 100;


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.post_photo_button)
    ImageView postPhotoBtn;
    @Bind(R.id.post_video_button)
    ImageView postVideoBtn;
    @Bind(R.id.post_location_button)
    ImageView postLocBtn;
    @Bind(R.id.photo_post)
    ImageView photoPost;
    @Bind(R.id.photo_divider)
    View photoDivider;
    @Bind(R.id.profile_image)
    ImageView profileImage;
    @Bind(R.id.post_field)
    EditText edContent;


    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();
    Uri mImageCaptureUri;
    User user;

    public static Intent getStartIntent(Context ctx, String uri) {
        Intent intent = new Intent(ctx, PostToBooActivity.class);
        intent.putExtra("path", uri);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_boo);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.post_to_boo);
        user = User.getCurrent();
        Picasso.with(this).load(ApiRequest.getMediaUrl(user.getUserPhoto())).placeholder(R.drawable.ic_no_avatar).fit().centerCrop().transform(circle)
                .into(profileImage);

        String photoPath = getIntent().getStringExtra("path");

            mImageCaptureUri = Uri.parse(photoPath);
            photoDivider.setVisibility(View.VISIBLE);
            photoPost.setVisibility(View.VISIBLE);
            Picasso.with(this).load(mImageCaptureUri).fit().into(photoPost);


        if (savedInstanceState != null) {
            String imageUri = savedInstanceState.getString("imageUri");
            if (imageUri != null) {
                mImageCaptureUri = Uri.parse(imageUri);
            }
        }

        TintUtils.setPressedTint(this, postPhotoBtn, R.color.colorGrayText, R.color.colorPrimary);
        TintUtils.setPressedTint(this, postVideoBtn, R.color.colorGrayText, R.color.colorPrimary);
        TintUtils.setPressedTint(this, postLocBtn, R.color.colorGrayText, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save:
                checkFields();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftKeyboard(this);
    }

    @OnClick(R.id.post_photo_button)
    public void onPostPhotoClick() {
        showImageDialog();
    }

    @OnClick(R.id.post_location_button)
    public void onPostLocationClick() {
        Intent locIntent = new Intent(PostToBooActivity.this, PlaceActivity.class);
        startActivity(locIntent);
    }

    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = new String[]{getString(R.string.take_photo), getString(R.string.from_gallery)};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        takePicture();
                        break;
                    case 1:
                        selectPicture();
                        break;
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    protected void checkFields() {
        PostManage postManage = new PostManage();
        String content = edContent.getText().toString();
        if (mImageCaptureUri == null && TextUtils.isEmpty(content)) {
            DialogUtils.showAlertDialog(this, R.string.post_empty);
            return;
        }
        final ProgressDialog pd = DialogUtils.showProgressDialog(this, R.string.please_wait);
        postManage.setUserId(user.getId());
        postManage.setManageType(PostManage.TYPE_ADD);
        postManage.setText(content);

        if (mImageCaptureUri != null) {
            convertImageAndSend(pd, mImageCaptureUri, postManage);
        } else {
            sendPost(pd, postManage);
        }

    }

    private void sendPost(final ProgressDialog pd, PostManage data) {
        Observable<PostManageResponse> observable = ApiRequest.getInstance().getApi().postManage(data);
        Observable.Transformer<PostManageResponse, PostManageResponse> transformer = bindToLifecycle();
        observable.compose(transformer).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostManageResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        e.printStackTrace();
                        String msg = Response.getResponse(e);
                        DialogUtils.showAlertDialog(PostToBooActivity.this, msg);
                    }

                    @Override
                    public void onNext(PostManageResponse response) {
                        pd.dismiss();
                        if (response.isSuccess()) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            DialogUtils.showAlertDialog(PostToBooActivity.this, Response.resolveCode(response));
                        }
                    }
                });

    }

    private void convertImageAndSend(final ProgressDialog pr, Uri uri, final PostManage data) {
        convertImageCall(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        data.setPhoto(result);
                        sendPost(pr, data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        pr.dismiss();
                        DialogUtils.showAlertDialog(PostToBooActivity.this, R.string.something_wrong);
                    }
                });
    }


    private Observable<String> convertImageCall(
            final Uri param) {
        return Observable.defer(new Func0<Observable<String>>() {
                                    @Override
                                    public Observable<String> call() {
                                        byte[] bytes = getBytes(param);
                                        String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                                        return Observable.just(encoded);
                                    }
                                }
        );
    }

    private byte[] getBytes(Uri uri) {
        byte[] bytes = BitmapUtils.getBytes(this, uri);
        return bytes;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            selectPicture();
        }
    }

    private void selectPicture() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_REQUEST_CODE);
            return;
        }

        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_FILE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_FILE);
        }
    }

    private void takePicture() {
        mImageCaptureUri = Uri.fromFile(new File(
                this.getExternalCacheDir(), String.valueOf(System.currentTimeMillis()
                + ".jpg")));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        photoDivider.setVisibility(View.VISIBLE);
        photoPost.setVisibility(View.VISIBLE);
        postPhotoBtn.setSelected(true);
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                Picasso.with(this).load(mImageCaptureUri).fit().centerCrop().into(photoPost);
                break;
            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                Picasso.with(this).load(mImageCaptureUri).fit().centerCrop().into(photoPost);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageCaptureUri != null) {
            outState.putString("imageUri", mImageCaptureUri.toString());
        }
    }
}
