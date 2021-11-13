package com.example.a110407_app.ui.tree;

import androidx.lifecycle.ViewModelProvider;

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

import com.example.a110407_app.R;
import com.example.a110407_app.ui.FirstFragment;

public class tree extends Fragment {

    private TreeViewModel mViewModel;

    private OnButtonClick onButtonClick;
//    private Button btnTreeGenerate;

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
        super.onViewCreated(view, savedInstanceState);

        System.out.println("你成功跳轉了喔!");
        //透過getArgument()來取得傳遞過來的參數
        String startDate = getArguments().getString("startDate");
        String endDate = getArguments().getString("endDate");

        System.out.println(startDate);
        System.out.println(endDate);
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