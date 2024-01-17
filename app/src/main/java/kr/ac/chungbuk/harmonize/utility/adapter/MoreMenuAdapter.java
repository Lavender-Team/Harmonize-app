package kr.ac.chungbuk.harmonize.utility.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.uicomponent.MoreMenuItemView;

public class MoreMenuAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> menus = new ArrayList<>();

    public MoreMenuAdapter(Context context) {
        this.context = context;
        String[] menuStrings = context.getResources().getStringArray(R.array.more_menu);
        for (int i = 0; i < menuStrings.length; i++)
            menus.add(menuStrings[i]);
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoreMenuItemView view = new MoreMenuItemView(context);
        view.setMenuName(menus.get(position));
        return view;
    }
}
