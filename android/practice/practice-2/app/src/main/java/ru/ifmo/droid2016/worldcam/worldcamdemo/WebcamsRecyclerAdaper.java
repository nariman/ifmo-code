package ru.ifmo.droid2016.worldcam.worldcamdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;

/**
 * Created by woofilee on 05.10.2016.
 */

public class WebcamsRecyclerAdaper extends RecyclerView.Adapter<WebcamsRecyclerAdaper.ViewHolder> {

    Context context;
    LayoutInflater inflater;

    List<Webcam> webcams = Collections.emptyList();

    public WebcamsRecyclerAdaper(Context context, List<Webcam> webcams) {
        this.context = context;
        this.webcams = webcams;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public WebcamsRecyclerAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(inflater, parent);
    }

    @Override
    public void onBindViewHolder(WebcamsRecyclerAdaper.ViewHolder holder, int position) {
        Webcam webcam = webcams.get(position);
        holder.webcamTitle.setText(webcam.title);
        holder.webcamImage.setImageURI(webcam.imageUrl);
    }

    @Override
    public int getItemCount() {
        return webcams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView webcamImage;
        final TextView webcamTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            webcamImage = (SimpleDraweeView) itemView.findViewById(R.id.webcam_image);
            webcamTitle = (TextView) itemView.findViewById(R.id.webcam_title);
        }

        public static ViewHolder newInstance(LayoutInflater inflater, ViewGroup parent) {
            return new ViewHolder(inflater.inflate(R.layout.item_webcam, parent, false));
        }
    }
}
