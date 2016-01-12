package com.duckma.restclientdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duckma.restclientdemo.adapters.ContentAdapter;
import com.duckma.restclientdemo.models.Content;
import com.duckma.restclientdemo.network.AppAPI;
import com.duckma.restclientdemo.network.models.ContentResponse;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<ContentResponse> {

    FrameLayout mLoadingFrame;
    RecyclerView mMainRecyclerView;
    TextView mTvTitle;
    ArrayList<Content> mContentsArray = new ArrayList<>();
    ContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);

        mLoadingFrame = (FrameLayout) findViewById(R.id.loadingFrame);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mMainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        GridLayoutManager gridLM = new GridLayoutManager(this, 2);
        mMainRecyclerView.setLayoutManager(gridLM);

        mAdapter = new ContentAdapter(mContentsArray);
        mMainRecyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // prepare call in Retrofit 2.0
        AppAPI appApi = retrofit.create(AppAPI.class);
        Call<ContentResponse> call = appApi.loadContents();
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
    public void onResponse(Response<ContentResponse> response) {
        // Set Title
        String title = response.body().getTitle() + " " +
                response.body().getType() + " - " +
                response.body().getVersion();
        mTvTitle.setText(title);

        mContentsArray.clear();
        mContentsArray.addAll(response.body().getContents());
        mAdapter.notifyDataSetChanged();
        mLoadingFrame.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Throwable t) {
        mLoadingFrame.setVisibility(View.INVISIBLE);
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}
