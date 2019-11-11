package com.example.kitchen;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    //Instance variables
    private List<WifiP2pDevice> devices;
    private OnClickListener onClickListener;

    /**
     * Use this constructor to make a Device adapter.
     *
     * @param devices the dataset for this adapter
     * @param listener the onclick listener for when a row is selected.
     */
    public DeviceAdapter(List<WifiP2pDevice> devices, OnClickListener listener) {
        super();
        this.devices = devices;
        this.onClickListener = listener;
    }

    /**
     * This method is run when the viewHolder for each cell is created
     *
     * @param parent the parent view
     * @param viewType an int referencing the type of view
     * @return
     */
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_recycler_view_item, parent, false);
        return new DeviceViewHolder(view, onClickListener);
    }

    /**
     * This method is run when the view holder is bound to the recycler view.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.bind(devices.get(position));
    }

    /**
     * This method gets the count of items in the recycler view.
     *
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return devices.size();
    }

    /**
     * This class is the controller for a single row in the recycler view
     */
    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WifiP2pDevice device;
        private TextView textView;
        private OnClickListener listener;


        public DeviceViewHolder(View view, OnClickListener onClickListener) {
            super(view);
            textView = view.findViewById(R.id.device_recycler_text);
            view.setOnClickListener(this);
            listener = onClickListener;
        }

        public void bind(WifiP2pDevice wifiP2pDevice) {
            textView.setText(device.deviceName);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }
    }


    public interface OnClickListener {
        void onClick(int position);
    }

}

