package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> implements Filterable {
    private Context context;
    private List<String> cityList;
    private List<String> filteredCityList;

    public CardAdapter(Context context, List<String> cityList) {
        this.context = context;
        this.cityList = cityList;
        this.filteredCityList = new ArrayList<>(cityList);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardvieww, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.cityTextView.setText(filteredCityList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredCityList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                List<String> filteredList = new ArrayList<>();
                if (query.isEmpty()) {
                    filteredList.addAll(cityList);
                } else {
                    for (String city : cityList) {
                        if (city.toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(city);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCityList.clear();
                filteredCityList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cityTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.textView);
        }
    }
}
