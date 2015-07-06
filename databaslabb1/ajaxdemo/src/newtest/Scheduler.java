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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;



public class Scheduler {

	ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

	public Scheduler(){
		//do nothing
	}

	public void phishtankScheduler() {
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
	}
	
	public void googleSBScheduler() {
		Runnable googleRunnable = new Runnable() {
			public void run() {
				try {
					String baseURL="https://sb-ssl.google.com/safebrowsing/api/lookup";
	
					String arguments = "";
					arguments +=URLEncoder.encode("client", "UTF-8") + "=" + URLEncoder.encode("api", "UTF-8") + "&";
					arguments +=URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("AIzaSyAOdwo7e888Fq1oFL9NN65nTuF04-DaJJI", "UTF-8") + "&";
					arguments +=URLEncoder.encode("appver", "UTF-8") + "=" + URLEncoder.encode("1.5.2", "UTF-8") + "&";
					arguments +=URLEncoder.encode("pver", "UTF-8") + "=" + URLEncoder.encode("3.1", "UTF-8");
					// Construct the url object representing cgi script
					URL url = new URL(baseURL + "?" + arguments);
					System.out.println(url);
	
					// Get a URLConnection object, to write to POST method
					HttpURLConnection connect = (HttpURLConnection) url.openConnection();
					connect.setRequestMethod("POST");
	
					// Specify connection settings
					connect.setDoInput(true);
					connect.setDoOutput(true);
					
					// Get an output stream for writing
					OutputStream output = connect.getOutputStream();
					PrintStream pout = new PrintStream (output);
					pout.print("2");
					pout.println();
					pout.print("http://www.google.com");
					pout.println();
					pout.print("http://www.ianfette.org");
					pout.close();
					
					BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
					String decodedString;
					while ((decodedString = in.readLine()) != null) {
					    System.out.println(decodedString);
					}
					in.close();
					
					System.out.println(connect.getResponseCode());
					
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		};
		service.scheduleAtFixedRate(googleRunnable, 0, 60, TimeUnit.MINUTES);
	}
}