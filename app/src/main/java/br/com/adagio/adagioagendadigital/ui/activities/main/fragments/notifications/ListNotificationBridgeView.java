package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications;

import android.content.Context;
import android.widget.ListView;

import java.util.List;

import br.com.adagio.adagioagendadigital.data.notification.NotificationDAO;
import br.com.adagio.adagioagendadigital.models.entities.Notification;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.adapter.NotificationAdapter;

public class ListNotificationBridgeView {
    private final NotificationDAO notificationDAO;
    private final NotificationAdapter notificationAdapter;
    private final Context context;

    public ListNotificationBridgeView(Context context) {
        this.context = context;
        this.notificationDAO = NotificationDAO.getInstance(context);
        this.notificationAdapter  =  new NotificationAdapter(context);
    }

    public void insert(Notification n){
        notificationDAO.save(n);
        updateList();
    }

    public void deletAll(){
        notificationDAO.deleteAll();
        System.out.println("morri muito2");
        updateList();
    }

    public void vereficarNumberNotification(){
        notificationDAO.deleteFirst();
    }

    public Notification get(int position) {
        return (Notification) notificationAdapter.getItem(position);
    }

    public void configureAdapter(ListView notificationList){
        notificationList.setAdapter(notificationAdapter);
        updateList();
    }

    public void updateList() {
        List<Notification> notifications = notificationDAO.list();
        notificationAdapter.setItems(notifications);
    }
}
