package org.myspecialway.android.mainscreen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.myspecialway.AgendaActivity;
import org.myspecialway.android.ListExamplesActivity;
import org.myspecialway.android.R;
import org.myspecialway.android.ScheduleRepository;

public class MainScreenActivity extends AppCompatActivity {
    TextView usernameView;
    ImageView userAvatarView;
    TextView currentScheduleNameView;
    Button scheduleNavButton;
    Button navButton;
    MainScreenViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        viewModel = ViewModelProviders.of(this).get(MainScreenViewModel.class);
        viewModel.setRepos(new ScheduleRepository());

        usernameView = findViewById(R.id.user_display_name);
        userAvatarView = findViewById(R.id.user_avatar_image);
        currentScheduleNameView =findViewById(R.id.current_schedule_name_text);
        scheduleNavButton = findViewById(R.id.schedule_button);
        navButton = findViewById(R.id.nav_button);

        usernameView.setText(viewModel.getUsername());
        observeCurrentScheduleName();

        navButton.setOnClickListener(v -> {
            //start navigation in Unity app
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.att.indar.poc", "com.unity3d.player.UnityPlayerActivity"));
                intent.putExtra("destination", "D"); //TODO - need to pass the real destination name

                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        scheduleNavButton.setOnClickListener(v -> startActivity(new Intent(MainScreenActivity.this,AgendaActivity.class)));
        navButton.setOnClickListener(v -> startActivity(new Intent(MainScreenActivity.this,ListExamplesActivity.class)));
    }

    private void observeCurrentScheduleName() {
        viewModel.getCurrentScheduleName().observe(this, currentSchedule -> currentScheduleNameView.setText(currentSchedule));
    }



}
