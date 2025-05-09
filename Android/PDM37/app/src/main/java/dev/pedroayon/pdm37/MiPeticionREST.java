package dev.pedroayon.pdm37;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MiPeticionREST extends AsyncTask<String,String,String> {
    private static final String API_KEY = "7728437463:AAEWzjmVh_8xHFh63Kl4OpJalbmFKAE5WDo";
    private TextView output;

    HttpURLConnection urlConnection;
    StringBuilder json;
    String datos = "";

    MiPeticionREST(TextView output){
        this.output = output;
    }

    @Override
    public void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... info) {
        String res = "";

        try
        {

            if( info[0].contains("POST")) {
                URL url = new URL("https://api.telegram.org/bot" + API_KEY + "/sendMessage?text=Hola");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                String input = "{\"id\": \"0\",\"nota\": \"" + info[0] + "\",\"fecha\": \"" + info[1] + "\",\"user\":" + info[2] + "}";
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {

                }

                conn.disconnect();
            }

            if( info[0].contains("GET-SEND")){
                HttpURLConnection conn = getHttpURLConnection("https://api.telegram.org/bot" + API_KEY + "/sendMessage?chat_id=7586582131&text=" + info[1]);

                int status = conn.getResponseCode();

                if ( status == 200 ) {
                    res = "Message Send as BOT";
                }

                conn.disconnect();
            }

            if ( info[0].contains("GET-UPDATES")) {
                HttpURLConnection conn = getHttpURLConnection("https://api.telegram.org/bot" + API_KEY + "/getUpdates");

                int status = conn.getResponseCode();

                if ( status == 200 ) {
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(reader);

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    String jsonResponse = sb.toString();

                    try {
                        // Parse the response using the org.json library
                        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResponse);
                        org.json.JSONArray results = jsonObject.getJSONArray("result");

                        StringBuilder resultBuilder = new StringBuilder();

                        // Iterate over each update and extract chat id, first_name, and text.
                        for (int i = 0; i < results.length(); i++) {
                            org.json.JSONObject update = results.getJSONObject(i);
                            org.json.JSONObject message = update.getJSONObject("message");
                            org.json.JSONObject chat = message.getJSONObject("chat");

                            // Extract values
                            long chatId = chat.getLong("id");
                            String firstName = chat.getString("first_name");
                            String text = message.getString("text");

                            // Append to a string builder in a formatted way
                            resultBuilder.append("Chat ID: ").append(chatId).append("\n");
                            resultBuilder.append("First Name: ").append(firstName).append("\n");
                            resultBuilder.append("Text: ").append(text).append("\n\n");
                        }

                        res = resultBuilder.toString();
                    } catch (org.json.JSONException e) {
                        Log.e("JSON_PARSING", "Error parsing JSON: " + e.getMessage());
                        res = "Error parsing JSON response.";
                    }
                }

                conn.disconnect();
            }

        }
        catch (MalformedURLException e) {
            Log.e("ENVIOREST", "[MalformedURLException]=>" + e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            Log.e("ENVIOREST", "[IOException]=>" + e.getMessage());
            e.printStackTrace();
        }

        return res;
    }

    @NonNull
    private static HttpURLConnection getHttpURLConnection(String API_KEY) throws IOException {
        URL url = new URL(API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-length", "0");
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000);
        conn.connect();
        return conn;
    }

    @Override
    protected void onProgressUpdate(String... progress){

    }

    @Override
    protected void onPostExecute(String result) {
        this.output.setText("["+result+"]");
    }
}

