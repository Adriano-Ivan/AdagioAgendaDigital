package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.adapter;

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
import br.com.adagio.adagioagendadigital.models.entities.Tag;

public class ListTagAdapter extends BaseAdapter {

    private final List<Tag> tags = new ArrayList<>();
    private final Context context;

    public ListTagAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Tag getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {

        return tags.get(position).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Tag tag = tags.get(i);

        View genereatedView = returnTaskView(viewGroup);
        defineTaskInformation(tag, genereatedView);

        return genereatedView;
    }

    private void defineTaskInformation(Tag tag, View generatedView){
        TextView name = generatedView.findViewById(R.id.item_tag_name);
        name.setText(tag.getName());

    }

    private View returnTaskView(ViewGroup viewGroup){
        return LayoutInflater.from(context)
                .inflate(R.layout.tag_item,viewGroup,false);
    }

    public void update(List<Tag> tags){
        this.tags.clear();
        this.tags.addAll(tags);
        notifyDataSetChanged();
    }
}

