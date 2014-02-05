package com.vuzix.model;

public class DeviceBluetooth {
	private String name;
	private String add;
	private boolean fgPaired = false;
	
	public DeviceBluetooth(String name, String add){
		this.name = name;
		this.add = add;
	}
	
	public DeviceBluetooth(String name, String add, boolean paired){
		this.name = name;
		this.add = add;
		this.fgPaired = paired;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public boolean isFgPaired() {
		return fgPaired;
	}

	public void setFgPaired(boolean fgPaired) {
		this.fgPaired = fgPaired;
	}

}
