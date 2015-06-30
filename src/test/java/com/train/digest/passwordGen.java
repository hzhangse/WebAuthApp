package com.train.digest;

import org.apache.catalina.realm.RealmBase;

public class passwordGen {
	static String algorithm = "MD5";
	static String realmName = "www.rainbow954-digest.cn";
	static String encoding = "UTF-8";
	public static void genTomcatBasicAuthPass() {
		genBasicUserPass("tomcat","tomcat");
		genBasicUserPass("mysql","mysql");
		genBasicUserPass("ryan","123456");
		genBasicUserPass("root","123456");
	}

	private static String genBasicUserPass(String credentials, String passwd) {
		String digestValue =  passwd;
		String result = RealmBase.Digest(digestValue, algorithm, encoding);
		//System.out.println("user:"+credentials + "  pwd:"+result);
		String sql = "UPDATE  `digest`.`users` SET `user_pass`='"+result+"' WHERE `user_name`='"+credentials+"';";
		System.out.println(sql);
		return result;
	}

	private static String genDigestUserPass(String credentials, String passwd) {
		String digestValue = credentials + ":" + realmName + ":" + passwd;
		String result = RealmBase.Digest(digestValue, algorithm, encoding);
		//System.out.println("user:"+credentials + "  pwd:"+result);
		
		String sql = "UPDATE  `digest`.`users` SET `user_pass`='"+result+"' WHERE `user_name`='"+credentials+"';";
		System.out.println(sql);
		return result;
	}
	
	public static void genTomcatDigestAuthPass() {
		genDigestUserPass("ryan","123456");
		genDigestUserPass("root","123456");
		genDigestUserPass("tomcat","tomcat");
		genDigestUserPass("mysql","mysql");
	}

	public static void main(String[] args) {
		//genTomcatDigestAuthPass();
		genTomcatBasicAuthPass();
	}
}
