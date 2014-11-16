package com.pere.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.pere.client.com.pere.client.model.BluetoothDeviceAdepter;
import com.pere.client.com.pere.client.model.BluetoothDeviceInfo;
import com.pere.client.utils.Util;

import java.util.*;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
    */
    // bluetooth devices spinner
    Spinner blueSpinner ;
    public static String address;
     StringBuffer out = new StringBuffer();

    private BluetoothAdapter btAdapter = null;
    private static final int REQUEST_ENABLE_BT = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //out = (TextView) findViewById(R.id.text);

        CheckBTState();

        out.append("\n...In onCreate()...");


        Button btnControl = (Button) findViewById(R.id.btnControl) ;

        blueSpinner = (Spinner) findViewById(R.id.bluSpinner);

        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get mac address from spinner
                address = ((BluetoothDeviceInfo) blueSpinner.getSelectedItem()).getAddress();

                Intent controlActivity = new Intent(getApplicationContext(),SlideNavigationActivity.class);
                controlActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(controlActivity);
            }
        });
        addBluetoothHostest();
    }

    private void addBluetoothHostest() {

        Set<BluetoothDevice> remoteDevicesSet =
                (Set<BluetoothDevice>) BluetoothAdapter.getDefaultAdapter().getBondedDevices();


        List<BluetoothDeviceInfo> remoteDevicesList = new ArrayList<BluetoothDeviceInfo>();

        for(BluetoothDevice device : remoteDevicesSet) {
            remoteDevicesList.add(new BluetoothDeviceInfo(
                    device.getName(),
                    device.getAddress(),
                    device.getBluetoothClass(),
                    device.getUuids(),
                    device.getBondState(),
                    device.getType()
            ));
        }

        SpinnerAdapter  blueSpinnerAdepter = new BluetoothDeviceAdepter
                (this,R.id.bluSpinner,remoteDevicesList);

        blueSpinner.setAdapter(blueSpinnerAdepter);

    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        btAdapter = BluetoothAdapter.getDefaultAdapter();

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

}