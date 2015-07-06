package tests;

import java.sql.*;
import java.util.Scanner;

import newtest.Algorithms;
import newtest.Database;
import newtest.DbQueries;

public class Main {
	
	static Database db;
	static DbQueries dbq = new DbQueries();
	static Connection conn;
	static Algorithms al = new Algorithms();
	
		
	public static void main(String args[]) {
		boolean flag = false;
		Scanner scan = new Scanner(System.in);
		int userid, rating, voteCount;
		String weburl;
		double userTrust, webscore;
		double[] divs;
		
		while (!flag) {
			
			System.out.println("Enter User ID:");
			userid = scan.nextInt();
			if(userid == -1.0) {
				flag = true;
				continue;
			}
			System.out.println("Enter Website URL:");
			weburl = scan.next();
			System.out.println("Enter User's Rating:");
			rating = scan.nextInt();
			
			userTrust = dbq.getUserTrustworthiness(userid);
			if (userTrust == -1) {
				System.out.println("Invalid userId");
				continue;
			}
			
			webscore = dbq.getSiteScore(weburl);
			if (webscore == -1) {
				System.out.println("Invalid url");
				continue;
			}
			
			voteCount = dbq.getUserVoteCount(userid);
			if (voteCount == -1) {
				System.out.println("Invalid userId");
				continue;
			}
			
			divs = dbq.getSiteDivs(weburl);
			if (divs == null) {
				System.out.println("Invalid url");
				continue;
			}
			
			System.out.println(webscore);
			
			double dev = al.calcUserVoteDeviation(webscore, rating);
			System.out.println("Deviation: " + dev);
			
			double trust = al.calcUserTrustworthiness(userTrust, dev, voteCount);
			System.out.println("Trust: " + trust);
			
			double finalScore = al.calcFinalSiteScore(rating, trust, divs[1], divs[0]);
			System.out.println("Score: " + finalScore);
			
			dbq.updateSiteScore(weburl, finalScore, al.getDivs(0), al.getDivs(1));
			dbq.updateUserTrust(userid, trust);
			dbq.insertUserRating(userid, weburl, rating);
			
		}
		
		
	}
}