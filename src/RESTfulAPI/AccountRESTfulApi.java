package RESTfulAPI;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import DAO.AccountDAO;
import DataObject.Account;
import Enums.AccountEnum;
import Enums.ResponseJSONEnum;


@Path("AccountManagement")
public class AccountRESTfulApi {
	private String mBaseUrl = BaseUrlDistributor.getBaseUrl();
	@GET
	@Path("/getAccountById/{accountId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountById(@PathParam("accountId") int accountId){
		Account account = AccountDAO.getInstance().getAccountById(accountId);
		String entity = TransformData.accountToJSONString(account);

		if (entity.isEmpty())
			return Response.status(Response.Status.NOT_FOUND).build();

		return Response.status(Response.Status.OK).entity(entity).build();
	}
	
	@POST
	@Path("/createAccount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAccount( String entity ){
		//Error Checking
		String message = JSONChecker.jsonAccountCheck(entity);
		if (!message.isEmpty()) {
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, message, "");
		}

		// Create Account
		Account account = TransformData.JSONStringtoAccount(entity);
		account.save();

		return ResponseFactory.getResponse(Response.Status.OK, ResponseJSONEnum.SUCCESS_MESSAGE, account.toString());
	}

	@PUT
	@Path("/editPassword}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editPassword (@HeaderParam(AccountEnum.USERNAME) String username,
					              String originPassword, String newPassword, String confirmNewPassword) {
		Account account = AccountDAO.getInstance().getAccountByUsername(username);

		//Origin password check
		if ( !account.getPassword().equals(originPassword) )
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, "Origin password is not matched.", "");

		//New password check
		if ( !newPassword.equals(confirmNewPassword) )
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, "New password is not matched.", "");

		account.setPassword(newPassword);
		AccountDAO.getInstance().update(account);
		return ResponseFactory.getResponse(Response.Status.OK, ResponseJSONEnum.SUCCESS_MESSAGE, account.toString());
	}

	@PUT
	@Path("/editEmail}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editEmail (@HeaderParam(AccountEnum.USERNAME) String username,
								String newEmail){
		Account account = AccountDAO.getInstance().getAccountByUsername(username);

		//Check email error
		if(!AccountChecker.isEmailValid(newEmail))
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, "Email is invalid.", "");
		if(!AccountChecker.isEmailUsed(newEmail))
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, "Email has been used.", "");

		account.setEmail(newEmail);
		AccountDAO.getInstance().update(account);
		return ResponseFactory.getResponse(Response.Status.OK, ResponseJSONEnum.SUCCESS_MESSAGE, account.toString());

	}

	@PUT
	@Path("/editNick_name")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editNickname (@HeaderParam(AccountEnum.USERNAME) String username,
								String newNickname) {
		Account account = AccountDAO.getInstance().getAccountByUsername(username);

		if(newNickname.isEmpty()) {
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, "Nickname can't be empty.", "");
		}

		account.setNickName(newNickname);
		AccountDAO.getInstance().update(account);
		return ResponseFactory.getResponse(Response.Status.OK, ResponseJSONEnum.SUCCESS_MESSAGE, account.toString());

	}

	/*@GET
	@Path("/getAccountsByProjectId/{projectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getAccountsByProjectId (@PathParam("projectId") int projectId) {
		ArrayList<String> accountsUsername = new ArrayList<>();
		ArrayList<Account> allAccounts = AccountDAO.getInstance().getAllAccounts();
		for (Account account : allAccounts)
			if (account.getProjectId() == projectId)
				accountsUsername.add(account.getUsername());
		return accountsUsername;
	}*/
	
	/*@DELETE
	@Path("/deleteAccount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAccount (@HeaderParam(AccountEnum.USERNAME) String username,
					              @HeaderParam(AccountEnum.PASSWORD) String password,
					              String targetUsername) {
		//Check if user is admin
		if (!AccountChecker.isAccountAdmin(username, password)) {
			return ResponseFactory.getResponse(Response.Status.FORBIDDEN, "", "");
		}

		//Check if account is exist.
		if (AccountDAO.getInstance().getAccountByUsername(targetUsername) == null) {
			return ResponseFactory.getResponse(Response.Status.BAD_REQUEST, "Target account is not exist.", "");
		}

		AccountDAO.getInstance().deleteAccount(targetUsername);
		return ResponseFactory.getResponse(Response.Status.OK, ResponseJSONEnum.SUCCESS_MESSAGE, account.toString());
	}*/
}
