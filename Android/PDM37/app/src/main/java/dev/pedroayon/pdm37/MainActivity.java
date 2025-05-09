package dev.pedroayon.pdm37;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etMessage;
    TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage = (EditText) findViewById(R.id.etMessage);
        tvResponse = (TextView) findViewById(R.id.tvResponse);
    }

    public void onSend(View v){
        MiPeticionREST obj = new MiPeticionREST(tvResponse);

        obj.execute("GET-SEND", etMessage.getText().toString());

    }

    public void onUpdate(View v){
        MiPeticionREST obj = new MiPeticionREST(tvResponse);

        obj.execute("GET-UPDATES");

    }
}