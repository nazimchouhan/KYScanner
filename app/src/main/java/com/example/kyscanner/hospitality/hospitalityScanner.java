package com.example.kyscanner.hospitality;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kyscanner.R;
import com.example.kyscanner.mainclasses.Format;

import com.example.kyscanner.model.hospitalityModel;
import com.example.kyscanner.security.JWTUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class hospitalityScanner extends AppCompatActivity {
    private View scanLine, flashLight;
    private PreviewView cameraPreview;
    private androidx.appcompat.widget.Toolbar scannerToolbar;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private ProcessCameraProvider cameraProvider;
    ProgressBar ScanProgressBar;
    private long lastAnalysisTime = 0;
    private Camera camera;
    private CameraControl cameraControl;
    private boolean isFlashOn, isScanning, isQRCodeDetected, isOverlayShown;
    private BarcodeScanner barcodeScanner;
    private ExecutorService cameraExecutor;
    private long startTime;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        ScanProgressBar=findViewById(R.id.ScanprogressBar);
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAllPotentialBarcodes()// Scan only QR codes
                .build();
        barcodeScanner=BarcodeScanning.getClient(options);

        checkAndRequestPermissions();

        scannerToolbar = findViewById(R.id.ScannerToolbar);
        cameraPreview = findViewById(R.id.PreviewCameraScanner);
        flashLight = findViewById(R.id.FlashLight);
        scanLine = findViewById(R.id.scanningLine);
        barcodeScanner = BarcodeScanning.getClient();

        setSupportActionBar(scannerToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Scan QR Code");
        }
        cameraExecutor = Executors.newSingleThreadExecutor();
        new Handler(Looper.getMainLooper()).post(() -> scanLine.setVisibility(View.VISIBLE));
        startCamera();
        startScanLineAnimation();
        flashLight.setOnClickListener(v -> {
            if (cameraControl != null) {
                isFlashOn = !isFlashOn;
                cameraControl.enableTorch(isFlashOn);
            }
        });
    }
    private void startScanLineAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(scanLine, "translationY", 0f, 800f);
        animator.setDuration(1500);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.start();
    }

    private void startCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll(); // Ensure previous camera instance is released
        }

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setTargetResolution(new Size(640, 480))
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, image -> {
                    if (System.currentTimeMillis() - lastAnalysisTime < 500) {
                        image.close(); // Skip this frame
                        return;
                    }
                    lastAnalysisTime = System.currentTimeMillis();
                    @SuppressLint("UnsafeOptInUsageError")
                    Image mediaImage = image.getImage();
                    if (mediaImage != null && !isQRCodeDetected) {
                        InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                        startTime = System.currentTimeMillis();
                        scanQRCode(inputImage, image);
                    } else {
                        image.close();
                    }
                });

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
                cameraControl = camera.getCameraControl();
            } catch (Exception e) {
                Log.e("Camera Error", "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void scanQRCode(InputImage image, ImageProxy imageProxy) {

        if (isScanning) {
            imageProxy.close();
            return;
        }
        isScanning = true;

        barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (barcodes.isEmpty()) {
                        isScanning = false;
                        imageProxy.close();
                        return;
                    }

                    Barcode barcode = barcodes.get(0);
                    String Result = barcode.getRawValue();
                    Log.e("Result","The QR Result is:" + Result);

                    String mainQrResult=JWTUtil.decodeJWT(Result);
                    Log.e("KyID","The KyID is :"+ mainQrResult);


                    Toast.makeText(this, "QRRESULT is :"+ mainQrResult, Toast.LENGTH_SHORT).show();
                    runOnUiThread(() ->ScanProgressBar.setVisibility(View.VISIBLE));
                    long scanTime = System.currentTimeMillis() - startTime;

                    if (mainQrResult != null) {
                        // Initialize the Handler with the main thread's Looper
                        Handler handler = new Handler(Looper.getMainLooper());

                        // Execute database operation on a background thread

                        // Execute database operation on a background thread
                        executor.execute(() -> {
                            Future<List<hospitalityModel>> futureList = hospitalityUserdetail.fetchHospitalityUserDetail(mainQrResult);
                            try {
                                List<hospitalityModel> userDetails = futureList.get();
                                if(userDetails.size()==1){
                                    hospitalityModel model = userDetails.get(0);
                                    // Switch to the main thread for UI updates
                                    handler.post(() -> {
                                        if (model != null) {
                                            Log.e("UserDetail", "Successfully Fetched model");
                                            Log.e("KyID","The KYID is :" + model.getKyID());
                                            Log.e("Name","The Name is :" + model.getName());

                                            Intent intent = new Intent(hospitalityScanner.this, hospitalityView.class);
                                            intent.putExtra("KYId", model.getKyID());
                                            intent.putExtra("userName", model.getName());
                                            intent.putExtra("college",model.getCollege());
                                            intent.putExtra("Gmail", model.getGmail());
                                            intent.putExtra("mobileNo",model.getMobileNo());
                                            intent.putExtra("hasfood",model.isHasfood());
                                            intent.putExtra("hasAccomodation",model.isHasAccomodation());
                                            intent.putExtra("hasTshirt",model.isHasTshirt());
                                            intent.putExtra("ScanTime", scanTime);
                                            startActivity(intent);
                                            isQRCodeDetected = true;
                                            runOnUiThread(() -> ScanProgressBar.setVisibility(View.GONE));
                                        } else {
                                            Log.e("UserDetail", "Not successfully Fetched");
                                            runOnUiThread(() -> Toast.makeText(this, "No User Detail is Found", Toast.LENGTH_SHORT).show());
                                            showInvalidQRCodeOverlay();
                                        }
                                    });
                                }
                                else{
                                    runOnUiThread(() -> Toast.makeText(this, "No User detail is Found", Toast.LENGTH_SHORT).show());
                                    handler.post(this::showInvalidQRCodeOverlay);
                                }

                            }catch (ExecutionException e) {
                                runOnUiThread(() -> Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show());
                            } catch (InterruptedException e) {
                                runOnUiThread(() -> Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show());
                            }

                            isScanning = false;
                            imageProxy.close(); // Ensure ImageProxy is closed here
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show());
                        isScanning = false;
                        imageProxy.close(); // Ensure ImageProxy is closed if QR code is null
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("QR Scanner", "Error: ", e);
                    isScanning = false;
                    imageProxy.close();
                });

    }
    private void showInvalidQRCodeOverlay() {

        runOnUiThread(() -> {
            if (!isOverlayShown){
                isOverlayShown = true;
                Intent intent = new Intent(hospitalityScanner.this, InvalidHospitalityUser.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        new Handler().postDelayed(() -> isScanning = false, 500);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
    protected void onDestroy() {
        super.onDestroy();

        // Release camera resources
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
            cameraExecutor = Executors.newSingleThreadExecutor(); // Restart executor
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2=new Intent(hospitalityScanner.this, Format.class);
        startActivity(intent2);
        finishAffinity();
    }
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (Boolean.TRUE.equals(permissions.get(Manifest.permission.READ_MEDIA_IMAGES)) ||
                            Boolean.TRUE.equals(permissions.get(Manifest.permission.READ_MEDIA_VIDEO))) {
                        // Perform task
                    }
                } else {
                    if (Boolean.TRUE.equals(permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE)) &&
                            Boolean.TRUE.equals(permissions.get(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                        // Perform task
                    } else {
                        Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private void requestPermissions() {
        String[] permissionsToRequest;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES ,
                    Manifest.permission.READ_MEDIA_VIDEO
            };
        } else {
            permissionsToRequest = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        requestPermissionLauncher.launch(permissionsToRequest);
    }

    private boolean arePermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Call this method where you need permission check
    private void checkAndRequestPermissions() {
        if (arePermissionsGranted()) {
            // Perform task
        } else {
            requestPermissions();
        }
    }
}