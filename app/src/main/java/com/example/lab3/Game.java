package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class Game extends Fragment {
    private Panel gamePanel;
    public static Game newInstance() {
        Game fragment = new Game();
        Bundle args = new Bundle();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game, container, false);
        FrameLayout panelContainer = view.findViewById(R.id.panelContainer);
        gamePanel = new Panel(getActivity());
        panelContainer.addView(gamePanel);
        startRound();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gamePanel != null) {
           gamePanel.stopGame();
        }
    }
    private void startRound() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(musicIntent);
    }
    @Override
    public void onPause() {
        super.onPause();
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().stopService(musicIntent);
    }
}