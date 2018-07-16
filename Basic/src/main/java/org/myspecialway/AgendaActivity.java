package org.myspecialway;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.myspecialway.android.R;

import java.util.ArrayList;
import java.util.List;

public class AgendaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AgendaAdapter adapter;
    private List<AgendaDetails> classdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_activity);
        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.agenda_recycler_view);

        initAgendaDetails();

        adapter = new AgendaAdapter(this, classdetails);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
    }

    private void initAgendaDetails() {
        classdetails = new ArrayList<>();
        classdetails.add(new AgendaDetails("מפגש בוקר", R.drawable.sun, ""));
        classdetails.add(new AgendaDetails("ארוחת בוקר", R.drawable.breakfest, ""));
        classdetails.add(new AgendaDetails("אומנות", R.drawable.art, ""));
        classdetails.add(new AgendaDetails("למידה", R.drawable.learning, ""));
        classdetails.add(new AgendaDetails("ארוחת צהריים", R.drawable.launch, ""));
    }

    
    class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

        private Context mContext;
        private List<AgendaDetails> classList;

        public AgendaAdapter(Context mContext, List<AgendaDetails> classList) {
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
            holder.title.setText(classList.get(position).getClassName());
            holder.thumbnail.setBackgroundResource(classList.get(position).classIcon);

            // loading album cover using Glide library
//            Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);


        }

        @Override
        public int getItemCount() {
            return classList.size();
        }
    }
}
