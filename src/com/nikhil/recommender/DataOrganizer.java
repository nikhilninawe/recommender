package com.nikhil.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.ozone.dao.CookieDao;
import com.ozone.dao.ItemDao;
import com.ozone.db.CookieDB;
import com.ozone.db.Item;

import Jama.Matrix;

public class DataOrganizer {

	List<String> items = new ArrayList<String>(); //Required for keeping track of item in Rating Matrix
	List<String> users = new ArrayList<String>(); //Required for keeping track of user in Rating Matrix
	Matrix ratingMatix;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = args[0];
		DataOrganizer dto = new DataOrganizer();
		dto.makeItems();
		dto.makeRatingMatrix();
		int userId = dto.getUsers().indexOf(user);
		double[] recommendation = dto.getRecommendationsForUser(userId).getColumnPackedCopy();
		TreeMap<Double, String> topRecommendation = dto.getTopRecommendations(recommendation);
		for (double score  : topRecommendation.descendingKeySet()){
			System.out.println(topRecommendation.get(score) + " " + score);
		}
	}

	public void makeItems(){
		ItemDao itemDao = new ItemDao();
		List<Item> itmList = itemDao.getItems();
		for ( Item i : itmList){
			if ( !items.contains(i.getItemProductID()))
				items.add(i.getItemProductID());
		}
	}

	public void makeRatingMatrix(){
		//Query CookieDB for Users
		//For each user, make its vector		
		List<CookieDB> cookies = CookieDao.INSTANCE().getCookieInfo();
		HashMap<String, List<String>> userVisitedProducts = new HashMap<String, List<String>>();
		for ( CookieDB cookie : cookies){
			if ( cookie.getProductID().get(0).trim().equalsIgnoreCase("") ){
				continue;
			}
			if ( userVisitedProducts.containsKey(cookie.getCookieId())){
				((List<String>) userVisitedProducts.get(cookie.getCookieId())).add(cookie.getProductID().get(0));
			}else{
				List<String> productList = new ArrayList<String>();
				productList.add(cookie.getProductID().get(0));
				userVisitedProducts.put(cookie.getCookieId(), productList);
				if ( !users.contains(cookie.getCookieId()))
					users.add(cookie.getCookieId());
			}
		}
		System.out.println(userVisitedProducts);
		ratingMatix = new Matrix(userVisitedProducts.size(), items.size(),0);
		for ( int i=0; i < users.size(); i++){
			List<String> productIds = userVisitedProducts.get(users.get(i));
			System.out.println(i+":" + users.get(i));
			for ( String productId : productIds){
				ratingMatix.set(i, items.indexOf(productId), 1);
			}
		}
		
		ratingMatix.print(0, 0);
	}
	
	public static void print(String s){
		System.out.println(s);
	}

	/*
	 * The id of user in List<user> 
	 */
	public Matrix getRecommendationsForUser(int user){
		Matrix recommendationMatrix = ItemSimilarity.getRecommenedationMatrixForUser(this.ratingMatix, user);		
		return recommendationMatrix;
	}

	public TreeMap<Double, String> getTopRecommendations(double[] recos){
		TreeMap<Double, String> map = new TreeMap<Double, String>();
		for (int i=0;i<recos.length;i++){
			map.put(recos[i], items.get(i));
		}
		return map;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public Matrix getRatingMatix() {
		return ratingMatix;
	}

	public void setRatingMatix(Matrix ratingMatix) {
		this.ratingMatix = ratingMatix;
	}

}
