package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Notification;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;

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
//        TextView menssage = generatedView.findViewById(R.id.item_notification_message);
//        menssage.setText(notification.getMessage());
        TextView item_notification_message_one = generatedView.findViewById(R.id.item_notification_message_one);
        item_notification_message_one.setText(R.string.notification_message_one);

        TextView item_notification_message_two = generatedView.findViewById(R.id.item_notification_message_two);
        String taskName = notification.getMessage();
        taskName = taskName.substring(9);
        taskName = taskName.split(" ")[0];
        taskName = taskName.toUpperCase();
        item_notification_message_two.setText(taskName);

        TextView item_notification_message_three = generatedView.findViewById(R.id.item_notification_message_three);
        item_notification_message_three.setText(R.string.notification_message_three);

        TextView priority = generatedView.findViewById(R.id.item_notification_priority);
        priority.setText(notification.getPriority_name());

        CardView cardViewIndicator = generatedView.findViewById(R.id.notifications_list_priority);
        cardViewIndicator.setCardBackgroundColor(returnColor(notification));

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

    private int returnColor(Notification notification){
        if(notification.getPriority_name().equals(Priorities.HIGH.getValue())){
            return context.getColor(R.color.adagio_yellow);
        } else if(notification.getPriority_name().equals(Priorities.LOW.getValue())){
            return context.getColor(R.color.adagio_gray);
        } else if(notification.getPriority_name().equals(Priorities.CRITICAL.getValue())){
            return context.getColor(R.color.adagio_red);
        }

        return context.getColor(R.color.adagio_blue);
    }
}
