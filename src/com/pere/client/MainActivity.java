package com.pere.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
    */
    // bluetooth devices spinner
    Spinner blueSpinner ;

     StringBuffer out = new StringBuffer();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //out = (TextView) findViewById(R.id.text);

        out.append("\n...In onCreate()...");





        Button btnControl = (Button) findViewById(R.id.btnControl) ;

        blueSpinner = (Spinner) findViewById(R.id.bluSpinner);

        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get mac address from spinner
                //address = blueSpinner.getSelectedItem().toString();

                Intent controlActivity = new Intent(getApplicationContext(),SlideNavigationActivity.class);
                controlActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(controlActivity);
            }
        });
        //addBluetoothHostest();
    }

    private void addBluetoothHostest() {


        Set<BluetoothDevice> remoteDevicesSet =
                (Set<BluetoothDevice>) BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        List<BluetoothDevice> remoteDevicesList = new ArrayList<BluetoothDevice>();
        remoteDevicesList.addAll(remoteDevicesSet);

        SpinnerAdapter blueSpinnerAdepter = new ArrayAdapter<BluetoothDevice>
              (this,R.id.bluSpinner,android.R.layout.simple_spinner_item,remoteDevicesList);

        blueSpinner.setAdapter(blueSpinnerAdepter);

    }



    @Override
    public void onResume() {
        super.onResume();


    }

}