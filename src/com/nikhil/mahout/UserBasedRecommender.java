package com.nikhil.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.nikhil.recommender.UserSimilarity;

public class UserBasedRecommender {
	
	public static void main(String[] args) throws IOException, TasteException{
		
		DataModel model = new FileDataModel(new File("dataset/mahout-formatted-data.txt"));
		LogLikelihoodSimilarity userSimilarity = new LogLikelihoodSimilarity(model);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.7, userSimilarity, model);
		GenericBooleanPrefUserBasedRecommender recommender = new GenericBooleanPrefUserBasedRecommender(model, neighborhood, userSimilarity);
		//10 recommendations for UserId 500
		List<RecommendedItem> recommendations = recommender.recommend(500, 10);
		List<String> shows = makeShowArray();
		for ( RecommendedItem item : recommendations ){
			if ( item.getItemID() - 1 <= 100)
				System.out.println(shows.get((int) (item.getItemID() - 1)) + " " + item.getValue());
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
