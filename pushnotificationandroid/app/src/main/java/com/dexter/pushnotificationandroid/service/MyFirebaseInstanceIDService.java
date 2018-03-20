package com.dexter.pushnotificationandroid.service;

import android.util.Log;

import com.dexter.pushnotificationandroid.Constants;
import com.dexter.pushnotificationandroid.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Firebase-quickstart-android
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private DatabaseReference mDatabase;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uuid = FirebaseAuth.getInstance().getUid();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        User user = new User(usernameFromEmail(userEmail), userEmail, token);
        mDatabase.child(Constants.USERS).child(uuid).setValue(user);
        Log.d("Registeration token ", "Sent to server");
    }

    public String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}