package com.pere.client.com.pere.client.model;

import android.bluetooth.BluetoothClass;
import android.os.ParcelUuid;

/**
 * Created by eltntawy on 16/11/14.
 */
public class BluetoothDeviceInfo {


    private String name ;
    private String address;
    private BluetoothClass bluetoothClass;
    private int bondState;
    private ParcelUuid [] uuids;
    private int type ;

    public BluetoothDeviceInfo() {
    }

    public BluetoothDeviceInfo(String name, String address, BluetoothClass bluetoothClass, ParcelUuid[] uuids,int bondState, int type) {
        this.name = name;
        this.address = address;
        this.bluetoothClass = bluetoothClass;
        this.uuids = uuids;
        this.type = type;
        this.bondState = bondState;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BluetoothClass getBluetoothClass() {
        return bluetoothClass;
    }

    public void setBluetoothClass(BluetoothClass bluetoothClass) {
        this.bluetoothClass = bluetoothClass;
    }

    public ParcelUuid[] getUuids() {
        return uuids;
    }

    public void setUuids(ParcelUuid[] uuids) {
        this.uuids = uuids;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    @Override
    public String toString() {
        return name.toString() +" - "+address;
    }


}
