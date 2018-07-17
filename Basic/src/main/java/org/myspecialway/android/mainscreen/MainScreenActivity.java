package org.myspecialway.android.mainscreen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.myspecialway.AgendaActivity;
import org.myspecialway.android.ListExamplesActivity;
import org.myspecialway.android.R;
import org.myspecialway.android.ScheduleRepository;
import org.myspecialway.android.UserDataRepository;

public class MainScreenActivity extends AppCompatActivity {
    TextView userNameView;
    ImageView userAvatarView;
    TextView currentScheduleNameView;
    Button scheduleNavButton;
    Button navButton;
    MainScreenViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
//        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main_screen);
        viewModel = ViewModelProviders.of(this).get(MainScreenViewModel.class);
        viewModel.setRepos(new UserDataRepository(), new ScheduleRepository());


        userNameView = findViewById(R.id.user_display_name);
        userAvatarView = findViewById(R.id.user_avatar_image);
        currentScheduleNameView =findViewById(R.id.current_schedule_name_text);
        scheduleNavButton = findViewById(R.id.schedule_button);
        navButton = findViewById(R.id.nav_button);

        observeUserName();
        observeUserAvatar();
        observeCurrentScheduleName();
        scheduleNavButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this,AgendaActivity.class));
            }
        });

        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this,ListExamplesActivity.class));
            }
        });
    }


    private void observeUserName() {
        final Observer<String> userNameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userName) {
                userNameView.setText(userName);
            }
        };
        viewModel.getUserName().observe(this, userNameObserver);


    }

    private void observeUserAvatar() {
        final Observer<String> userAvatarObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userAvatar) {

                Glide.with(MainScreenActivity.this).load(userAvatar).into(userAvatarView);
            }
        };
        viewModel.getUserAvatar().observe(this, userAvatarObserver);
    }

    private void observeCurrentScheduleName() {
        final Observer<String> currentScheduleName = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String currentSchedule) {
                currentScheduleNameView.setText(currentSchedule);
            }
        };
        viewModel.getCurrentScheduleName().observe(this, currentScheduleName);
    }



}
