package com.songyuwong.app;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.songyuwong.app.webPage.EnterActivity;

public class MainActivity extends AppCompatActivity {

    private Button submit;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
        submit = findViewById(R.id.submit);
    }

    @Override
    protected void onStart() {
        super.onStart();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString();
                if ("".equals(s)){
                    Toast.makeText(MainActivity.this, "请输入！！！", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, EnterActivity.class);
                    intent.putExtra("address", s);
                    startActivity(intent);
                }
            }
        });
    }
}