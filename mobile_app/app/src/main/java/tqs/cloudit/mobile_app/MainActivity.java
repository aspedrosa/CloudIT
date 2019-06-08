package tqs.cloudit.mobile_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public MainActivity() {
        CookieHandler.setDefault(new CookieManager());
    }

    /**
     * When the users tries to login
     */
    public void onSubmit(View v) {
        final String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (username.length() == 0 && password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insert an username and password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (username.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insert an username", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insert a password", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://192.168.160.63:8082/login", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(), JobsActivity.class);
                        startActivity(intent);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401)
                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Error trying to authenticate. Please try again later.", Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            public Map<String, String> getHeaders() {
                ((CookieManager) CookieHandler.getDefault()).getCookieStore().removeAll();

                Map<String, String> headers = new HashMap<>();

                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", auth);

                return headers;
            }
        };

    Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}
