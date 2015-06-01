package com.train.HttpClient.digestAuth;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

import com.train.HttpClient.BaseHttpClient;

public class FormAuthTest extends BaseHttpClient {

	static String appAddr = "http://localhost:8080";
	
	static String appUri = "/FormAuth/jsp/index.html";

	static String username = "tomcat";
	static String password = "tomcat";
	static String realm = "www.rainbow954-digest.cn";




	
	public void testFormAuthAppSucc() throws Exception {

		testFormAuth("root", "123456", appAddr, appUri);
	}

	public void testFormAuth(String username, String password,
			String appAddr, String docUri) throws Exception {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpClient defHttp = new HttpClient(connectionManager);

		GetMethod httpGetDoc = new GetMethod(appAddr + docUri);

		int statuscode = executeWithStatus(defHttp, httpGetDoc);

		
		if (statuscode == 200) {
			// 服务端响应头中会带有一个WWW-Authenticate的信息

			Header[] authHeaders = httpGetDoc
					.getResponseHeaders("WWW-Authenticate");
			httpGetDoc.getResponseHeaders();
			Header authHeader = authHeaders[0];
			System.out.println(authHeader.getValue());

			/**
			 * server generate Authorization:(Digest)
			 * realm="www.rainbow954-digest.cn", qop=auth,
			 * nonce="1432912158728:2bbc8d55a47cc44cb41e067b3c094e0c",
			 * opaque="0589D14093CC6F8FF91BCB46E42C1CC9",
			 **/

			/**
			 * client side generate nc=00000001, username="tomcat",
			 * uri="/DigestAuth/doc/index.html",
			 * response="e3343a50c80ec2d8afea7ec4d3f456c1",
			 * cnonce="9e9e69b37374548c"
			 **/

			// WWW-Authenticate value中有很多信息，如nonce、qop、opaque、realm信息
			Map<String, String> maps = getMapByKeyArray(authHeader.getValue()
					.split(","));

			maps.put("username", username);
			maps.put("nc", "00000001");
			maps.put("cnonce", "9e9e69b37374548c");
			maps.put("uri", docUri);
		//	password = this.getDigestPassword(username, password, docUri);
			maps.put("response", getResponse(maps, password));

			// 开始拼凑Authorization 头信息
			StringBuffer authorizationHaderValue = new StringBuffer();
			authorizationHaderValue
					.append("Digest username=\"")
					.append(maps.get("username"))
					.append("\", ")
					.append("realm=\"")
					.append(maps.get("realm"))
					.append("\", ")
					// .append("nonce=\"").append(maps.get("nonceTime")).append(maps.get("nonce")).append("\", ")
					.append("nonce=\"").append(maps.get("nonce"))
					.append("\", ").append("uri=\"").append(maps.get("uri"))
					.append("\", ").append("response=\"")
					.append(maps.get("response")).append("\", ")
					.append("opaque=\"").append(maps.get("opaque"))
					.append("\", ").append("qop=").append(maps.get("qop"))
					.append(", ").append("nc=").append(maps.get("nc"))
					.append(", ").append("cnonce=\"")
					.append(maps.get("cnonce")).append("\"");

			System.out.println(authorizationHaderValue.toString());

			defHttp = new HttpClient(connectionManager);

			// 添加到请求头中

			httpGetDoc.addRequestHeader("Authorization",
					authorizationHaderValue.toString());
			// 请求资源

			this.executeWithResponse(defHttp, httpGetDoc);
			// 打印响应码
			// System.out.println(response.getStatusLine().getStatusCode());
			// // 打印响应的信息
			// System.out.println(readResultStreamString(response.getEntity(),
			// defHttp));
		}

	}

	/**
	 * 通过HTTP 摘要认证的算法得出response
	 * 
	 * @return String
	 */
	public static String getResponse(Map<String, String> maps, String password)
			throws Exception {
		String HA1 = MD5Object.encrypt(maps.get("username") + ":"
				+ maps.get("realm") + ":" + password);
		System.out.println("HA1：" + HA1);

		String HA2 = MD5Object.encrypt("GET:" + maps.get("uri"));
		System.out.println("HA2:" + HA2);

		String response = MD5Object.encrypt(HA1 + ":" + maps.get("nonce") + ":"
				+ maps.get("nc") + ":" + maps.get("cnonce") + ":"
				+ maps.get("qop") + ":" + HA2);
		System.out.println(response);
		return response;
	}

	public static String getValueByName(String resourceStr) {
		return resourceStr.substring(resourceStr.indexOf("\"") + 1,
				resourceStr.lastIndexOf("\""));

	}

	public static Map<String, String> getMapByKeyArray(String[] resourceStr) {
		Map<String, String> maps = new HashMap<String, String>(8);
		for (String str : resourceStr) {
			if (str.contains("realm")) {
				maps.put("realm", getValueByName(str));
			} else if (str.contains("qop")) {
				maps.put("qop", getValueByName(str));
			} else if (str.contains("nonce")) {
				maps.put("nonce", getValueByName(str));
				// maps.put("nonce", getValueByName(str, "nonce"));
				// maps.put("nonceTime", getValueByName(str, "nonceTime") +
				// ":");
			} else if (str.contains("opaque")) {
				maps.put("opaque", getValueByName(str));
			}
		}

		return maps;
	}

	// /**
	// * 用于读取字符串响应结果
	// *
	// * @return String
	// *
	// * @throws IOException
	// */
	// protected static String readResultStreamString(HttpEntity httpEntity,
	// DefaultHttpClient defaultHttpClient) throws IOException {
	// String result = null;
	// InputStream resultStream = null;
	//
	// ByteArrayOutputStream outputStream = null;
	// try {
	// resultStream = httpEntity.getContent();
	//
	// outputStream = new ByteArrayOutputStream(45555);
	//
	// byte[] temp = new byte[4096];
	// int length = resultStream.read(temp);
	// while (length > 0) {
	// outputStream.write(temp, 0, length);
	//
	// length = resultStream.read(temp);
	// }
	//
	// defaultHttpClient.getConnectionManager().shutdown();
	// } catch (IOException ioEx) {
	// throw new IOException(ioEx);
	// } finally {
	// if (null != resultStream) {
	// try {
	// resultStream.close();
	// } catch (Exception ex) {
	// resultStream = null;
	// }
	// }
	// }
	//
	// if (null != outputStream) {
	// result = outputStream.toString();
	// }
	// return result;
	// }
}
