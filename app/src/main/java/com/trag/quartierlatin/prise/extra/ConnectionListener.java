package com.trag.quartierlatin.prise.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by QuartierLatin on 1/8/2559.
 */
public class ConnectionListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)) ;
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if(activeInfo != null){
            Toast.makeText(context, "CONNECTED", Toast.LENGTH_LONG);
            Log.v("isConnected:","YES");

        } else {
            Log.v("isConnected:","NO");
            Toast.makeText(context, "NO CONNECTED", Toast.LENGTH_LONG);
        }
    }
}
