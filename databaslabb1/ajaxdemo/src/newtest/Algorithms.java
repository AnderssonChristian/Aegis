package newtest;

import java.net.URL;
import java.sql.Array;

import com.google.common.net.InternetDomainName;


public class Algorithms {
	
	double divident;
	double divisor;
	double[] arr = new double[2];
		
	public Algorithms(){
		
	}
	
	public double calcUserVoteDeviation(double siteScore, int userVote) {
		return (100 - Math.abs(siteScore - userVote));
	}
	
	public double calcUserTrustworthiness(double userTrustworthiness, double userDeviation, int userVoteAmount) {
		return userTrustworthiness + (((userDeviation - userTrustworthiness) * (calcArctan(userVoteAmount))) / 1000);
	}
	
	public double calcFinalSiteScore(int userVote, double userTrustworthiness, double divisor, double divident) {
		double d1 = divident + (userVote * Math.pow(userTrustworthiness, 0.5));
		double d2 = divisor + Math.pow(userTrustworthiness, 0.5);
		this.divident = d1;
		this.divisor = d2;
		return d1/d2;
	}
	
	public double getDivs(int idx){
		arr[0] = divident;
		arr[1] = divisor;
		return arr[idx];
	}
	
	public double averageScore(double[] scoreList){
		
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
	
}
