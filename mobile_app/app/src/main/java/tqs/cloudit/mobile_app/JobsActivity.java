package tqs.cloudit.mobile_app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JobsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
    }

    private JSONObject generateAdvancedSearch(String searchText) {
        JSONObject advancedSearch = new JSONObject();

        try {
            advancedSearch.put("area", true);
            advancedSearch.put("title", true);
            advancedSearch.put("query", searchText);
            advancedSearch.put("fromAmount", -1);
            advancedSearch.put("toAmount", -1);
            advancedSearch.put("fromDate", "");
            advancedSearch.put("toDate", "");
        } catch (JSONException e) {
            /*
             * Never happens
             */
        }

        return advancedSearch;
    }

    public void createAlarm(View v) {
        final String searchText = ((EditText) findViewById(R.id.searchText)).getText().toString().trim();

        if (searchText.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insert a search word.", Toast.LENGTH_SHORT).show();
            return;
        }

        final JSONObject advancedSearch = generateAdvancedSearch(searchText);


        final Context context = this;

        new Thread(new Runnable() {

            private boolean wantedJobCreated = false;
            private int countOfJobOffersOnStart = -2;

            private void displayToast(final String message, final int duration) {
                ((JobsActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                context,
                                message,
                                duration
                        ).show();
                    }
                });
            }

            @Override
            public void run() {

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.160.63:8080/joboffer/advancedSearch", advancedSearch,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jobs = response.getJSONArray("data");

                                    countOfJobOffersOnStart = jobs.length();
                                } catch (JSONException e) {
                                    countOfJobOffersOnStart = -1;
                                }
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                countOfJobOffersOnStart = -1;
                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(request);

                while (countOfJobOffersOnStart == -2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (countOfJobOffersOnStart == -1) {
                    displayToast("Some error occurred while creating an alarm. Try again later.", Toast.LENGTH_SHORT);
                }
                else {
                    displayToast("Alarm created. You will be notified if a job offer is created with the search word \"" + searchText + "\"", Toast.LENGTH_SHORT);
                }

                while (!wantedJobCreated) {
                    request = new JsonObjectRequest(Request.Method.POST, "http://192.168.160.63:8080/joboffer/advancedSearch", advancedSearch,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jobs = response.getJSONArray("data");

                                        if (jobs.length() > countOfJobOffersOnStart) {
                                            wantedJobCreated = true;
                                        }
                                    } catch (JSONException e) {
                                        /*
                                         * Ignore if some error occurs. Just keep retrying
                                         */
                                    }
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    /*
                                     * Ignore if some error occurs. Just keep retrying
                                     */
                                }
                            });

                    Volley.newRequestQueue(getApplicationContext()).add(request);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                displayToast("A job offer was created with the search word \"" + searchText + "\"", Toast.LENGTH_LONG);
            }
        }).start();
    }

    private void updateJobs(JSONArray jobs) {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), jobs);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void search(View v) {
        final JSONObject advancedSearch = generateAdvancedSearch(((EditText) findViewById(R.id.searchText)).getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.160.63:8080/joboffer/advancedSearch", advancedSearch,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jobs = response.getJSONArray("data");

                            updateJobs(jobs);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Same error occurred while retrieving job offers. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        updateJobs(new JSONArray());
                        if (volleyError.networkResponse.statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "No job offers found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Same error occurred while retrieving job offers. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}
