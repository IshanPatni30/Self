package com.example.yourself;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalAPI;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton, createAccountButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AutoCompleteTextView emailAddress;
    private EditText password;

    private FirebaseUser user;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailAddress=findViewById(R.id.email);
        password=findViewById(R.id.password);
        progressBar=findViewById(R.id.login_progress);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.email_Sign_in_button);
        createAccountButton = findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailPasswordUser(emailAddress.getText().toString().trim(), password.getText().toString().trim());


            }
        });
    }

    private void loginEmailPasswordUser(String email, String pword) {
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pword)) {
            firebaseAuth.signInWithEmailAndPassword(email, pword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String currentUserId = user.getUid();

                collectionReference.whereEqualTo("userId", currentUserId)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {

                                }
                                if (!value.isEmpty()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    for (QueryDocumentSnapshot snapshot : value) {
                                        JournalAPI journalAPI = JournalAPI.getInstance();
                                        journalAPI.setUsername(snapshot.getString("userName"));
                                        journalAPI.setUserId(snapshot.getString("userId"));

                                        //Go to List Activity
                                        startActivity(new Intent(LoginActivity.this,PostJournalActivity.class));
                                        finish();
                                    }

                                }
                            }
                        });
            }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });

        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

        }

    }
}