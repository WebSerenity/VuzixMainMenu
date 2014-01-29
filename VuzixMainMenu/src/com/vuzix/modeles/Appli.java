package com.vuzix.modeles;

public class Appli {
	private String name = "";
	private String pack = "";
	
	public Appli(String name, String pack){
		this.name = name;
		this.pack = pack;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}
	
	

}
