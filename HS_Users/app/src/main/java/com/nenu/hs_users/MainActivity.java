package com.nenu.hs_users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MainActivity extends AppCompatActivity {
    DatabaseReference linksRef;
    RecyclerView videolist;
    Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linksRef = FirebaseDatabase.getInstance().getReference().child("URL");
        videolist= findViewById(R.id.video_list);

        videolist.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        videolist.setLayoutManager(layoutManager);






    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Values> options = null;

        options = new FirebaseRecyclerOptions.Builder<Values>()
                .setQuery(linksRef, Values.class)
                .build();

        FirebaseRecyclerAdapter<Values,ViewHolder> adapter = new FirebaseRecyclerAdapter<Values, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int i, @NonNull final Values values) {


                holder.video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);

                        String url1 = values.getUrl().toString();
                        youTubePlayer.loadVideo(url1,0);


                    }
                });




            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
                RecyclerView.ViewHolder viewHolder = new ViewHolder(view);
                return (ViewHolder) viewHolder;


            }
        };
        videolist.setAdapter(adapter);
        adapter.startListening();


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        YouTubePlayerView video;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            video =itemView.findViewById(R.id.item_video);
            getLifecycle().addObserver(video);

        }
    }


}