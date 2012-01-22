package mikroprocki.projekt.klient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;


/*
 * Glowna Activity
 * tu bedzie sie wyswietlal obraz..
 */
public class ClientActivity extends Activity {
	
	private BluetoothAdapter bluetoothAdapter = null;
	private BluetoothService bluetoothService = null;
	private ImageView imageView;
	
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_SELECT_DEVICE = 2;
	
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_IMG = 5;
	public static final int MESSAGE_TEST = 6;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
        	Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }
        
        bluetoothService = new BluetoothService(this, handler );
        imageView = (ImageView) findViewById(R.id.imageView);
    }
    
    public void onPause() {
    	super.onPause();
    }
    public void onStop() {    	
    	bluetoothService.finish();    	
    	super.onStop();
    }
    
    public void onDestroy() {
    	bluetoothService.finish();    	
    	super.onDestroy();
    }
    
    public void oonResume() {
    	super.onResume();
    	Intent selectDeviceIntent = new Intent(this, DevicesListActivity.class);
        startActivityForResult(selectDeviceIntent, REQUEST_SELECT_DEVICE);
    	//TODO dalsze dzialanie, gdyby bt byl wylaczony na poczatku
    }
    
    public void onStart() {
    	super.onStart();
    	if (!bluetoothAdapter.isEnabled()) {
    		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    	}
		Intent selectDeviceIntent = new Intent(this, DevicesListActivity.class);
        startActivityForResult(selectDeviceIntent, REQUEST_SELECT_DEVICE);
        
      
        
		//TODO dalsze dzia³anie programu
    	
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(requestCode) {	
            case REQUEST_ENABLE_BT:
            	if (resultCode == Activity.RESULT_OK) {	    
            		// TODO dalsze dzialanie programu
            	}	
                else {
                	Toast.makeText(this, "Blad wlaczania bluetooth", Toast.LENGTH_LONG).show();
                	finish();
                	return;
                }	
                break;	
            case REQUEST_SELECT_DEVICE:
            	if (resultCode == Activity.RESULT_OK) {
            		String address = data.getExtras().getString(DevicesListActivity.EXTRA_DEVICE_ADDRESS);
            		//Toast.makeText(this, "wybrany adres: " + address, Toast.LENGTH_LONG).show();
            		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            		bluetoothService.connect(device);
            	}
            	else {
            		finish();
            		return;
            	}
            break;
	    }    
    }
    
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            
            	case MESSAGE_DEVICE_NAME:
            		String n = msg.getData().getString("bb");
            		String y = msg.getData().getString("aa");
            		if (n!=null)
            			Toast.makeText(getApplicationContext(), "blad", Toast.LENGTH_LONG).show();
            		if (y!=null)
            			Toast.makeText(getApplicationContext(), "polaczono " + y, Toast.LENGTH_LONG).show();
            	break;
            	
            	case MESSAGE_IMG:
            		imageView.setImageDrawable((Drawable)msg.obj);
            		//imageView.setImageBitmap((Bitmap)msg.obj);
            	break;
            	
            	case MESSAGE_TEST:
            		String nn = msg.getData().getString("buffer");
            		Toast.makeText(getApplicationContext(), nn, Toast.LENGTH_LONG).show();
            	break;
            }
        }
    };
    
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
    	if (keyCode == KeyEvent.KEYCODE_HOME) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
}