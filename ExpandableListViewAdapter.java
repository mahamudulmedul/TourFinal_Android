package bd.org.bitm.mad.batch33.tourmate.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.org.bitm.mad.batch33.tourmate.R;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listheader;
    private Map<String, List<String>> hashChild;

    public ExpandableListViewAdapter(Context context, List<String> listheader, Map<String, List<String>> hashChild) {
        this.context = context;
        this.listheader = listheader;
        this.hashChild = hashChild;
    }

    @Override
    public int getGroupCount() {
        return listheader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return hashChild.get(listheader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listheader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return hashChild.get(listheader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.list_group,null);
        TextView textGroup = convertView.findViewById(R.id.parentTitle);
        textGroup.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String subTitle = (String) getChild(groupPosition,childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        TextView textSubtitle = convertView.findViewById(R.id.optional);
        textSubtitle.setText(subTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
