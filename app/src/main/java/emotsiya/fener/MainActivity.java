package emotsiya.fener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private CameraManager cam = null;
    private String camId;

    private boolean status = false;
    private boolean hasFlash;

    ImageView image;
    Button powerButton;
    Parameters params;
    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-5009286998211327~6381772222");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Cihaz flaş özelliğini desteklemiyor");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // closing the application
                            finish();
                            System.exit(0);
                        }
                    });
            alert.show();
            return;
        }

        powerButton = findViewById(R.id.powerButton);
        image = findViewById(R.id.buttonView);
        image.setVisibility(View.VISIBLE);
        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature("android.hardware.camera.flash");


        cam = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            camId = cam.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (status) {
                        camoff();
                    } else {
                        camOn();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void camOn() {
        image.setImageResource(R.drawable.button_on);
        status = true;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cam.setTorchMode(camId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            camoff();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void camoff() {
        image.setImageResource(R.drawable.button_off);
        status = false;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cam.setTorchMode(camId, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
