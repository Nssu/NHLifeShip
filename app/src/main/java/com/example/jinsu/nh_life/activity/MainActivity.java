package com.example.jinsu.nh_life.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinsu.nh_life.R;
import com.example.jinsu.nh_life.service.StepCheckService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Intent manboService;

    BroadcastReceiver receiver;

    boolean flag = true;
    String serviceData;
    @BindView(R.id.test_txt)
    TextView testTxt;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        manboService = new Intent(this, StepCheckService.class);
        receiver = new PlayingReceiver();

        Draw draw = new Draw(this);
        layoutMain.addView(draw);
        try {
            IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
            registerReceiver(receiver, mainFilter);
            startService(manboService);

        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        testTxt.setText(String.valueOf(StepCheckService.getStep()));
    }

    class PlayingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("PlayignReceiver", "IN");
            serviceData = intent.getStringExtra("stepService");
            testTxt.setText(serviceData);
        }
    }

    class Draw extends View {
        public Draw(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setTextSize(22);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(0.1f);

            //원
            canvas.drawCircle(240, 100, 200, paint);

            paint.setColor(Color.WHITE);
            //원
            canvas.drawCircle(240, 100, 190, paint);

            paint.setColor(Color.BLUE);
            //부채꼴
            RectF rf = new RectF(300, 700, 700, 1100);
            //sweepAngle : 몇도 그릴지, useCenter : true(부채꼴)
            




        }
    }

}
