package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText serverPortEditText = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;

    private EditText keyEditText = null;
    private EditText valueEditText = null;
    private EditText putEditText = null;
    private EditText getEditText = null;
    private TextView dictionaryTextView = null;
    private Button connectButton = null;
    private Button sendRequestButton = null;

    private ServerThread serverThread = null;

    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private final SendRequestButtonClickListener sendRequestButtonClickListener = new SendRequestButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // Retrieves the server port. Checks if it is empty or not
            // Creates a new server thread with the port and starts it
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private class SendRequestButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            // Retrieves the client address and port. Checks if they are empty or not
            //  Checks if the server thread is alive. Then creates a new client thread with the address, port, city and information type
            //  and starts it
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
//            String get = getEditText.getText().toString();
            String put = putEditText.getText().toString();
            String key = keyEditText.getText().toString();
            String value = valueEditText.getText().toString();
            if (put.isEmpty() || key.isEmpty() || value.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            dictionaryTextView.setText(Constants.EMPTY_STRING);

            ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), put,key, value, dictionaryTextView);
            clientThread.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = findViewById(R.id.server_port_edit_text);
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = findViewById(R.id.client_address_edit_text);
        clientPortEditText = findViewById(R.id.client_port_edit_text);

        putEditText = findViewById(R.id.put_edit_text);
//        getEditText = findViewById(R.id.get_edit_text);
        valueEditText = findViewById(R.id.value_1_edit_text);
        keyEditText = findViewById(R.id.key_1_edit_text);

        Button getWeatherForecastButton = findViewById(R.id.get_weather_forecast_button);
        getWeatherForecastButton.setOnClickListener(sendRequestButtonClickListener);
        dictionaryTextView = findViewById(R.id.dictionary_text_view);

    }
}