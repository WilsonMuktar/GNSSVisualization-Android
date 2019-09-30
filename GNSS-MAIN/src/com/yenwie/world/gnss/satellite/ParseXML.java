package com.yenwie.world.gnss.satellite;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXML {

	private ArrayList<SatelliteSystem> SSs = null;
		public ArrayList<SatelliteSystem> getSSs() {
			return SSs;
		}
		public void setSSs(ArrayList<SatelliteSystem> sSs) {
			SSs = sSs;
		}

	public ParseXML(){
		SSs = new ArrayList<SatelliteSystem>();
	}
	
	public void clear(){
		SSs.clear();
	}
	
	public void Parse(InputStream xmlString){
		
		SatelliteSystem SS = new SatelliteSystem();
		DocumentBuilderFactory factory=null;
		DocumentBuilder builder=null;
		Document document=null;
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(xmlString);
			Element root = document.getDocumentElement();
					
			Node Title = root.getElementsByTagName("Title").item(0);
				SS.setTitle(Title.getChildNodes().item(0).getNodeValue());
			Node OrbitCount = root.getElementsByTagName("OrbitCount").item(0);
				SS.setOrbitCount(Integer.parseInt(OrbitCount.getChildNodes().item(0).getNodeValue()));
			Node SatelliteCount = root.getElementsByTagName("SatelliteCount").item(0);
				SS.setSatelliteCount(Integer.parseInt(SatelliteCount.getChildNodes().item(0).getNodeValue()));
			
			NodeList nodes = root.getElementsByTagName("Orbit");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element Orbit = (Element) (nodes.item(i));
				
				NodeList tnl = Orbit.getElementsByTagName("Title");
				Element tel = (Element) tnl.item(0);
				String OrbitTitle = (tel.getChildNodes().item(0).getNodeValue());

				NodeList snl = Orbit.getElementsByTagName("SatelliteCount");
				Element sel = (Element) snl.item(0);
				int  OrbitSatelliteCount = Integer.parseInt(sel.getChildNodes().item(0).getNodeValue());

				NodeList rnl = Orbit.getElementsByTagName("Radius");
				Element rel = (Element) rnl.item(0);
				float  Radius = Float.parseFloat(rel.getChildNodes().item(0).getNodeValue());

				NodeList inl = Orbit.getElementsByTagName("Incline");
				Element iel = (Element) inl.item(0);
				float  Incline = Float.parseFloat(iel.getChildNodes().item(0).getNodeValue());
				
				NodeList enl = Orbit.getElementsByTagName("Ecc");
				Element eel = (Element) enl.item(0);
				float  Ecc = Float.parseFloat(eel.getChildNodes().item(0).getNodeValue());

				NodeList ranl = Orbit.getElementsByTagName("RAAN");
				Element rael = (Element) ranl.item(0);
				float  RAAN = Float.parseFloat(rael.getChildNodes().item(0).getNodeValue());

				NodeList pnl = Orbit.getElementsByTagName("Perigee");
				Element pel = (Element) pnl.item(0);
				float  Perigee = Float.parseFloat(pel.getChildNodes().item(0).getNodeValue());
				
				SS.addOrbit(OrbitTitle, OrbitSatelliteCount, Radius, Incline, Ecc, RAAN, Perigee);
			}
			SSs.add(SS);
//			System.err.println("Total Orbit : "+SSs.size());
			
		}catch (IOException e){
			System.err.println("ERROR");
			e.printStackTrace();
		}catch (SAXException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}catch (ParserConfigurationException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}
	}
	
}
