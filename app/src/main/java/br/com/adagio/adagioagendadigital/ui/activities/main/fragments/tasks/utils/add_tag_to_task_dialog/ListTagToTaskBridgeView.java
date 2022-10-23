package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import br.com.adagio.adagioagendadigital.data.tag.TagDAO;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.TagStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.adapter.ListTagAdapter;

public class ListTagToTaskBridgeView{
    private final TagDAO tagDAO ;
    private final ListTagToTaskAdapter listTagToTaskAdapter;
    private final Context context;

    public ListTagToTaskBridgeView(Context context){
        this.context = context;
        this.tagDAO = TagDAO.getInstance(context);
        this.listTagToTaskAdapter = new ListTagToTaskAdapter(context);
    }

    public void updateList(int limit,int offset){
        listTagToTaskAdapter.update(tagDAO.list(limit,offset));
    }

    public void insert(Tag t){
        tagDAO.save(t);
        updateListAux();
    }

    public Tag get(int position){
        return listTagToTaskAdapter.getItem(position);
    }

    public void delete(int position){
        long id = listTagToTaskAdapter.getItemId(position);
        Log.i("DELETE", "delete: "+id);
        tagDAO.delete(id);
        updateListAux();
    }

    private void updateListAux(){
        updateList(TagStaticValues.LIMIT_LIST,
                TagStaticValues.OFFSET_LIST);
    }

    public void configureAdapter(ListView tagsList,AddTagToTaskDialog fragment){
        tagsList.setAdapter(listTagToTaskAdapter);
        listTagToTaskAdapter.setParentFragment(fragment);
    }

    public void update(Tag tag, int id) {
        tagDAO.update(id, tag);
        updateListAux();
    }
}
