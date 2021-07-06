package com.example.afor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<answer_recieve> messagesList;
    private OnItemClickListener listener;



    public RecyclerAdapter(Context mContext, ArrayList<answer_recieve> messagesList, OnItemClickListener listener) {
        this.mContext = mContext;
        this.messagesList = messagesList;
        this.listener = listener;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
//xml from which we want to inflate in recyclerview
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.message_item, parent, false);

    return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.ViewHolder holder, int position) {
       //text
        holder.textView.setText(messagesList.get(position).getName());

        //image uses Glide library
        Glide.with(mContext)
                .load(messagesList.get(position).getImageUrl())
                .into(holder.imageView1);




    }


    @Override
    public int getItemCount() {

        return messagesList.size();
    }

    //onlcick recyclerview
    public interface OnItemClickListener{
        void onClick(View v, int position);
    }

    /*public  void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
//what gets inflated in recyclerview and where it comes from
        TextView textView;
        ImageView imageView1;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.imageView1);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());


        }
    }
}
