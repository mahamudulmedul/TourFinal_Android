package bd.org.bitm.mad.batch33.tourmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bd.org.bitm.mad.batch33.tourmate.Utils.Constant;
import bd.org.bitm.mad.batch33.tourmate.Weather.CameraActivity;
import bd.org.bitm.mad.batch33.tourmate.Weather.WeatherActivity;
import bd.org.bitm.mad.batch33.tourmate.fragment.Camera.CamerFragment;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameET,passwordET;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.passwordET);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void LoginUser(View view) {
        String email = userNameET.getText().toString();
        String pass = passwordET.getText().toString();
        if(email.isEmpty()){
            userNameET.setError("Please input UserName");
            userNameET.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            passwordET.setError("Please input Password");
            passwordET.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){

                            user = mAuth.getCurrentUser();
                            Constant.UserData.email = user.getEmail();
                            Constant.UserData.uid = user.getUid();
                            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this,MainActivity.class);

                            startActivity(i);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Your Email And Password is Not Successful", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void SignUp(View view) {

        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }

}
