package dev.pedroayon.pdm33c;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private Button btnBluetooth;
    private Button btnBuscarDispositivo;
    private BluetoothAdapter bAdapter;
    private ArrayList<BluetoothDevice> arrayDevices;
    private ListView lvDispositivos;
    private TextView tvMensaje;
    private EditText etSendData;
    private BluetoothService obj;
    private BluetoothDevice bluetoothDevice;

    private static final int REQUEST_ENABLE_BT = 1;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBluetooth = findViewById(R.id.btnBluetooth);
        btnBuscarDispositivo = findViewById(R.id.btnBuscarDispositivo);
        lvDispositivos = findViewById(R.id.lvDispositivos);
        tvMensaje = findViewById(R.id.tvMensaje);
        etSendData = findViewById(R.id.etDataSend);

        // Listener for ListView item clicks: select Bluetooth device
        lvDispositivos.setOnItemClickListener((parent, view, position, id) -> {
            bluetoothDevice = arrayDevices.get(position);
            Toast.makeText(getApplicationContext(), bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
        });

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            btnBluetooth.setEnabled(false);
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set button text depending on Bluetooth state
        if (bAdapter.isEnabled()) {
            btnBluetooth.setText("Desactivar");
            btnBuscarDispositivo.setEnabled(true);
        } else {
            btnBluetooth.setText("Activar");
            btnBuscarDispositivo.setEnabled(false);
        }

        String[] perms = {"android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN",
                "android.permission.BLUETOOTH_CONNECT",
                "android.permission.BLUETOOTH_SCAN"};

        BluetoothAdmin.getPermissions(perms, this);
        BluetoothAdmin.registrarEventosBluetooth(this, bReceiver);
        obj = new BluetoothService(bAdapter, handler);

        // Set separate click listeners for buttons
        btnBluetooth.setOnClickListener(v -> {
            if (bAdapter.isEnabled()) {
                bAdapter.disable();
                btnBuscarDispositivo.setEnabled(false);
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        });

        btnBuscarDispositivo.setOnClickListener(v -> {
            if (arrayDevices != null) {
                arrayDevices.clear();
            }
            if (bAdapter.isDiscovering()) {
                bAdapter.cancelDiscovery();
            }
            if (bAdapter.startDiscovery()) {
                Toast.makeText(MainActivity.this, "Iniciando búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Error al iniciar búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // BroadcastReceiver for handling Bluetooth state changes and discovery events
    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Log.e("ON-RECIEVE", "ACTION_STATE_CHANGED");
                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (estado) {
                    case BluetoothAdapter.STATE_OFF:
                        btnBluetooth.setText("Activar");
                        btnBuscarDispositivo.setEnabled(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btnBluetooth.setText("Desactivar");
                        btnBuscarDispositivo.setEnabled(true);
                        break;
                    default:
                        break;
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.e("ON-RECIEVE", "ACTION_FOUND");
                if (arrayDevices == null)
                    arrayDevices = new ArrayList<>();
                BluetoothDevice dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayDevices.add(dispositivo);
                @SuppressLint("MissingPermission")
                String descripcionDispositivo = dispositivo.getName() + " [" + dispositivo.getAddress() + "]";
                Toast.makeText(getBaseContext(), "Dispositivo Detectado: " + descripcionDispositivo, Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.e("ON-RECIEVE", "ACTION_DISCOVERY_FINISHED");
                ArrayAdapter<BluetoothDevice> arrayAdapter = new BluetoothDeviceArrayAdapter(getBaseContext(),
                        android.R.layout.simple_list_item_2, arrayDevices);
                lvDispositivos.setAdapter(arrayAdapter);
                Toast.makeText(getBaseContext(), "Fin de la búsqueda", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // Handler to process messages from BluetoothService
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] buffer;
            String mensaje;
            switch (msg.what) {
                case BluetoothService.MSG_LEER:
                    buffer = (byte[]) msg.obj;
                    mensaje = new String(buffer, 0, msg.arg1);
                    tvMensaje.setText(mensaje);
                    break;
                case BluetoothService.MSG_ESCRIBIR:
                    buffer = (byte[]) msg.obj;
                    mensaje = new String(buffer);
                    Toast.makeText(getApplicationContext(), "Enviando mensaje: " + mensaje, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    // onDataSend can still be used as before, possibly tied via XML or set a listener
    public void onDataSend(View v) {
        String data = etSendData.getText().toString();
        byte[] buffer = data.getBytes();
        obj.escribir(buffer);
        etSendData.setText("" + (1 - Integer.valueOf(data)) % 2);
    }

    // Methods for client/server modes
    public void onServer(View v) {
        obj.iniciarServidor();
    }

    public void onClient(View v) {
        obj.iniciarCliente(bluetoothDevice);
    }

    // Handling activity result for Bluetooth enabling
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.e("ON-ACTIVITY-RESULT", "RESULT_OK");
//                btnBuscarDispositivo.setEnabled(true);
            } else {
                Log.e("ON-ACTIVITY-RESULT", "RESULT_NO_OK");
//                btnBuscarDispositivo.setEnabled(false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        Log.e("OR-PERMISSIONS", "Respuesta");
        if (permsRequestCode == 200) {
            boolean fineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean coarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            Log.e("OR-PERMISSIONS-200", "Respuesta a solicitud FINE: [" + fineLocationAccepted + "] + COARSE: [" + coarseLocationAccepted + "]");
        }
    }
}
