package RESTfulAPI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import Enums.ResponseJSONEnum;


public class ResponseFactory {
	public static Response getResponse(Status status, String message, String content) {
		JSONObject responseJSON = new JSONObject();
		try {
			if (message == null) {
				responseJSON.put(ResponseJSONEnum.JSON_KEY_MESSAGE, "");
			} else {
				responseJSON.put(ResponseJSONEnum.JSON_KEY_MESSAGE, message);
			}
			JSONObject contentJSON;
			if (content == null || content.isEmpty()) {
				contentJSON = new JSONObject();
			} else {
				contentJSON = new JSONObject(content);
			}
			responseJSON.put(ResponseJSONEnum.JSON_KEY_CONTENT, contentJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String entity = responseJSON.toString();
		return Response.status(status).entity(entity).build();
	}
}
