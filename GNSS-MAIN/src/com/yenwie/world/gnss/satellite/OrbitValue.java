package com.yenwie.world.gnss.satellite;

public class OrbitValue {

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
		
	float Radius;
		public float getRadius() {
			return Radius;
		}
		public void setRadius(float radius) {
			Radius = radius;
		}
		
	float Incline;
		public float getIncline() {
			return Incline;
		}
		public void setIncline(float incline) {
			Incline = incline;
		}
		
	float Ecc;
		public float getEcc() {
			return Ecc;
		}
		public void setEcc(float ecc) {
			Ecc = ecc;
		}
	
	float RAAN;
		public float getRAAN() {
			return RAAN;
		}
		public void setRAAN(float rAAN) {
			RAAN = rAAN;
		}
	
	float Perigee;
		public float getPerigee() {
			return Perigee;
		}
		public void setPerigee(float perigee) {
			Perigee = perigee;
		}

	public OrbitValue(){
		
	}
}
