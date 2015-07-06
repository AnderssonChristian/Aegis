package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTimeServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	public void doPost (HttpServletRequest request,HttpServletResponse response) 
			throws ServletException, IOException 
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
		//Date currentTime= new Date();
		//String message = String.format("Currently time is %tr on %tD.", currentTime, currentTime);
		//out.print(message);
		newtest.DbQueries dbq = new newtest.DbQueries();
		out.print((int) Math.round(dbq.getSiteScore(sb.toString())));
	}
}