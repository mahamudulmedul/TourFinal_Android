package bd.org.bitm.mad.batch33.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bd.org.bitm.mad.batch33.tourmate.model.User;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameET,phoneET,emailET,passET;
private FirebaseAuth auth;
private ProgressBar progressBar;
private TextView textView;
private DatabaseReference firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameET = findViewById(R.id.nameET);
        phoneET = findViewById(R.id.phoneET);
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passwordET);
        progressBar = findViewById(R.id.progrssbar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseDatabase.keepSynced(true);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){

        }
    }

    public void SignUpUser(View view) {
        final String name = nameET.getText().toString();
        final String phone = phoneET.getText().toString();
        final String email = emailET.getText().toString();
        String pass = passET.getText().toString();
        if(name.isEmpty()){
            nameET.setError("Please input Your Name");
            nameET.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailET.setError("Please input Your Email");
            emailET.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            phoneET.setError("Please input Your phone");
            phoneET.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            passET.setError("Please input Your Password");
            passET.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            User user = new User(name,email,phone);

                            firebaseDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "Registration is Successful ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(SignUpActivity.this, "this is Not Ok", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
