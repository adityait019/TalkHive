package com.example.talkhive.utilities.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talkhive.R;
import com.example.talkhive.fragments.UsersFragment;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserDetailsModel;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class UpdateUserAdapter extends RecyclerView.Adapter<UpdateUserAdapter.UserHolder> {
    private static final String CLASS_TAG = UpdateUserModel.class.getName();
    private final UserDetailsModel detailsModel;
    private ArrayList<UpdateUserModel> dataSet;
    private final UsersFragment context;

    public interface userItemClickListener {
        void onClick(UpdateUserModel model);
    }

    private userItemClickListener listener;

    public UpdateUserAdapter(UsersFragment context) {
        this.context = context;
        dataSet = new ArrayList<>();
        detailsModel = UserDetailsModel.getInstance();
        listener = (userItemClickListener) context;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.rv_chat_bubble_user, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.bindView(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(final ArrayList<UpdateUserModel> dataSet) {
        this.dataSet = dataSet;
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatImageView displayUserView;
        private final TextView displayNameView;
        private final TextView displayEmailView;
        private userItemClickListener listener;


        public UserHolder(@NonNull View itemView, final userItemClickListener listener) {
            super(itemView);
            displayUserView = itemView.findViewById(R.id.chat_bubble_user_dp);
            displayNameView = itemView.findViewById(R.id.name_tv);
            displayEmailView = itemView.findViewById(R.id.email_tv);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(dataSet.get(getAdapterPosition()));
        }

        public void bindView(final UpdateUserModel model) {
            displayNameView.setText(model.getName());
            displayEmailView.setText(model.getEmail());
            detailsModel.getStorageReference().child(model.getEmail().replace(".","")+"/dp.jpg")
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i(CLASS_TAG, "Called here");
                            Glide.with(context).load(uri).into(displayUserView);
                        }
                    });
        }
    }
}