package DAO;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataObject.Account;
import Enums.AccountEnum;
import Enums.ProjectRoleEnum;
import Enums.RoleEnum;
import Enums.SystemEnum;
import SQLService.Configuration;
import SQLService.MySQLControl;
import SQLService.MySQLQuerySet;
import SQLService.QuerySet;

public class AccountDAO {
	protected Configuration mConfig;
	private MySQLControl mControl;
	private static AccountDAO sInstance = null;
	
	public static AccountDAO getInstance() {
		if (sInstance == null) {
			sInstance = new AccountDAO();
		}
		return sInstance;
	}
	private AccountDAO() {
		mConfig = new Configuration();
		mControl = new MySQLControl(mConfig);
		mControl.connect();
	}
	
	protected void closeResultSet(ResultSet result) {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	}
	

	public long create(Account account) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		valueSet.addInsertValue(AccountEnum.USERNAME, account.getUsername());
		valueSet.addInsertValue(AccountEnum.NICK_NAME, account.getNickName());
		valueSet.addInsertValue(AccountEnum.EMAIL, account.getEmail());
		valueSet.addInsertValue(AccountEnum.PASSWORD, getMd5(account.getPassword()));
		valueSet.addInsertValue(AccountEnum.ENABLE, String.valueOf(account.getEnable() == true ? 1 : 0));
		valueSet.addInsertValue(AccountEnum.CREATE_TIME, account.getCreateTime());
		valueSet.addInsertValue(AccountEnum.UPDATE_TIME, account.getCreateTime());
		String query = valueSet.getInsertQuery();

		mControl.execute(query, true);

		String[] keys = mControl.getKeys();
		long id = Long.parseLong(keys[0]);
		return id;
	}

	public boolean update(Account account) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		valueSet.addEqualCondition(AccountEnum.ID, account.getId());
		valueSet.addInsertValue(AccountEnum.NICK_NAME, account.getNickName());
		valueSet.addInsertValue(AccountEnum.EMAIL, account.getEmail());
		if (account.getPassword() != null && !account.getPassword().equals("")) {
			valueSet.addInsertValue(AccountEnum.PASSWORD, getMd5(account.getPassword()));
		}
		valueSet.addInsertValue(AccountEnum.ENABLE,
				account.getEnable() == true ? 1 : 0);
		valueSet.addInsertValue(AccountEnum.UPDATE_TIME, account.getUpdateTime());
		String query = valueSet.getUpdateQuery();

		return mControl.executeUpdate(query);
	}

	public boolean delete(long id) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		valueSet.addEqualCondition(AccountEnum.ID, id);
		String query = valueSet.getDeleteQuery();
		return mControl.executeUpdate(query);
	}

	/**
	 * Create map about user and role in each attend project
	 * 
	 * @param projectId
	 * @param accountId
	 * @param role
	 * @return isCreateSuccess
	 */
	public boolean createProjectRole(long projectId, long accountId,
			RoleEnum role) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ProjectRoleEnum.TABLE_NAME);
		valueSet.addInsertValue(ProjectRoleEnum.PROJECT_ID, projectId);
		valueSet.addInsertValue(ProjectRoleEnum.ACCOUNT_ID, accountId);
		valueSet.addInsertValue(ProjectRoleEnum.ROLE, String.valueOf(role.ordinal()));
		valueSet.addInsertValue(ProjectRoleEnum.CREATE_TIME, String.valueOf(System.currentTimeMillis()));
		valueSet.addInsertValue(ProjectRoleEnum.UPDATE_TIME, String.valueOf(System.currentTimeMillis()));
		String query = valueSet.getInsertQuery();
		return mControl.executeUpdate(query);
	}


	/**
	 * Delete account's role in project
	 * 
	 * @param projectId
	 * @param accountId
	 * @param role
	 * @return isDeleteSuccess
	 */
	public boolean deleteProjectRole(long projectId, long accountId, RoleEnum role) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ProjectRoleEnum.TABLE_NAME);
		valueSet.addEqualCondition(ProjectRoleEnum.PROJECT_ID, projectId);
		valueSet.addEqualCondition(ProjectRoleEnum.ACCOUNT_ID, accountId);
		valueSet.addEqualCondition(ProjectRoleEnum.ROLE, String.valueOf(role.ordinal()));
		String query = valueSet.getDeleteQuery();
		return mControl.executeUpdate(query);
	}

	/**
	 * Create project system role
	 * 
	 * @param accountId
	 * @return isCreateSuccess
	 */
	public boolean createSystemRole(long accountId) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(SystemEnum.TABLE_NAME);
		valueSet.addInsertValue(SystemEnum.ACCOUNT_ID, accountId);
		String query = valueSet.getInsertQuery();
		return mControl.executeUpdate(query);
	}



	/**
	 * Delete account's system role in project
	 * 
	 * @param accountId
	 * @return isDeleteSuccess
	 */
	public boolean deleteSystemRole(long accountId) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(SystemEnum.TABLE_NAME);
		valueSet.addEqualCondition(SystemEnum.ACCOUNT_ID, accountId);
		String query = valueSet.getDeleteQuery();
		return mControl.executeUpdate(query);
	}


	/**
	 * 嚙踝蕭嚙緻嚙瞎嚙論下嚙踝蕭嚙課佗蕭嚙踝蕭嚙踝蕭
	 * 
	 * @param id
	 *            project id
	 * @return project member list
	 */
	/**
	 * 嚙踝蕭嚙緻嚙瞎嚙論下嚙踝蕭嚙課佗蕭嚙踝蕭嚙踝蕭
	 * 
	 * @param id
	 *            project id
	 * @return project member list
	 */
	public ArrayList<Account> getProjectMembers(long id) {
		MySQLQuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ProjectRoleEnum.TABLE_NAME);
		valueSet.addEqualCondition(ProjectRoleEnum.PROJECT_ID, id);
		valueSet.addCrossJoinMultiCondition(AccountEnum.TABLE_NAME,
				ProjectRoleEnum.ACCOUNT_ID, AccountEnum.TABLE_NAME + '.'
						+ AccountEnum.ID, AccountEnum.ENABLE, "1");
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		ArrayList<Account> members = new ArrayList<Account>();
		try {
			while (result.next()) {
				members.add(convertAccountUseAccountId(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}

		return members;
	}

	/**
	 * 嚙踝蕭嚙緻嚙瞎嚙論下嚙踝蕭嚙課佗蕭嚙踝蕭u嚙瑾嚙踝蕭嚙踝蕭嚙踝蕭
	 * 
	 * @param id
	 *            project id
	 * @return return worker list
	 */
	public ArrayList<Account> getProjectWorkers(long id) {
		StringBuilder query = new StringBuilder();
		query.append("select ").append(AccountEnum.TABLE_NAME).append(".* from ").append(ProjectRoleEnum.TABLE_NAME).append(", ").append(AccountEnum.TABLE_NAME)
			.append(" where ")
			.append(ProjectRoleEnum.TABLE_NAME).append(".").append(ProjectRoleEnum.PROJECT_ID)
			.append(" = ").append(id)
			.append(" AND ")
			.append(ProjectRoleEnum.TABLE_NAME).append(".").append(ProjectRoleEnum.ACCOUNT_ID)
			.append(" = ").append(AccountEnum.TABLE_NAME).append(".").append(AccountEnum.ID)
			.append(" AND (")
			.append(ProjectRoleEnum.TABLE_NAME).append(".").append(ProjectRoleEnum.ROLE).append(" = ").append(RoleEnum.ScrumMaster.ordinal())
			.append(" OR ")
			.append(ProjectRoleEnum.TABLE_NAME).append(".").append(ProjectRoleEnum.ROLE).append(" = ").append(RoleEnum.ScrumTeam.ordinal())
			.append(")");
		ResultSet result = mControl.executeQuery(query.toString());
		ArrayList<Account> workers = new ArrayList<Account>();
		try {
			while (result.next()) {
				workers.add(convertAccount(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return workers;
	}

	/**
	 * Get account by account id
	 * 
	 * @param id
	 *            account id
	 * @return AccountObject
	 */
	public Account getAccountById(long id) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		valueSet.addEqualCondition(AccountEnum.ID, id);
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		Account account = null;
		try {
			if (result.next()) {
				account = convertAccount(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return account;
	}

	/**
	 * Get account object by account's username
	 * 
	 * @param username
	 *            Account's username
	 * @return AccountObject
	 */
	public Account getAccountByUsername(String username) {
		Account accountObject = null;

		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		valueSet.addTextFieldEqualCondition(AccountEnum.USERNAME, username);
		String query = valueSet.getSelectQuery();

		ResultSet result = mControl.executeQuery(query);
		try {
			if (result.next()) {
				accountObject = convertAccount(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return accountObject;
	}

	/**
	 * Get all accounts in ezScrum
	 * 
	 * @return account list
	 */
	public ArrayList<Account> getAllAccounts() {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		ArrayList<Account> accounts = new ArrayList<Account>();

		try {
			while (result.next()) {
				accounts.add(convertAccount(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return accounts;
	}

	/**
	 * Use username and password to get the account.
	 * 
	 * NOTICE: password already be md5 hashed
	 * 
	 * @param username
	 * @param passwordMd5
	 * @return AccountObject
	 */
	public Account confirmAccount(String username, String passwordMd5) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(AccountEnum.TABLE_NAME);
		valueSet.addTextFieldEqualCondition(AccountEnum.USERNAME, username);
		valueSet.addTextFieldEqualCondition(AccountEnum.PASSWORD, passwordMd5);
		valueSet.addEqualCondition(AccountEnum.ENABLE, 1);
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		Account account = null;

		try {
			while (result.next()) {
				account = convertAccount(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return account;
	}

	public Account convertAccount(ResultSet result) throws SQLException {
		long id = result.getLong(AccountEnum.ID);
		String username = result.getString(AccountEnum.USERNAME);
		String nickName = result.getString(AccountEnum.NICK_NAME);
		String password = result.getString(AccountEnum.PASSWORD);
		String email = result.getString(AccountEnum.EMAIL);
		boolean enable = result.getBoolean(AccountEnum.ENABLE);

		Account account = new Account(id, username);
		account.setPassword(password).setNickName(nickName).setEmail(email)
				.setEnable(enable);
		return account;
	}

	/**
	 * For getProjectMembers 嚙稽嚙踝蕭 join 嚙碼嚙諉迎蕭嚙踝蕭嚙瞌 ACCOUNT_ID
	 * 
	 * @param result
	 * @return AccountObject
	 * @throws SQLException
	 */
	public Account convertAccountUseAccountId(ResultSet result)
			throws SQLException {
		long id = result.getLong(ProjectRoleEnum.ACCOUNT_ID);
		String username = result.getString(AccountEnum.USERNAME);
		String nickName = result.getString(AccountEnum.NICK_NAME);
		String password = result.getString(AccountEnum.PASSWORD);
		String email = result.getString(AccountEnum.EMAIL);
		boolean enable = result.getBoolean(AccountEnum.ENABLE);
		long createTime = result.getLong(AccountEnum.CREATE_TIME);
		long updateTime = result.getLong(AccountEnum.UPDATE_TIME);
		Account account = new Account(id, username);
		account.setPassword(password).setNickName(nickName).setEmail(email)
				.setEnable(enable).setCreateTime(createTime).setUpdateTime(updateTime);
		return account;
	}

	public static String getMd5(String str) {
		// 嚙緘嚙瘦嚙皺嚙碼嚙緩嚙篇嚙瞌md5嚙賣式嚙璀嚙篁嚙踝蕭嚙賞換
		if (isMD5(str)) {
			return str;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		md.update(str.getBytes());
		byte b[] = md.digest();
		str = byte2hex(b);
		return str;
	}

	private static String byte2hex(byte b[]) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 255);
			if (stmp.length() == 1) {
				hs = (new StringBuilder(String.valueOf(hs))).append("0")
						.append(stmp).toString();
			} else {
				hs = (new StringBuilder(String.valueOf(hs))).append(stmp)
						.toString();
			}
		}
		return hs;
	}
	
	private static boolean isMD5(String str) {
		int lengthOfMD5 = 32;
		boolean isMD5 = (str.length() == lengthOfMD5);
		isMD5 &= !isDigit(str);
		isMD5 &= !isAlpha(str);
		isMD5 &= isAlphaDigit(str);
		return isMD5;
	}

	private static boolean isDigit(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isDigit = pattern.matcher(str);
		return isDigit.matches();
	}

	private static boolean isAlpha(String str) {
		Pattern pattern = Pattern.compile("[a-z]*");
		Matcher isDigit = pattern.matcher(str);
		return isDigit.matches();
	}

	private static boolean isAlphaDigit(String str) {
		Pattern pattern = Pattern.compile("[0-9a-z]*");
		Matcher isDigit = pattern.matcher(str);
		return isDigit.matches();
	}
	
	/*public ScrumRole convertScrumRole(String projectName, String role,
	ResultSet result) throws SQLException {
ScrumRole scrumRole = new ScrumRole(projectName, role);
scrumRole.setisGuest(RoleEnum.Guest == RoleEnum.valueOf(role));
scrumRole.setAccessProductBacklog(result.getBoolean(ScrumRoleEnum.ACCESS_PRODUCT_BACKLOG));
scrumRole.setAccessReleasePlan(result.getBoolean(ScrumRoleEnum.ACCESS_RELEASE_PLAN));
scrumRole.setAccessReport(result.getBoolean(ScrumRoleEnum.ACCESS_REPORT));
scrumRole.setAccessRetrospective(result.getBoolean(ScrumRoleEnum.ACCESS_RETROSPECTIVE));
scrumRole.setAccessSprintBacklog(result.getBoolean(ScrumRoleEnum.ACCESS_SPRINT_BACKLOG));
scrumRole.setAccessSprintPlan(result.getBoolean(ScrumRoleEnum.ACCESS_SPRINT_PLAN));
scrumRole.setAccessUnplanItem(result.getBoolean(ScrumRoleEnum.ACCESS_UNPLAN));
scrumRole.setAccessTaskBoard(result.getBoolean(ScrumRoleEnum.ACCESS_TASKBOARD));
scrumRole.setAccessEditProject(result.getBoolean(ScrumRoleEnum.ACCESS_EDIT_PROJECT));
return scrumRole;
}

public ProjectRole getProjectWithScrumRole(ResultSet result) throws SQLException {
ProjectObject project = ProjectObject.get(result.getLong(ProjectRoleEnum.PROJECT_ID));
RoleEnum role = RoleEnum.values()[result.getInt(ProjectRoleEnum.ROLE)];
ScrumRole scrumRole = convertScrumRole(project.getName(), role.name(), result);

return new ProjectRole(project, scrumRole);
}
*/
	/**
	 * �� account id ������撠���恣���董���
	 * 
	 * @param accountId
	 * @return admin account's project role
	 */
	/*public ProjectRole getSystemRole(long accountId) {
		QuerySet valueSet = new MySQLQuerySet();
		valueSet.addTableName(SystemEnum.TABLE_NAME);
		valueSet.addEqualCondition(SystemEnum.ACCOUNT_ID, accountId);
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		ProjectRole projectRole = null;

		try {
			if (result.next()) {
				ProjectObject project = new ProjectObject(0, "system");
				project.setDisplayName("system")
				       .setComment("system")
					   .setManager("admin")
					   .setAttachFileSize(0)
					   .setCreateTime(0);
				ScrumRole scrumRole = new ScrumRole("system", "admin");
				scrumRole.setisAdmin(true);
				projectRole = new ProjectRole(project, scrumRole);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return projectRole;
	}*/
	/**
	 * Get account access mapping each attend project
	 * 
	 * @param accountId
	 * @return account access map
	 */
	/*public HashMap<String, ProjectRole> getProjectRoleMap(long accountId) {
		StringBuilder query = new StringBuilder();
		query.append("select * from ").append(ProjectRoleEnum.TABLE_NAME)
				.append(" as pr").append(" cross join ")
				.append(ProjectEnum.TABLE_NAME).append(" as p on ")
				.append(ProjectRoleEnum.PROJECT_ID).append(" = p.")
				.append(ProjectEnum.ID).append(" cross join ")
				.append(ScrumRoleEnum.TABLE_NAME).append(" as sr on")
				.append(" pr.").append(ProjectRoleEnum.PROJECT_ID)
				.append(" = sr.").append(ScrumRoleEnum.PROJECT_ID)
				.append(" and pr.").append(ProjectRoleEnum.ROLE)
				.append(" = sr.").append(ScrumRoleEnum.ROLE).append(" where ")
				.append(ProjectRoleEnum.ACCOUNT_ID).append(" = ")
				.append(accountId);

		String queryString = query.toString();
		HashMap<String, ProjectRole> map = new HashMap<String, ProjectRole>();
		ProjectRole systemRole = getSystemRole(accountId);
		ResultSet result = mControl.executeQuery(queryString);

		try {
			if (systemRole != null) {
				map.put("system", systemRole);
			}
			while (result.next()) {
				map.put(result.getString(ProjectEnum.NAME),
						getProjectWithScrumRole(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return map;
	}
*/
}
