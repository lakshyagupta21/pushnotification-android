package com.dexter.pushnotificationandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dexter.pushnotificationandroid.Constants;
import com.dexter.pushnotificationandroid.Keys;
import com.dexter.pushnotificationandroid.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dexter.pushnotificationandroid.Constants.FCM_SERVER_CONNECTION;


public class ReplyActivity extends AppCompatActivity {

    EditText replyEditText;
    Button bReply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        replyEditText = (EditText) findViewById(R.id.etReply);
        bReply = (Button) findViewById(R.id.bReply);

        bReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(replyEditText.getText() != null && replyEditText.getText().toString().length() > 0) {
                    // send reply
                } else {
                    Toast.makeText(ReplyActivity.this, "Please enter reply message", Toast.LENGTH_LONG).show();
                }
            }
        });

        int type = getIntent().getIntExtra(Constants.TYPE, 0);
        String dataReceived = getIntent().getStringExtra("message");

        HashMap<String, String> jsonMessage = new HashMap<>();
        jsonMessage.put(Constants.TITLE,"Title of Message");
        jsonMessage.put(Constants.MESSAGE,"message content");

        if (type == 1) {
            // reply
            // don't add anything server can know that the message is for server itself.
            getSupportActionBar().setTitle("Reply");
        } else if (type == 2) {
            // reply topic
            getSupportActionBar().setTitle("Reply All");
            Map<String, Object> map = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to Map
            try {
                map = mapper.readValue(dataReceived, new TypeReference<Map<String, String>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonMessage.put("sendTo", (String) map.get("topic"));
        }

        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(Keys.SENDER_ID + "@" + FCM_SERVER_CONNECTION)
                .setMessageId(Integer.toString(1248653))
                .setData(jsonMessage)
                .build());
        Toast.makeText(this, "Sending reply", Toast.LENGTH_LONG).show();
        finish();
    }


}
