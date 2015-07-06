package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class GetTimeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	String pluginId, usercomment, site, req, siteScheme;
	int userrating = -1;
	String[] chk = new String[8];
	newtest.DbQueries dbq = new newtest.DbQueries();
	newtest.Algorithms alg = new newtest.Algorithms();

	public void doPost (HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
	    StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    } finally {
	        reader.close();
	    }
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();		
		
		String postvar = sb.toString();
		String[] postarr = new String[26];
		postarr = postvar.split("&");
		
		req = postarr[0].split("=")[1];
		site = postarr[1].split("=")[1];
		site = java.net.URLDecoder.decode(site, "UTF-8");
		siteScheme = site.substring(0, site.indexOf(":"));
		site = alg.trimUrl(site);
		pluginId = postarr[2].split("=")[1];
		System.out.println("Request:" + req);
		
		int usr_id = dbq.getUserId(pluginId);
		
		if(req.equals("1")) {
			userrating = (int) Double.parseDouble(postarr[3].split("=")[1]);			
			try {
				usercomment = postarr[4].split("=")[1];
			} catch (Exception e) {
				usercomment = "";
			}
			for(int i=0; i<8; i++) {
				chk[i] = postarr[i+5].split("=")[1];
				if (chk[i].equals("Set")) {
					chk[i] = "1";
				} else {
					chk[i] = "0";
				}
			}

			System.out.println(usr_id);
			if (usr_id == -1) {
				if(dbq.createUser(pluginId)) {
					usr_id = dbq.getUserId(pluginId);
				}
			}
			dbq.insertUserRating(usr_id, site, userrating, usercomment, chk);
		} else if (req.equals("2")) System.out.println("yes!");
		
		JSONObject JSONobj = new JSONObject();
		double[] arr = dbq.getSiteScore(site);
		String userSiteRating[] = (dbq.getUserRating(usr_id, site));
		
		if (!(arr == null)) {
			try {
				for (int i=0; i<4; i++) {				
					JSONobj.put(Integer.toString(i), Math.round(arr[i])); //insert into JSON object user/server/3rd/overall score.
				}
				if (userSiteRating != null) { 
					JSONobj.put(Integer.toString(4), userSiteRating[0]);
					JSONobj.put(Integer.toString(5), userSiteRating[1]);
					for(int i=2; i<10; i++) {
						if (userSiteRating[i].equals("1")) {
							userSiteRating[i] = "Set";
						} else {
							userSiteRating[i] = "None";
						}
						
					}
					JSONobj.put(Integer.toString(6), userSiteRating[2]);
					JSONobj.put(Integer.toString(7), userSiteRating[3]);
					JSONobj.put(Integer.toString(8), userSiteRating[4]);
					JSONobj.put(Integer.toString(9), userSiteRating[5]);
					JSONobj.put(Integer.toString(10), userSiteRating[6]);
					JSONobj.put(Integer.toString(11), userSiteRating[7]);
					JSONobj.put(Integer.toString(12), userSiteRating[8]);
					JSONobj.put(Integer.toString(13), userSiteRating[9]);
					System.out.println(userSiteRating[9]);
				} else {
					JSONobj.put(Integer.toString(4), "-1");
					JSONobj.put(Integer.toString(5), "");
					JSONobj.put(Integer.toString(6), "None");
					JSONobj.put(Integer.toString(7), "None");
					JSONobj.put(Integer.toString(8), "None");
					JSONobj.put(Integer.toString(9), "None");
					JSONobj.put(Integer.toString(10), "None");
					JSONobj.put(Integer.toString(11), "None");
					JSONobj.put(Integer.toString(12), "None");
					JSONobj.put(Integer.toString(13), "None");
				}
			} catch (JSONException e) {
			}
		} else {
			try {
				JSONobj.put("0", 0);
				JSONobj.put("1", 0);
				JSONobj.put("2", 0);
				JSONobj.put("3", 0);
				JSONobj.put("4", "-1");
				JSONobj.put("5", "");
				JSONobj.put("6", "None");
				JSONobj.put("7", "None");
				JSONobj.put("8", "None");
				JSONobj.put("9", "None");
				JSONobj.put("10", "None");
				JSONobj.put("11", "None");
				JSONobj.put("12", "None");
				JSONobj.put("13", "None");
			} catch (JSONException e) {}
		}
		double[] siteCatPerc = dbq.getSiteCatPercent(site);
		String[] classifications = {"Good Site", "Ads/Popups", "Privacy Risks", "Illegal Content", "Scam/Phishing", "Virus/Malware", "Adult Content", "Other"};
		String class_result = "";
		double class_other = 0;
		try {
			for (int i=0; i<7; i++) {
				if (siteCatPerc[i] >= 0.05){
					class_result += classifications[i] + " (" + Math.round(siteCatPerc[i] * 100) + "%), ";
				} else {
					class_other += siteCatPerc[i];
				}
			}
			class_other += siteCatPerc[7];
			if (class_other >= 0.05){
				class_result += "Other (" + Math.round(class_other * 100) + "%)  ";
			}
			if (class_result != null && class_result.length() > 0) {
				class_result = class_result.substring(0, class_result.length()-2);
			}
			System.out.println(class_result);			
			JSONobj.put("14", class_result);
			
			String res[][] = new String[10][2];
			res = dbq.getSiteComments(site);
			for (int i=0; i<10; i++) {
				if (res[i][0] != null) {
					JSONobj.put("comment_text_" + i, res[i][0]);
					JSONobj.put("comment_date_" + i, res[i][1]);
				}
			}
			if (siteScheme.equals("http")) {
				JSONobj.put("siteScheme", "HTTP (insecure)");
			} else if (siteScheme.equals("https")) {
				JSONobj.put("siteScheme", "HTTPS (secure)");
			} else {
				JSONobj.put("siteScheme", "Unknown; potentially insecure");
			}
			JSONobj.put("siteUrl", site);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		out.print(JSONobj);
	}
}