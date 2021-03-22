package umn.ac.id.musicplayer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private final LinkedList<String> mlistSong = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private String[] items;
    private String[] path_items;
    private CustomAdapter myAdapter;
    private static boolean accepted = false;
    private String flag;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        try {
            flag = i.getStringExtra("login");
            Log.d("login", "asd : " + flag);
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        if(!accepted || flag != null) {
            Log.d("login", "masuk ini");
            AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.titlebar, null);

            TextView myMsg = new TextView(PlaylistActivity.this);
            builder.setCustomTitle(view);
            myMsg.setText("Gilbert Haensel\n00000033147");
            myMsg.setGravity(Gravity.CENTER);
            builder.setView(myMsg);
            builder.setNegativeButton("Okay!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    accepted = true;
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        runtimePermission();

    }


    public void runtimePermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                displaySong();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findSong (File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singlefile: files)
        {
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(findSong(singlefile));
            }else{
                if(singlefile.getName().endsWith(".mp3") /*|| singlefile.getName().endsWith(".wav")*/){
                    arrayList.add(singlefile);
                    Log.d("myTag", singlefile.toString());
                }
            }
        }
        return arrayList;
    }

    void displaySong(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        items = new String[mySongs.size()];
        for(int i = 0;i<mySongs.size();i++){
            items[i] = mySongs.get(i).toString();
            mlistSong.add(items[i]);
            Log.d("items", items[i].substring(items[i].lastIndexOf("/")+1));
        }

        myAdapter = new CustomAdapter(this, mlistSong);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.dd_profil) {
            Intent intentDD = new Intent(PlaylistActivity.this, ProfilActivityDup.class);
            startActivity(intentDD);
        }

        else if (id == R.id.dd_logout) {
            Intent intentDD = new Intent(PlaylistActivity.this, MainActivity.class);
            startActivity(intentDD);
        }

        return super.onOptionsItemSelected(item);
    }
}
