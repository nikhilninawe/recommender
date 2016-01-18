package com.nikhil.recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;


public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File f = new File("dataset/results");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int counter = 1;
		TreeMap<Double,Integer> map = new TreeMap<Double, Integer>();
		while ( (line = br.readLine()) != null ){
			double value = Double.parseDouble(line);
			map.put(value,counter);
			counter++;
		}
		br.close();
		System.out.println(map.size());
		for ( Double key : map.keySet()){
			System.out.println(key + ":" + map.get(key));
		}
	}
}
