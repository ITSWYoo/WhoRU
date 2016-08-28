package com.yoo.ymh.whoru.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoo.ymh.whoru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    public static InfoFragment newInstance()
    {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        infoFragment.setArguments(args);
        return infoFragment;
    }


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            Log.e("infoFragment","resume");
        }
    }

}
