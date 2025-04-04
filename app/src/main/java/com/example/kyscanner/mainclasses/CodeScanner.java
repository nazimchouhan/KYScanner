package com.example.kyscanner.mainclasses;

import static com.example.kyscanner.security.UserDatabaseHelper.COLUMN_EVENT1;
import static com.example.kyscanner.security.UserDatabaseHelper.COLUMN_EVENT2;
import static com.example.kyscanner.security.UserDatabaseHelper.COLUMN_EVENT3;
import android.annotation.SuppressLint;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.animation.ObjectAnimator;
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
import com.example.kyscanner.security.JWTUtil;
import com.example.kyscanner.R;
import com.example.kyscanner.model.UserModel;
import com.example.kyscanner.security.UserDatabaseHelper;
import com.example.kyscanner.server.KYIdManager;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
public class CodeScanner extends AppCompatActivity {
    private View scanLine, flashLight;
    private PreviewView cameraPreview;
    private Handler Handler;
    public boolean isUpdated=false;
    private androidx.appcompat.widget.Toolbar scannerToolbar;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private ProcessCameraProvider cameraProvider;
    ProgressBar ScanProgressBar;
    private long lastAnalysisTime = 0;
    private Camera camera;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    private CameraControl cameraControl;
    private boolean isFlashOn, isScanning, isQRCodeDetected, isOverlayShown;
    private BarcodeScanner barcodeScanner;
    String gender;
    private ExecutorService cameraExecutor;
    private long startTime;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        sharedPreferences = getSharedPreferences("GenderPrefs", MODE_PRIVATE);
        gender= sharedPreferences.getString("gender","UnKnown");
        Log.d("GenderCheck", "Stored gender: " + gender);

        ScanProgressBar=findViewById(R.id.ScanprogressBar);
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAllPotentialBarcodes()// Scan only QR codes
                .build();
        barcodeScanner=BarcodeScanning.getClient(options);
        Handler = new Handler(Looper.getMainLooper());

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
        new Handler().postDelayed(() -> startCamera(), 200);
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
                    Log.e("Result", "The QR Result is: " + Result);

                    String mainQrResult = JWTUtil.decodeJWT(Result);
                    Log.e("KyID", "The KyID is: " + mainQrResult);

                    Toast.makeText(this, "QRRESULT is: " + mainQrResult, Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> ScanProgressBar.setVisibility(View.VISIBLE));
                    long scanTime = System.currentTimeMillis() - startTime;
                    if (mainQrResult != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        executor.execute(() -> {
                            UserDatabaseHelper db = UserDatabaseHelper.getInstance(getApplicationContext());
                            try {
                                // Step 1: Fetch user details using KYID
                                UserModel userDetail = db.getUserByKyId(mainQrResult);
                                Log.e("UserDetail","The UserDetail is :" + userDetail);
                                Log.e("UserDetail","The UserDetail is :" + userDetail.getGender());


                                if (userDetail == null) {
                                    Log.e("UserDetail", "User not found for KYID: " + mainQrResult);
                                    handler.post(() -> {
                                        Toast.makeText(getApplicationContext(), "User not found for KYID: " + mainQrResult, Toast.LENGTH_SHORT).show();
                                        showInvalidQRCodeOverlay();
                                    });
                                    return;
                                }

                                // Step 2: Check event eligibility
                                Calendar calendar = Calendar.getInstance();
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int month = calendar.get(Calendar.MONTH) + 1; // January is 0
                                int year = calendar.get(Calendar.YEAR);

                                String eventColumn = null;
                                boolean isEligible = false;

                                if (year == 2025 && month == 3) { // March 2025
                                    switch (day) {
                                        case 7:
                                            eventColumn = COLUMN_EVENT1;
                                            isEligible = userDetail.isEvent1();
                                            break;
                                        case 8:
                                            eventColumn = COLUMN_EVENT2;
                                            isEligible = userDetail.isEvent2();
                                            break;
                                        case 9:
                                            eventColumn = COLUMN_EVENT3;
                                            isEligible = userDetail.isEvent3();
                                            break;
                                    }
                                }
                                final String finalEventColumn = eventColumn;
                                final boolean finalIsEligible = isEligible;
                                Log.e("User Status","User is Eligible" + isEligible);
                                // Switch to the main thread for UI updates
                                handler.post(() -> {
                                    if (userDetail != null) {
                                        if(finalEventColumn != null &&
                                                (gender.equalsIgnoreCase(userDetail.getGender()) ||
                                                        "other".equalsIgnoreCase(userDetail.getGender())) &&
                                                finalIsEligible) {
                                            Log.e("Gender","The gender is :" + userDetail.getGender());
                                            Log.e("Gender","The gender of user is :" + userDetail.getGender());
                                            Log.e("UserDetail", "Successfully Fetched UserDetail");
                                            executor.execute(()->{
                                                    isUpdated = db.updateEventEligibility(mainQrResult, finalEventColumn, false);

                                            });
                                            // Log the updated value to verify changes

                                            Intent intent = new Intent(CodeScanner.this, Success.class);
                                            intent.putExtra("KYId", userDetail.getKyId());
                                            intent.putExtra("userName", userDetail.getName());
                                            intent.putExtra("Gmail", userDetail.getGmail());
                                            intent.putExtra("ScanTime", scanTime);
                                            startActivity(intent);
                                            KYIdManager.getInstance(getApplicationContext()).addScannedKyIDs(mainQrResult);

                                        } else {
                                            Log.e("UserDetail", "User is of Opposite Gender ");
                                            runOnUiThread(()->showInvalidQRCodeOverlay());
                                        }
                                    }else{
                                        runOnUiThread(()->Toast.makeText(this,"The User is not Valid ",Toast.LENGTH_SHORT).show());
                                        runOnUiThread(()->showInvalidQRCodeOverlay());
                                    }
                                });
                            } finally {
                                imageProxy.close();
                                isScanning = false;
                            }
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show());
                        isScanning = false;
                        imageProxy.close();
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
                Intent intent = new Intent(CodeScanner.this, InvalidUser.class);
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
        Intent intent2=new Intent(CodeScanner.this, Format.class);
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

