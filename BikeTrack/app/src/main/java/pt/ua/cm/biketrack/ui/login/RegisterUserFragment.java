package pt.ua.cm.biketrack.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import pt.ua.cm.biketrack.R;
import pt.ua.cm.biketrack.User;
import pt.ua.cm.biketrack.ui.home.HomeViewModel;

public class RegisterUserFragment extends Fragment implements View.OnClickListener {

    private RegisterViewModel registerViewModel;
    private FirebaseAuth mAuth;
    private TextView banner,registerUser;
    private EditText editTextFullName,editTextAge,editTextEmail,editTextPassword;
    private ProgressBar progressBar;
    public View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);
        v = inflater.inflate(R.layout.fragment_register_user, container, false);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) v.findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) v.findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
        editTextFullName = (EditText) v.findViewById(R.id.fullName);
        editTextAge = (EditText) v.findViewById(R.id.age);
        editTextEmail = (EditText) v.findViewById(R.id.email);
        editTextPassword = (EditText) v.findViewById(R.id.password);

        progressBar  = (ProgressBar) v.findViewById(R.id.progressBar);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                //startActivity(new Intent(this,MainActivity.class));
                Fragment fragment = new LogInFragment();
                FragmentManager fragmentManager =((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment,fragment)
                        .commit();
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    //some validation
    public void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            editTextAge.setError("Full name is required");
            editTextAge.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Full name is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email! ");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required! ");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, age, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(RegisterUser.this, "User has been registerd successfully! ",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        System.out.println("aquiiii");
                                    } else {
                                        //Toast.makeText(RegisterUser.this, "Failed to register! Try again ",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        } else {
                            //Toast.makeText(RegisterUser.this, "Failed to register! Try again ", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        //getActivity().onBackPressed();
                        Fragment fragment = new LogInFragment();
                        FragmentManager fragmentManager =((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment,fragment)
                                .commit();
                    }
                });
    }
}
