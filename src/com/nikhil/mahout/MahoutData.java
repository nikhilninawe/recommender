package com.nikhil.mahout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MahoutData {
	
	
	public static void main(String[] args) throws IOException{
		mainWithPreferences(args);
	}
	
	public static void mainWithoutPreference(String[] args) throws IOException{
		File f = new File("dataset/mahout-formatted-data.txt");
		File input = new File("dataset/user-shows.txt");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter wr = new BufferedWriter(new FileWriter(f));
		String line = null;
		int i = 1;
		while ( (line = br.readLine()) != null){
			int j = 1;
			String[] userLikings = line.split(" ");
			for (String s : userLikings ){
				if (s.trim().equals("1")){
					wr.write(i + "," + j + "\n");
				}
				j++;
			}
			i++;
		}
		br.close();
		wr.close();
	}
	
	public static void mainWithPreferences(String[] args) throws IOException{
		File f = new File("dataset/mahout-preference-data.txt");
		File input = new File("dataset/user-shows.txt");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter wr = new BufferedWriter(new FileWriter(f));
		String line = null;
		int i = 1;
		while ( (line = br.readLine()) != null){
			int j = 1;
			String[] userLikings = line.split(" ");
			for (String s : userLikings ){
				wr.write(i + "," + j + "," + s.trim() + "\n");
				j++;
			}
			i++;
		}
		br.close();
		wr.close();
	}
}
