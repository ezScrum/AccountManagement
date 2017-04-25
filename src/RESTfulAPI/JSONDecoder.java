package RESTfulAPI;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import DataObject.Account;
import Enums.AccountEnum;


public class JSONDecoder {
	// Translate JSON String to Account
	public static Account toAccount(String accountJSONString) {
		Account account = null;
		try {
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
		} catch (JSONException e) {
			account = null;
		}
		return account;
	}

	// Translate AttachFile JSON String to base64 binary String
	/*public static String toBase64BinaryString(String attachFileJSONString) {
		String base64BinaryString = "";
		try {
			JSONObject attachFileJSON = new JSONObject(attachFileJSONString);
			base64BinaryString = attachFileJSON.getString(AttachFileJSONEnum.BINARY);
		} catch (JSONException e) {
			base64BinaryString = "";
		}
		return base64BinaryString;
	}*/

	// Translate JSON String to Scrum Roles
	/*public static ArrayList<ScrumRole> toScrumRoles(String projectName, String scrumRolesJSONString) {
		ArrayList<ScrumRole> scrumRoles = new ArrayList<>();
		JSONObject scrumRolesJSON;
		try {
			scrumRolesJSON = new JSONObject(scrumRolesJSONString);
			Iterator<?> iterator = scrumRolesJSON.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				JSONObject roleJSON;
				roleJSON = scrumRolesJSON.getJSONObject(key);
				ScrumRole scrumRole = new ScrumRole(projectName, key);
				scrumRole.setAccessProductBacklog(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_PRODUCT_BACKLOG));
				scrumRole.setAccessReleasePlan(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_RELEASE_PLAN));
				scrumRole.setAccessRetrospective(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_RETROSPECTIVE));
				scrumRole.setAccessSprintBacklog(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_SPRINT_BACKLOG));
				scrumRole.setAccessSprintPlan(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_SPRINT_PLAN));
				scrumRole.setAccessTaskBoard(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_TASKBOARD));
				scrumRole.setAccessUnplanItem(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_UNPLAN));
				scrumRole.setAccessEditProject(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_EDIT_PROJECT));
				scrumRole.setAccessReport(roleJSON.getBoolean(ScrumRoleJSONEnum.ACCESS_REPORT));
				scrumRoles.add(scrumRole);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return scrumRoles;
	}*/

	// Translate JSON String to HistoryObject
	/*public static HistoryObject toHistory(long issueId, int issueType, String historyJSONString) {
		HistoryObject history = null;
		try {
			JSONObject historyJSON = new JSONObject(historyJSONString);

			// Get Story Information
			String type = historyJSON.getString(HistoryJSONEnum.HISTORY_TYPE);
			String oldValue = historyJSON.getString(HistoryJSONEnum.OLD_VALUE);
			String newValue = historyJSON.getString(HistoryJSONEnum.NEW_VALUE);
			long createTime = historyJSON.getLong(HistoryJSONEnum.CREATE_TIME);

			// Create HistoryObject
			history = new HistoryObject();
			history.setIssueId(issueId)
			       .setIssueType(issueType)
			       .setHistoryType(HistoryTypeTranslator.getHistoryType(type))
			       .setCreateTime(createTime);
			if (HistoryTypeTranslator.getHistoryType(type) == HistoryObject.TYPE_STATUS) {
				history.setOldValue(String.valueOf(StatusTranslator.getStatusByStatusString(oldValue)))
				       .setNewValue(String.valueOf(StatusTranslator.getStatusByStatusString(newValue)));
			} else {
				history.setOldValue(oldValue)
			           .setNewValue(newValue);
			}
		} catch (JSONException e) {
			history = null;
		}
		return history;
	}
	*/


	// Translate JSON String to AttachFileInfo
/*	public static AttachFileInfo toAttachFileInfo(String projectName, long issueId, int issueType, String attachFileJSONString) {
		AttachFileInfo attachFileInfo = new AttachFileInfo();
		try {
			JSONObject attachFileJSON = new JSONObject(attachFileJSONString);
			attachFileInfo.issueId = issueId;
	        attachFileInfo.issueType = issueType;
	        attachFileInfo.name = attachFileJSON.getString(AttachFileJSONEnum.NAME);
	        attachFileInfo.contentType = attachFileJSON.getString(AttachFileJSONEnum.CONTENT_TYPE);
	        attachFileInfo.projectName = projectName;
		} catch (JSONException e) {
			attachFileInfo = null;
		}
		return attachFileInfo;
	}*/


}
