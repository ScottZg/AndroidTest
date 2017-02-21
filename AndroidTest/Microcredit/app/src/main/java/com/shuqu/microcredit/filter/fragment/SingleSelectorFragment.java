package com.shuqu.microcredit.filter.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.filter.adapter.SingleSelectorAdapter;
import com.shuqu.microcredit.filter.entity.SingleSelectorEntity;
import com.shuqu.microcredit.filter.interfac.SelectorCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 16/7/12.
 */
public class SingleSelectorFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    ListView filterList;
    TextView tvCancel;
    TextView tvNoSelecte;
    View viewDivider;

    private String mFilterDatas; //json串
    private String mSingleSelectorType; //数据type
    private List<SingleSelectorEntity> singleSelecteDatas;
    private SingleSelectorAdapter mAdapter;
    private SelectorCallback mSingleCallback;
    private JSONObject mJsData;//jsData,如果为null,则没有选择
    private boolean isShowNoSelect;

    public static SingleSelectorFragment newInstance(Bundle bundle) {
        SingleSelectorFragment fragment = new SingleSelectorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();

        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setWindowAnimations(R.style.BottomToTopAnim);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(attributes);
    }

    private void setDialog() {

        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey(ParamKeys.DIALOG_SINGLE_FILTER_KEY)) {
            mFilterDatas = bundle.getString(ParamKeys.DIALOG_SINGLE_FILTER_KEY);
        }

        if (StringUtil.checkStr(mFilterDatas)) {
            setSingleDatas();
        }

        View view = inflater.inflate(R.layout.fragment_dialog_single, container);

        filterList = (ListView) view.findViewById(R.id.filter_list);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvNoSelecte = (TextView) view.findViewById(R.id.tv_no_select);
        viewDivider = view.findViewById(R.id.view_divider);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if(isShowNoSelect) {
            tvNoSelecte.setVisibility(View.VISIBLE);
            viewDivider.setVisibility(View.VISIBLE);
            tvNoSelecte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mJsData = new JSONObject();
                    try {
                        mJsData.put("type", mSingleSelectorType);
                        mJsData.put("option", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                }
            });
        } else {
            tvNoSelecte.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
        }

        return view;
    }

    /**
     * 处理json数据
     */
    private void setSingleDatas() {

        try {
            JSONObject jsonObject = new JSONObject(mFilterDatas);
            JSONArray option = jsonObject.optJSONArray("option");
            mSingleSelectorType = jsonObject.optString("type");
            isShowNoSelect = jsonObject.optBoolean("show", true);
            singleSelecteDatas = new ArrayList<>();
            int length = option.length();
            for (int i = 0; i < length; i++) {

                JSONObject entityJsonObject = option.optJSONObject(i);
                if (entityJsonObject != null) {

                    SingleSelectorEntity singleSelectorEntity = new SingleSelectorEntity();
                    singleSelectorEntity.setName(entityJsonObject.optString("name"));
                    singleSelectorEntity.setObj(entityJsonObject);

                    singleSelecteDatas.add(singleSelectorEntity);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDialog();
        mAdapter = new SingleSelectorAdapter(singleSelecteDatas, getActivity());
        filterList.setAdapter(mAdapter);
        filterList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {
            mJsData = new JSONObject();
            mJsData.put("type", mSingleSelectorType);
            mJsData.put("option", singleSelecteDatas.get(position).getObj());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mSingleCallback != null) {
            mSingleCallback.onSingleSelecte(mJsData);
        }
        super.onDismiss(dialog);
    }

    public void addSingleCallback(SelectorCallback mSingleCallback) {
        this.mSingleCallback = mSingleCallback;
    }
}
