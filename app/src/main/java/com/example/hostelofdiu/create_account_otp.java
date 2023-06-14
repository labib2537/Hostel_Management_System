package com.example.hostelofdiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class create_account_otp extends AppCompatActivity implements View.OnClickListener {

    PinView pinFromUser;
    Button otp_login_btn;
    Intent intent;
    String codeBySystem;
    String name, phone, password;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_create_account_otp);
        getSupportActionBar().hide();

        pinFromUser = findViewById(R.id.create_account_otp_pnvw_id);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phn");
        password = getIntent().getStringExtra("pswrd");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sendVerifivationCodeUser(phone);
        otp_login_btn = findViewById(R.id.otp_verify_btn_id);
        otp_login_btn.setOnClickListener(this);
    }

    private void sendVerifivationCodeUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    showTryagainLaterDialogue();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference DR = db.collection("users").document(phone);
                            // Create a new user with a first and last name
                            Map<String, String> user = new HashMap<>();
                            user.put("phone", phone);
                            user.put("name", name);
                            user.put("name_lower", name.toLowerCase());
                            user.put("password", password);
                            user.put("profile_img", "");



                            // Add a new document with a generated ID
                            db.collection("users")
                                    .document(phone)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            session ssn = new session(create_account_otp.this);
                                            ssn.createLoginSession(name, phone,"user");

                                            intent = new Intent(getApplicationContext(), home_page.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showTryagainLaterDialogue();
                                        }
                                    });


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                showVarificationNotCompletedDialogue();
                            }
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {

        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }

    }
    private void showVarificationNotCompletedDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Verification Not Completed!\nDo you want to try again ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(),create_account_otp.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showVarificationNotCompletedWaiteDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Verification Not Completed!\nDo you want to waite for a while ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(
                Html.fromHtml("<font color='#004383'>No</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showTryagainLaterDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Too many attempt with that Phone Number!\nTry again after a while ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                Html.fromHtml("<font color='#004383'>OK</font>"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(),login_page.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onBackPressed() {
        showVarificationNotCompletedWaiteDialogue();
    }
}