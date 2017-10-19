package io.hasura.orange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ArticleReadActivity extends AppCompatActivity {
    Article[] article= new Article[3];
    TextView title, byline, content;
    int count;
    String[] pics={"https://scontent.fmaa2-1.fna.fbcdn.net/v/t1.0-9/15241899_663343800502402_8842690344397269999_n.jpg?oh=f43684887077f0fd55a8ded02822f15c&oe=5A3D0574",
    "https://scontent.fmaa2-1.fna.fbcdn.net/v/t1.0-9/18582312_1006398012834046_3523580720088196319_n.jpg?oh=ec994053f3085bd67f9529323e83ca59&oe=5A72C6AA",
    "https://scontent.fmaa2-1.fna.fbcdn.net/v/t1.0-9/18670760_115329625717149_6189962588494711639_n.jpg?oh=b87f7151330bd76b20ccacf6a65bbb30&oe=5A7A6911"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
        title= (TextView) findViewById(R.id.title1);
        content= (TextView) findViewById(R.id.content1);

        setTitle("Article Reader");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setArticles();
        count= getIntent().getIntExtra("count", 0);
        setContent(count);
    }

    public Target target;
    MenuItem item;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.author, menu);

        item=menu.findItem(R.id.profile_pic);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                item.setIcon(drawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };

        Picasso.with(ArticleReadActivity.this).load(pics[count]).transform(new CircleTransform()).into(target);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        switch (id){
            case R.id.profile_pic:
                switch(count){
                    case 0: your_profile("Sandeep Kiran JS","https://www.facebook.com/sandeepkiran.js.22");break;
                    case 1:your_profile("Joel Raymann","https://www.facebook.com/joel.raymann");break;
                    case 2:your_profile("Hariharan M", "https://www.facebook.com/hariharan.m.313");

                }
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    private void setContent(int count) {
        title.setText(article[count].getTitle());
        content.setText(Html.fromHtml(article[count].getContent()));
    }

    private void your_profile(String name, String profile_link) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("profile", profile_link);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void setArticles(){

        article[0]=new Article("Common Excuses Why Developers Don’t Test Their Software","Insights through - Sandeep", "<p>Once upon a time I hated testing software. It wasn’t important to me. I didn’t see the purpose. It seemed like a huge waste of everybody’s time and money.\n" +
                "Throughout my career I was never taught how, or why, I should be testing my software. I made lots of excuses for not wanting to learn. I spoke to many developers who also made excuses for not wanting to learn. They still make the same excuses today. I eventually learned, they didn’t.\n" +
                "Throughout my time working with others I’ve came across many different opposing views to software testing </p>" +
                "<p>I’m going to take a quote from this StackExchange answer, because it makes sense:<br><br>" +
                "1. Test the common case of everything you can. This will tell you when that code breaks after you make some change (which is, in my opinion, the single greatest benefit of automated unit testing).<br>" +
                "2. Test the edge cases of a few unusually complex code that you think will probably have errors.<br>" +
                "3. Whenever you find a bug, write a test case to cover it before fixing it<br>" +
                "4. Add edge-case tests to less critical code whenever someone has time to kill.</p>"
        );

        article[1]=new Article("Getting your head around chrome headless","A slew of automation 'stuff' by Ray", "" +
                "<h6>So here’s what you need to know<h6>" +
                "<p>A headless browser means you don’t see it when you start it, it’s all in memory — and it also implies user actions are automated.</p>" +
                "<h6>Uses of automation<h6>" +
                "<p>1. QA tests<br>" +
                "2. Scraping<br>" +
                "3. Pre-rendering single-page apps.<br>" +
                "<h6>Uses of headless</h6>" +
                "Less resource intensive.<br>" +
                "Great for build-systems running tests before a deploy.<br>" +
                "Can run in many more server environments, like Lambda.<br>" +
                "Is this the first headless automated browser? No, phantomJS has been the goto browser like this, but the main contributor almost immediately stepped down when he heard about Chrome’s new headless feature . Turns out its hard to maintain an ENTIRE browser.</p>");

        article[2]=new Article("Why and How to implement Web Notification API?", "A Social Thought by yours truly Hari", "" +
                "Most of the websites nowadays use Web Notification API be it Facebook, Twitter, Flowdock, Slack …. you name it. Ever wondered how does that happen? <br>" +
                "<p>First and foremost, we need to check if our browser supports Notification or not. You might be wondering everyone does, why should I check it? This is web, you have n number of browsers in the market for each platform. We cannot know who supports it who doesn’t for such a huge number of browsers. So prevention is better than cure and hence, I suggest we check it before your users go bananas on different browsers. Also, the best way to test these codes is to keep typing everything on browser console right away.</p>");
    }
}
