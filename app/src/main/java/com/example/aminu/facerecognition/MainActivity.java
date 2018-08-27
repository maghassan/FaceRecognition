package com.example.aminu.facerecognition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processImage();
            }
        });
    }

    private void processImage() {

        ImageView imageView = findViewById(R.id.image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.testthree,
                options
        );

        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);

        com.google.android.gms.vision.face.FaceDetector faceDetector =
                new com.google.android.gms.vision.face.FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();
        if (!faceDetector.isOperational()) {

            new AlertDialog.Builder(getBaseContext()).setMessage("Could not face detection").show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        for (int i = 0; i < faces.size(); i++) {

            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();

            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, paint);
        }

        imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
}
