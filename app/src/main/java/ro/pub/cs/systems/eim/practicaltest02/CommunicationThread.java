package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread{
    private final ServerThread serverThread;
    private final Socket socket;

    // Constructor of the thread, which takes a ServerThread and a Socket as parameters
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    // run() method: The run method is the entry point for the thread when it starts executing.
    // It's responsible for reading data from the client, interacting with the server,
    // and sending a response back to the client.
    @Override
    public void run() {
        // It first checks whether the socket is null, and if so, it logs an error and returns.
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            // Create BufferedReader and PrintWriter instances for reading from and writing to the socket
            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (word!");

            // Read the city and informationType values sent by the client
            String request = bufferedReader.readLine();
            String key = bufferedReader.readLine();
            String value = bufferedReader.readLine();
//            String get = bufferedReader.readLine();
            if (key == null || key.isEmpty() || value == null || value.isEmpty()  ){
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (word)");
                return;
            }
            DataObject dataResponse = null;
            if(Objects.equals(request, "put")){
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";

                // make the HTTP request to the web service
                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                }
                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else Log.i(Constants.TAG, pageSourceCode);

                // Parse the page source code into a JSONObject and extract the needed information
                JSONObject content = new JSONObject(pageSourceCode);
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] : " + content);

                String unixtime = content.getString("unixtime");


//                JSONObject current = content.getJSONObject("unixtime");
//                String unixtime = current.getString("unixtime");
//                Integer unixtimeInt = Integer.parseInt(unixtime);
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] unixtime: " + unixtime);
//                dataResponse = new DataObject(value,unixtimeInt);
//                dictionaryResponse = new DictionaryResponse(definition);
//                String result = "put, " +key+"," + value +"\n" ;
//                serverThread.setData(key, dataResponse);
            } else if (Objects.equals(request, "get")) {

            }

//
            if (dataResponse == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Dictionary response is null!");
                return;
            }

            // Send the information back to the client
//            String result = dictionaryResponse.getDefinitionStr();
            String result = "put, " +key+"," + value +"\n" ;


            // Send the result back to the client
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
