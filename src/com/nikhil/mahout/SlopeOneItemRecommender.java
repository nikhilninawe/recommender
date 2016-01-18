package com.nikhil.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class SlopeOneItemRecommender {


	public static void main(String[] args) throws IOException, TasteException {
		DataModel model = new FileDataModel(new File("dataset/mahout-preference-data.txt"));
		SlopeOneRecommender recommender = new SlopeOneRecommender(model);
		List<RecommendedItem> recommendations = recommender.recommend(500, 10);
		System.out.println(recommendations);
		List<String> shows = makeShowArray();
		for ( RecommendedItem item : recommendations ){
			System.out.println(shows.get((int) (item.getItemID() -1)) + " " + item.getValue());
		}
	}
	
	public static ArrayList<String> makeShowArray() throws IOException{
		File shows = new File("dataset/shows.txt");
		String show = null;
		BufferedReader br = new BufferedReader(new FileReader(shows));
		ArrayList<String> showArray = new ArrayList<String>();
		while((show=br.readLine()) != null){
			showArray.add(show);
		}		
		br.close();
		return showArray;
	}
}
