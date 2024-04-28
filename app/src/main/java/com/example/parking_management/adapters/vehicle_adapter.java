package com.example.parking_management.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking_management.R;
import com.example.parking_management.models.vehicleModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class vehicle_adapter extends FirebaseRecyclerAdapter<vehicleModel,vehicle_adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public vehicle_adapter(@NonNull FirebaseRecyclerOptions<vehicleModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull vehicleModel model) {
        holder.make.setText(model.getMake());
        holder.model.setText(model.getModel());
        holder.plate.setText(model.getNumber());
        holder.type.setText(model.getType());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_details,parent,false);
        return new vehicle_adapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView make,model,plate,type;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            make = (TextView) itemView.findViewById(R.id.vehicle_make);
            model = (TextView) itemView.findViewById(R.id.vehicle_model);
            plate = (TextView) itemView.findViewById(R.id.vehicle_numberplate);
            type = (TextView) itemView.findViewById(R.id.vehicle_type);
        }
    }
}
