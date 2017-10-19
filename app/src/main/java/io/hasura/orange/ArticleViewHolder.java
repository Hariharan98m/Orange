package io.hasura.orange;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;
    public TextView article_name, byline;


    public ArticleViewHolder(View itemView) {
        super(itemView);
        cardView= (CardView) itemView.findViewById(R.id.articleCard);
        article_name= (TextView) itemView.findViewById(R.id.article_name);
        byline=(TextView) itemView.findViewById(R.id.by_line);

    }

}
