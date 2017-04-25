package DataObject;

import java.util.ArrayList;

import DAO.AccountDAO;

public class Account {
	private final static int DEFAULT_VALUE = -1;

	private long mId = DEFAULT_VALUE;
	private String mUsername = "";
	private String mPassword = "";
	private String mEmail = "";
	private String mNickName = "";
	private boolean mEnable = false;
	private long mCreateTime = DEFAULT_VALUE;
	private long mUpdateTime = DEFAULT_VALUE;

	public Account(long id, String username) {
		mUsername = username;
		mId = id;
	}

	public Account(String username) {
		if (username != null && username != "") {
			mUsername = username;
		} else {
			throw new RuntimeException();
		}
	}

	public boolean isAdmin(){
		// TODO design
		return false;
	}
	
	public long getId() {
		return mId;
	}

	public long getCreateTime() {
		return mCreateTime;
	}

	public Account setCreateTime(long createTime) {
		mCreateTime = createTime;
		return this;
	}

	public long getUpdateTime() {
		return mUpdateTime;
	}

	public Account setUpdateTime(long updateTime) {
		mUpdateTime = updateTime;
		return this;
	}

	public String getPassword() {
		return mPassword;
	}

	public Account setPassword(String password) {
		mPassword = password;
		return this;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getEmail() {
		return mEmail;
	}

	public Account setEmail(String email) {
		mEmail = email;
		return this;
	}

	public String getNickName() {
		return mNickName;
	}

	public Account setNickName(String nickName) {
		mNickName = nickName;
		return this;
	}

	public boolean getEnable() {
		return mEnable;
	}

	public Account setEnable(boolean enable) {
		mEnable = enable;
		return this;
	}

	/**
	 * Get account by account id
	 * 
	 * @param id account id
	 * @return AccountObject
	 */
	public static Account get(long id) {
		return AccountDAO.getInstance().getAccountById(id);
	}

	/**
	 * Get account by account user name
	 * 
	 * @param username account user name
	 * @return AccountObject
	 */
	public static Account get(String username) {
		return AccountDAO.getInstance().getAccountByUsername(username);
	}

	/**
	 * get accounts in ezScrum
	 * 
	 * @return AccountObject list
	 */
	public static ArrayList<Account> getAllAccounts() {
		return AccountDAO.getInstance().getAllAccounts();
	}

	/**
	 * Create map about user and role in each attend project
	 * 
	 * @param projectId
	 * @param role
	 * @return isCreateSuccess
	 */

	/**
	 * Get account access mapping each attend project
	 * 
	 * @return account access map <"Project name", "Project role">
	 */


	/**
	 * Delete account's role in project
	 * 
	 * @param projectId
	 * @param role
	 * @return isDeleteSuccess
	 */

	/**
	 * Create project system role
	 * 
	 * @return isCreateSuccess
	 */
	public boolean createSystemRole() {
		return AccountDAO.getInstance().createSystemRole(mId);
	}

	/*	 * 
	 * @return admin account's project role
	 */


	/**
	 * Delete account's system role in project
	 * 
	 * @return isDeleteSuccess
	 */
	public boolean deleteSystemRole() {
		return AccountDAO.getInstance().deleteSystemRole(mId);
	}

	/**
	 * Use username and password to get account
	 * 
	 * @param username
	 * @param password
	 * @return AccountObject
	 */
	public static Account confirmAccount(String username, String password) {
				if (password.length() != 32) {
			password = AccountDAO.getMd5(password);
		}
		return AccountDAO.getInstance().confirmAccount(username, password);
	}

	public boolean delete() {
		boolean success = AccountDAO.getInstance().delete(mId);
		if (success) {
			mId = DEFAULT_VALUE;
		}
		return success;
	}

	public void save() {
		if (exists()) {
			doUpdate();
		} else {
			doCreate();
		}
	}

	public void reload() {
		if (exists()) {
			Account account = AccountDAO.getInstance().getAccountById(mId);
			resetData(account);
		}
	}

	public boolean exists() {
		Account account = AccountDAO.getInstance().getAccountById(mId);
		return account != null;
	}

	private void resetData(Account account) {
		mId = account.getId();
		mUsername = account.getUsername();
		mPassword = account.getPassword();
		mEmail = account.getEmail();
		mNickName = account.getNickName();
		mEnable = account.getEnable();
		mCreateTime = account.getCreateTime();
		mUpdateTime = account.getUpdateTime();
	}

	private void doCreate() {
		mCreateTime = System.currentTimeMillis();
		mId = AccountDAO.getInstance().create(this);
		reload();
	}

	private void doUpdate() {
		mUpdateTime = System.currentTimeMillis();
		AccountDAO.getInstance().update(this);
	}
}
