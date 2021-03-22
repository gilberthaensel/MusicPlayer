package umn.ac.id.musicplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().equals("uasmobile") &&
                    etPassword.getText().toString().equals("uasmobilegenap")){
                    Intent intentPlaylist = new Intent(LoginActivity.this, PlaylistActivity.class);
                    intentPlaylist.putExtra("login", "ok");
                    startActivity(intentPlaylist);
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
