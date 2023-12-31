package com.example.talkhive.utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.talkhive.MainActivity;
import com.example.talkhive.fragments.LoginFragment;
import com.example.talkhive.fragments.SignUpFragment;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class VerificationUtilities {
    private static final String CLASS_TAG = VerificationUtilities.class.getName();
    final static String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean verifyEmail(final String email) {
        return email.matches(emailPattern);
    }

    public static boolean checkEqualityPassword(final String pass1, final String pass2) {
        return !(pass1.isEmpty() || pass2.isEmpty() || !pass1.equals(pass2));
    }

    public static void replaceInMain(Fragment fragment, Context context) {
        MainActivity activity = (MainActivity) context;
        activity.addOrReplace(fragment);
    }

    public static void updateUserHelper(UpdateUserModel model) {
        final String nameToSearch = model.getEmail();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i(CLASS_TAG, "Called Here");
            final String userId = Objects.requireNonNull(user.getEmail()).replace(".","");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

            reference.child(nameToSearch.replace(".", "")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.i(CLASS_TAG, "User Exist");
                        reference.child(userId+"/contacts/"+ model.getEmail().replace(".",""))
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i(CLASS_TAG,"Success in add user");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i(CLASS_TAG,"Failure to add user");
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
