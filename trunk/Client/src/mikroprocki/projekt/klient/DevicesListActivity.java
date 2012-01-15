package mikroprocki.projekt.klient;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/*
 * Wyswietla liste urzadzen sparowanych i szuka nowych
 */
public class DevicesListActivity extends Activity {
	
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private ArrayAdapter<String> pairedDevices;
	private ArrayAdapter<String> devices;
	private ListView devicesListView;
	private ListView pairedDevicesListView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_list);
        
        setResult(Activity.RESULT_CANCELED);
        
        /*
         * ustawianie buttona
         */
        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if (bluetoothAdapter.isDiscovering()) {
					bluetoothAdapter.cancelDiscovery();
		        }
				devices.clear();
				bluetoothAdapter.startDiscovery();
			}
		});
        
        /*
         * dodawanie sparowanych urz¹dzen
         */
        pairedDevices = new ArrayAdapter<String>(this, R.layout.list_item);
        pairedDevicesListView = (ListView) this.findViewById(R.id.pairedDevicesListView);
        pairedDevicesListView.setAdapter(pairedDevices);        
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
        	pairedDevices.add(device.getName() + "\n" + device.getAddress());
        }
        /*
         * inicjowanie nowych urzadzen
         */
        devices = new ArrayAdapter<String>(this, R.layout.list_item);
        devicesListView = (ListView) this.findViewById(R.id.newDevicesListView);
        devicesListView.setAdapter(devices);
        
        /*
         * kiedy znajdzie nowe urz¹dzenie
         */
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        
        /*
         * dodawanie akcji po kliknieciu na urzadzenie
         */
        devicesListView.setOnItemClickListener(mDeviceClickListener);
        pairedDevicesListView.setOnItemClickListener(mDeviceClickListener);
    }
    
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
        	bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    devices.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };
    
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            bluetoothAdapter.cancelDiscovery();
            
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
    
}
