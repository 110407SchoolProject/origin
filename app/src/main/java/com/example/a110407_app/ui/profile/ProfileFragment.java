package com.example.a110407_app.ui.profile;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a110407_app.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private ImageView image;
    private TextView name, username, email,birthday;


    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 必須先呼叫getView()取得程式畫面物件，然後才能呼叫它的
        // findViewById()取得介面物件
        image = (ImageView) getView().findViewById(R.id.profileImage);
        image.setImageResource(R.drawable.ic_menu_camera);
        name = (TextView) getView().findViewById(R.id.profileName);
        name.setText("Gary Wang");
        username = (TextView) getView().findViewById(R.id.profileUsername);
        username.setText("guanyu0827");
        email = (TextView) getView().findViewById(R.id.profileEmail);
        email.setText("garywang0827@gmail.com");
        birthday = (TextView) getView().findViewById(R.id.profileBirhtday);
        birthday.setText("20000827");


    }


}