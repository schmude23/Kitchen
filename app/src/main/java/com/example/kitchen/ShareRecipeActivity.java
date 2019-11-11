package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShareRecipeActivity extends AppCompatActivity implements DeviceAdapter.OnClickListener, WifiP2pManager.ConnectionInfoListener {

    //Instance variables
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    RecyclerView recyclerView;
    List<WifiP2pDevice> devices;
    DeviceAdapter adapter;

    //Used for sharing
    WifiP2pInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_recipe);

        //Set up recyclerView
        recyclerView = findViewById(R.id.share_recipe_recycler_view);
        adapter = new DeviceAdapter(devices, this);
        recyclerView.setAdapter(adapter);

        //Set up broadcast reciever
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);

        //Setup intent filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //Check for broadcasts
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //If there is anything that needs to be done if it is successful
                //NOTE: to see actual peers go to the reciever. This will send a
                //    thing to the receiver.
            }

            @Override
            public void onFailure(int reasonCode) {
                //If something needs to be done on failure do it here.
            }
        });


    }

    /**
     * register the broadcast receiver with the intent values to be matched
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    /**
     * unregister the broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    /**
     * Use this method as the onclick for when the share button is pressed.
     *
     * @param view
     */
    public void onShareButtonPressed(View view) {

        //This recipe is just default. Use an actual recipe
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Recipe recipe = db.getAllRecipes().get(0);

        //TODO: Do the stuff
        Context context = this.getApplicationContext();
        String host = info.groupOwnerAddress.getHostAddress();
        int port = 8888;
        Socket socket = new Socket();

        try {
            /**
             * Create a client socket with the host,
             * port, and timeout information.
             */
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 500);

            /**
             * Send recipe as a string
             */
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(recipe.toString().getBytes(Charset.forName("UTF-8")));

            outputStream.close();
        } catch( FileNotFoundException e) {
            //catch logic
        } catch(IOException e) {
            //catch logic
        } finally {
            /*
             * Clean up any open sockets when done
             * transferring or if an exception occurred.
             */
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }
    }

    /**
     * This method is called when the refresh button is pressed and will call for the p2pManager to
     * check for updates in the list of people broadcasting.
     *
     * @param view the view calling this method.
     */
    public void onShareActivityRefreshButtonPressed(View view) {
        //Check for broadcasts
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //If there is anything that needs to be done if it is successful
                //NOTE: to see actual peers go to the reciever. This will send a
                //    thing to the receiver.
            }

            @Override
            public void onFailure(int reasonCode) {
                //If something needs to be done on failure do it here.
            }
        });
    }

    /**
     * This method is used when connection info is available. Part of the sending data stuff.
     *
     * @param wifiP2pInfo
     */
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        this.info = wifiP2pInfo;
    }

    /**
     * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
     */
    public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

        private WifiP2pManager manager;
        private WifiP2pManager.Channel channel;
        private ShareRecipeActivity activity;
        private WifiP2pManager.PeerListListener peerListListener;

        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                           final ShareRecipeActivity activity) {
            super();
            this.manager = manager;
            this.channel = channel;
            this.activity = activity;

            //Setup peerListListener which is used to recieve the list of peers if they are found
            peerListListener = new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                    activity.devices = new ArrayList<WifiP2pDevice>(wifiP2pDeviceList.getDeviceList());
                    activity.recyclerView.getAdapter().notifyDataSetChanged();
                }
            };
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //TODO: Check if wifip2p is enabled.

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

                // Call WifiP2pManager.requestPeers() to get a list of current peers
                if (manager != null) {
                    manager.requestPeers(channel, peerListListener);
                    //The peerListListener is what will deal with any peers that were found.
                }

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections

                if (manager == null) {
                    return;
                }
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    // we are connected with the other device, request connection
                    // info to find group owner IP
                    manager.requestConnectionInfo(channel, activity);
                } else {
                    // It's a disconnect
                }

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }
    }

    /**
     * This method implements the Device Adapter OnClickListener and is run when a row in the
     * view is clicked.
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        //Use position in list to select broadcasting device and establish connection.
        WifiP2pDevice device = devices.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //It has succeeded in connecting.
            }

            @Override
            public void onFailure(int reason) {
                //It failed connecting. Show user a message saying something.
                Toast.makeText(recyclerView.getContext(), "Could not connect to device!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This class is used for recieving a recipe a recipe
     */
    public static class FileServerAsyncTask extends AsyncTask {

        private Context context;

        public FileServerAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Object[] objects) {
            try {

                /*
                 * Create a server socket and wait for client connections. This
                 * call blocks until a connection is accepted from a client
                 */
                ServerSocket serverSocket = new ServerSocket(8888);
                Socket client = serverSocket.accept();

                /*
                 * If this code is reached, a client has connected and transferred data
                 */

                InputStream inputStream = client.getInputStream();

                //Read in the input as a String.
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }

                String recipeString = total.toString();

                //Create recipe using that string and save it to the database
                Recipe recipe = new Recipe(recipeString);
                DatabaseHelper db = new DatabaseHelper(context);
                db.addRecipe(recipe);

                //Close socket
                serverSocket.close();
            } catch (IOException e) {
                //TODO: Handle IOException appropriately
                return null;
            }

            return "";
        }

    }
}
