package pt.ua.cm.biketrack.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.SocketHandler;

import pt.ua.cm.biketrack.R;
import pt.ua.cm.biketrack.User;
import pt.ua.cm.biketrack.ui.login.LogInFragment;
import pt.ua.cm.biketrack.ui.login.LogInViewModel;
import pt.ua.cm.biketrack.ui.login.RegisterUserFragment;

public class ProfileFragment extends Fragment {
    public View v;
    private ProfileViewModel profileViewModel;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button logout;

    public ProfileFragment(String userID){
        this.userID = userID;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = (Button) v.findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.remove("userID");
                myEdit.commit();
                FirebaseAuth.getInstance().signOut();
                //startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                Fragment fragment = new LogInFragment();
                FragmentManager fragmentManager =((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment,fragment)
                        .commit();
            }
        });

        //user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //userID = user.getUid();
        final TextView fullNameTextView = (TextView) v.findViewById(R.id.fullName);
        final TextView emailTextView = (TextView) v.findViewById(R.id.emailAddress);
        final TextView ageTextView = (TextView) v.findViewById(R.id.age);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile!= null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;


                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    ageTextView.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ProfileActivity.this,"Something went wrong...",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
