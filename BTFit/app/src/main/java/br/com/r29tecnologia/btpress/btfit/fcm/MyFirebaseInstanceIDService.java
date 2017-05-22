package br.com.r29tecnologia.btpress.btfit.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by victor on 21/05/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    
    public static final String TAG = "FirebaseInstanceIDSvc";
    
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        
    }
}
