package th.ac.kku.asayaporn.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTask extends AsyncTask<String, String, String> {
    ProgressDialog pd;
    InsideMainActivity insideMainActivity;



    public JsonTask(InsideMainActivity insideMainActivity) {
        this.insideMainActivity = insideMainActivity;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(insideMainActivity);
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

                buffer.append(line + "\n");


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
        if (pd.isShowing()) {
            pd.dismiss();
        }
        JSONObject obj = null;
        try {

            obj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sp ;
        SharedPreferences.Editor editor ;
        sp = insideMainActivity.getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("json", new String(String.valueOf(obj)));
        editor.commit();

    }
}