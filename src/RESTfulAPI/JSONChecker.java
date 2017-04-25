package RESTfulAPI;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import Enums.AccountEnum;

public class JSONChecker {
	public static String jsonAccountCheck (String accountJSONString) {
		String message = "";
		String username = "";
		String email = "";
		try {
			JSONObject accountJSON = new JSONObject(accountJSONString);
			username = accountJSON.getString(AccountEnum.USERNAME);
			accountJSON.getString(AccountEnum.NICK_NAME);
			accountJSON.getString(AccountEnum.PASSWORD);
			email = accountJSON.getString(AccountEnum.EMAIL);
			accountJSON.getBoolean(AccountEnum.ENABLE);
			
		} catch (JSONException e) {
			message = e.getMessage();
		}
		if (message.isEmpty()){
			if (!AccountChecker.isUsernameValid(username))
				return "Username has been used.";
			if (!AccountChecker.isEmailValid(email))
				return "Email is invalid.";
			if (!AccountChecker.isEmailUsed(email))
				return "Email has been used.";
		}
		return message;
	}
}
