package com.example.womensafetyapp.ui;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.womensafetyapp.R;

public class FakeCallActivity extends AppCompatActivity {

    private View setupLayout;
    private View incomingCallLayout;
    private Ringtone ringtone;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call);

        setupLayout = findViewById(R.id.setupLayout);
        incomingCallLayout = findViewById(R.id.incomingCallLayout);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);

        findViewById(R.id.startFakeCall).setOnClickListener(v -> scheduleFakeCall());
        findViewById(R.id.acceptCall).setOnClickListener(v -> answerCall());
        findViewById(R.id.rejectCall).setOnClickListener(v -> endCall());
    }

    private void scheduleFakeCall() {
        setupLayout.setVisibility(View.GONE);
        new Handler().postDelayed(this::showIncomingCall, 5000);
    }

    private void showIncomingCall() {
        incomingCallLayout.setVisibility(View.VISIBLE);
        if (ringtone != null) ringtone.play();
        if (vibrator != null) {
            long[] pattern = {0, 1000, 1000};
            vibrator.vibrate(pattern, 0);
        }
    }

    private void answerCall() {
        stopAlerts();
        TextView status = findViewById(R.id.callStatus);
        status.setText("Connected");
        findViewById(R.id.acceptCall).setVisibility(View.GONE);
    }

    private void endCall() {
        stopAlerts();
        finish();
    }

    private void stopAlerts() {
        if (ringtone != null && ringtone.isPlaying()) ringtone.stop();
        if (vibrator != null) vibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        stopAlerts();
        super.onDestroy();
    }
}
