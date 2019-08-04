package com.petermunyao.travelmantics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.petermunyao.travelmantics.databinding.RowCardBinding;
import com.petermunyao.travelmantics.model.TravelDeal;

import java.util.ArrayList;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    private List<TravelDeal> mList;
    private Context mContext;

    public DealAdapter(List<TravelDeal> list, Context context) {

        mList = list;
        mContext = context;

    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowCardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.row_card, parent, false);
        return new DealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal travelDeal = mList.get(position);
        holder.binding.setTraveldeal(travelDeal);
        holder.binding.setListInterface((ListInterface) mContext);
        holder.binding.txtTitle.setText(travelDeal.getTitle());
        holder.binding.txtPrice.setText(travelDeal.getPrice());
        holder.binding.txtDescription.setText(travelDeal.getDescription());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class DealViewHolder extends RecyclerView.ViewHolder {

        RowCardBinding binding;

        public DealViewHolder(RowCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
