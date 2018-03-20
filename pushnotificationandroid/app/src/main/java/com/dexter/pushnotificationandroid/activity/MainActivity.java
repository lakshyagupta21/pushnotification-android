/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexter.pushnotificationandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dexter.pushnotificationandroid.Constants;
import com.dexter.pushnotificationandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    ListView subscribeView;
    ArrayAdapter<String> subscribeAdapter;
    EditText etSubscibe;
    Button bSubscribe;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subscribeView = (ListView) findViewById(R.id.subscribeList);
        getSupportActionBar().setTitle("Subscribe");

        etSubscibe = (EditText) findViewById(R.id.etSubscribe);
        bSubscribe = (Button) findViewById(R.id.bSubscribe);

        subscribeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked);
        subscribeView.setAdapter(subscribeAdapter);
        subscribeAdapter.notifyDataSetChanged();

        bSubscribe.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.child(Constants.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.SUBSCRIPTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            subscribeAdapter.add(childSnapshot.getValue(String.class));
                            subscribeAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.bSubscribe) {
            String subscibtionId = etSubscibe.getText().toString();

            if (isValid(subscibtionId)) {
                FirebaseMessaging.getInstance().subscribeToTopic(subscibtionId);

                mDatabase.child(Constants.USERS).child(FirebaseAuth.getInstance().getUid()).child(Constants.SUBSCRIPTION).push().
                        setValue(subscibtionId);
                Toast.makeText(this, "Subscribed to " + etSubscibe.getText().toString(), Toast.LENGTH_LONG).show();
                subscribeAdapter.add(etSubscibe.getText().toString());
                subscribeAdapter.notifyDataSetChanged();
                etSubscibe.setText("");
            } else {
                Toast.makeText(this, "Enter correct subscription ID", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isValid(String id) {
        if (id != null && id.length() > 0) {
            return true;
        }
        return false;
    }
}
