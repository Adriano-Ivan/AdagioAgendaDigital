package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.entities.Priority;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter.ListTaskAdapter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RelatoriesFragment extends Fragment {

    private View rootView;
    private PieChart pieChart;
    private final TaskDAO taskDAO;
    private final Context context;

    public RelatoriesFragment(Context context) {
        this.context = context;
        this.taskDAO = TaskDAO.getInstance(context);
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
        pieChart = rootView.findViewById(R.id.dailyChart);
        setupPieChart();
        loadPieChartData();
        return rootView;
    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        //dividir em outro método byPriority
        //alterar o nome de medium para average
        int low;
        int medium;
        int high;
        int critical;
        low = taskDAO.getQuantityOfTasksBy(11, 2022, Priorities.LOW);
        medium = taskDAO.getQuantityOfTasksBy(11, 2022, Priorities.AVERAGE);
        high = taskDAO.getQuantityOfTasksBy(11, 2022, Priorities.HIGH);
        critical = taskDAO.getQuantityOfTasksBy(11, 2022, Priorities.CRITICAL);
        ArrayList<Integer> values = new ArrayList<>();
        Collections.addAll(values, low, medium, high, critical);
        //
        //toPercent
        int total = 0;
        for(int value : values){
            total += value;
        }
        float percLow = (low*10000)/total;
        float percMedium = (medium*10000)/total;
        float percHigh = (high*10000)/total;
        float percCritical = (critical*10000)/total;
        //

        ArrayList<Integer> cores = new ArrayList<>();

        if (percLow > 0){
            entries.add(new PieEntry(percLow, "Baixa: " + low));
            cores.add(Color.rgb(204, 204, 255));
        }
        if (percMedium > 0){
            entries.add(new PieEntry(percMedium, "Média: " + medium));
            cores.add(Color.rgb(102, 153, 255));
        }
        if (percHigh > 0){
            entries.add(new PieEntry(percHigh, "Alta: " + high));
            cores.add(Color.rgb(255, 255, 102));
        }
        if (percCritical > 0){
            entries.add(new PieEntry(percCritical, "Crítica: " + critical));
            cores.add(Color.rgb(255,0,0));
        }
        /*
        entries.add(new PieEntry(percLow, "Baixa: " + low));
        entries.add(new PieEntry(percMedium, "Média: " + medium));
        entries.add(new PieEntry(percHigh, "Alta: " + high));
        entries.add(new PieEntry(percCritical, "Crítica: " + critical));

        final int[] CORES = {Color.rgb(204, 204, 255), Color.rgb(102, 153, 255), Color.rgb(255, 255, 102), Color.rgb(255,0,0)};
        for (int cor: CORES){
            cores.add(cor);
        } */
        for (int cor: ColorTemplate.VORDIPLOM_COLORS){
            cores.add(cor);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Prioridades");
        dataSet.setColors(cores);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Distribuição de tarefas por prioridades em Novembro");
        pieChart.setCenterTextSize(18);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }
}