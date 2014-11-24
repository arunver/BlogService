package service.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class test {

	/**
	 * @param args
	 * @return
	 */

	public String show() {

		try {
			
			//URL url = newURL("http://localhost:8080/MyService-1/rs/postService/addPost");
			//URL url = new URL("http://localhost:8080/MyService-1/rs/postService/addComment");
			//URL url = new URL("http://localhost:8080/MyService-1/rs/postService/show");
			
			String newUrl="http://localhost:8080/MyService-1/rs/postService/getPost";
			String permalink="bjdgkubdbesvrgkvvelt";
			newUrl= newUrl + "/{"+permalink+"}";
			URL url = new URL(newUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			//conn.setRequestMethod("POST");
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");

			/*Date date = new Date();
			SimpleDateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String formattedDate = tsdf.format(date);*/
			
			
			//String input = "{\"body\": \"This is a dummy blog new comment. \",\"author\": \"Arun Verma\",\"email\": \"arun.verma@netapp.com\",\"date\":\""+formattedDate+"\"}";
			// String
			// input="{\"body\": \"This is a dummy blog post. \",\"permalink\": \"cxzdzjkztkqraoqlgcru\",\"author\": \"Arun\",\"title\": \"Nice Post\"}";
			
			/*OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();*/

			// System.out.println("response code:"+conn.getResponseCode() );
			// System.out.println("conn.getInputStream :"+conn.getInputStream()
			// );

			Scanner scanner;
			String response;
			if (conn.getResponseCode() != 200) {
				scanner = new Scanner(conn.getErrorStream());
				response = "Error From Server \n\n";
			} else {
				scanner = new Scanner(conn.getInputStream());
				response = "Response From Server \n\n";
			}
			scanner.useDelimiter("\\Z");
			response = scanner.next();
			scanner.close();
			conn.disconnect();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		test t = new test();

		System.out.println(t.show());

		// Post post= new Post();

	}

}
