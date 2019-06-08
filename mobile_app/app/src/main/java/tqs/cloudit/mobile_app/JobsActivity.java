package tqs.cloudit.mobile_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.HashSet;
import java.util.Set;

public class JobsActivity extends AppCompatActivity {

    // to store the jobs the ids of the jobs
    //  that the user has on the favourites
    private Set<Long> favourites = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        //Get the favourite jobs of the user
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://192.168.160.63:8082/favourite", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jobs = response.getJSONArray("data");

                            for (int i = 0; i < jobs.length(); i++) {
                                JSONObject job = jobs.getJSONObject(i);
                                favourites.add(job.getLong("id"));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Some error occurred while retrieving personal data. Favourite jobs may not be shown as favourite.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Some error occurred while retrieving personal data. Favourite jobs may not be shown as favourite.", Toast.LENGTH_SHORT).show();
                    }


                });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    /**
     * Method to avoid some code duplication.
     * Returns the json object needed to send to the endpoint /joboffer/advancedSearch
     *  with the a given searchWord
     */
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

    /**
     * When the users wants to receive a notification (Toast) when
     *  a new job offer is created that matches the search word that he
     *  inserted
     */
    public void createAlarm(View v) {
        final String searchText = ((EditText) findViewById(R.id.searchText)).getText().toString().trim();

        if (searchText.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insert a search word.", Toast.LENGTH_SHORT).show();
            return;
        }

        final JSONObject advancedSearch = generateAdvancedSearch(searchText);

        final Context context = this;

        //Launch a new thread that will be doing pulling on the background
        new Thread(new Runnable() {

            //stop condition for the this thread
            private boolean wantedJobCreated = false;

            /**
             * Used to know how much offer existed on the moment the users
             *  created the alarm
             * Starts at -2
             * If an error occurs at getting the number of offers at the beginning
             *  is set to -1 and the thread fails
             */
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

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.160.63:8082/joboffer/advancedSearch", advancedSearch,
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
                                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 404) {
                                    countOfJobOffersOnStart = 0;
                                }
                                else {
                                    countOfJobOffersOnStart = -1;
                                }
                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(request);

                //If the variable `countOfJobOffersOnStart` is at -2
                // it means that a result from the API call was not
                // received yet
                while (countOfJobOffersOnStart == -2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (countOfJobOffersOnStart == -1) {
                    displayToast("Some error occurred while creating an alarm. Try again later.", Toast.LENGTH_SHORT);
                    return;
                }
                else {
                    displayToast("Alarm created. You will be notified if a job offer is created with the search word \"" + searchText + "\"", Toast.LENGTH_SHORT);
                }

                while (!wantedJobCreated) {
                    request = new JsonObjectRequest(Request.Method.POST, "http://192.168.160.63:8082/joboffer/advancedSearch", advancedSearch,
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

                    //Check with an interval of 60 seconds
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                displayToast("A job offer was created with the search word \"" + searchText + "\"", Toast.LENGTH_LONG);
            }
        }).start();
    }

    /**
     * Updates the recycler view with a new list of jobs
     *
     * Method used to reduce code duplication.
     * @param jobs new list of jobs to display
     */
    private void updateJobs(JSONArray jobs) {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), jobs, favourites);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    /**
     * OnClick of the search button.
     * Searches for search offers where the title or area match
     *  the received word
     */
    public void search(View v) {
        String searchWord = ((EditText) findViewById(R.id.searchText)).getText().toString().trim();

        final JSONObject advancedSearch = generateAdvancedSearch(searchWord);

        if (searchWord.length() == 0) {
            try {
                advancedSearch.put("area", false);
                advancedSearch.put("title", false);
            } catch (JSONException e) {
                /**
                 * Never happens
                 */
            }
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.160.63:8082/joboffer/advancedSearch", advancedSearch,
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
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "No job offers found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Same error occurred while retrieving jobs. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}
