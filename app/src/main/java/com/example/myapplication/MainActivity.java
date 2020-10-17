package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView message , sender , room ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.myMessage);
        sender = findViewById(R.id.sender);
        room = findViewById(R.id.room);

        if (!permissionGrantred()) {
            Intent intent = new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }
    }

    private boolean permissionGrantred() {
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (sets != null && sets.contains(getPackageName())) {
            return true;
        } else {
            return false;
        }
    }


}
