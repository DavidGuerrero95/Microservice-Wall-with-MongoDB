package com.appcity.app.muro.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MuroMath implements IMuroMath{

	@Override
	public Double distanciaCoord(List<Double> pos1, List<Double> pos2) {  
        //double radioTierra = 3958.75;//en millas  
		Double lat1 = pos1.get(0);
		Double lat2 = pos2.get(0);
		Double lon1 = pos1.get(1);
		Double lon2 = pos2.get(1);
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return (double) 0;
		}
		else {
			Double theta = lon1 - lon2;
			Double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
			return dist;
		} 
    }

	@Override
	public List<Double> distanciaMedia(List<Double> pos1, List<Double> pos2) {
		List<Double> lista = new ArrayList<Double>();
		Double lat1 = pos1.get(0);
		Double lon1 = pos1.get(1);
		Double lat2 = pos2.get(0);
		Double lon2 = pos1.get(1);
		
		Double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        Double Bx = Math.cos(lat2) * Math.cos(dLon);
        Double By = Math.cos(lat2) * Math.sin(dLon);
        Double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        Double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        lat3 = Math.toDegrees(lat3);
        lon3 = Math.toDegrees(lon3);
        BigDecimal bdlat3 = new BigDecimal(lat3).setScale(5, RoundingMode.HALF_UP);
        lat3 = bdlat3.doubleValue();
        BigDecimal bdlon3 = new BigDecimal(lon3).setScale(5, RoundingMode.HALF_UP);
        lon3 = bdlon3.doubleValue();
        lista.add(lat3);
        lista.add(lon3);
        return lista;
	}
}
