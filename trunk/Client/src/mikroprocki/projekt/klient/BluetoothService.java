package mikroprocki.projekt.klient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BluetoothService {
	
	//private String myUUID = "1e0ca4ea-299d-4335-93eb-27fcfe7fa848";
	
	private BluetoothAdapter bluetoothAdapter;
	private ConnectThread connectThread = null;
	private ConnectedThread connectedThread = null;
	private Handler handler;
	
	
	
	
	public BluetoothService(Context context, Handler handler) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.handler = handler;
	}
	
	public void connect(BluetoothDevice device) {
		if (connectThread != null) {
			connectThread.cancel();
		}
		try {
			connectThread = new ConnectThread(device);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		connectThread.start();
	}

	public void connected(BluetoothSocket socket, BluetoothDevice device) {
		Message msg = handler.obtainMessage(ClientActivity.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString("aa", device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);
		
		if (connectedThread != null) {
			connectedThread.cancel();
		}
		
		connectedThread = new ConnectedThread(socket);
		connectedThread.start();
		
	}

	public void notConnected(BluetoothSocket socket, BluetoothDevice device) {
		if (!socket.getRemoteDevice().equals(device)){
		Message msg = handler.obtainMessage(ClientActivity.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString("bb", device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);}
	}
	
	private class ConnectThread extends Thread {
		private final BluetoothSocket socket;
		private final BluetoothDevice device;
		
		public ConnectThread(BluetoothDevice device) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
															IllegalAccessException, InvocationTargetException {
			this.device = device;
			BluetoothSocket tmp = null;		
			Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
			tmp = (BluetoothSocket) m.invoke(device, 1);
			
			socket = tmp;
		}
		
		public void run() {
			bluetoothAdapter.cancelDiscovery();
			try {
				socket.connect();
			} catch (IOException e) {
				//nie udalo sie polaczyc
				notConnected(socket, device);
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			//polaczono!
			connected(socket, device);
		}
		
		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
	
	private class ConnectedThread extends Thread {
		private InputStream inputStream;
		private BufferedInputStream bufferedInputStream;
		private BluetoothSocket bluetoothSocket;
		private OutputStream outputStream;
		
		public ConnectedThread(BluetoothSocket bs) {
			bluetoothSocket = bs;
			try {
				inputStream = bs.getInputStream();
				outputStream = bs.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferedInputStream = new BufferedInputStream(inputStream, 8192);
		}
		
		public void run() {
//			int current = 0;
//			
//			while (true) {
//				List<Byte> buffer = new ArrayList<Byte>();
//				try {
//					while ( (current = bufferedInputStream.available()) > 0  ) {
//						byte[] tmp = new byte[current];
//						bufferedInputStream.read(tmp, 0, current);
//						for(Byte b : tmp)
//							buffer.add(b);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				if (current==0) {
//					byte[] tmp = new byte[buffer.size()];
//					for (int i=0; i<buffer.size(); i++)
//						tmp[i] = buffer.get(i);
//				
//					int size = tmp.length;
//					
//					Message msg = handler.obtainMessage(ClientActivity.MESSAGE_TEST);
//					Bundle bundle = new Bundle();
//					bundle.putString("buffer", new Integer(size).toString() );
//					msg.setData(bundle);
//					handler.sendMessage(msg);
//				
//				
//					Bitmap bitmap = BitmapFactory.decodeByteArray(tmp, 0, current);
//					Drawable img = new BitmapDrawable(bitmap);
//					handler.obtainMessage(ClientActivity.MESSAGE_IMG, img).sendToTarget();
//				}
//			}
			
			int a = 90000;
			
			while (true) { 
				try {
					Thread.sleep(1231);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				List<Byte> buf = new ArrayList<Byte>();
				byte[] tmp = new byte[a];
				int pobrane = 0;
				try {
				//	while ( (pobrane = bufferedInputStream.read(tmp, 0, a)) > 1) {
						pobrane = bufferedInputStream.read(tmp, 0, a);
						Message msg = handler.obtainMessage(ClientActivity.MESSAGE_TEST);
						Bundle bundle = new Bundle();
						bundle.putString("buffer", new Integer(pobrane).toString() );
						msg.setData(bundle);
						handler.sendMessage(msg);
//						for(int i=0; i<pobrane; i++)
//							buf.add(tmp[i]);
//					
//					byte[] gotowe = new byte[buf.size()];
//					int size = buf.size();
//					for (int i=0; i<size; i++)
//						gotowe[i] = buf.get(i);
					
//					Message msg = handler.obtainMessage(ClientActivity.MESSAGE_TEST);
//					Bundle bundle = new Bundle();
//					bundle.putString("buffer", new Integer(size).toString() );
//					msg.setData(bundle);
//					handler.sendMessage(msg);
					
					
					Bitmap bitmap = BitmapFactory.decodeByteArray(tmp, 0, pobrane);
					Drawable img = new BitmapDrawable(bitmap);
					handler.obtainMessage(ClientActivity.MESSAGE_IMG, img).sendToTarget();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					outputStream.write(1);
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
//			int last = 0;
//			int current = 0;
//
//			// Keep listening to the InputStream while connected
//			while (true) {
//				last = 0;
//				current = 0;
//				try {
//					while ((last < (current = bufferedInputStream.available()))
//							&& (current != 0) ) {
//						last = current;
//
//						try {
//							Thread.sleep(100);
//						} catch (InterruptedException e) {
//						}
//					}
//
//					if (current != 0) {
//						byte[] buffer = new byte[current];
//						
//						bufferedInputStream.read(buffer, 0, current);
//
////						try {
////							bufferedInputStream.write((new Integer(current))
////									.toString().getBytes(), 0, (new Integer(
////									current)).toString().getBytes().length);
////							bufferedInputStream.flush();
////						} catch (IOException e1) {
////						}
//
//						Bitmap bitmap = BitmapFactory.decodeByteArray(buffer,
//								0, current);
//						Drawable img = new BitmapDrawable(bitmap);
//
//						int size = buffer.length;
//						Message msg = handler.obtainMessage(ClientActivity.MESSAGE_TEST);
//						Bundle bundle = new Bundle();
//						bundle.putString("buffer", new Integer(size).toString() );
//						msg.setData(bundle);
//						handler.sendMessage(msg);
//						
//						
//						handler.obtainMessage(
//								ClientActivity.MESSAGE_IMG, img)
//								.sendToTarget();
//					}
//				} catch (IOException e1) {
//				}
//			}
			
			
			
//			int last;
//			int current;
//			
//			while(true) {
//				last = 0;
//				current = 0;
//				try {
//					while ( (last < (current = bufferedInputStream.available()) ) && (current != 0) ) {
//						last = current;						
//						try {
//							Thread.sleep(85L);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				if (current != 0) {
//					int current = 0;
//					try {
//						//Thread.sleep(3000);
//						current = bufferedInputStream.available();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//					byte[] buffer = new byte[2600];
//					int size = -123;
//					try {
//						size = bufferedInputStream.read(buffer, 0, current);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					
					
					
//					Message msg = handler.obtainMessage(ClientActivity.MESSAGE_TEST);
//					Bundle bundle = new Bundle();
//					bundle.putString("buffer", new Integer(size).toString() );
//					msg.setData(bundle);
//					handler.sendMessage(msg);
					
//					byte[] b = new byte[123123123];
//					Resources r = new Resources();
//					r.getDrawable(R.drawable.ic_launcher);
					
				
//					Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, current);
//					Drawable img = new BitmapDrawable(bitmap);
//					handler.obtainMessage(ClientActivity.MESSAGE_IMG, img).sendToTarget();
					//handler.obtainMessage(ClientActivity.MESSAGE_IMG, bitmap).sendToTarget();
					
//				}
//			}
		}
		
		public void cancel() {
			try {
				bluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	
}
