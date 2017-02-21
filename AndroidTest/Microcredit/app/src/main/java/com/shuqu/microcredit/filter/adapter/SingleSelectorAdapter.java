package com.shuqu.microcredit.filter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.filter.entity.SingleSelectorEntity;
import java.util.List;

/**
 * Created by wuxin on 16/7/12.
 */
public class SingleSelectorAdapter extends BaseAdapter {

    private List<SingleSelectorEntity> mFilterDatas;
    private Context mContext;

    public SingleSelectorAdapter(List<SingleSelectorEntity> list, Context context){
        this.mFilterDatas = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mFilterDatas == null ? 0 : mFilterDatas.size();
    }

    @Override
    public SingleSelectorEntity getItem(int position) {
        return mFilterDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.filter_single_selector, parent, false);
            holder.tvFilterItem = (TextView) convertView.findViewById(R.id.tv_filter_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFilterItem.setText(mFilterDatas.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        TextView tvFilterItem;
    }
}
