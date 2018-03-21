package com.alexstyl.specialdates.people;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.images.ImageLoader;

class PeopleViewHolder extends RecyclerView.ViewHolder {

    private final ImageView avatarView;
    private final TextView nameView;
    private final ImageLoader imageLoader;

    PeopleViewHolder(View rowView, ImageLoader imageLoader, ImageView avatarView, TextView nameView) {
        super(rowView);
        this.imageLoader = imageLoader;
        this.avatarView = avatarView;
        this.nameView = nameView;
    }

    public void bind(final PersonViewModel viewModel, final PeopleViewHolderListener listener) {
        nameView.setText(viewModel.getPersonName());
//        avatarView.setCircleColorVariant((int) viewModel.getPersonId());
//        avatarView.setLetter(viewModel.getPersonName(), true);
        imageLoader.load(viewModel.getAvatarURI())
                .asCircle()
                .into(avatarView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPersonClicked(viewModel.getContact(), avatarView);
            }
        });
    }
}
