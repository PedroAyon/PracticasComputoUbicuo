package dev.pedroayon.pdm33c;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private Button btnBluetooth, btnBuscarDispositivo;
    private BluetoothAdapter bAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_CONNECT = 1001;

    private ArrayList<BluetoothDevice> arrayDevices;
    private ListView lvDispositivos;
    private TextView tvMensaje;
    private EditText etSendData;
    private BluetoothService obj;
    private BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBluetooth = findViewById(R.id.btnBluetooth);
        btnBuscarDispositivo = findViewById(R.id.btnBuscarDispositivo);
        lvDispositivos = findViewById(R.id.lvDispositivos);
        tvMensaje = findViewById(R.id.tvMensaje);
        etSendData = findViewById(R.id.etDataSend);

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            btnBluetooth.setEnabled(false);
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bAdapter.isEnabled()) btnBluetooth.setText("Desactivar");
        else btnBluetooth.setText("Activar");

        btnBluetooth.setOnClickListener(v -> {
            if (!checkBluetoothConnectPermission()) return;

            if (bAdapter.isEnabled()) {
                bAdapter.disable();
                btnBuscarDispositivo.setEnabled(false);
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        });

        btnBuscarDispositivo.setOnClickListener(v -> {
            if (!checkBluetoothConnectPermission()) return;

            if (arrayDevices != null) arrayDevices.clear();

            if (bAdapter.isDiscovering()) bAdapter.cancelDiscovery();

            if (bAdapter.startDiscovery())
                Toast.makeText(this, "Iniciando búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Error al iniciar búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
        });

        lvDispositivos.setOnItemClickListener((parent, view, position, id) -> {
            if (!checkBluetoothConnectPermission()) return;

            bluetoothDevice = arrayDevices.get(position);
            Toast.makeText(getApplicationContext(), bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
        });

        String[] perms = {
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN",
                "android.permission.BLUETOOTH_CONNECT",
                "android.permission.BLUETOOTH_SCAN"
        };

        BluetoothAdmin.getPermissions(perms, this);
        BluetoothAdmin.registrarEventosBluetooth(this, bReceiver);
        obj = new BluetoothService(bAdapter, handler);
    }

    private boolean checkBluetoothConnectPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT);
                return false;
            }
        }
        return true;
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (estado) {
                    case BluetoothAdapter.STATE_OFF:
                        btnBluetooth.setText("Activar");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btnBluetooth.setText("Desactivar");
                        break;
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if (!checkBluetoothConnectPermission()) return;

                if (arrayDevices == null) arrayDevices = new ArrayList<>();

                BluetoothDevice dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayDevices.add(dispositivo);

                String descripcionDispositivo = dispositivo.getName() + " [" + dispositivo.getAddress() + "]";
                Toast.makeText(getBaseContext(), "Dispositivo Detectado: " + descripcionDispositivo, Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                ArrayAdapter arrayAdapter = new BluetoothDeviceArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2, arrayDevices);
                lvDispositivos.setAdapter(arrayAdapter);
                Toast.makeText(getBaseContext(), "Fin de la búsqueda", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void onDataSend(View v) {
        String data = etSendData.getText().toString();
        byte[] buffer = data.getBytes();
        obj.escribir(buffer);
        etSendData.setText("" + (1 - Integer.parseInt(data)) % 2);
    }

    public void onServer(View v) {
        obj.iniciarServidor();
    }

    public void onClient(View v) {
        obj.iniciarCliente(bluetoothDevice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.e("ON-ACTIVITY-RESULT", "RESULT_OK");
                btnBuscarDispositivo.setEnabled(true);
            } else {
                Log.e("ON-ACTIVITY-RESULT", "RESULT_NO_OK");
                btnBuscarDispositivo.setEnabled(false);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso BLUETOOTH_CONNECT concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso BLUETOOTH_CONNECT denegado", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                    mensaje = "Enviando mensaje: " + new String(buffer);
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
