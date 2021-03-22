package umn.ac.id.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_profil, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_profil = findViewById(R.id.btn_profil);
        btn_login = findViewById(R.id.btn_login);

        btn_profil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentProfil =new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intentProfil);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

    }
}