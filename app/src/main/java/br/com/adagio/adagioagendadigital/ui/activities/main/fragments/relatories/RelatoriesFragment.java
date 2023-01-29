package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.entities.Priority;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.models.enums.RelatoriesTypes;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.utils.RelatoriesDatePicker;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.utils.RelatoriesTypePickerDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter.ListTaskAdapter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RelatoriesFragment extends Fragment implements View.OnClickListener, OnChartGestureListener {

    private View rootView;
    private PieChart pieChart;
    private BarChart barChart;
    private TextView finishedTasksCountTxt;
    private Button relatoryTypeBtn;

    private final TaskDAO taskDAO;
    private final Context context;
    private RelatoriesTypes relatoriesTypes;
    private String relatoriesCenter = "";
    private String day, month, year;
    private int chartPeriod;
    private LocalDate relatoriesDate;
    private RelatoriesDatePicker relatoriesDatePicker;

    private FloatingActionButton recordType;
    private FloatingActionButton dayRecord;
    private FloatingActionButton monthRecord;
    private FloatingActionButton yearRecord;

    private final Animation rotateOpen;
    private final Animation rotateClose;
    private final Animation fromBottom;
    private final Animation toBottom;
    private boolean clicked = false;

    public RelatoriesFragment(Context context) {
        this.context = context;
        this.taskDAO = TaskDAO.getInstance(context);
        this.rotateOpen = AnimationUtils.loadAnimation(this.context, R.anim.rotate_open_anim);
        this.rotateClose = AnimationUtils.loadAnimation(this.context, R.anim.rotate_close_anim);
        this.fromBottom = AnimationUtils.loadAnimation(this.context, R.anim.from_bottom_anim);
        this.toBottom = AnimationUtils.loadAnimation(this.context, R.anim.to_bottom_anim);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_relatories, container, false);
        relatoryTypeBtn = rootView.findViewById(R.id.relatoryTypeBtn);
        finishedTasksCountTxt = rootView.findViewById(R.id.finishedTasksCountTxt);

        //setters
        relatoriesDate = LocalDate.now();
        relatoriesTypes = RelatoriesTypes.FINISHED;
        chartPeriod = 0;
        relatoriesDatePicker = new RelatoriesDatePicker(this);

        //FAB buttons
        recordType = rootView.findViewById(R.id.fragment_relatories_fab_button_tag);
        dayRecord = rootView.findViewById(R.id.fragment_relatories_fab_button_day);
        monthRecord = rootView.findViewById(R.id.fragment_relatories_fab_button_month);
        yearRecord = rootView.findViewById(R.id.fragment_relatories_fab_button_year);


        //charts
        pieChart = rootView.findViewById(R.id.dailyChart);
        barChart = rootView.findViewById(R.id.barsChart);

        pieChart.setOnClickListener(this);
        recordType.setOnClickListener(this);
        dayRecord.setOnClickListener(this);
        monthRecord.setOnClickListener(this);
        yearRecord.setOnClickListener(this);
        pieChart.setOnChartGestureListener(this);
        relatoryTypeBtn.setOnClickListener(this);

        loadPieChartData(true, chartPeriod);
       return rootView;
    }

    public void loadPieChartData(Boolean isFromOpen, int period){
        this.day = "0";
        this.month = "0";
        this.year = "0";

        if (isFromOpen || period == 0){
            this.day = Integer.toString(relatoriesDate.getDayOfMonth());
            if (this.day.length() < 2)
                this.day = "0" + this.day;
            this.month = Integer.toString(relatoriesDate.getMonthValue());
            if (this.month.length() < 2)
                this.month = "0" + this.month;
            this.year = Integer.toString(relatoriesDate.getYear());
            setRelatoriesCenter(this.day + " de " + relatoriesDatePicker.getMonthString(this.month) + " de " + this.year);
        }
        else if (period == 1){
            this.month = Integer.toString(relatoriesDate.getMonthValue());
            if (this.month.length() < 2)
                this.month = "0" + this.month;
            this.year = Integer.toString(relatoriesDate.getYear());
            setRelatoriesCenter(relatoriesDatePicker.getMonthString(this.month) + " de " + this.year);
        }
        else {
            this.year = Integer.toString(relatoriesDate.getYear());
            setRelatoriesCenter(this.year);
        }

        if (this.relatoriesTypes == RelatoriesTypes.PRIORITIES){
            setByPriority(this.day, this.month, this.year);
        }
        if (this.relatoriesTypes == RelatoriesTypes.FINISHED){
            setByFinished(this.day, this.month, this.year);
        }
        setBarChartData();
        setupPieChart();
    }
    public void setBarChartData(){
        int low = 0;
        int medium = 0;
        int high = 0;
        int critical = 0;

        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        low = taskDAO.getQuantityOfTasksByPriorityAndFinished(this.day, this.month, this.year, Priorities.LOW);
        medium = taskDAO.getQuantityOfTasksByPriorityAndFinished(this.day, this.month, this.year, Priorities.AVERAGE);
        high = taskDAO.getQuantityOfTasksByPriorityAndFinished(this.day, this.month, this.year, Priorities.HIGH);
        critical = taskDAO.getQuantityOfTasksByPriorityAndFinished(this.day, this.month, this.year, Priorities.CRITICAL);

        barEntryArrayList.add(new BarEntry(0, low));
        colors.add(this.getContext().getColor(R.color.adagio_gray));
        labels.add("Baixa");
        barEntryArrayList.add(new BarEntry(1, medium));
        colors.add(this.getContext().getColor(R.color.adagio_blue));
        labels.add("Média");
        barEntryArrayList.add(new BarEntry(2, high));
        colors.add(this.getContext().getColor(R.color.adagio_yellow));
        labels.add("Alta");
        barEntryArrayList.add(new BarEntry(3, critical));
        colors.add(this.getContext().getColor(R.color.adagio_red));
        labels.add("Crítica");

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Prioridades");
        barDataSet.setColors(colors);

        Description description = new Description();
        description.setText("Tarefas finalizadas por prioridade.");
        barChart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setLabelRotationAngle(340);
        barChart.animateY(2000);
        barChart.invalidate();
    }

    public void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(0);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText(relatoriesCenter);
        pieChart.setCenterTextSize(18);

        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(12);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    public void setByPriority(String d, String m, String y){
//        this.chartTitle.setText("Por prioridades");
        ArrayList<PieEntry> entries = new ArrayList<>();
        int low;
        int medium;
        int high;
        int critical;
        low = taskDAO.getQuantityOfTasksByPriority(d, m, y, Priorities.LOW);
        medium = taskDAO.getQuantityOfTasksByPriority(d, m, y, Priorities.AVERAGE);
        high = taskDAO.getQuantityOfTasksByPriority(d, m, y, Priorities.HIGH);
        critical = taskDAO.getQuantityOfTasksByPriority(d, m, y, Priorities.CRITICAL);
        ArrayList<Integer> values = new ArrayList<>();
        Collections.addAll(values, low, medium, high, critical);

        int total = 0;
        for(int value : values){
            total += value;
        }
        if (total == 0)
            total = 1;
        float percLow = (low*10000)/total;
        float percMedium = (medium*10000)/total;
        float percHigh = (high*10000)/total;
        float percCritical = (critical*10000)/total;

        ArrayList<Integer> colors = new ArrayList<>();

        if (percLow > 0){
            entries.add(new PieEntry(percLow, "Baixa: " + low));
            colors.add(this.getContext().getColor(R.color.adagio_gray));
        }
        if (percMedium > 0){
            entries.add(new PieEntry(percMedium, "Média: " + medium));
            colors.add(this.getContext().getColor(R.color.adagio_blue));
        }
        if (percHigh > 0){
            entries.add(new PieEntry(percHigh, "Alta: " + high));
            colors.add(this.getContext().getColor(R.color.adagio_yellow));
        }
        if (percCritical > 0){
            entries.add(new PieEntry(percCritical, "Crítica: " + critical));
            colors.add(this.getContext().getColor(R.color.adagio_red));
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(6.5f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        pieChart.animateXY(600, 600);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void setByFinished(String d, String m, String y){
//        this.chartTitle.setText("Por finalização");
        ArrayList<PieEntry> entries = new ArrayList<>();
        int finished;
        int unfinished;

        finished = taskDAO.getQuantityOfTasksByFinished(d, m, y, true);
        unfinished =taskDAO.getQuantityOfTasksByFinished(d, m, y, false);

        ArrayList<Integer> values = new ArrayList<>();
        Collections.addAll(values, finished, unfinished);

        int total = 0;
        for(int value : values){
            total += value;
        }
        if (total == 0)
            total = 1;
        float percFinished = (finished*10000)/total;
        float percUnfinished = (unfinished*10000)/total;

        ArrayList<Integer> colors = new ArrayList<>();

        if (percFinished > 0){
            entries.add(new PieEntry(percFinished, "Finalizadas: " + finished));
            colors.add(this.getContext().getColor(R.color.adagio_blue));
        }
        if (percUnfinished > 0){
            entries.add(new PieEntry(percUnfinished, "Pendentes: " + unfinished));
            colors.add(this.getContext().getColor(R.color.adagio_gray));
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(6.5f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.animateXY(600, 600);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == recordType.getId()){
            onTypeButtonClicked();
            clicked = !clicked;
        }
        if (view.getId() == dayRecord.getId()){
            chartPeriod = 0;
            loadPieChartData(false, chartPeriod);
        }
        if (view.getId() == monthRecord.getId()){
            chartPeriod = 1;
            loadPieChartData(false, chartPeriod);
        }
        if (view.getId() == yearRecord.getId()){
            chartPeriod = 2;
            loadPieChartData(false, chartPeriod);
        }
        if (view.getId() == relatoryTypeBtn.getId()){
            RelatoriesTypePickerDialog dialog = new RelatoriesTypePickerDialog(this);
            dialog.show(getFragmentManager(), "RelatoriesTypePickerDialog");
        }

    }

    //controle de animações
    public void onTypeButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
    }

    public void setAnimation(boolean clicked) {

        if(!clicked){
            dayRecord.startAnimation(fromBottom);
            monthRecord.startAnimation(fromBottom);
            yearRecord.startAnimation(fromBottom);
            recordType.startAnimation(rotateOpen);
        }
        else{
            dayRecord.startAnimation(toBottom);
            monthRecord.startAnimation(toBottom);
            yearRecord.startAnimation(toBottom);
            recordType.startAnimation(rotateClose);
        }
    }

    public void setVisibility(boolean clicked) {

        if(!clicked){
            dayRecord.setVisibility(View.VISIBLE);
            monthRecord.setVisibility(View.VISIBLE);
            yearRecord.setVisibility(View.VISIBLE);
        }
        else{
            dayRecord.setVisibility(View.INVISIBLE);
            monthRecord.setVisibility(View.INVISIBLE);
            yearRecord.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        relatoriesDatePicker.startDatePicker();

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public RelatoriesTypes getRelatoriesTypes() {
        return relatoriesTypes;
    }

    public void setRelatoriesTypes(RelatoriesTypes relatoriesTypes) {
        this.relatoriesTypes = relatoriesTypes;
    }

    public String getRelatoriesCenter() {
        return relatoriesCenter;
    }

    public void setRelatoriesCenter(String relatoriesCenter) {
        this.relatoriesCenter = relatoriesCenter;
    }

    public int getChartPeriod() {
        return chartPeriod;
    }

    public void setChartPeriod(int chartPeriod) {
        this.chartPeriod = chartPeriod;
    }

    public LocalDate getRelatoriesDate() {
        return relatoriesDate;
    }

    public void setRelatoriesDate(LocalDate relatoriesDate) {
        this.relatoriesDate = relatoriesDate;
    }
}