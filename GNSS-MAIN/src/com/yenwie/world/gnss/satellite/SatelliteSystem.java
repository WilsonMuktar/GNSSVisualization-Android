package com.yenwie.world.gnss.satellite;

import java.util.ArrayList;

public class SatelliteSystem {

	SatelliteSystem(){
		
	}

	String Title;
		public String getTitle() {
			return Title;
		}
		public void setTitle(String title) {
			Title = title;
		}
		
	int SatelliteCount;
		public int getSatelliteCount() {
			return SatelliteCount;
		}
		public void setSatelliteCount(int satelliteCount) {
			SatelliteCount = satelliteCount;
		}
		
	int OrbitCount;
		public int getOrbitCount() {
			return OrbitCount;
		}
		public void setOrbitCount(int orbitCount) {
			OrbitCount = orbitCount;
		}
	
	private ArrayList<OrbitValue> Orbits = new ArrayList<OrbitValue>();
	public void addOrbit(String OrbitTitle, int SatelliteCount, float Radius,
			float Incline, float Ecc, float RAAN, float Perigee){
		OrbitValue Orbit = new OrbitValue();
		Orbit.setOrbitTitle(OrbitTitle);
		Orbit.setSatelliteCount(SatelliteCount);
		Orbit.setRadius(Radius);
		Orbit.setIncline(Incline);
		Orbit.setEcc(Ecc);
		Orbit.setRAAN(RAAN);
		Orbit.setPerigee(Perigee);
		Orbits.add(Orbit);
	}
	public ArrayList<OrbitValue> getOrbit(){
		return Orbits;
	}
}

