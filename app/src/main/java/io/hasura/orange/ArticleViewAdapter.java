package io.hasura.orange;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    Article[] article= new Article[3];
    int ItemCount=3;
    Activity act;
    boolean write;

    public ArticleViewAdapter(Article[] article, int ItemCount, boolean write, Activity act) {
        this.article = article;
        this.ItemCount= ItemCount;
        this.act= act;
        this.write=write;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {
        //Set the data
        holder.article_name.setText(article[position].getTitle());
        if(write){
            ((ViewManager)holder.byline.getParent()).removeView(holder.byline);
        }

        else
            holder.byline.setVisibility(View.VISIBLE);
        holder.byline.setText(article[position].getSubtitle());
        holder.cardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(write){
                    Intent intent= new Intent(act, ArticleWriteActivity.class);
                    intent.putExtra("count",position);
                    act.startActivity(intent);

                }else{
                    Intent intent= new Intent(act, ArticleReadActivity.class);
                    intent.putExtra("count",position);
                    act.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ItemCount;
    }

}
