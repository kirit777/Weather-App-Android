package com.example.feather.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.feather.R;
import com.example.feather.model.ForcastClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForcastAdapter extends RecyclerView.Adapter<ForcastAdapter.ViewHolder>{
    private Context context;
    List<ForcastClass> forcastClasses;
    MediaPlayer mediaPlayer;
    // RecyclerView recyclerView;
    public ForcastAdapter(List<ForcastClass> forcastClasses,Context context) {
        this.forcastClasses = forcastClasses;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.forcast_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.day.setText(forcastClasses.get(position).getDay());
        holder.temp.setText(forcastClasses.get(position).getTemp());
        holder.min.setText(forcastClasses.get(position).getMin());
        holder.max.setText(forcastClasses.get(position).getMax());
        holder.wind.setText(forcastClasses.get(position).getWind());
        String url = forcastClasses.get(position).getIcon();
        Glide.with(context).load(url).into(holder.img);

        if(!holder.day.getText().toString().isEmpty()){
            holder.prb.setVisibility(View.GONE);
            holder.l1.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return forcastClasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day,min,max,temp,wind;
        public ImageView img;
        public LinearLayout l1;
        ProgressBar prb;



        public ViewHolder(View itemView) {
            super(itemView);
            this.day = (TextView)itemView.findViewById(R.id.day);
            this.min = (TextView)itemView.findViewById(R.id.min);
            this.max = (TextView)itemView.findViewById(R.id.max);
            this.temp = (TextView)itemView.findViewById(R.id.temp);
            this.wind = (TextView)itemView.findViewById(R.id.wind);

            this.img = (ImageView)itemView.findViewById(R.id.img);
            this.l1 = (LinearLayout)itemView.findViewById(R.id.l1);
            this.prb = (ProgressBar)itemView.findViewById(R.id.prb);
        }
    }
}
