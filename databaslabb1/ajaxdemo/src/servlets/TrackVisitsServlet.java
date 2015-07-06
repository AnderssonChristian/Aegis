package servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/TrackVisitsServlet")
public class TrackVisitsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	String url, pluginId;
	newtest.DbQueries dbq = new newtest.DbQueries();
	newtest.Algorithms alg = new newtest.Algorithms();
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	    String postvar = sb.toString();
	    String[] postarr = new String[26];
		postarr = postvar.split("&");
	    
	    url = java.net.URLDecoder.decode(postarr[0].split("=")[1], "UTF-8");
		pluginId = postarr[1].split("=")[1];
		
		url = alg.trimUrl(url);
		if(!dbq.siteExistsInDB(url)) {
			dbq.addNewSite(url);
		}
		
		int usr_id = dbq.getUserId(pluginId);		
		if (usr_id == -1) {
			if(dbq.createUser(pluginId)) {
				usr_id = dbq.getUserId(pluginId);
			}
		}
		dbq.updateSiteVisits(url, usr_id);
		
	}

}