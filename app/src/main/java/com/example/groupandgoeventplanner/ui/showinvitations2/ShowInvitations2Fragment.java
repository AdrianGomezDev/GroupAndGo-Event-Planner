package com.example.groupandgoeventplanner.ui.showinvitations2;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupandgoeventplanner.R;

public class ShowInvitations2Fragment extends Fragment {

    private ShowInvitations2ViewModel mViewModel;

    public static ShowInvitations2Fragment newInstance() {
        return new ShowInvitations2Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_invitations2_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShowInvitations2ViewModel.class);
        // TODO: Use the ViewModel
    }

}
