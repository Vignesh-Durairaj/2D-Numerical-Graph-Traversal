package com.vikhi.skiing.pojo;

public class SkiPoint {
	
	private int elevationValue;
	
	private int horizontalPoint;
	
	private int verticalPoint;
	
	private boolean isVisited;

	public SkiPoint(int elevationValue, int horizontalPoint, int verticalPoint) {
		super();
		this.elevationValue = elevationValue;
		this.horizontalPoint = horizontalPoint;
		this.verticalPoint = verticalPoint;
		this.isVisited = false;
	}

	public int getElevationValue() {
		return elevationValue;
	}

	public int getHorizontalPoint() {
		return horizontalPoint;
	}

	public int getVerticalPoint() {
		return verticalPoint;
	}

	public void setVerticalPoint(int verticalPoint) {
		this.verticalPoint = verticalPoint;
	}
	
	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	@Override
	public String toString() {
		return "[" + this.verticalPoint + "][" + this.horizontalPoint + "]:" + this.elevationValue;
	}
}
