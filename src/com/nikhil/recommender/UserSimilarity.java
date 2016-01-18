package com.nikhil.recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import Jama.Matrix;

public class UserSimilarity {

	public static void main(String[] args) throws IOException {
		File file = new File("dataset/user-shows.txt");
		File shows = new File("dataset/shows.txt");
		Matrix r = ItemSimilarity.getRatingMatrix(file);
		Matrix p = computePMatrix(r);
		Matrix s = computeUserSimilarityMatrix(r, p);
		Matrix recommendation = computeRecommendationMatrix(r, s, 499);
		double[] reco = recommendation.getColumnPackedCopy();
		BufferedReader br = new BufferedReader(new FileReader(shows));
		String show = null;
		ArrayList<String> showArray = new ArrayList<String>();
		int i=0;
		while((show=br.readLine()) != null){
			showArray.add(show);
			i++;
		}		
		br.close();
		TreeMap<Double, String> scoreShowMap = new TreeMap<Double, String>();
		for ( i =0; i < reco.length; i++){
			scoreShowMap.put(reco[i], showArray.get(i));
		}

		i = 0;
		for ( Double d : scoreShowMap.descendingKeySet()){
			if ( showArray.indexOf(scoreShowMap.get(d)) > 100){
				continue;
			}
			System.out.println(scoreShowMap.get(d) + " " + d);
			i++;
			if ( i == 9){
				break;
			}
		}
	}

	public static Matrix computePMatrix(Matrix r){
		Matrix p = new Matrix(r.getRowDimension(), r.getRowDimension(),0);
		for ( int m = 0; m < r.getRowDimension(); m++){
			int sum = 0;
			for ( int j = 0; j < r.getColumnDimension(); j++){
				sum += r.get(m, j);
			}
			System.out.println(m + " " + sum);
			p.set(m, m, sum);
		}
		return p;
	}

	public static Matrix computeUserSimilarityMatrix(Matrix r, Matrix p){
		Matrix s = new Matrix(r.getRowDimension(),r.getRowDimension(),-1);
		for ( int i=0; i < r.getRowDimension(); i++){
			System.out.println(i);
			double[] ri = r.getMatrix(i, i, 0, r.getColumnDimension()-1).getColumnPackedCopy();
			double pii = Math.sqrt(p.get(i, i));
			for( int j=0; j<r.getRowDimension(); j++){
				if ( i==j ){
					s.set(i, j, 1);
					continue;
				}
				if ( s.get(i, j) == -1 ){
					double[] rj = r.getMatrix(j, j, 0, r.getColumnDimension()-1).getColumnPackedCopy();
					double pjj = Math.sqrt(p.get(j, j));
					double cosine = ItemSimilarity.dotProductOfArrays(ri, rj) / (pii*pjj);
					s.set(i, j, cosine);
					s.set(j, i, cosine);
				}
			}
		}
		return s;
	}

	public static Matrix computeRecommendationMatrix(Matrix r, Matrix s, int user){
		Matrix recommendation = new Matrix(r.getColumnDimension(),1);
		for ( int j = 0; j< r.getColumnDimension(); j++){
			double score = 0;
			for ( int x =0;  x<s.getColumnDimension(); x++){
				score += s.get(user,x)*r.get(x, j);
			}
			recommendation.set(j, 0, score);
		}
		return recommendation;
	}
}
