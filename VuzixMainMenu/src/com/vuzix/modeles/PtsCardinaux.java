package com.vuzix.modeles;

public class PtsCardinaux {
	private int id;
	private int min;
	private int max;
	private int axe;
	private String name;
	private String aff;
	
	public PtsCardinaux(int id,int axe, int min, int max, String name, String aff){
		this.id = id;
		this.axe = axe;
		this.min = min;
		this.max = max;
		this.name = name;
		this.aff = aff;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAff() {
		return aff;
	}

	public void setAff(String aff) {
		this.aff = aff;
	}

	public int getAxe() {
		return axe;
	}

	public void setAxe(int axe) {
		this.axe = axe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

}
