package com.yenwie.world.gnss.satellite;

public class Orbit {

	String OrbitTitle;
		public String getOrbitTitle() {
			return OrbitTitle;
		}
		public void setOrbitTitle(String orbitTitle) {
			OrbitTitle = orbitTitle;
		}

	int SatelliteCount;
		public int getSatelliteCount() {
			return SatelliteCount;
		}
		public void setSatelliteCount(int satelliteCount) {
			SatelliteCount = satelliteCount;
		}
		
	int Radius;
		public int getRadius() {
			return Radius;
		}
		public void setRadius(int radius) {
			Radius = radius;
		}
		
	public Orbit(){
		
	}
}
