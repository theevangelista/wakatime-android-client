package com.wakatime.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

/**
 * @author Joao Pedro Evangelista
 */

public class DurationWearActivity extends WearableActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, MessageApi.MessageListener {

    private Node mNode;

    private GoogleApiClient mGoogleApiClient;

    private TextView mLoggedTimeView;

    private boolean mResolvingError = false;

    private static final String DURATIONS_PATH = "/durations";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_activity_duration);

        //Connect the GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                mLoggedTimeView = (TextView) findViewById(R.id.text_view_logged_time_today);
                Wearable.MessageApi.addListener(mGoogleApiClient, DurationWearActivity.this);
                sendRequestTimeMessage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(DurationWearActivity.class.getSimpleName(), "Failed to connect \n"+
                connectionResult.isSuccess() + "\n" +
                connectionResult.getErrorCode() + "\n" +
                connectionResult.toString());
    }

    private void sendRequestTimeMessage() {
        Log.d(DurationWearActivity.class.getSimpleName(), "Start send");
        if (mNode != null && mGoogleApiClient.isConnected()) {
            Log.d(DurationWearActivity.class.getSimpleName(), "Sending message!!!");
            Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(), DURATIONS_PATH, null)
                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e("TAG", "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            } else {
                                //toast ?
                            }
                        }
                    });
        }
    }

    private void resolveNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                        for (Node node : nodes.getNodes()) {
                            if (node.isNearby()) {
                                mNode = node;
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        byte[] bytes = messageEvent.getData();
        mLoggedTimeView.setText(new String(bytes));
    }
}
