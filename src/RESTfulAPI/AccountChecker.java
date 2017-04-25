package RESTfulAPI;

import java.util.ArrayList;

import DAO.AccountDAO;
import DataObject.Account;

public class AccountChecker {
	public static boolean isUsernameValid(String username) {
		ArrayList<Account> allAccounts = AccountDAO.getInstance().getAllAccounts();
		for (Account account : allAccounts) {
			String originUsername = account.getUsername();
			if (username == originUsername)
				return false;
		}
		return true;
	}

	public static boolean isAccountAdmin(String md5EncodingUsername, String md5EncodingPassword){
		ArrayList<Account> allAcocunts = AccountDAO.getInstance().getAllAccounts();
		for (Account account : allAcocunts) {
			String originUsername = account.getUsername();
			String md5EncodingAccountUsername = AccountDAO.getMd5(originUsername);
			String md5EncodingAccountPassword = account.getPassword();
			if (md5EncodingUsername != null
					&&  md5EncodingPassword != null
					&& (md5EncodingUsername.equals(md5EncodingAccountUsername))
					&& (md5EncodingPassword.equals(md5EncodingAccountPassword))
					&& account.isAdmin()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmailUsed(String email) {
		ArrayList<Account> allAccounts = AccountDAO.getInstance().getAllAccounts();
		for (Account account : allAccounts) {
			String originEmail = account.getEmail();
			if (email == originEmail)
				return false;
		}
		if (email.indexOf('@') == -1 || email.indexOf(".com") == -1)
			return false;

		return true;
	}

	public static boolean isEmailValid(String email) {
		if (email.indexOf('@') == -1 || email.indexOf(".com") == -1)
			return false;

		return true;
	}

}
