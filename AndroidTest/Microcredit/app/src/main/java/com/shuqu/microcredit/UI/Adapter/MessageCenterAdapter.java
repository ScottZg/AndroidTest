package com.shuqu.microcredit.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuqu.microcredit.Model.MessageCenterEntity;
import com.shuqu.microcredit.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wuxin on 16/9/12.
 */
public class MessageCenterAdapter extends BaseAdapter {

    private Context mContext;
    private List<MessageCenterEntity> mDatas;
    private LayoutInflater mInflater;

    public MessageCenterAdapter(Context context, List<MessageCenterEntity> list) {
        this.mContext = context;
        this.mDatas = list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public MessageCenterEntity getItem(int position) {
        return mDatas.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_center, parent, false);
            holder.ivMessageTip = (ImageView) convertView.findViewById(R.id.iv_message_tip);
            holder.tvMessageTip = (TextView) convertView.findViewById(R.id.tv_message_tip);
            holder.tvMessageTime = (TextView) convertView.findViewById(R.id.tv_message_time);
            holder.tvMessageContent = (TextView) convertView.findViewById(R.id.tv_message_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MessageCenterEntity entity = mDatas.get(position);

        holder.tvMessageTip.setText(entity.getMessageTip());
        holder.tvMessageTime.setText(entity.getMessageTime());
        holder.tvMessageContent.setText(entity.getMessageContent());


        return convertView;
    }

    class ViewHolder {
        ImageView ivMessageTip;
        TextView tvMessageTip;
        TextView tvMessageTime;
        TextView tvMessageContent;
    }
}
