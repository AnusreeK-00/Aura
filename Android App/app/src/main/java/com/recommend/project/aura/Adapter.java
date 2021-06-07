package com.recommend.project.aura;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatDrawableManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    String data[];
    private final int[] backgroundColors = {
            R.drawable.gradient1,
            R.drawable.gradient2,
            R.drawable.gradient3,
            R.drawable.gradient4,
            R.drawable.gradient5,
            R.drawable.gradient6,
            R.drawable.gradient2,
            R.drawable.gradient4,
            R.drawable.gradient5,
            R.drawable.gradient1 };
    public Adapter(Context ct, String music[]){
        context=ct;
        data=music;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.list_item_movie_music,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int i) {
        holder.title.setText(data[i]);
        int index=i%backgroundColors.length;
        @SuppressLint("RestrictedApi") Drawable color= AppCompatDrawableManager.get().getDrawable(context,backgroundColors[index]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityWebview.class);
                intent.putExtra("song",data[i]);
                context.startActivity(intent);
            }
        });

        holder.bg.setBackground(color);

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RelativeLayout bg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.music);
            bg=itemView.findViewById(R.id.background);
        }

    }
}