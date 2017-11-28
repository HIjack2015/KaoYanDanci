/**
 *
 * @(#) JsonReader.java
 * @Package cn.jk.EnglishUtil
 * 
 * Copyright © SingleWindow Corporation. All rights reserved.
 *
 */

package cn.jk.kaoyandanci.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description :
 * 
 * @author: Jia Kang
 *
 *          History: 2017年4月17日 下午5:51:22 Jia Kang Created.
 * 
 */
public class JsonReader {

	private static String readAllByChar(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private static String readAllByLine(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;

		// read from the urlconnection via the bufferedreader
		while ((line = rd.readLine()) != null) {
			sb.append(line + "\n");
		}

		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String urlS) throws IOException, JSONException {

		// create a url object
		URL url = new URL(urlS);

		// create a urlconnection object
		URLConnection urlConnection = url.openConnection();

		// wrap the urlconnection in a bufferedreader
		BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

		String jsonText = readAllByLine(rd);
		

		return new JSONObject(jsonText);

	}

	public static String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		try {
			// create a url object
			URL url = new URL(theUrl);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			content.append(readAllByLine(bufferedReader));
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
