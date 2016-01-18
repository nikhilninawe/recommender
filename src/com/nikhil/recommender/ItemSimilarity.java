package com.nikhil.recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import Jama.Matrix;


public class ItemSimilarity {
	
	public static void main(String[] args) throws IOException{
		File file = new File("dataset/user-shows.txt");
		File shows = new File("dataset/shows.txt");
		Matrix r = getRatingMatrix(file);
		Matrix recommendation = getRecommenedationMatrixForUser(r,499);
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

	/*
	 *Input: Rating Matrix of User-Item, User for whom recommendation to be made
	 *Output: Recommendation Matrix for user with scores for each item
	 *		  Return items with high scores for recommendation
	 */

	public static Matrix getRecommenedationMatrixForUser(Matrix r, int user){
		Matrix q = getQMatrix(r);
		Matrix s = getSimilarityMatrix(r,q);
		Matrix recommendation = getRecommenedationMatrixForUser(r, s, user);
		return recommendation;
	}

	public static int[] getIntArrayFromString(String input){
		String[] stringArray = input.split(" ");
		int[] result = new int[stringArray.length];
		int i = 0;
		for (String s : stringArray){
			result[i] = Integer.parseInt(s);
			i++;
		}
		return result;
	}

	public static Matrix getRatingMatrix(File f) throws IOException{
		Matrix r = null;
		BufferedReader br = new BufferedReader(new FileReader(f));
		String result = null;
		int i = 0;
		while( (result = br.readLine()) != null){
			int[] ratings = getIntArrayFromString(result);
			if ( r == null){
				int m = numberOfLinesInFile(f);
				r = new Matrix(m, ratings.length);
			}
			int j = 0;
			for ( int jIter : ratings ){
				r.set(i, j, jIter);
				j++;
			}
			i++;
		}
		br.close();
		return r;
	}

	public static int numberOfLinesInFile(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		int count = 0;
		while(br.readLine() != null){
			count++;
		}
		br.close();
		return count;
	}

	public static Matrix getQMatrix(Matrix m){
		Matrix q = new Matrix(m.getColumnDimension(), m.getColumnDimension(), 0);
		for ( int j = 0; j < m.getColumnDimension(); j++){
			Matrix intermediate = m.getMatrix(0, m.getRowDimension() - 1, j , j);
			int norm = (int) intermediate.norm1();
			q.set(j, j, norm);
		}
		return q;

	}

	public static double dotProductOfArrays(double[] a, double[] b){
		double sum = 0;
		for ( int i=0; i < a.length; i++){
			sum += a[i]*b[i];
		}
		return sum;
	}

	public static Matrix getSimilarityMatrix(Matrix r, Matrix q){
		Matrix s = new Matrix(r.getColumnDimension(), r.getColumnDimension(),-1);
		for ( int i=0; i<s.getColumnDimension(); i++){
			Matrix ri = r.getMatrix(0, r.getRowDimension() -1, i, i);
			double qii = Math.sqrt(q.get(i, i));
			for (int j=0; j<s.getRowDimension();j++){
				if ( i == j){
					s.set(i, j, 1);
					continue;
				}
				if ( s.get(i, j) == -1 ){
					Matrix rj = r.getMatrix(0, r.getRowDimension() - 1, j, j);
					double dotProduct = dotProductOfArrays(ri.getColumnPackedCopy(), rj.getColumnPackedCopy());
					double qjj = Math.sqrt(q.get(j, j));
					double cosine = dotProduct / (qii * qjj);
					s.set(i,j,cosine);
					s.set(j,i,cosine); //Due to symmetry of the Similarity Matrix
				}
			}
		}
		return s;
	}

	public static Matrix getRecommenedationMatrixForUser(Matrix r, Matrix s, int user){
		Matrix recommendationMatrixForUser = new Matrix(s.getColumnDimension(), 1);
		for ( int j=0; j<s.getRowDimension(); j++){
			double score = 0;
			for ( int x =0; x <s.getRowDimension(); x++){
				score += r.get(user, x)*s.get(x, j);
			}
			recommendationMatrixForUser.set(j, 0, score);
		}
		return recommendationMatrixForUser;
	}
}
