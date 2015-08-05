package newtest;

import java.net.URL;
import java.sql.Array;

import com.google.common.net.InternetDomainName;


public class Algorithms {
	
	double dev, trust, finalScore;
	double[] site_divs;
	DbQueries dbq;
		
	public Algorithms(){
		dbq = new DbQueries();
	}
	
	public void calcAlgorithms(double siteScore, int userVote, int usr_id, String url){
		dev = calcUserVoteDeviation(siteScore, userVote);
		double userTrustworthiness = dbq.getUserTrustworthiness(usr_id);
		int userVoteAmount = dbq.getUserVoteCount(usr_id);
		site_divs = dbq.getSiteDivs(url);	
		trust = calcUserTrustworthiness(userTrustworthiness, dev,userVoteAmount);
		finalScore =calcFinalSiteScore(userVote, trust,site_divs[1], site_divs[0]);	}
	
	public double getDev(){
		return dev;
	}
	
	public double getTrust(){
		return trust;
	}
	
	public double getFinalScore() {
		return finalScore;
	}
	
	public double[] getSiteDivs(){
		return site_divs;
	}
	
	
	public String trimUrl(String url) {
		String host = null;
		InternetDomainName domainName = null;
		try {
			host = new URL(url).getHost();
			domainName = InternetDomainName.from(host);
		} catch (Exception e) {
			return "";
		}
		return domainName.topPrivateDomain().toString();
	}
	
	private double calcUserVoteDeviation(double siteScore, int userVote) {
		return (100 - Math.abs(siteScore - userVote));
	}
	
	private double calcUserTrustworthiness(double userTrustworthiness, double userDeviation, int userVoteAmount) {
		return userTrustworthiness + (((userDeviation - userTrustworthiness) * (calcArctan(userVoteAmount))) / 1000);
	}
	
	private double calcFinalSiteScore(int userVote, double userTrustworthiness, double divisor, double divident) {
		double d1 = divident + (userVote * Math.pow(userTrustworthiness, 0.5));
		double d2 = divisor + Math.pow(userTrustworthiness, 0.5);
		site_divs[0] = d1;
		site_divs[1] = d2;
		return d1/d2;
	}
	
	private double averageScore(double[] scoreList){
		
		double sum = 0;
		
		sum = scoreList[1]* 0.5;
		sum = sum + scoreList[2]*0.15;
		sum = sum + scoreList[3]*0.35;
		
		return sum/3;		
	}
	
	private double calcArctan(int userVoteAmount) {
		double result = (28.82 * Math.atan((userVoteAmount - 60) / 20.0) + 56);
		if (result > 100.0) return 100.0;
		return result;
	}
	
	public double calc3rdScore(int alexarank, boolean is_malware, boolean is_unwanted, boolean is_phishing) {
		if (alexarank > 10000000) alexarank = 10000000;
		double result = Math.ceil(100 - (alexarank/200000));
		
		if (is_unwanted) result -= 15;
		if (is_malware) result -= 30;
		if (is_phishing) result -= 50;
		if (result < 0) result = 0;
		
		return result;
	}
	
}
