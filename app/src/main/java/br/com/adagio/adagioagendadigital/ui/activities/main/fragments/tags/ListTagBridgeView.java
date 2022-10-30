package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import br.com.adagio.adagioagendadigital.data.tag.TagDAO;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.adapter.ListTagAdapter;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter.ListTaskAdapter;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.TagsToTaskStaticValues;

public class ListTagBridgeView {
    private final TagDAO tagDAO ;
    private final ListTagAdapter listTagAdapter;
    private final Context context;

    public ListTagBridgeView(Context context){
        this.context = context;
        this.tagDAO = TagDAO.getInstance(context);
        this.listTagAdapter = new ListTagAdapter(context);
    }

    public void updateList(int limit,int offset){
        if(offset >= 0){
            listTagAdapter.update(tagDAO.list(limit,offset));
            TagStaticValues.setOffsetList(offset);
        }
    }

    public void insert(Tag t){
        tagDAO.save(t);
        updateListAux();
    }

    public Tag get(int position){
        return listTagAdapter.getItem(position);
    }

    public void delete(int position){
        long id = listTagAdapter.getItemId(position);
        Log.i("DELETE", "delete: "+id);
        tagDAO.delete(id);
        updateListAux();
    }

    private void updateListAux(){
        updateList(TagStaticValues.LIMIT_LIST,
                TagStaticValues.OFFSET_LIST);
    }

    public void configureAdapter(ListView tagsList){
        tagsList.setAdapter(listTagAdapter);
    }

    public void update(Tag tag, int id) {
        tagDAO.update(id, tag);
        updateListAux();
    }

    public boolean thereArePreviousOrNextPage() {

        return thereIsPreviousPage() || thereIsNextPage();
    }

    public boolean thereIsNextPage() {
        if(tagDAO.getQuantityOfTags() >
                TagStaticValues.NEXT_POSSIBLE_QUANTITY ){
            return true;
        }

        return false;
    }

    public boolean thereIsPreviousPage() {
        if(TagStaticValues.OFFSET_LIST == 0){
            return false;
        }

        return true;
    }
}
