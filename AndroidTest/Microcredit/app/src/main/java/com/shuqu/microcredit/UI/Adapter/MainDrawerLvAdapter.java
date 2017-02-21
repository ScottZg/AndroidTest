package com.shuqu.microcredit.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuqu.microcredit.Model.MainDrawerOptionItem;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.MenuIconMap;

import java.util.List;

/**
 * Created by wuxin on 16/9/11.
 */
public class MainDrawerLvAdapter extends BaseAdapter{
    private Context mContext;
    private List<MainDrawerOptionItem> mDatas;
    public MainDrawerLvAdapter(Context context, List<MainDrawerOptionItem> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public void setData(List<MainDrawerOptionItem> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public MainDrawerOptionItem getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_drawer_option, parent, false);
            holder.tvOptionName = (TextView) convertView.findViewById(R.id.tv_option_name);
            holder.ivOption = (ImageView) convertView.findViewById(R.id.iv_option);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MainDrawerOptionItem optionItem = mDatas.get(position);

        holder.tvOptionName.setText(optionItem.mTitle);
        holder.tvOptionName.setTextColor(Color.parseColor(optionItem.mTitleColorDefault));

        holder.ivOption.setImageDrawable(mContext.getResources().getDrawable(MenuIconMap.getMenuIcon(optionItem.mIconActive)));

        return convertView;
    }

    class ViewHolder {
        TextView tvOptionName;
        ImageView ivOption;
    }
}
