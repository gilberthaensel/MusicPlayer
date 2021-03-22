package umn.ac.id.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private LinkedList<String> mlistSong;
    ArrayList<String> mlistSongA;



    public CustomAdapter(Context context, LinkedList<String> listSong) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mlistSong=listSong;
        this.mlistSongA = new ArrayList<String>(listSong);
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, int position) {
        String mCurrent = mlistSong.get(position).substring(mlistSong.get(position).lastIndexOf("/")+1).replace(".mp3", "").replace(".wav", "");
        holder.serial_number.setText(mCurrent);
        holder.serial_number.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return mlistSong.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView serial_number;
        final CustomAdapter mAdapter;

        public MyViewHolder(View itemView, CustomAdapter adapter) {
            super(itemView);
            serial_number = (TextView)itemView.findViewById(R.id.txtsongname);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int mPosition = getLayoutPosition();
            String element = mlistSong.get(mPosition).substring(mlistSong.get(mPosition).lastIndexOf("/")+1).replace(".mp3", "").replace(".wav", "");
            context = v.getContext();
            Intent intentPlayer = new Intent(context, MusicPlayerActivity.class);
            intentPlayer.putExtra("songs", mlistSongA);
            intentPlayer.putExtra("songname", element);
            intentPlayer.putExtra("pos", mPosition);



            Log.d("songs", mlistSong.toString());
            Log.d("songname", element);
            //Log.d("pos", mPosition);

            context.startActivity(intentPlayer);
        }

    }


}