package io.hasura.orange;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class YourArticlesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Article[] articles= new Article[20];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_articles);
        setTitle("Your Articles");
        setArticles();
        recyclerView = (RecyclerView) findViewById(R.id.yourarticlesList);
        //Step 1--Set the adapter
        recyclerView.setAdapter(new ArticleViewAdapter(articles,getApplicationContext().getSharedPreferences("art_count",MODE_PRIVATE).getInt("count",0), true, this));

        //Step2 -- Set the layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setArticles() {
        for (int i=0;i<getApplicationContext().getSharedPreferences("art_count",MODE_PRIVATE).getInt("count",0);i++){
            SharedPreferences art= getApplicationContext().getSharedPreferences(Integer.toString(i),MODE_PRIVATE);
            articles[i]=new Article(art.getString("title",""),art.getString("byline",""),art.getString("content",""));
        }
    }
}
