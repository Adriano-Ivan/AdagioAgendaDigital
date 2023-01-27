package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.adagio.adagioagendadigital.R;


public class NotificationsFragment extends Fragment {
    private View rootView;
    private ListNotificationBridgeView listNotificationBridgeView;
    private ListView listNotification;


    public NotificationsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView =  inflater.inflate(R.layout.fragment_notifications, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAttributes();
    }

    private void setAttributes(){
        defineViews();
        configureAdapter();
        updateNotificationList();
    }


   private void defineViews(){
        listNotification = rootView.findViewById(R.id.fragment_notification_list_notification);
    }


    public void configureAdapter() {
        listNotificationBridgeView = new ListNotificationBridgeView(getActivity());
        listNotificationBridgeView.configureAdapter(listNotification);
        registerForContextMenu(listNotification);
    }

    private  void updateNotificationList(){
        listNotificationBridgeView.updateList();

    }



}