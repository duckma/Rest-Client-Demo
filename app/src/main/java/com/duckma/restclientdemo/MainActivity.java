package com.duckma.restclientdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.duckma.restclientdemo.adapters.ContentAdapter;
import com.duckma.restclientdemo.models.Content;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        GridLayoutManager gridLM= new GridLayoutManager(this,2);
        mMainRecyclerView.setLayoutManager(gridLM);

        pupulateContentsArray();
        mAdapter = new ContentAdapter(mContentsArray);
        mMainRecyclerView.setAdapter(mAdapter);
    }

    private void pupulateContentsArray() {
        mLoadingFrame.setVisibility(View.VISIBLE);

        mTvTitle.setText("Star Wars Characters");

        Content c = new Content();
        c.setTitle("Darth Vader");
        c.setImage("http://megaicons.net/static/img/icons_sizes/286/2125/128/darth-vader-icon.png");
        mContentsArray.add(c);

        Content c1 = new Content();
        c1.setTitle("Clone");
        c1.setImage("http://megaicons.net/static/img/icons_sizes/286/2125/128/clone-old-icon.png");
        mContentsArray.add(c1);
        mLoadingFrame.setVisibility(View.INVISIBLE);
    }
}
