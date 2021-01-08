package com.example.yourself;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import model.Journal;
import ui.JournalRecyclerAdapter;
import util.JournalAPI;

public class MainActivity extends AppCompatActivity {
private Button getStartedButton;
private FirebaseAuth firebaseAuth;
private FirebaseAuth.AuthStateListener authStateListener;
private FirebaseUser user;
private FirebaseFirestore db=FirebaseFirestore.getInstance();
private CollectionReference collectionReference=db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        firebaseAuth=FirebaseAuth.getInstance();
      authStateListener=new FirebaseAuth.AuthStateListener() {
          @Override
          public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              user=firebaseAuth.getCurrentUser();
                if(user!=null){
                        user=firebaseAuth.getCurrentUser();
                        String currentUSerID=user.getUid();
                        collectionReference.whereEqualTo("userId",currentUSerID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if(error!=null){
                                            return;
                                    }
                                 String name;
                                    if(!value.isEmpty()){
                                        for(QueryDocumentSnapshot snapshot:value){
                                            JournalAPI journalAPI= JournalAPI.getInstance();
                                            journalAPI.setUserId(snapshot.getString("userId"));
                                            journalAPI.setUsername(snapshot.getString("username"));
                                            startActivity(new Intent(MainActivity.this,JournalListActivity.class));
                                        finish();
                                        }
                                    }
                            }
                        });
                }
                else{

                }
              }
      };
        setContentView(R.layout.activity_main);
        getStartedButton=findViewById(R.id.start_button);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we go to Login Activity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}