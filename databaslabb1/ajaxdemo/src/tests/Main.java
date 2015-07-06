package tests;

import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import newtest.Algorithms;
import newtest.Database;
import newtest.DbQueries;

public class Main {
	
	static Database db;
	static DbQueries dbq = new DbQueries();
	static Connection conn;
	static Algorithms al = new Algorithms();
	
		
	public static void main(String args[]) {

		new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, TimeUnit.SECONDS);		
		/*double dev = al.calcUserVoteDeviation(webscore, rating);
		System.out.println("Deviation: " + dev);
		
		double trust = al.calcUserTrustworthiness(userTrust, dev, voteCount);
		System.out.println("Trust: " + trust);
		
		double finalScore = al.calcFinalSiteScore(rating, trust, divs[1], divs[0]);
		System.out.println("Score: " + finalScore);
		
		dbq.updateSiteScore(weburl, finalScore, al.getDivs(0), al.getDivs(1));
		dbq.updateUserTrust(userid, trust);
		dbq.insertUserRating(userid, weburl, rating);*/	
	}
}