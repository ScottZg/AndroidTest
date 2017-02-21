package com.shuqu.microcredit.UI;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shuqu.microcredit.Model.MessageCenterEntity;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.Adapter.MessageCenterAdapter;
import com.shuqu.microcredit.Utils.NativeOnBacklistener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 16/9/12.
 */
public class MessageCenterActivity extends BaseActivity{
    ListView mLvMessage;
    ImageView leftBack;
    TextView tvTitle;
    private MessageCenterAdapter mAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message_center);

        mLvMessage = (ListView) findViewById(R.id.lv_message);
        leftBack = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        leftBack.setOnClickListener(new NativeOnBacklistener(this));
        tvTitle.setText(R.string.message_center_title);
    }

    @Override
    protected void initData() {
        List<MessageCenterEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageCenterEntity entity = new MessageCenterEntity();
            entity.setMessageContent("我是内容===" + i);
            entity.setMessageTime("我是时间===" + i);
            entity.setMessageTip("我是提示===" + i);
            list.add(entity);
        }
        mAdapter = new MessageCenterAdapter(this, list);
        mLvMessage.setAdapter(mAdapter);
    }
}
