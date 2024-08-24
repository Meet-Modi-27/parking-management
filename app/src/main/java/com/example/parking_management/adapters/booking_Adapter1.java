package com.example.parking_management.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking_management.R;
import com.example.parking_management.models.bookingsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class booking_Adapter1 extends FirebaseRecyclerAdapter<bookingsModel, booking_Adapter1.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public booking_Adapter1(@NonNull FirebaseRecyclerOptions<bookingsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull bookingsModel model) {
        holder.username.setText(model.getUserName());
        holder.number.setText(model.getNumber());
        holder.timestamp.setText(model.getTimestamp());
        holder.spotId.setText(model.getSpotId());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_details,parent,false);
        return new booking_Adapter1.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView username,number,spotId,timestamp;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.bookings_name);
            number = (TextView) itemView.findViewById(R.id.bookings_number);
            spotId = (TextView) itemView.findViewById(R.id.bookings_spot);
            timestamp = (TextView) itemView.findViewById(R.id.bookings_timestamp);
        }
    }
}
