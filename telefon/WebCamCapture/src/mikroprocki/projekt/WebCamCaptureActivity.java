package mikroprocki.projekt;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import android.widget.Toast;

public class WebCamCaptureActivity extends Activity {
	
	private static final int REQUEST_ENABLE_BT = 1;
	
	private BluetoothAdapter myBluetoothAdapter;
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            devices.add(device);
	        }
	    }
	};
	private IntentFilter filter;
	
	private ListView pairedDevicesListView;
	private ListView devicesListView;
	private Button scanButton;
	private ArrayAdapter<String> pairedDevices;
	private ArrayAdapter<BluetoothDevice> devices;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter == null) {
        	Toast.makeText(this, "Blad bluetooth, aplikacja zostanie zamnkieta", Toast.LENGTH_LONG);
        	finish();
        	return;
        }
    	
    	scanButton = (Button)this.findViewById(R.id.scan);
    	scanButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				scan();
			}
		});
    	
    	devicesListView = (ListView) this.findViewById(R.id.devices);
    	pairedDevicesListView = (ListView) this.findViewById(R.id.paired_devices);
    	devices = new ArrayAdapter<BluetoothDevice>(this, R.layout.list_item);
    	pairedDevices = new ArrayAdapter<String>(this, R.layout.list_item);    	
    	devicesListView.setAdapter(devices);
    	pairedDevicesListView.setAdapter(pairedDevices);    	
    	
    	
    	devicesListView.setOnItemClickListener(new OnItemClickListener() {
    		    public void onItemClick(AdapterView<?> parent, View view,
    		        int position, long id) {
    		    	BluetoothDevice dev = devices.getItem(position-1);
    		    	try {
						BluetoothSocket socket = dev.createRfcommSocketToServiceRecord(UUID.randomUUID());
						socket.connect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    		    }
    		  });
    	
    }
    public void onStart() {
    	super.onStart();
    	if ( !myBluetoothAdapter.isEnabled() ) {
    		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    	}
    	filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }
   
    public void onDestroy() {
    	unregisterReceiver(mReceiver);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(requestCode) {	
	            case REQUEST_ENABLE_BT:
	            	if (resultCode == Activity.RESULT_OK) {	
	            		
	            	}	
	                else {
	                	Toast.makeText(this, "Blad wlaczania bluetooth", Toast.LENGTH_LONG);
	                	finish();
	                	return;
	                }	
	                break;	
	    }    
    }
    public void scan() {
    	pairedDevices.clear();
    	devices.clear();
    	myBluetoothAdapter.startDiscovery();
    	Set<BluetoothDevice> _devices =  myBluetoothAdapter.getBondedDevices();
    	for (BluetoothDevice d : _devices) {
    		pairedDevices.add(d.getName() + " " + d.getAddress());
    	}
    	
    }
}
