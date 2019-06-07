package tqs.cloudit.mobile_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JobInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        JSONObject job = null;
        try {
            job = new JSONObject(getIntent().getStringExtra("job"));

            setTitle(job.getString("title"));

            ((TextView) findViewById(R.id.descriptionValue)).setText(job.getString("description"));
            ((TextView) findViewById(R.id.areaValue)).setText(job.getString("area"));
            ((TextView) findViewById(R.id.dateValue)).setText(job.getString("date"));
            ((TextView) findViewById(R.id.amountValue)).setText(job.getString("amount"));

            JSONObject creatorInfo = job.getJSONObject("creator");
            ((TextView) findViewById(R.id.creatorNameValue)).setText(creatorInfo.getString("name"));
            ((TextView) findViewById(R.id.usernameValue)).setText(creatorInfo.getString("username"));
            ((TextView) findViewById(R.id.emailValue)).setText(creatorInfo.getString("email"));

        } catch (JSONException e) {
            Toast.makeText(this, "Some error occurred when displaying job's information. Try again later.", Toast.LENGTH_SHORT);
            this.finish();
        }
    }
}
