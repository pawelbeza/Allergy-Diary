package com.example.allergydiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.Fragments.ChangeAllergensFragment;

public class PickerAdapter extends RecyclerView.Adapter {
    private String[] names;
    private boolean[] isChecked;


    public PickerAdapter(Context context) {
        names = context.getResources().getStringArray(R.array.allergen_picker);
        isChecked = new boolean[names.length];
        SharedPreferences sharedPref = context.getSharedPreferences(ChangeAllergensFragment.prefName, Context.MODE_PRIVATE);
        for (int i = 0; i < isChecked.length; i++)
            isChecked[i] = sharedPref.getBoolean(names[i], true);
    }

    public boolean[] getIsChecked() {
        return isChecked;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allergen_picker_item, parent, false);
        return new PickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PickerAdapter.PickerViewHolder viewHolder = (PickerAdapter.PickerViewHolder) holder;
        viewHolder.bindData(position, names[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class PickerViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView imageView;

        PickerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(view -> {
                int index = getAdapterPosition();
                isChecked[index] = !isChecked[index];
                notifyItemChanged(index);
            });
        }

        void bindData(int position, String name) {
            boolean isCheck = isChecked[position];
            bindImageSwitcher(isCheck);

            TextView allergen = itemView.findViewById(R.id.allergen_name);
            allergen.setText(name);

        }

        private void bindImageSwitcher(boolean isChecked) {
            if (isChecked)
                imageView.setImageResource(R.drawable.ic_checked);
            else
                imageView.setImageResource(R.drawable.ic_cancel);

        }
    }

}