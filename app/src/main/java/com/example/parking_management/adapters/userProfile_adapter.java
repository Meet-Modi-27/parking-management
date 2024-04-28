package com.example.parking_management.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking_management.R;
import com.example.parking_management.models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class userProfile_adapter extends FirebaseRecyclerAdapter<usersModel,userProfile_adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public userProfile_adapter(@NonNull FirebaseRecyclerOptions<usersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull usersModel model) {
        holder.name.setText(model.getName());
        holder.username.setText(model.getUsername());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getNumber());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile,parent,false);
        return new userProfile_adapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name,username,email,phone;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.profile_name);
            username=(TextView) itemView.findViewById(R.id.profile_username);
            email=(TextView) itemView.findViewById(R.id.profile_email);
            phone=(TextView) itemView.findViewById(R.id.profile_phone);
        }
    }
}
