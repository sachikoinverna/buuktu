package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.NotikieModel;
import com.example.buuktu.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotikieListAdapter extends RecyclerView.Adapter<NotikieListAdapter.ViewHolder>{

    private final ArrayList<NotikieModel> dataSet;
    private final Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_text_notikie_list_layout;
        final TextView tv_date_notikie_list_layout;
        final ImageView iv_icon_notikie_list_layout;
        public ViewHolder(View view) {
            super(view);
            iv_icon_notikie_list_layout = view.findViewById(R.id.iv_icon_notikie_list_layout);
            tv_date_notikie_list_layout = view.findViewById(R.id.tv_date_notikie_list_layout);
            tv_text_notikie_list_layout = view.findViewById(R.id.tv_text_notikie_list_layout);
        }


        public TextView getTv_text_notikie_list_layout() {
            return tv_text_notikie_list_layout;
        }

        public TextView getTv_date_notikie_list_layout() {
            return tv_date_notikie_list_layout;
        }

        public ImageView getIv_icon_notikie_list_layout() {
            return iv_icon_notikie_list_layout;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public NotikieListAdapter(ArrayList<NotikieModel> dataSet, Context ctx) {
        this.dataSet = dataSet;
        this.context = ctx;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notikie_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotikieModel notikieModel = dataSet.get(position);
        holder.getTv_text_notikie_list_layout().setText(notikieModel.getMessage());
        holder.getIv_icon_notikie_list_layout()
                .setImageAlpha(notikieModel.getIcon());
        Date date = notikieModel.getDate().toDate();
        long serverTimeMs = date.getTime();
        long current = DateUtils.currentMs();
        long timeDifference = DateUtils.getTimeDiference(current,serverTimeMs);
        long seconds = DateUtils.secondsFrom(timeDifference);
        long minutes = DateUtils.minutesFrom(timeDifference);
        long hours = DateUtils.hoursFrom(timeDifference);
        long days = DateUtils.daysFrom(timeDifference);
        String timeAgo;
        SimpleDateFormat dateFormat;
        if (days > 30) {
            // Si han pasado más de 30 días, mostrar la fecha completa con hora
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            timeAgo = context.getResources().getString(R.string.date)+": " + dateFormat.format(date);
        } else if (days > 7) {
            // Si han pasado más de 7 días pero menos de 30 días, mostrar el día de la semana
            dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            timeAgo = context.getResources().getString(R.string.it_was_a) +" "+ dateFormat.format(date)+".";
        } else {
            // Si han pasado menos de 7 días, mostrar el tiempo transcurrido
            timeAgo = getTimeAgo(seconds, minutes, hours, days);
        }

        // Mostrar el tiempo transcurrido o la fecha completa
        holder.getTv_date_notikie_list_layout().setText(timeAgo);
    }
private String getTimeAgo(long seconds, long minutes, long hours, long days) {
    if (days > 0) {
        return days + " " +context.getResources().getString(R.string.day) + (days > 1 ? "s" : "") +" "+context.getResources().getString(R.string.ago)+".";
    } else if (hours > 0) {
        return  hours + " " +context.getResources().getString(R.string.hour) + (hours > 1 ? "s" : "") +" "+context.getResources().getString(R.string.ago)+ ".";
    } else if (minutes > 0) {
        return minutes + " " +context.getResources().getString(R.string.minute) + (minutes > 1 ? "s" : "") +" "+context.getResources().getString(R.string.ago)+ ".";
    } else {
        return seconds + " " +context.getResources().getString(R.string.second) + (seconds > 1 ? "s" : "") +" "+context.getResources().getString(R.string.ago)+ ".";
    }
}

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
