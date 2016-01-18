package com.nikhil.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.CosineSimilarity;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.TanimotoCoefficientSimilarity;

public class ItemBasedRecommender {

	public static void main(String[] args) throws IOException, TasteException{
		
		DataModel model = new FileDataModel(new File("dataset/mahout-formatted-data.txt"));
		model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(model));
		/*
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
				return new GenericItemBasedRecommender(model, itemSimilarity);
			}
		};

		DataModelBuilder modelBuilder = new DataModelBuilder() {
			@Override
			public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
				return new GenericBooleanPrefDataModel(
						GenericBooleanPrefDataModel.toDataMap(trainingData));
			}
		};

		IRStatistics stats = evaluator.evaluate(recommenderBuilder, modelBuilder , model, null, 5 ,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
				0.7);

		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
		System.out.println(stats.getF1Measure());
		ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
		*/

		ItemSimilarity itemSimilarity = new org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity(model);
		long stime = System.currentTimeMillis();;
		GenericBooleanPrefItemBasedRecommender recommender = new GenericBooleanPrefItemBasedRecommender(model, itemSimilarity);
		long etime = System.currentTimeMillis();
		System.out.println("Total time " + (etime - stime) + " ms" );
		//10 recommendations for UserId 500
		stime = System.currentTimeMillis();;
		List<RecommendedItem> recommendations = recommender.recommend(500, 10);
		etime = System.currentTimeMillis();
		System.out.println("Total time " + (etime - stime) + " ms" );
		stime = System.currentTimeMillis();;
		recommendations = recommender.recommend(500, 10);
		etime = System.currentTimeMillis();
		System.out.println("Total time " + (etime - stime) + " ms" );
		List<String> shows = makeShowArray();
		for ( RecommendedItem item : recommendations ){
			if ( (item.getItemID() - 1) <= 100 )
				System.out.println(shows.get((int) (item.getItemID() - 1)) + " " + item.getValue() + " " + (item.getItemID() - 1) );
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
