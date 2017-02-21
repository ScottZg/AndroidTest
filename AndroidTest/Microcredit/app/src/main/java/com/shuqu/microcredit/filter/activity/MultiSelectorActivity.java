package com.shuqu.microcredit.filter.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.BaseActivity;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.filter.adapter.MultiSelectorAdapter;
import com.shuqu.microcredit.filter.entity.MultiSelectorEntity;
import com.shuqu.microcredit.filter.interfac.MultiSelecteAllListener;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxin on 16/7/28.
 */
public class MultiSelectorActivity extends BaseActivity implements MultiSelecteAllListener, View.OnClickListener, AdapterView.OnItemClickListener {

    ListView filterList;
    ImageView imgSelectAll;
    RelativeLayout rlSelectAll;
    private TextView tvTitle;
    private ImageView rightImage;

    private String mFilterDatas; //json串
    private MultiSelectorAdapter mAdapter;
    private String mMultiSelectorType; //数据type

    private List<MultiSelectorEntity> multiSelecteDatas; //解析js数据

    private JSONObject mJsData;//jsData,如果为null,则没有选择


    @Override
    protected void initView() {
        setContentView(R.layout.activity_multil_selector);

        rlSelectAll = (RelativeLayout) findViewById(R.id.rl_select_all);
        filterList = (ListView) findViewById(R.id.filter_list);
        imgSelectAll = (ImageView) findViewById(R.id.select_all);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rightImage = (ImageView) findViewById(R.id.iv_right);

        tvTitle.setText(getResources().getText(R.string.city_choose_title));
        rightImage.setImageResource(R.mipmap.ic_launcher);
        rightImage.setOnClickListener(this);
        rlSelectAll.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        if (intent != null) {
            mFilterDatas = intent.getStringExtra(ParamKeys.DIALOG_MULTI_FILTER_KEY);
        }
        if (StringUtil.checkStr(mFilterDatas)) {
            setMultiDatas();
        }

        mAdapter = new MultiSelectorAdapter(multiSelecteDatas, this);
        mAdapter.addSelectedAllListener(this);
        filterList.setAdapter(mAdapter);
        filterList.setOnItemClickListener(this);
    }

    /**
     * 处理json数据
     */
    private void setMultiDatas() {

        try {
            JSONObject jsonObject = new JSONObject(mFilterDatas);
            JSONArray option = jsonObject.optJSONArray("option");
            mMultiSelectorType = jsonObject.optString("type");
            multiSelecteDatas = new ArrayList<>();
            int length = option.length();
            for (int i = 0; i < length; i++) {
                JSONObject entityJsonObject = option.optJSONObject(i);
                if (entityJsonObject != null) {

                    MultiSelectorEntity selectorEntity = new MultiSelectorEntity();
                    selectorEntity.setName(entityJsonObject.optString("name"));
                    selectorEntity.setObj(entityJsonObject);
                    selectorEntity.setChecked(true);

                    multiSelecteDatas.add(selectorEntity);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isSelecteAll(boolean isSelectAll) {
        imgSelectAll.setVisibility(isSelectAll ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 选择完成
     */
    private void selectFinish() {
        JSONArray jsonArray = new JSONArray();
        mJsData = new JSONObject();
        try {
            for (MultiSelectorEntity entity : multiSelecteDatas) {
                if (entity.isChecked()) {
                    jsonArray.put(entity.getObj());
                }
            }
            mJsData.put("type", mMultiSelectorType);
            mJsData.put("option", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.putExtra(ParamKeys.MULTI_SELECTOR_KEY, mJsData.toString());

        setResult(RESULT_OK, intent);
        MultiSelectorActivity.this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_select_all:

                boolean isVisible = imgSelectAll.getVisibility() == View.VISIBLE;

                imgSelectAll.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
                mAdapter.setSelectAll(!isVisible);
                break;
            case R.id.iv_right:
                selectFinish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        MultiSelectorEntity entity = multiSelecteDatas.get(position);
        entity.setChecked(!entity.isChecked());
        mAdapter.notifyDataSetChanged();
    }
}
