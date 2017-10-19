package io.hasura.orange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static io.hasura.orange.R.id.coordinatorLayout;
import static io.hasura.orange.R.id.search_it;

public class HomeActivity extends BaseActivity {

    WebView webView;
    Article[] article= new Article[3];
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ListView listView;
    DrawerLayout drawerLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    TextView hi;
    boolean shouldGoInvisible;

    public static void startActivity(Activity startingActivity, boolean login, String s){
        Intent intent = new Intent(startingActivity, HomeActivity.class);
        intent.putExtra("login",login);
        intent.putExtra("snack",s);
        startingActivity.startActivity(intent);
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        setTitle("Article Feed");
        final boolean login=getIntent().getBooleanExtra("login",false);
        hi= (TextView) findViewById(R.id.hi);
        listView = (ListView)findViewById(R.id.list_view);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hi.setText("Hi "+Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName()+",");

        recyclerView = (RecyclerView) findViewById(R.id.articleList);
        setArticles();
            //Step 1--Set the adapter
        recyclerView.setAdapter(new ArticleViewAdapter(article, 3, false, this));

            //Step2 -- Set the layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        List<DrawerListItems> items=new ArrayList<>();

        String[] display={"Your Profile","Write an Article","e-Buy","Your Articles"};
        for (int i=0;i<4;i++){
            items.add(new DrawerListItems(display[i],i));
        }
        listView.setAdapter(new CustomAdapter(getBaseContext(), items));
        // Set the list's click listener
        listView.setOnItemClickListener(new DrawerItemClickListener());
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open,R.string.close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle("Book Feed");
                shouldGoInvisible = false;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle("What Next");
                shouldGoInvisible = true;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(login)
                    showSnackbar("Logged In");
                else{
                    if(getIntent().getStringExtra("snack")!=null &&getIntent().getStringExtra("snack").equals("Published on Orange"))
                        showSnackbar("Published on Orange");
                }
            }
        },300);

    }

    public Target target;
    MenuItem item;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.logout_button, menu);

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

        Picasso.with(HomeActivity.this).load(Profile.getCurrentProfile().getProfilePictureUri(100,100)).transform(new CircleTransform()).into(target);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id){
            case R.id.profile_pic:
                your_profile("","");
                break;
            case R.id.logout:
                LoginManager.getInstance().logOut();
                MainActivity.startActivity(HomeActivity.this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void your_profile(String book_name, String profile_link) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("profile", Profile.getCurrentProfile().getLinkUri().toString());
        intent.putExtra("name", Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
        if(!book_name.equals(""))
            intent.putExtra("book_name", book_name);
        if(!profile_link.equals(""))
            intent.putExtra("profile_name", profile_link);
        startActivity(intent);
    }

    void showSnackbar(String s){
        Snackbar snackbar=Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>"+s+"</i></font>"),Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.blue));
        snackbar.show();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(listView);
        if(shouldGoInvisible) {
            menu.findItem(R.id.profile_pic).setVisible(!shouldGoInvisible);
            menu.findItem(R.id.logout).setVisible(!shouldGoInvisible);
            actionBarDrawerToggle.syncState();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    public void e_buy() {

        View view= getLayoutInflater().inflate(R.layout.search_book,null);
        //Get the song details
        final EditText book_name= (EditText) view.findViewById(R.id.book_name);

            Button search_it = (Button) view.findViewById(R.id.search_it);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(view);
            final AlertDialog dialog = alert.create();
            dialog.show();
            search_it.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(book_name.getText().toString().isEmpty())
                        Toast.makeText(HomeActivity.this, "Enter a valid Book Name", Toast.LENGTH_SHORT).show();
                    else {
                        dialog.dismiss();
                        your_profile(book_name.getText().toString().trim(), "");
                    }

                }
            });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("Position is", Integer.toString(position));
            Toast.makeText(HomeActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    public void setArticles(){

        article[0]=new Article("Common Excuses Why Developers Don’t Test Their Software","Insights through - Sandeep", "<p>Once upon a time I hated testing software. It wasn’t important to me. I didn’t see the purpose. It seemed like a huge waste of everybody’s time and money.\n" +
                "Throughout my career I was never taught how, or why, I should be testing my software. I made lots of excuses for not wanting to learn. I spoke to many developers who also made excuses for not wanting to learn. They still make the same excuses today. I eventually learned, they didn’t.\n" +
                "Throughout my time working with others I’ve came across many different opposing views to software testing </p>" +
                "<p>I’m going to take a quote from this StackExchange answer, because it makes sense:<br>" +
                "1. Test the common case of everything you can. This will tell you when that code breaks after you make some change (which is, in my opinion, the single greatest benefit of automated unit testing).<br>" +
                "2. Test the edge cases of a few unusually complex code that you think will probably have errors.<br>" +
                "3. Whenever you find a bug, write a test case to cover it before fixing it<br>" +
                "4. Add edge-case tests to less critical code whenever someone has time to kill.</p>"
        );

        article[1]=new Article("Getting your head around chrome headless","A slew of automation 'stuff' by Ray", "" +
                "<h4>So here’s what you need to know:<h4>" +
                "<p>A headless browser means you don’t see it when you start it, it’s all in memory — and it also implies user actions are automated.</p>" +
                "<h4>Uses of automation:<h4>" +
                "<p>1. QA tests<br>" +
                "2. Scraping<br>" +
                "3. Pre-rendering single-page apps.<br>" +
                "<h4>Uses of headless:</h4>" +
                "Less resource intensive.<br>" +
                "Great for build-systems running tests before a deploy.<br>" +
                "Can run in many more server environments, like Lambda.<br>" +
                "Is this the first headless automated browser? No, phantomJS has been the goto browser like this, but the main contributor almost immediately stepped down when he heard about Chrome’s new headless feature . Turns out its hard to maintain an ENTIRE browser.</p>");

        article[2]=new Article("Why and How to implement Web Notification API", "A Social Thought by yours truly Hari", "" +
                "Most of the websites nowadays use Web Notification API be it Facebook, Twitter, Flowdock, Slack …. you name it. Ever wondered how does that happen? <br>" +
                "<p>First and foremost, we need to check if our browser supports Notification or not. You might be wondering everyone does, why should I check it? This is web, you have n number of browsers in the market for each platform. We cannot know who supports it who doesn’t for such a huge number of browsers. So prevention is better than cure and hence, I suggest we check it before your users go bananas on different browsers. Also, the best way to test these codes is to keep typing everything on browser console right away.</p>");
    }





























    public class CustomAdapter extends ArrayAdapter<DrawerListItems> {
        Context context;

        public CustomAdapter(@NonNull Context context, @NonNull List<DrawerListItems> objects) {
            super(context,0, objects);
            context= context;
        }


        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DrawerListItems item = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_layout_card, parent, false);
            }
            final TextView text_msg=(TextView) convertView.findViewById(R.id.msg);
            text_msg.setText(item.getMsg());

            CardView cardView= (CardView) convertView.findViewById(R.id.card_view_list);
            cardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(listView);
                    Handler handler= new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(text_msg.getText().toString().equals("e-Buy"))
                                e_buy();
                            else if (text_msg.getText().toString().equals("Your Profile")){
                                your_profile("","");
                            }else if(text_msg.getText().toString().equals("Write an Article")){
                                Intent intent= new Intent(HomeActivity.this, ArticleWriteActivity.class);
                                intent.putExtra("write",true);
                                startActivity(intent);
                            }else if(text_msg.getText().toString().equals("Your Articles")){
                                if(getApplicationContext().getSharedPreferences("art_count",MODE_PRIVATE).getInt("count",0)==0)
                                    Toast.makeText(HomeActivity.this, "No Articles to Show", Toast.LENGTH_SHORT).show();
                                else{
                                    Intent intent= new Intent(HomeActivity.this, YourArticlesActivity.class);
                                    startActivity(intent);
                                }

                            }
                        }
                    }, 300);
                }
            });
            return convertView;
        }


    }
}
























