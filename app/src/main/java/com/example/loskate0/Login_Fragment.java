package com.example.loskate0;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login_Fragment extends Fragment
{

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login,container, false);

        // Point our variables at their respective UI layout locations
        emailField = v.findViewById(R.id.editText_Email);
        passwordField = v.findViewById(R.id.editText_Password);
        loginButton = v.findViewById(R.id.button_Submit);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null)
        {
            Log.d("PAGNOZZI", "User is " + user);
            // do register stuff
            // When the Login Button is Clicked
            loginButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String _email = emailField.getText().toString();
                    String _password = passwordField.getText().toString();
                    RegisterUser(_email,_password);
                }
            });
        }
        else
        {
            // switch to profile fragment
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.Frag_Trans_Profile();
        }


        return v;
    }


 private void RegisterUser(String email, String password)
 {
     mAuth.createUserWithEmailAndPassword(email, password);
 }
}
