package io.hasura.orange;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ArticleWriteActivity extends AppCompatActivity {
    EditText title, byline, content;
    Button save, publish;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_write);
        setTitle("Article Editor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        save= (Button) findViewById(R.id.save);
        publish= (Button) findViewById(R.id.publish);

        if(getIntent().getBooleanExtra("write",false)) {
            count = getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).getInt("count", 0);
            Log.i("Write","True");
            Log.i("Count",Integer.toString(count));

        }else{
            count=getIntent().getIntExtra("count",0);
            Log.i("Your articles","True");
            Log.i("Count",Integer.toString(count));
        }

        if(getApplicationContext().getSharedPreferences(Integer.toString(count),MODE_PRIVATE).getBoolean("pub",false)) {
            publish.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            publish.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        title=(EditText) findViewById(R.id.title);
        byline=(EditText) findViewById(R.id.byline1);
        content=(EditText) findViewById(R.id.content1);

        setContent(count);

    }

    private void setContent(int count) {
        SharedPreferences art= getApplicationContext().getSharedPreferences(Integer.toString(count),MODE_PRIVATE);
        title.setText(art.getString("title", ""));
        byline.setText(art.getString("byline", ""));
        content.setText(Html.fromHtml(art.getString("content", "")));
    }

    public void save_onclick(View v){

        if(validateFields(title.getText().toString(), byline.getText().toString(), content.getText().toString())) {
            SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences(Integer.toString(count), MODE_PRIVATE).edit();
            edit.putString("title", title.getText().toString());
            edit.putString("byline", byline.getText().toString());
            edit.putString("content", content.getText().toString());
            edit.putInt("id", count);
            if(getApplicationContext().getSharedPreferences(Integer.toString(count), MODE_PRIVATE).getBoolean("save",true)) {
                int count = getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).getInt("count", 0);
                count++;
                getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).edit().putInt("count", count).commit();
                edit.putBoolean("save",false).commit();
                Log.i("After Save: Count",Integer.toString(getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).getInt("count", 0)));
            }
            edit.commit();
            Toast.makeText(this, "Work Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields(String title, String byline, String content) {
        if(title.isEmpty()){
            Log.i("Inside isFormValid","name is empty");
            Toast.makeText(this,"Title can't be left empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(byline.isEmpty()){
            Toast.makeText(this,"Byline can't be blank",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(content.isEmpty()){
            Toast.makeText(this,"Content can't be left blank",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void publish_onclick(View v){
        if(validateFields(title.getText().toString(), byline.getText().toString(), content.getText().toString())) {
            SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences(Integer.toString(count), MODE_PRIVATE).edit();
            edit.putBoolean("pub", true);
            edit.putString("title", title.getText().toString());
            edit.putString("byline", byline.getText().toString());
            edit.putString("content", content.getText().toString());
            edit.putInt("id", count);
            if(getApplicationContext().getSharedPreferences(Integer.toString(count), MODE_PRIVATE).getBoolean("save",true)) {
                int count = getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).getInt("count", 0);
                Log.i("count",Integer.toString(count));
                count++;
                getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).edit().putInt("count", count).commit();
                edit.putBoolean("save",false).commit();
                Log.i("After Publish: Count",Integer.toString(getApplicationContext().getSharedPreferences("art_count", MODE_PRIVATE).getInt("count", 0)));
            }
            edit.commit();
            HomeActivity.startActivity(this, false, "Published on Orange");
        }
    }
}
