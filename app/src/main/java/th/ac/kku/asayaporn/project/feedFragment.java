package th.ac.kku.asayaporn.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class feedFragment extends Fragment  {
    protected ListView mListView;
    protected CustomAdapter mAdapter;
    double[] numunit;

    public static feedFragment newInstance() {
        
        Bundle args = new Bundle();
        
        feedFragment fragment = new feedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed,
                container, false);
        setHasOptionsMenu(true);
        mListView = (ListView) view.findViewById(R.id.feedlistview);

        new JsonTask().execute("https://www.kku.ac.th/ikku/api/activities/services/topActivity.php");


        return view;
    }

    private void showData(String json) {
        Gson gson = new Gson();
        Blog blog = gson.fromJson(json, Blog.class);
        List<ActivityKKU> activity = blog.getActivities();


        mAdapter = new CustomAdapter(this.getActivity(), activity);

        mListView.setAdapter(mAdapter);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu,inflater);
    }
    ProgressDialog pd;
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getContext());
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            showData(new String(String.valueOf(obj)));
        }
    }
}


