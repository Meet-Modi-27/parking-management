package com.example.parking_management.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking_management.R;
import com.example.parking_management.models.spotModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class spotId_adapter extends FirebaseRecyclerAdapter<spotModel,spotId_adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public spotId_adapter(@NonNull FirebaseRecyclerOptions<spotModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull spotModel model) {
        String spot = model.getSpotId();
        String[] parts = spot.split("_");

        if (parts.length >= 2) { // Check if there are at least two parts after splitting
            String dataAfterUnderscore = parts[1];
            String new_string = "Spot " + dataAfterUnderscore;
            holder.Spot_id.setText(new_string);
        } else {
            holder.Spot_id.setText("Invalid spot ID format");
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avalable_spots,parent,false);
        return new spotId_adapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView Spot_id;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Spot_id = (TextView) itemView.findViewById(R.id.spot_id);
        }
    }
}
