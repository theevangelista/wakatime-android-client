package com.wakatime.androidclient.support;

import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.api.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
public class NavigationHeaderView {

    private final Picasso picasso;

    private TextView mUserEmailView;

    private TextView mUserNameView;

    private CircleImageView mUserPictureView;

    @Inject
    public NavigationHeaderView(Picasso picasso) {
        this.picasso = picasso;
    }

    public NavigationHeaderView on(View view) {
        this.mUserEmailView = (TextView) view.findViewById(R.id.text_view_user_email);
        this.mUserNameView = (TextView) view.findViewById(R.id.text_view_user_name);
        this.mUserPictureView = (CircleImageView) view.findViewById(R.id.image_profile);
        return this;
    }

    public void load(Realm realm) {
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            this.mUserNameView.setText(user.getFullName());
            this.mUserEmailView.setText(user.getEmail());
            picasso.load(user.getPhoto()).resize(R.dimen.profile_image,R.dimen.profile_image)
                    .into(mUserPictureView);
        }
    }
}
