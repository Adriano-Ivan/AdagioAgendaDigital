package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.AddTagToTaskDialog;

public class TasksOfDayDialogAdapter  extends BaseAdapter {

    private final List<TaskDtoRead> tasks = new ArrayList<>();
    private final Context context;
    private HomeTodayDialog parentFragment;

    public TasksOfDayDialogAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public TaskDtoRead getItem(int position) {
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
        TextView name = generatedView.findViewById(R.id.task_of_day_item_text);
        name.setText(task.getDescription());

    }

    private View returnTaskView(ViewGroup viewGroup){
        return LayoutInflater.from(context)
                .inflate(R.layout.task_of_day_item,viewGroup,false);
    }

    public void update(List<TaskDtoRead> tasks){
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void setParentFragment(HomeTodayDialog fragment){
        parentFragment=fragment;
    }
}


