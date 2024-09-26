package com.example.myapplication.rutina.business.repository;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageSource {

    private final Activity activity;
    private final ActivityResultLauncher<Intent> cameraLauncher;
    private final ActivityResultLauncher<Intent> galleryLauncher;
    private String currentPhotoPath;

    public ImageSource(Activity activity, ActivityResultLauncher<Intent> cameraLauncher, ActivityResultLauncher<Intent> galleryLauncher) {
        this.activity = activity;
        this.cameraLauncher = cameraLauncher;
        this.galleryLauncher = galleryLauncher;
    }

    // Método para abrir la cámara
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }

    // Método para abrir la galería
    public void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhotoIntent);
    }

    // Método para obtener un Bitmap desde un Uri
    public Bitmap getImageFromUri(Uri uri) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return compressImage(imageBitmap);  // Comprimir la imagen
    }
    // Método para guardar la imagen capturada en almacenamiento local
    public String saveImageToStorage(Bitmap bitmap) {
        FileOutputStream outStream = null;
        File storageDir = activity.getExternalFilesDir(null);
        try {
            File imageFile = File.createTempFile("profile_image_", ".jpg", storageDir);
            outStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outStream);
            outStream.flush();
            currentPhotoPath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return currentPhotoPath;
    }

    // Método para comprimir la imagen a 600x600 píxeles
    public Bitmap compressImage(Bitmap original) {
        return Bitmap.createScaledBitmap(original, 600, 600, true);
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }
}
