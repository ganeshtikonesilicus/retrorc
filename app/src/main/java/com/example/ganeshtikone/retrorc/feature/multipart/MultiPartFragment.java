package com.example.ganeshtikone.retrorc.feature.multipart;


import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ganeshtikone.retrorc.R;
import com.example.ganeshtikone.retrorc.network.RequestService;

import io.reactivex.disposables.CompositeDisposable;
import java.io.File;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiPartFragment extends Fragment {

  private static final int IMAGE_PICK_REQUEST_CODE = 9001;
  private static final int RUNTIME_REQUEST_CODE = 8001;

  /**
   *
   */
  private AppCompatImageView imageViewPicked;

  /**
   * RxAndroid init object
   */
  private CompositeDisposable compositeDisposable;

  /**
   *
   */
  RequestService requestService;

  /**
   *
   */
  private Realm realm;

  public MultiPartFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    realm = Realm.getDefaultInstance();
    compositeDisposable = new CompositeDisposable();
    requestService = new RequestService();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_multi_part, container, false);
    initUI(rootView);
    return rootView;
  }

  /**
   * Initialise UI
   *
   * @param rootView inflated view
   */
  private void initUI(View rootView) {

    imageViewPicked = rootView.findViewById(R.id.imagePicked);

    AppCompatButton buttonPickUpload = rootView.findViewById(R.id.buttonPickUpload);
    buttonPickUpload.setOnClickListener(v -> openAndUpload());
  }

  /**
   * Open and Upload Image
   */
  private void openAndUpload() {

    // request RunTime Permission
    requestRunTimePermission();
  }

  /**
   *
   */
  private void requestRunTimePermission() {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      if (!isPermissionAlreadyGranted()) {
        makeRequest();
      } else {
        openGallery();
      }

    } else {
      openGallery();
    }
  }

  private void makeRequest() {

    requestPermissions(
        new String[]{permission.READ_EXTERNAL_STORAGE},
        RUNTIME_REQUEST_CODE
    );
  }

  private boolean isPermissionAlreadyGranted() {

    int permissionStatus = ContextCompat.checkSelfPermission(
        getActivity(),
        permission.READ_EXTERNAL_STORAGE
    );

    return permissionStatus == PackageManager.PERMISSION_GRANTED;
  }

  private void openGallery() {

    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (IMAGE_PICK_REQUEST_CODE == requestCode
        && RESULT_OK == resultCode
        && null != data) {

      Uri selectedImage = data.getData();

      if (null != selectedImage) {
        Log.e(TAG, selectedImage.toString());
        uploadImage(selectedImage);
      } else {
        Log.e(TAG, "NO URI FOUND");
      }


    }
  }

  /**
   * Upload Image
   */
  private void uploadImage(Uri uri) {

    String filePath = getRealPathFromURI(uri);
    File file = new File(filePath);


    RequestBody requestFile = RequestBody
        .create(MediaType.parse(getActivity().getContentResolver().getType(uri)), file);

    requestService.uploadImage(requestFile)
        .subscribe(this::handleResponse, this::handleError);

  }

  private void handleError(Throwable throwable) {
    Log.e(TAG, throwable.getMessage());
  }

  private void handleResponse(String response) {
    Log.e(TAG, response);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (RUNTIME_REQUEST_CODE == requestCode
        && grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      openGallery();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    compositeDisposable.clear();
  }


  /*
   * This method is fetching the absolute path of the image file
   * if you want to upload other kind of files like .pdf, .docx
   * you need to make changes on this method only
   * Rest part will be the same
   * */
  private String getRealPathFromURI(Uri contentUri) {
    String[] proj = {MediaStore.Images.Media.DATA};
    CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
    Cursor cursor = loader.loadInBackground();
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    String result = cursor.getString(column_index);
    cursor.close();
    return result;
  }
}
