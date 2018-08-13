package org.myspecialway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.myspecialway.android.MswApplication;
import org.myspecialway.android.R;
import org.myspecialway.android.login.RequestCallback;
import org.myspecialway.android.mainscreen.MainScreenActivity;
import org.myspecialway.android.schedule.ScheduleRepository;
import org.myspecialway.android.schedule.gateway.ScheduleResponse;

import java.util.List;

public class AgendaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AgendaAdapter adapter;
    private List<ScheduleResponse.Schedule> classdetails;
    private ScheduleRepository scheduleRepository;
    private final String query = "query{classById(id:\"5b5ebd8b0144ac628f9de97f\"){schedule{index lesson{title icon}location{name}}}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_activity);
        initToolbar();
        scheduleRepository = MswApplication.getInstance().getScheduleRepository();

        initAgendaDetails();




    }

    private void initToolbar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
    }

    private void initAgendaDetails() {
       scheduleRepository.getSchedule(query, "", new RequestCallback<List<ScheduleResponse.Schedule>>() {
            @Override
            public void onSuccess(List<ScheduleResponse.Schedule> result) {
                classdetails = result;
                adapter = new AgendaAdapter(AgendaActivity.this, classdetails);
                recyclerView = (RecyclerView) findViewById(R.id.agenda_recycler_view);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AgendaActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(AgendaActivity.this,"לא מתאפשר להציג כרגע את מערכת השעות", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AgendaActivity.this, MainScreenActivity.class));
                finish();
            }
        });
    }

    
    static class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

        private Context mContext;
        private List<ScheduleResponse.Schedule> classList;

        public AgendaAdapter(Context mContext, List<ScheduleResponse.Schedule> classList) {
            this.mContext = mContext;
            this.classList = classList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.agenda_text);
                thumbnail = (ImageView) view.findViewById(R.id.agenda_icon);
            }
        }

        @NonNull
        @Override
        public AgendaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AgendaAdapter.MyViewHolder holder, int position) {
            holder.title.setText(classList.get(position).getLesson().getTitle());
            holder.thumbnail.setBackgroundResource(R.drawable.sun);

            // loading album cover using Glide library
//            Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);


        }

        @Override
        public int getItemCount() {
            if (classList == null)return 0;
            return classList.size();
        }
    }
}
