package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.entities.Notification;

public class NotificationAdapter extends BaseAdapter {
   private final Context context;
   private List<Notification> notifications = new ArrayList<>();

    public NotificationAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int i) {
        return notifications.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notifications.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Notification notification = notifications.get(i);
        //System.out.println(notification.getId());

        View genereatedView = returnNotificationView(viewGroup);
        defineNotificationInformation(notification, genereatedView);

        return genereatedView;
    }

    private void defineNotificationInformation(Notification notification, View generatedView){
        TextView menssage = generatedView.findViewById(R.id.item_notification_message);
        menssage.setText(notification.getMessage());

        TextView priority = generatedView.findViewById(R.id.item_notification_priority);
        priority.setText(notification.getPriority_name());

        TextView emited_at = generatedView.findViewById(R.id.item_notification_emited_at);
        String dia;
        String mes;
        String ano;
        String hora;
        String minuto;

        String menssagem_corrigida;

        dia = String.valueOf(notification.getEmitted_at().getDayOfMonth());
        mes = convertToDefoultDay(notification.getEmitted_at().getMonth().getValue());
        ano = String.valueOf(notification.getEmitted_at().getYear());
        hora = String.valueOf(notification.getEmitted_at().getHour());
        minuto = convertToDefoultDay(notification.getEmitted_at().getMinute());

        menssagem_corrigida = dia+"/"+mes+"/"+ano+" "+hora+":"+minuto;

        emited_at.setText(menssagem_corrigida);
    }

    private String convertToDefoultDay(int dayOfMonth) {
        if(Integer.toString(dayOfMonth).length() == 1){
            return "0"+dayOfMonth;
        }
        return Integer.toString(dayOfMonth);
    }

    private View returnNotificationView(ViewGroup viewGroup){
        return LayoutInflater.from(context)
                .inflate(R.layout.notifications_item,viewGroup,false);
    }

    public void setItems(List<Notification> notifications) {
        this.notifications.clear();
        this.notifications.addAll(notifications);
        notifyDataSetChanged();
    }
}
