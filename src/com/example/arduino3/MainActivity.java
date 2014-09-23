package com.example.arduino3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ArrayAdapter<String> listAdapter;
	ListView listView;
	ImageView iv;
	BluetoothAdapter btAdapter;
	ImageView b1;
	int c = 2;
	int z = 10;
	int x = 5;
	int n = 0;
	boolean a = false;
	boolean b = false;
	int successconnection = 0;

	// NFC CODE
	boolean con = false;
	NfcAdapter myNfcAdapter;
	TextView view;
	TextView v2;
	TextView v3;

	StringBuilder myText = new StringBuilder();
	IntentFilter[] intentFiltersArray;
	PendingIntent pendingIntent;
	String[][] techListsArray;
	String[] ElementList = null;
	// ends here

	public static String address = "20:13:09:12:26:11";
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;
	public static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	protected static final int SUCCESS_CONNECT = 0;
	protected static final int MESSAGE_READ = 1;
	protected static final int bdli = 2;
	protected static final int acli = 3;
	IntentFilter filter;
	RelativeLayout k;
	public ConnectedThread mConnectedThread;
	TextView bd;
	BroadcastReceiver receiver = null;
	String tag = "debugging";
	protected static final int AC = 0;
	BluetoothSocket msock;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.i(tag, "in handler");
			super.handleMessage(msg);

			switch (msg.what) {
			case SUCCESS_CONNECT:

				// DO something
				MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.connected);
				mp.start();
				msock = (BluetoothSocket) msg.obj;
				ConnectedThread connectedThread = new ConnectedThread(
						(BluetoothSocket) msg.obj);
				// ((BluetoothSocket)msg.obj)
				Toast.makeText(getApplicationContext(),
						"CONNECTION IS ESTABLISHED", Toast.LENGTH_SHORT).show();

				// String s = "1";
				if (n == 0)
					setContentView(R.layout.third);
				Log.i(tag, "connected");
				/*
				 * if (n == 1) { int numn = 0; connectedThread.write(numn); }
				 */
				break;

			// next();
			// finish();

			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				String string = new String(readBuf);
				Toast.makeText(getApplicationContext(), string,
						Toast.LENGTH_SHORT).show();
				break;

			case bdli:
				// Toast.makeText(getApplicationContext(),
				// "HI",Toast.LENGTH_SHORT).show();
				ConnectedThread connectedThread1 = new ConnectedThread(
						(BluetoothSocket) msg.obj);
				if (a == true)
					a = false;
				else
					a = true;
				if (a == true) {
					 mp = MediaPlayer.create(MainActivity.this,R.raw.on);
					mp.start();
					int num = 0;
					Toast.makeText(getApplicationContext(), "Element is on",
							Toast.LENGTH_SHORT).show();
					connectedThread1.write(num);
				} else if (a == false) {
					 mp = MediaPlayer.create(MainActivity.this,R.raw.off);
					mp.start();
					int num = 2;
					Toast.makeText(getApplicationContext(), "Element is off",
							Toast.LENGTH_SHORT).show();
					connectedThread1.write(num);
				}

				break;
			case acli:// Toast.makeText(getApplicationContext(),
						// "HI",Toast.LENGTH_SHORT).show();
				ConnectedThread connectedThread2 = new ConnectedThread(
						(BluetoothSocket) msg.obj);
				if (b == true)
					b = false;
				else
					b = true;
				if (b == true) {
					 mp = MediaPlayer.create(MainActivity.this,R.raw.on);
					mp.start();
					int nume = 1;
					Toast.makeText(getApplicationContext(), "Element is on",
							Toast.LENGTH_SHORT).show();
					connectedThread2.write(nume);
				} else if (b == false) {
					 mp = MediaPlayer.create(MainActivity.this,R.raw.off);
					mp.start();
					int nume = 3;
					Toast.makeText(getApplicationContext(), "Element is off",
							Toast.LENGTH_SHORT).show();
					connectedThread2.write(nume);
				}

				break;

			}
		}

		// public void next() {
		//
		// Intent nextScreen= new
		// Intent(getApplicationContext(),SecondScreenActivity.class);
		// startActivity(nextScreen);
		// TODO Auto-generated method stub

		// }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Intent nextScreen=new
		// Intent(getApplicationContext(),Main1Activity.class);
		// startActivity(nextScreen);

		setContentView(R.layout.activity_main);

		getActionBar().hide();
		/*
		 * getWindow().getDecorView().setSystemUiVisibility(
		 * View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
		 * View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
		 * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
		 * View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
		 * | View.SYSTEM_UI_FLAG_IMMERSIVE);
		 */
		Toast.makeText(getApplicationContext(),
				"Welcome to Home Automator", Toast.LENGTH_SHORT).show();
		MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.welcome);
		mp.start();
		init();
		if (btAdapter == null) {
			Toast.makeText(getApplicationContext(), "No bluetooth detected",
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			if (btAdapter.isEnabled()) {
			} else {

				turnOnBT();
			}

		}

		// nfc
		initialise();
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		//Intent filter filters NDEF tags
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("text/plain"); /*
											 * Handles all MIME based
											 * dispatches. You should specify
											 * only the ones that you need.
											 */
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		intentFiltersArray = new IntentFilter[] { ndef, };
		techListsArray = new String[][] { new String[] { NfcV.class.getName() } };

	}
	
	//Initialises NFC and checks for NFC tag intent

	private void initialise() {
		myNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		//checks for NFC device
		if (myNfcAdapter == null) {
			Toast.makeText(getApplicationContext(), "NOT FOUND",
					Toast.LENGTH_SHORT).show();
		} else {
			// Toast.makeText(getApplicationContext(), "FOUND",
			// Toast.LENGTH_SHORT).show();

		}

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			resolveIntent(getIntent());
		}
	}

		//Payload in NDEF data from tag is used for identifying 
	    //Room and corresponding xml is set
	private void resolveIntent(Intent intent) {
		// TODO Auto-generated method stub
		String payload = new String();
		String text = new String();
		NdefMessage[] messages = getNdefMessages(intent);

		for (int i = 0; i < messages.length; i++) {
			myText.append("Message " + (i + 1) + "\n");
			for (int j = 0; j < messages[0].getRecords().length; j++) {
				NdefRecord record = messages[i].getRecords()[j];
				myText.append((j + 1) + "th. Record Tnf: " + record.getTnf()
						+ "\n");
				myText.append((j + 1) + "th. Record type: "
						+ new String(record.getType()) + "\n");
				myText.append((j + 1) + "th. Record id: "
						+ new String(record.getId()) + "\n");
				payload = new String(record.getPayload());
				text = payload;
				myText.append((j + 1) + "th. Record payload: " + payload + "\n");
			}

		}

		// setContentView(R.layout.nfcjoe);
		ElementList = text.split(" ");
		
		//Room selection based on NFC data
		if (ElementList[0].equals("R1") == true) {
			n = 1;
			setContentView(R.layout.activity_main);
			init();
			func();

		} else if (ElementList[0].equals("R2") == true) {
			n = 2;
			setContentView(R.layout.activity_main);
			init();
			func();
		}
		

	}
	//NDEF message is read from Tag and is stored in variable
	//message of type NdefMessage
	NdefMessage[] getNdefMessages(Intent intent) {
		NdefMessage[] message = null;

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Parcelable[] rawMessages = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMessages != null) {
				message = new NdefMessage[rawMessages.length];
				for (int i = 0; i < rawMessages.length; i++) {
					message[i] = (NdefMessage) rawMessages[i];
				}
			} else {
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				message = new NdefMessage[] { msg };
			}
		} else {
			Log.d("", "Unknown intent.");
			finish();
		}
		return message;

	}

	public void accli(View v) {// Toast.makeText(getApplicationContext(),
								// "AIRCONDITIONING IS TURNING ON",
								// Toast.LENGTH_SHORT).show();

		mHandler.obtainMessage(acli, msock).sendToTarget();

	}

	public void Bdlight(View v) {

		// Toast.makeText(getApplicationContext(),
		// "BEDROOM LIGHT IS TURNING ON", Toast.LENGTH_SHORT).show();
		bd = (TextView) findViewById(R.id.textView1);

		mHandler.obtainMessage(bdli, msock).sendToTarget();

		// sendData("0");

		// if(c%2==0)
		// bd.setText("STATE ON");
		// else
		// bd.setText("STATE OFF");
		// c++;
	}

	public void context(int i) {
		// TODO Auto-generated method stub

	}

	public void turnon(View v) {
		btAdapter.enable();
		k.setBackgroundResource(R.drawable.on);
		Toast.makeText(getApplicationContext(), "YOU TURNED ON THE BT",
				Toast.LENGTH_SHORT).show();
	}

	public void four(View v) {
		setContentView(R.layout.four);
	}

	public void back(View v) {
		setContentView(R.layout.third);
	}

	public void fourth(View v) {
		setContentView(R.layout.fourth);
	}

	public void turnoff(View v) {
		btAdapter.disable();
		k.setBackgroundResource(R.drawable.dead);
		Toast.makeText(getApplicationContext(), "YOU TURNED OFF THE BT",
				Toast.LENGTH_SHORT).show();
		finish();
	}

	public void can(View v) {
		btAdapter.cancelDiscovery();
		k.setBackgroundResource(R.drawable.off);

		Toast.makeText(getApplicationContext(), "KILL THE DISCOVERY",
				Toast.LENGTH_SHORT).show();
	}

	public void sup(View v) {
		// TODO Auto-generated method stub
		btAdapter.cancelDiscovery();
		View b = findViewById(R.id.imageButton1);

		View tnih1 = findViewById(R.id.textView1);
		View tnih2 = findViewById(R.id.textView2);
		tnih1.setVisibility(View.GONE);
		tnih2.setVisibility(View.GONE);
		b.setVisibility(View.GONE);
		k.setBackgroundResource(R.drawable.s3);

		Toast.makeText(getApplicationContext(), "SEARCHING", Toast.LENGTH_SHORT)
				.show();
		MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.searching);
		mp.start();
		func();

		btAdapter.startDiscovery();
		getPairedDevices();

	}

	private void func() {
		// TODO Auto-generated method stub

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();

				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

					if (device.getName().toString().equals("HC-06")) {
						if (btAdapter.isDiscovering()) {
							btAdapter.cancelDiscovery();
						}

						BluetoothDevice selectedDevice = device;
						ConnectThread connect = new ConnectThread(
								selectedDevice);
						connect.start();
						Toast.makeText(getApplicationContext(),
								"ATTEMPTING TO CONNECT", Toast.LENGTH_SHORT)
								.show();
						Log.i(tag, "in click listener");
					} else {
						Toast.makeText(getApplicationContext(),
								"Device ISNT PAIRED", Toast.LENGTH_SHORT)
								.show();
					}

					if (n == 1)
						setContentView(R.layout.four);
					else if (n == 2)
						setContentView(R.layout.fourth);

					devices.add(device);
					String s = "";
					for (int a = 0; a < pairedDevices.size(); a++) {
						if (device.getName().equals(pairedDevices.get(a))) {
							// append
							s = "(Paired)";
							break;
						}
					}

					listAdapter.add(device.getName() + " " + s + " " + "\n");
				}

				// else
				// if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
				// Toast.makeText(getApplicationContext(),
				// "DISCOVERY STARTED AGAIN", Toast.LENGTH_SHORT).show();
				// }
				// else
				// if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				// Toast.makeText(getApplicationContext(),
				// "DISCOVERY IS FINISHED",Toast.LENGTH_SHORT).show();

				// }

			}
		};
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(receiver, filter);
		// filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		// registerReceiver(receiver, filter);
		// filter = new
		// IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		// registerReceiver(receiver, filter);
		// filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		// registerReceiver(receiver, filter);

	}

	private void turnOnBT() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, 0);
		

	}

	private void getPairedDevices() {
		// TODO Auto-generated method stub

		Set<BluetoothDevice> devicesArray = btAdapter.getBondedDevices();
		if (devicesArray.size() > 0) {
			for (BluetoothDevice device : devicesArray) {
				pairedDevices.add(device.getName());
			}

			

		}

	}

	private void init() {
		// TODO Auto-generated method stub

		listView = (ListView) findViewById(R.id.listView);
		// listView.setOnItemClickListener(this);
		listAdapter = new ArrayAdapter<String>(this, R.layout.color, 0);
		listView.setAdapter(listAdapter);
		k = (RelativeLayout) findViewById(R.id.back);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		pairedDevices = new ArrayList<String>();
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		devices = new ArrayList<BluetoothDevice>();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		myNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		myNfcAdapter.enableForegroundDispatch(this, pendingIntent,
				intentFiltersArray, techListsArray);

	}

	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			resolveIntent(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(getApplicationContext(),
					"Bluetooth must be enabled to continue", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	/*
	 * public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long
	 * arg3) {
	 * 
	 * // TODO Auto-generated method stub // con=true;
	 * 
	 * if (btAdapter.isDiscovering()) { btAdapter.cancelDiscovery(); } if
	 * (listAdapter.getItem(arg2).contains("Paired")) {
	 * 
	 * BluetoothDevice selectedDevice = devices.get(arg2); ConnectThread connect
	 * = new ConnectThread(selectedDevice); connect.start();
	 * Toast.makeText(getApplicationContext(), "ATTEMPTING TO CONNECT",
	 * Toast.LENGTH_SHORT).show(); Log.i(tag, "in click listener"); } else {
	 * Toast.makeText(getApplicationContext(), "Device ISNT PAIRED",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * if (n == 1) setContentView(R.layout.four); else if (n == 2)
	 * setContentView(R.layout.fourth);
	 * 
	 * 
	 * }
	 */

	private class ConnectThread extends Thread {

		private final BluetoothSocket mmSocket;

		// private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			// mmDevice = device;
			Log.i(tag, "construct");
			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.i(tag, "get socket failed");

			}
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			btAdapter.cancelDiscovery();
			Log.i(tag, "connect - run");
			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
				Log.i(tag, "connect - succeeded");
			} catch (IOException connectException) {
				Log.i(tag, "connect failed");
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)

			mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();

		}

		/** Will cancel an in-progress connection, and close the socket */
		// public void cancel() {
		// try {
		// mmSocket.close();
		// } catch (IOException e) { }
		// }
	}

	private class ConnectedThread extends Thread {
		// private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			// mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer; // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					buffer = new byte[1024];
					bytes = mmInStream.read(buffer);
					// Send the obtained bytes to the UI activity
					mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
							.sendToTarget();

				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(int bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}
		// public void sendData(String message)
		// {
		// byte[] msgBuffer=message.getBytes();
		// try
		// {
		// mmOutStream.write(msgBuffer);
		// }catch(IOException e){ }
		// }

		/* Call this from the main activity to shutdown the connection */
		// public void cancel() {
		// try {
		// mmSocket.close();
		// } catch (IOException e) { }
		// }
	}

}
