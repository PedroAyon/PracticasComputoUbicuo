package com.example.pdm19_asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {
    TextView tvResultado;
    EditText etL;
    EditText etU;
    EditText et1, et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResultado = (TextView) findViewById(R.id.tvResultado);
        etL = (EditText) findViewById(R.id.etL);
        etU = (EditText) findViewById(R.id.etU);

        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
    }

    public void onClick(View v){
        MiHilo hilo = new MiHilo(etL, etU, tvResultado);
        hilo.execute();
    }

    public class MiHilo extends AsyncTask<Integer, String, Long> {
        int L;
        int U;

//        EditText etL;
//        EditText etU;
//        TextView tvResultado;

  //      MiHilo(EditText etL, EditText etU, TextView tvResultado){
  MiHilo(EditText etL, EditText etU, TextView tvResultado){
         //   this.etL = etL;
         //   this.etU = etU;
         //   this.tvResultado = tvResultado;
        }

        @Override
        protected void onPreExecute(){
            L = Integer.parseInt(etL.getText().toString());
            U = Integer.parseInt(etU.getText().toString());
        }

        @Override
        protected Long doInBackground(Integer... params) {
            //int inicio = params[0].intValue();
            //int fin = params[1].intValue();
            int inicio = L;
            int fin = U;

            long suma = 0;

            for(int i = inicio; i <= fin/2; ++i){
                suma += i;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //String res = "PARCIAL-IN : " + suma;
               // tvResultado.setText(res);
                publishProgress("" + suma);
            }
            
            for(int i = fin/2+1; i <= fin; ++i){
                suma += i;
            }

            return new Long(suma);
        }

        @Override
        protected void onProgressUpdate(String... progress){
            String res = tvResultado.getText().toString();
            res = res + "\n" + "PARCIAL-OUT: " + Long.valueOf(progress[0]);
            tvResultado.setText(res);
        }

        @Override
        protected void onPostExecute(Long result){
            String res = tvResultado.getText().toString();
            res = res + "\n" + "TOTAL      : " + result;
            tvResultado.setText(res);
        }
    }



    public void grabar(View v){
        String nomarchivo = et1.getText().toString();
        String contenido = et2.getText().toString();

        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write(contenido);
            osw.flush();
            osw.close();
            Toast.makeText(this, "Los datos fueron grabados correctamente",
                    Toast.LENGTH_SHORT).show();
            et1.setText("");
            et2.setText("");
        } catch (IOException ioe) {
        }
    }

    public void recuperar(View v) {
        String nomarchivo = et1.getText().toString();
        File tarjeta = 	Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader archivo = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            String todo = "";
            while (linea != null) {
                todo = todo + linea + "\n";
                linea = br.readLine();
            }
            br.close();
            archivo.close();
            et2.setText(todo);

        } catch (IOException e) {
        }
    }

}


