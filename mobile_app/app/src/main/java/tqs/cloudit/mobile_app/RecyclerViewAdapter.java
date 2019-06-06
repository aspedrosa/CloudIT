package tqs.cloudit.mobile_app;

import android.app.job.JobInfo;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private JSONArray jobs;
    private Context context;

    public RecyclerViewAdapter(Context context, JSONArray jobs) {
        this.jobs = jobs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_offer_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        try {
            final JSONObject job = jobs.getJSONObject(i);

            viewHolder.name.setText(job.getString("title"));
            viewHolder.creator.setText(job.getJSONObject("creator").getString("name"));
            if (false) {
                viewHolder.star.setImageResource(R.drawable.selectedstar);
                viewHolder.star.setTag(R.drawable.selectedstar);
            }
            else {
                viewHolder.star.setImageResource(R.drawable.emptystar);
                viewHolder.star.setTag(R.drawable.emptystar);
            }

            viewHolder.star.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    final int method;
                    if ((Integer) viewHolder.star.getTag() == R.drawable.emptystar) {//if (false) {
                        //method = Request.Method.DELETE;
                        viewHolder.star.setImageResource(R.drawable.selectedstar);
                        viewHolder.star.setTag(R.drawable.selectedstar);
                    }
                    else {
                        //method = Request.Method.POST;
                        viewHolder.star.setImageResource(R.drawable.emptystar);
                        viewHolder.star.setTag(R.drawable.emptystar);
                    }

                    return;


                    /*
                    JsonObjectRequest request = null;
                    try {
                        request = new JsonObjectRequest(method, "http://192.168.160.63:8080/favourite/" + job.getInt("id"), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (method == Request.Method.POST) {
                                            ((ImageView) v).setImageResource(R.drawable.selectedstar);
                                            Toast.makeText(context, "Job offer added to the favourites", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ((ImageView) v).setImageResource(R.drawable.emptystar);
                                            Toast.makeText(context, "Job offer removed from the favourites", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                                    }


                                });
                    }
                    catch (JSONException e) {
                        Toast.makeText(context, "Some error occurred while adding a job to your favourites. Please try again later.", Toast.LENGTH_SHORT).show();
                    }

                    Volley.newRequestQueue(context).add(request);
                    */
                }
            });


            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobInfoActivity.class);
                    intent.putExtra("job", job.toString());
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {

        }


    }

    @Override
    public int getItemCount() {
        return jobs.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView creator;
        ImageView star;
        ImageView info;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.offerName);
            creator = itemView.findViewById(R.id.offerCreator);
            star = itemView.findViewById(R.id.favourite);
            info = itemView.findViewById(R.id.infoButton);
        }
    }
}
