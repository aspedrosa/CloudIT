package tqs.cloudit.utils;

import org.json.simple.JSONObject;

/**
 * Used to prevent code duplication.
 * With this class we can easily create the response content
 */
public class ResponseBuilder {

    /**
     * Generates a response to be sent by a controller with only a message
     *
     * @param message to be sent
     * @return json object with key "message"
     */
    public static JSONObject buildWithMessage(String message) {
        JSONObject response = new JSONObject();

        response.put("message", message);

        return response;
    }

    /**
     * Generates a response to be sent by a controller with both message and data
     *
     * @param message to be sent
     * @param data to be sent
     * @return json object with keys "message" and "data"
     */
    public static JSONObject buildWithMessageAndData(String message, Object data) {
        JSONObject response = buildWithMessage(message);

        response.put("data", data);

        return response;
    }
}
