package com.pere.client.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pere.client.R;
import com.pere.client.utils.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by eltntawy on 06/11/14.
 */
public class SlideNavigationActivity extends Activity implements View.OnClickListener {


    Button btnUp;
    Button btnDown;
    Button btnRight;
    Button btnLeft;


    StringBuffer out = new StringBuffer();
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    private TextView timerTextView;

    private int timerSecond = MainActivity.timer*60;

    // Well known SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC address
    //private static String address = "90:4C:E5:D6:51:7B";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        dyInit();
        init();

        if (out.length() > 0) {
            Util.AlertBox(this, "Fatal Error", out.toString());
        }
        timerTextView = (TextView) findViewById(R.id.lblTimer);

    }

    Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    private long startTime = 0L;

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            if(timerSecond >= 0) {
                int secs = timerSecond % 60;
                int mins = (timerSecond / 60) % 60;
                int hour = (timerSecond /60 )/ 60;

                timerTextView.setText(String.format("%02d",hour)+":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
                timerSecond--;

                customHandler.postDelayed(this, 1000);
            } else {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                mp.start();

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);

                customHandler.removeCallbacks(updateTimerThread);
            }
        }
    };

    public void startCounter(View view) {
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void pauseCounter (View view ) {
        customHandler.removeCallbacks(updateTimerThread);
    }

    /*
    *
    * Dynamic init
    *
    * */
    public void dyInit() {
        //btnUp = (Button) findViewById(R.id.btnUp);
        //btnDown = (Button) findViewById(R.id.btnDown);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnLeft = (Button) findViewById(R.id.btnLeft);


//      btnUp.setOnClickListener(this);
//      btnDown.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
    }

    /*
    *
    * initialize and establish server connection
    *
    * */
    public void init() {

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();

        out = new StringBuffer();

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(MainActivity.address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            out.append("In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
            //out.append("\n...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                out.append("In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
            out.append("unable to connect socket during connection failure\n" +
                    "Please check your server side is on and listing" + e.getMessage() + ".");
        }

        // Create a data stream so we can talk to server.
        //out.append("\n...Sending message to server...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            out.append("In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }


    }


/*
    class ThreadTimer implements Runnable {


        @Override
        public void run() {

            timerTextView = (TextView) findViewById(R.id.lblTimer);

            while (timerSecond > 0) {

                timerTextView.setText(timerSecond + "");
                System.out.println(timerSecond);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                timerSecond--;
            }

        }
    }
*/

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            Util.AlertBox(this, "Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                out.append("\n...Bluetooth is enabled...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyConnection();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyConnection();
    }

    public void destroyConnection() {
        boolean isException = false;
        sendCommand("EOF");

        out.append("\n...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                out.append( "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
                isException = true;
            }
        }

        try {
            btSocket.close();
        } catch (IOException e2) {
            out.append("In onPause() and failed to close socket." + e2.getMessage() + ".");
            isException = true;
        }

        if(isException)
            Util.AlertBox(this, "Fatal Error", out.toString());
    }

    @Override
    public void onClick(View view) {

        // identify actions
        if (view == btnUp) {
            sendCommand("up");
        } else if (view == btnDown) {
            sendCommand("down");
        } else if (view == btnRight) {
            sendCommand("right");
        } else if (view == btnLeft) {
            sendCommand("left");
        }
    }


    /*
    *
    * Send action command to server side
    *
    * */
    public boolean sendCommand(String command) {

        String message = command + "\n";
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (MainActivity.address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            //Util.AlertBox(this, "Fatal Error", msg);
            Util.AlertBox(this, "Exception", e.getMessage());


        }
        return false;
    }
}