package dev.pedroayon.pdm11;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerOpciones;
    private Spinner spinnerDinamico;
    private EditText editTextValor;
    private Button btnOk;
    private ArrayAdapter<String> adapterDinamico;
    private ArrayList<String> listaDinamica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerOpciones = findViewById(R.id.spinnerOpciones);
        spinnerDinamico = findViewById(R.id.spinnerDinamico);
        editTextValor = findViewById(R.id.editTextValor);
        btnOk = findViewById(R.id.btnOk);

        listaDinamica = new ArrayList<>();
        adapterDinamico = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDinamica);
        adapterDinamico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDinamico.setAdapter(adapterDinamico);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String operacion = spinnerOpciones.getSelectedItem().toString();
                String valor = editTextValor.getText().toString().trim();

                if(valor.isEmpty()){
                    Toast.makeText(MainActivity.this, "Ingrese un valor", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(operacion.equals("Agregar")){
                    if(!listaDinamica.contains(valor)){
                        listaDinamica.add(valor);
                        adapterDinamico.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Elemento agregado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "El elemento ya existe", Toast.LENGTH_SHORT).show();
                    }
                } else if(operacion.equals("Borrar")){
                    if(listaDinamica.contains(valor)){
                        listaDinamica.remove(valor);
                        adapterDinamico.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Elemento borrado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "El elemento no existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
