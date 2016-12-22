package ru.ifmo.droid2016.rzddemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.rzddemo.model.TimetableEntry;

class TimetableRecyclerAdapter
        extends RecyclerView.Adapter<TimetableRecyclerAdapter.TimetableViewHolder> {

    @NonNull
    private List<TimetableEntry> data;

    @NonNull
    private final LayoutInflater layoutInflater;

    @NonNull
    private final Context context;


    public TimetableRecyclerAdapter(@NonNull Context context,
                                    @NonNull List<TimetableEntry> data) {
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        setHasStableIds(false);
    }

    public void setData(@NonNull List<TimetableEntry> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TimetableViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(TimetableViewHolder holder, int position) {
        final TimetableEntry entry = data.get(position);
        final String trainIdText = entry.trainName == null
                ? context.getString(R.string.train_id_format, entry.trainRouteId)
                : context.getString(R.string.train_id_name_format, entry.trainRouteId,
                                    entry.trainName);
        holder.trainIdView.setText(trainIdText);
        holder.routeDescriptionView.setText(context.getString(R.string.route_descr_format,
                entry.routeStartStationName, entry.routeEndStationName));

        CharSequence departureTime = DateFormat.format("HH:mm", entry.departureTime);
        holder.departureInfoView.setText(context.getString(R.string.departure_format,
                entry.departureStationName, departureTime));

        CharSequence arrivalTime = DateFormat.format("HH:mm", entry.arrivalTime);
        holder.arrivalInfoView.setText(context.getString(R.string.arrival_format,
                entry.arrivalStationName, arrivalTime));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class TimetableViewHolder extends RecyclerView.ViewHolder {

        final TextView trainIdView;
        final TextView routeDescriptionView;
        final TextView departureInfoView;
        final TextView arrivalInfoView;

        TimetableViewHolder(View view) {
            super(view);
            trainIdView = (TextView) view.findViewById(R.id.train_id);
            routeDescriptionView = (TextView) view.findViewById(R.id.route_description);
            departureInfoView = (TextView) view.findViewById(R.id.departure_info);
            arrivalInfoView = (TextView) view.findViewById(R.id.arrival_info);
        }

        static TimetableViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.item_timetable, parent, false);
            return new TimetableViewHolder(view);
        }
    }
}
