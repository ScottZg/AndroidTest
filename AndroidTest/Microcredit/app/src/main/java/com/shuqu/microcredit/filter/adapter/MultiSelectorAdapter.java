package com.shuqu.microcredit.filter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.filter.entity.MultiSelectorEntity;
import com.shuqu.microcredit.filter.interfac.MultiSelecteAllListener;
import java.util.List;

/**
 * Created by wuxin on 16/7/26.
 */
public class MultiSelectorAdapter extends BaseAdapter {


    private List<MultiSelectorEntity> mFilterDatas;
    private Context mContext;
    private boolean isSelectAll;
    private MultiSelecteAllListener mMultiSelecteAllListener;

    public MultiSelectorAdapter(List<MultiSelectorEntity> list, Context context) {
        this.mFilterDatas = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mFilterDatas == null ? 0 : mFilterDatas.size();
    }

    @Override
    public MultiSelectorEntity getItem(int position) {
        return mFilterDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.filter_multi_selector, parent, false);
            holder.tvFilterItem = (TextView) convertView.findViewById(R.id.tv_filter_item);
            holder.imgSelect = (ImageView) convertView.findViewById(R.id.img_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MultiSelectorEntity entity = mFilterDatas.get(position);

        if(mMultiSelecteAllListener != null && !entity.isChecked()) {
            mMultiSelecteAllListener.isSelecteAll(false);
        }

        holder.tvFilterItem.setText(entity.getName());
        holder.imgSelect.setVisibility(entity.isChecked() ? View.VISIBLE : View.INVISIBLE);

        return convertView;
    }

    public void setSelectAll(boolean isSelect) {
        for (MultiSelectorEntity entity : mFilterDatas) {
            entity.setChecked(isSelect);
        }
        notifyDataSetChanged();
    }

    public void addSelectedAllListener(MultiSelecteAllListener multiSelecteAllListener) {
        this.mMultiSelecteAllListener = multiSelecteAllListener;
    }

    class ViewHolder {
        TextView tvFilterItem;
        ImageView imgSelect;
    }
}
