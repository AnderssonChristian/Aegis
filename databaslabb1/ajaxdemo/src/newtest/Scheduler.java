package newtest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class Scheduler {

	ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	DbQueries dbq = new DbQueries();

	public Scheduler(){
		//do nothing
	}
	
	public void startScheduler() {
		Runnable scheduleRunnable = new Runnable() {
			public void run() {
				updateAlexaNewSites();
				ArrayList<String[]> al = getSitesToUpdate();
				updateScannedSites(al);
			}
		};
		service.scheduleAtFixedRate(scheduleRunnable, 0, 20, TimeUnit.SECONDS);
	}

	/*public void phishtankScheduler() {
		Runnable ptRunnable = new Runnable() {
			public void run() {
				try {
				URL url = new URL("http://checkurl.phishtank.com/checkurl/");
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			    writer.write("url=http://shaghaf-edu.com/wp-content/confirmation/&format=xml&app_key=eb3a9e30109c0804656ed91eec3c0468becf2daff15c9e3e7a6b5664959f5c70");
			    writer.flush();
			    String line;
			    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    while ((line = reader.readLine()) != null) {
			      System.out.println(line);
			    }
			    writer.close();
			    reader.close();
				} catch (Exception e) {
					
				}
			}
		};	
		service.scheduleAtFixedRate(ptRunnable, 0, 60, TimeUnit.MINUTES);
	}*/
	
	private ArrayList<String[]> googleSBLookup(ArrayList<String> al) {
		ArrayList<String[]> new_al = new ArrayList<String[]>();
		try {
			String baseURL="https://sb-ssl.google.com/safebrowsing/api/lookup";

			String arguments = "";
			arguments +=URLEncoder.encode("client", "UTF-8") + "=" + URLEncoder.encode("api", "UTF-8") + "&";
			arguments +=URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("AIzaSyAOdwo7e888Fq1oFL9NN65nTuF04-DaJJI", "UTF-8") + "&";
			arguments +=URLEncoder.encode("appver", "UTF-8") + "=" + URLEncoder.encode("1.5.2", "UTF-8") + "&";
			arguments +=URLEncoder.encode("pver", "UTF-8") + "=" + URLEncoder.encode("3.1", "UTF-8");
			// Construct the url object representing cgi script
			URL url = new URL(baseURL + "?" + arguments);

			// Get a URLConnection object, to write to POST method
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("POST");

			// Specify connection settings
			connect.setDoInput(true);
			connect.setDoOutput(true);
			
			// Get an output stream for writing
			OutputStream output = connect.getOutputStream();
			PrintStream pout = new PrintStream (output);
			pout.print(al.size());
			pout.println();
			for (String temp : al) {
				pout.print(temp);
				pout.println();
			}			
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			String decodedString;
			int i = 0;
			while ((decodedString = in.readLine()) != null) {
				String[] arr = new String[2];
				arr[0] = al.get(i);
				arr[1] = decodedString;
			    new_al.add(arr);
			    i++;
			    al.remove(i);
			}
			for (String temp : al){
				String[] arr = new String[2];
				arr[0] = temp;
				arr[1] = "ok";
				new_al.add(arr);
			}
			in.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return new_al;
	}
	
		
	private int getAlexaRanking(String domain) {
		 
		int result = 0;
 
		String url = "http://data.alexa.com/data?cli=10&url=" + domain;
 
		try {
 
			URLConnection conn = new URL(url).openConnection();
			InputStream is = conn.getInputStream();
 
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
 
			Element element = doc.getDocumentElement();
 
			NodeList nodeList = element.getElementsByTagName("POPULARITY");
			if (nodeList.getLength() > 0) {
				Element elementAttribute = (Element) nodeList.item(0);
				String ranking = elementAttribute.getAttribute("TEXT");
				if(!"".equals(ranking)){
					result = Integer.valueOf(ranking);
				}
			}
 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 
		return result;
	}
	
	private void updateAlexaNewSites(){
		int rank;
    	ArrayList<String> al = dbq.getNoAlexaSites();
    	for (String url : al){
    		rank = getAlexaRanking(url);
    		dbq.updateSiteAlexa(rank, url);
    	}
	}
	
	private ArrayList<String[]> getSitesToUpdate(){
		ArrayList<String[]> al = dbq.getSitesToUpdate();
		ArrayList<String> urllist = new ArrayList<String>();
		ArrayList<String[]> new_al;
		
		for (String[] url : al){
    		urllist.add(url[0]);
    	}

		return new_al = googleSBLookup(urllist);
	}
	
	private void updateScannedSites(ArrayList<String[]> al){
		for (String[] site : al) {
			switch (site[1]) {
            	case "ok":  dbq.updateSiteCategories(site[0], 0, 0, 0);
            		break;
            	case "malware":  dbq.updateSiteCategories(site[0], 1, 0, 0);
            		break;
            	case "unwanted":  dbq.updateSiteCategories(site[0], 0, 1, 0);
            		break;
            	case "phishing":  dbq.updateSiteCategories(site[0], 0, 0, 1);
            		break;
            	case "malware,unwanted":  dbq.updateSiteCategories(site[0], 1, 1, 0);
            		break;
            	case "phishing,malware":  dbq.updateSiteCategories(site[0], 1, 0, 1);
            		break;
            	case "phishing,unwanted":  dbq.updateSiteCategories(site[0], 0, 1, 1);
            		break;
            	case "phishing,malware,unwanted":  dbq.updateSiteCategories(site[0], 1, 1, 1);
            		break;
			}
		}
	}
}
