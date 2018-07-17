package org.myspecialway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.myspecialway.android.ListExamplesActivity;
import org.myspecialway.android.R;


public class NotificationActivity extends Activity {
    private final String TAG = NotificationActivity.this.getClass().getSimpleName();

    public static final String MESSAGE_TEXT = "message_text";
    ImageButton startNavigationButton;
    TextView messageTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();
        String messageText = intent.getStringExtra(MESSAGE_TEXT);
        messageTextView = findViewById(R.id.dialog_notification_text);
        messageTextView.setText(messageText);
        startNavigationButton = findViewById(R.id.dialog_notification_button);
        startNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListActivity();
            }
        });
    }

    private void openListActivity() {
        Log.i(TAG, "Open ListExamplesActivity");
        Intent intent = new Intent(this, ListExamplesActivity.class);
        startActivity(intent);
        finish();
    }
}
