package com.car;

import com.netconnection.SocketConnect;

import android.app.Application;

public class MyApplication extends Application {
	private SocketConnect sc;
	private String WifiName;
	private String WifiPass;
	private int WifiNameID;
	private int WifiPassID;
	private String ControlStr;

	public String getControlStr() {
		return ControlStr;
	}

	public void setControlStr(String controlStr) {
		ControlStr = controlStr;
	}

	public int getWifiNameID() {
		return WifiNameID;
	}

	public void setWifiNameID(int wifiNameID) {
		WifiNameID = wifiNameID;
	}

	public int getWifiPassID() {
		return WifiPassID;
	}

	public void setWifiPassID(int wifiPassID) {
		WifiPassID = wifiPassID;
	}

	private String KeyA;
	private String KeyB;

	public String getWifiName() {
		return WifiName;
	}

	public void setWifiName(String wifiName) {
		WifiName = wifiName;
	}

	public String getWifiPass() {
		return WifiPass;
	}

	public void setWifiPass(String wifiPass) {
		WifiPass = wifiPass;
	}

	public String getKeyA() {
		return KeyA;
	}

	public void setKeyA(String keyA) {
		KeyA = keyA;
	}

	public String getKeyB() {
		return KeyB;
	}

	public void setKeyB(String keyB) {
		KeyB = keyB;
	}
}
