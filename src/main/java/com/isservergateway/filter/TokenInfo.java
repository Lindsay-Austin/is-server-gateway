/**
 * 
 */
package com.isservergateway.filter;

import lombok.Data;

import java.util.Date;

/**
 * @author jojo
 * tokenInfo中存储从/token/check中返回的用户信息
 *
 */
@Data
public class TokenInfo {

	private boolean active;
	
	private String client_id;
	
	private String[] scope;
	
	private String user_name;
	
	private String[] aud;
	
	private Date exp;
	
	private String[] authorities;

	public boolean isActive(){
		return active;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
