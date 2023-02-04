package br.com.adagio.adagioagendadigital.ui.activities.main;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.notification.NotificationDAO;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Notification;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.ListNotificationBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.NotificationsFragment;


@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationService extends Service {

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private ListNotificationBridgeView listNotificationBridgeView;
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private Notification notification;
    private NotificationDAO notificationDAO;
    private TaskDAO Tdao;

    private final String CHANNEL_ID = "primary_channel_id";
    private final String CHANNEL_ID2 = "my_channel_02";
    private final String KEY_GROUP = "KEY";
    private  int NOTIFICATION_ID = 001;
    private  int NOTIFICATION_ID2 = 1000;
    int SUMMARY_ID = 0;
    public  int x = 0;



    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {

        listNotificationBridgeView = new ListNotificationBridgeView(this);
        Tdao =  TaskDAO.getInstance(this);
        notification(NOTIFICATION_ID2);
        start();
        start2();
        return START_STICKY;
    }



    private void createNotificationChannel(){
        CharSequence name = "Notification";
        String description = "Channel Notification";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationChannel2(){
        CharSequence name = "Notification2";
        String description = "Channel Notification2";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID2, name, importance);
            channel2.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel2);
        }
    }

    private  void notification( int NOTIFICATION_ID2){
        createNotificationChannel2();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        android.app.Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID2)
                .setSmallIcon(R.drawable.ic_stat_adagio)
                .setColor(Color.rgb(124,58,255))
                .setContentTitle("executando")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID2,builder);
    }

    private  void notificationTaskStarted(String nome, String prioridade, int x, int id){
        int tanks_id = id;
        LocalDateTime dataAtual = LocalDateTime.now();
        String message = getResources().getString(R.string.the_task)+nome+getResources().getString(R.string.has_started);
        System.out.println(message);
        String priority_name = prioridade ;
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_adagio)
                .setColor(Color.rgb(124,58,255))
                .setContentTitle(message)
                .setContentText(priority_name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);


        salvar(tanks_id, dataAtual, message, priority_name);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(x, builder.build());
    }

    private  void notificationTask15minToFinish(String nome, String prioridade, int x, int id){
        int tanks_id = id;
        LocalDateTime dataAtual = LocalDateTime.now();
        String message =getResources().getString(R.string.missing_15)+nome+getResources().getString(R.string.is_finish) ;
        String priority_name = prioridade ;
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_adagio)
                .setColor(Color.rgb(124,58,255))
                .setContentTitle(message)
                .setContentText(priority_name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        salvar(tanks_id, dataAtual, message, priority_name);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(x, builder.build());
    }


    private  void notificationTaskFinished(String nome, String prioridade, int x, int id){
        int tanks_id = id;
        LocalDateTime dataAtual = LocalDateTime.now();
        String message = getResources().getString(R.string.is_finish2)+nome+getResources().getString(R.string.is_finish3);
        String priority_name = prioridade ;
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_adagio)
                .setColor(Color.rgb(124,58,255))
                .setContentTitle(message+"foi finalizada")
                .setContentText(priority_name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        salvar(tanks_id, dataAtual, message, priority_name);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(x, builder.build());
    }

    public void salvar(int task_id, LocalDateTime emitted_at, String message, String priority_name){
        notification = new Notification(task_id,emitted_at,message,priority_name);
        listNotificationBridgeView.insert(notification);
        System.out.println(notification.getMessage());
    }


    public String translate(String priority){
        String priorityName = null;
        if(priority.equals(Priorities.LOW.getValue())){
            priorityName = getResources().getString(R.string.low);
        } else if(priority.equals(Priorities.AVERAGE.getValue())){
            priorityName = getResources().getString(R.string.average);
        } else if(priority.equals(Priorities.HIGH.getValue())){
            priorityName = getResources().getString(R.string.high);
        } else if(priority.equals(Priorities.CRITICAL.getValue())){
            priorityName = getResources().getString(R.string.critical);
        }

        return  priorityName;
    }



    public void start() {
        Timer t = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                getTaskNotification();
                System.out.println("entrei aqui");
            }
        };
        t.schedule(tk,new Date(),60000);
    }

    public void start2() {
        Timer t = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
            }
        };
        t.schedule(tk,new Date(),1000);
    }

    public int tratar_minutos(int minutos){
        int auxmin = minutos - 15;
        int resto = minutos - 15;
        int resultado;
        if(auxmin <0){
            auxmin  = 60;
            resultado = auxmin  + resto;
            System.out.println(resultado);
            return resultado;
        }
        return auxmin;
    }

    public int tratar_hora(int hora, int min){
        int auxmin = min - 15;
        if( auxmin < 0){
            hora--;
            if(hora< 0){
                hora = 23;
                return hora;
            }
            return hora;
        }
        return hora;
    }

    private void getTaskNotification(){
        List<TaskDtoRead> taks = Tdao.listAllFromToday(LocalDateTime.now().getHour());
        for(TaskDtoRead T : taks){
            SystemClock.sleep(2000);
            if(T.getInitialMoment().getMinute() == LocalDateTime.now().getMinute() && T.getInitialMoment().getHour()  == LocalDateTime.now().getHour()){
                notificationTaskStarted(T.getDescription(),translate(T.getPriorityName()),x, T.getId());
                x++;
            }else
            if(tratar_minutos(T.getLimitMoment().getMinute()) == LocalDateTime.now().getMinute() && tratar_hora(T.getLimitMoment().getHour(),T.getLimitMoment().getMinute() )  == LocalDateTime.now().getHour() && T.isFinished() == false){
                notificationTask15minToFinish(T.getDescription(),translate(T.getPriorityName()),x,T.getId());
                x++;
            }else
            if(T.getLimitMoment().getMinute() == LocalDateTime.now().getMinute() && T.getLimitMoment().getHour()  == LocalDateTime.now().getHour() && T.isFinished() == false){
                notificationTaskFinished(T.getDescription(),translate(T.getPriorityName()),x,T.getId());
                x++;
            }
        }
    }

}
