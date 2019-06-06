package tqs.cloudit.utils;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Used to prevent code duplication.
 * With this class we can easily create the response content
 */
public class ResponseBuilder {

    private ResponseBuilder() {}

    private static JSONObject jsonWithMessage(String message) {
        JSONObject response = new JSONObject();

        response.put("message", message);

        return response;
    }

    /**
     * Generates a response to be sent by a controller with only a message
     *
     * @param message to be sent
     * @return json object with key "message"
     */
    public static ResponseEntity buildWithMessage(HttpStatus status, String message) {
        JSONObject response = jsonWithMessage(message);

        return new ResponseEntity<>(response, status);
    }

    /**
     * Generates a response to be sent by a controller with both message and data
     *
     * @param message to be sent
     * @param data to be sent
     * @return json object with keys "message" and "data"
     */
    public static ResponseEntity buildWithMessageAndData(HttpStatus status, String message, Object data) {
        JSONObject response = jsonWithMessage(message);

        response.put("data", data);

        return new ResponseEntity<>(response, status);
    }
}
