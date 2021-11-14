package com.example.a110407_app.ui.tree;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a110407_app.Model.MoodAnalysisLinechart;
import com.example.a110407_app.Model.MoodTree;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.FirstFragment;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tree extends Fragment {

    private TreeViewModel mViewModel;
    private OnButtonClick onButtonClick;
    private Button btnCreateTree;
    private ImageView tree;
    APIService ourAPIService;
    String userToken;

    public static tree newInstance() {
        return new tree();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tree_fragment2, container, false);
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }
    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }
    public interface OnButtonClick{
        public void onClick(View view);
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(TreeViewModel.class);
//        // TODO: Use the ViewModel
//
////        btnTreeGenerate=getActivity().findViewById(R.id.btnTreeGenerateInTree);
//
////        btnTreeGenerate.setOnClickListener(View.OnClickListener);
//    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        tree = getActivity().findViewById(R.id.tree);
        btnCreateTree = getActivity().findViewById(R.id.btnCreateTree);
        ourAPIService = RetrofitManager.getInstance().getAPI();
        Intent intent = getActivity().getIntent();
        userToken = intent.getStringExtra("userToken");
        String start = getArguments().getString("start");
        String end = getArguments().getString("end");
        System.out.println(start);
        System.out.println(end);
        MoodTree moodTree = new MoodTree(start,end);
        Call<MoodTree> callMoodTree = ourAPIService.postMoodTree("bearer " + userToken, moodTree);
        super.onViewCreated(view, savedInstanceState);
        System.out.println("你成功跳轉了喔!");
        btnCreateTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click Tree", Toast.LENGTH_LONG).show();
                callMoodTree.enqueue(new Callback<MoodTree>() {
                    @Override
                    public void onResponse(Call<MoodTree> call, Response<MoodTree> response) {
                        try {
                            String result = response.message();
                            String tree_image_url = response.body().getImage_url();
                            System.out.println(result);
                            System.out.println(tree_image_url);
                            String test = "http://server.gywang.io/8084/" + tree_image_url;
                            System.out.println("TEST:" + test);
                            Picasso.get().load("http://server.gywang.io:8084/" + tree_image_url ).fit().centerInside().into(tree);
                        }catch (Exception e){
                            System.out.println(e);
                            System.out.println("回應文字雲失敗");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoodTree> call, Throwable t) {

                    }
                });
            }
        });

        //透過getArgument()來取得傳遞過來的參數


//        String a = savedInstanceState.getString("abc");
//        System.out.println(a);
//        btnTreeGenerate=getActivity().findViewById(R.id.btnTreeGenerateInTree);
//        btnTreeGenerate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(getActivity());
//            }
//        });
//        view.findViewById(R.id.btnTreeGenerateInTree).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                System.out.println("Hello 你按下了按鈕");
////                NavHostFragment.findNavController(tree.this)
////                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

}