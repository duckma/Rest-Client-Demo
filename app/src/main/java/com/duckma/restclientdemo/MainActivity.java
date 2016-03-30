package com.duckma.restclientdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duckma.restclientdemo.adapters.ContentAdapter;
import com.duckma.restclientdemo.models.Content;
import com.duckma.restclientdemo.network.AppAPI;
import com.duckma.restclientdemo.network.models.ContentResponse;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<ContentResponse> {

    public static final int CACHE_SIZE = 10 * 1024 * 1024;
    public static final String CACHE_DIR = "httpCache";

    FrameLayout mLoadingFrame;
    RecyclerView mMainRecyclerView;
    TextView mTvTitle;
    ArrayList<Content> mContentsArray = new ArrayList<>();
    ContentAdapter mAdapter;
    private Realm mRealm;
    private AppAPI mAppApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);

        // Init Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();

        // Clear the realm from last time
        //Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        mRealm = Realm.getInstance(realmConfiguration);

        mLoadingFrame = (FrameLayout) findViewById(R.id.loadingFrame);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mMainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        GridLayoutManager gridLM = new GridLayoutManager(this, 2);
        mMainRecyclerView.setLayoutManager(gridLM);

        mAdapter = new ContentAdapter(mContentsArray);
        mMainRecyclerView.setAdapter(mAdapter);

        // create a converter compatible with Realm
        // GSON can parse the data.
        // Note there is a bug in GSON 2.5 that can cause it to StackOverflow when working with RealmObjects.
        // To work around this, use the ExclusionStrategy below or downgrade to 1.7.1
        // See more here: https://code.google.com/p/google-gson/issues/detail?id=440
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();


        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Cache cache = new Cache(new File(getCacheDir(), CACHE_DIR), CACHE_SIZE);
        okBuilder.cache(cache);

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder.addInterceptor(logInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okBuilder.build())
                .build();

        // prepare call in Retrofit
        mAppApi = retrofit.create(AppAPI.class);
        Call<ContentResponse> call = mAppApi.loadContents();
        call.enqueue(this);

        // if you want to execute a syncronous call just replace
        // call.enqueue(this) with call.execute()
        // remember that you have to perform this outside the main thread

        // to cancel a running request
        // call.cancel();
        // calls can only be used once but you can easily clone them
        //Call<ContentResponse> c = call.clone();
        //c.enqueue(this);

        mLoadingFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.act_sync:
                Call<ContentResponse> call = mAppApi.loadCachedContents();
                call.enqueue(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<ContentResponse> call, Response<ContentResponse> response) {
        mContentsArray.clear();

        if (response.isSuccessful()) {
            // Set Title
            String title = response.body().getTitle() + " " +
                    response.body().getType() + " - " +
                    response.body().getVersion();
            mTvTitle.setText(title);
            mContentsArray.addAll(response.body().getContents());

            // add content to the Relam DB
            // Open a transaction to store items into the realm
            // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
            mRealm.beginTransaction();
            mRealm.copyToRealm(mContentsArray);
            mRealm.commitTransaction();
            Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error) + " CODE:" + response.code(), Toast.LENGTH_LONG).show();
            RealmResults<Content> results = mRealm.where(Content.class).findAll();
            mContentsArray.addAll(results);
        }
        mAdapter.notifyDataSetChanged();
        mLoadingFrame.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<ContentResponse> call, Throwable t) {
        mLoadingFrame.setVisibility(View.INVISIBLE);
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
        RealmResults<Content> results = mRealm.where(Content.class).findAll();
        mContentsArray.addAll(results);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }
}
