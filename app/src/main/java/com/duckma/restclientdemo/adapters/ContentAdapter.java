package com.duckma.restclientdemo.adapters;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duckma.restclientdemo.R;
import com.duckma.restclientdemo.models.Content;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Copyright © 2016 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 12/01/16.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.CharacterViewHolder> {
    List<Content> characters;

    public ContentAdapter(List<Content> characters) {
        this.characters = characters;
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new CharacterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder characterViewHolder, int i) {
        characterViewHolder.swTitle.setText(characters.get(i).getTitle());
        characterViewHolder.swImage.setImageURI(Uri.parse(characters.get(i).getImage()));
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView swTitle;
        SimpleDraweeView swImage;

        CharacterViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cvContent);
            swTitle = (TextView) itemView.findViewById(R.id.swTitle);
            swImage = (SimpleDraweeView) itemView.findViewById(R.id.swImage);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
