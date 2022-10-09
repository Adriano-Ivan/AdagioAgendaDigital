package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;

public class ListTaskAdapter extends BaseAdapter {

    private final List<TaskDtoRead> tasks = new ArrayList<>();
    private final Context context;

    public ListTaskAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TaskDtoRead task = tasks.get(i);

        View genereatedView = returnTaskView(viewGroup);
        defineTaskInformation(task, genereatedView);

        return genereatedView;
    }

    private void defineTaskInformation(TaskDtoRead task, View generatedView){
        TextView description = generatedView.findViewById(R.id.item_task_description);
        description.setText(task.getDescription());

        TextView initialMoment =generatedView.findViewById(R.id.item_task_initial_moment);
        initialMoment.setText(task.getInitialMoment().toString());

        TextView limitMoment =generatedView.findViewById(R.id.item_task_limit_moment);
        limitMoment.setText(task.getLimitMoment().toString());
    }

    private View returnTaskView(ViewGroup viewGroup){
        return LayoutInflater.from(context)
                .inflate(R.layout.task_item,viewGroup,false);
    }

    public void update(List<TaskDtoRead> tasks){
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }
}
