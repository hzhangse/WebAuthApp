package com.train.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LdapConnectTest {

	public LdapConnectTest() {
		// TODO Auto-generated constructor stub
	}

	private void test(){
		String root = "dc=train,dc=com"; // root

		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost/" + root);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=manager,dc=train,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "secret");
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);
			
			System.out.println("认证成功");
		} catch (javax.naming.AuthenticationException e) {
			e.printStackTrace();
			System.out.println("认证失败");
		} catch (Exception e) {
			System.out.println("认证出错：");
			e.printStackTrace();
		}

		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
				// ignore
			}
		}
		System.exit(0);
	}
	private Hashtable getEnv() {  
        Hashtable ht = new Hashtable();  
        ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");  
        ht.put(Context.PROVIDER_URL, "ldap://localhost:389");  
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");  
        ht.put(Context.SECURITY_PRINCIPAL, "cn=manager,dc=train,dc=com");  
        ht.put(Context.SECURITY_CREDENTIALS, "secret");  
        return ht;  
    }  
    private DirContext getContext() {  
        DirContext dc = null;  
        try {  
            dc = new InitialDirContext(this.getEnv());  
            System.out.println("Authentication Successful");  
        } catch (javax.naming.AuthenticationException ex) {  
            ex.printStackTrace();  
            System.out.println("Authentication Failed");  
        } catch (Exception x) {  
            x.printStackTrace();  
            System.out.println("Error!");  
        }  
        return dc;  
    }  
	 private void print() throws NamingException {  
	        DirContext dc=this.getContext();  
	        String root = "dc=train,dc=com";  
	        StringBuffer output = new StringBuffer();  
	        SearchControls ctrl = new SearchControls();  
	        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);  
	        NamingEnumeration enu = dc.search(root, "uid=*", ctrl);//root是入口，不能设置成空，网上很多源码都设为空的。  
	        while (enu.hasMore()) {  
	            SearchResult sr = (SearchResult) enu.next();  
	            Attributes ab = sr.getAttributes();  
	            NamingEnumeration values = ((BasicAttribute) ab.get("userPassword")).getAll();  
	            while (values.hasMore()) {  
	                if (output.length() > 0) {  
	                    output.append("|");  
	                }  
	                output.append(values.next().toString());  
	            }  
	        }  
	         System.out.println("The Password:" + output.toString());  
	       if (dc != null) {  
	            try {  
	                dc.close();  
	            } catch (NamingException e) {  
	            }  
	        }  
	    }  
	 
	 
	 private void search( String uid) throws NamingException {  
	        DirContext dc=this.getContext();  
	        String root = "dc=train,dc=com";  
	        StringBuffer output = new StringBuffer();  
	        SearchControls ctrl = new SearchControls();  
	        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);  
	        NamingEnumeration enu = dc.search(root, "uid=*", ctrl);//root是入口，不能设置成空，网上很多源码都设为空的。  
	        while (enu.hasMore()) {  
	            SearchResult sr = (SearchResult) enu.next();  
	            Attributes ab = sr.getAttributes();  
	            NamingEnumeration values = ((BasicAttribute) ab.get("userPassword")).getAll();  
	            while (values.hasMore()) {  
	                if (output.length() > 0) {  
	                    output.append("|");  
	                }  
	                output.append(values.next().toString());  
	            }  
	        }  
	         System.out.println("The Password:" + output.toString());  
	       if (dc != null) {  
	            try {  
	                dc.close();  
	            } catch (NamingException e) {  
	            }  
	        }  
	    }  
	public static void main(String[] args) throws Exception{
		LdapConnectTest test = new LdapConnectTest();
		test.print();
	}

}
