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
public class AlarmFragment extends Fragment {

    public static AlarmFragment newInstance()
    {
        AlarmFragment alarmFragment = new AlarmFragment();
        Bundle args = new Bundle();
        alarmFragment.setArguments(args);
        return alarmFragment;
    }

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            Log.e("alarmFragment","resume");
        }
    }

}
