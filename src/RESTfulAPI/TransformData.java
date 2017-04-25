package RESTfulAPI;

import DataObject.Account;
import Enums.AccountEnum;
import json.JSONObject;


public class TransformData {
	public static String accountToJSONString (Account account) {
		String entity = "";
		if (account != null) {
			JSONObject accountJSON = new JSONObject();
			accountJSON.put(AccountEnum.ID, account.getId());
			accountJSON.put(AccountEnum.USERNAME, account.getUsername());
			accountJSON.put(AccountEnum.NICK_NAME, account.getNickName());
			accountJSON.put(AccountEnum.PASSWORD, account.getPassword());
			accountJSON.put(AccountEnum.EMAIL, account.getEmail());
			accountJSON.put(AccountEnum.CREATE_TIME, account.getCreateTime());
			accountJSON.put(AccountEnum.UPDATE_TIME, account.getUpdateTime());
			entity = accountJSON.toString();
		}
		return entity;
	}
	public static Account JSONStringtoAccount (String accountJSONString){
		Account account = null;
		JSONObject accountJSON = new JSONObject(accountJSONString);

		// Get Account Information
		String userName = accountJSON.getString(AccountEnum.USERNAME);
		String userNickName = accountJSON.getString(AccountEnum.NICK_NAME);
		String userPassword = accountJSON.getString(AccountEnum.PASSWORD);
		String userEmail = accountJSON.getString(AccountEnum.EMAIL);
		boolean userEnable = accountJSON.getBoolean(AccountEnum.ENABLE);

		// Create Account
		account = new Account(userName);
		account.setNickName(userNickName)
		       .setPassword(userPassword)
		       .setEmail(userEmail)
		       .setEnable(userEnable);
		return account;
	}


}
