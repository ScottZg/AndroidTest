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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.DisplayUtil;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.filter.adapter.MultiSelectorAdapter;
import com.shuqu.microcredit.filter.entity.MultiSelectorEntity;
import com.shuqu.microcredit.filter.interfac.MultiSelecteAllListener;
import com.shuqu.microcredit.filter.interfac.SelectorCallback;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxin on 16/7/26.
 */
public class MultiSelectorFragment extends DialogFragment implements MultiSelecteAllListener, View.OnClickListener {

    ListView filterList;
    TextView tvCancel;
    TextView tvConfirm;
    CheckBox cbSelectAll;

    private String mFilterDatas; //json串
    private MultiSelectorAdapter mAdapter;
    private String mMultiSelectorType; //数据type

    private SelectorCallback mMultiCallback;

    private List<MultiSelectorEntity> multiSelecteDatas; //解析js数据

    private JSONObject mJsData;//jsData,如果为null,则没有选择

    public static MultiSelectorFragment newInstance(Bundle bundle) {
        MultiSelectorFragment fragment = new MultiSelectorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();

        attributes.width = DisplayUtil.getWidth(getActivity()) - 2 * DisplayUtil.dp2px(getActivity(), 10);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(attributes);
    }

    private void setDialog() {
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        getDialog().getWindow().setGravity(Gravity.CENTER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(ParamKeys.DIALOG_MULTI_FILTER_KEY)) {
            mFilterDatas = bundle.getString(ParamKeys.DIALOG_MULTI_FILTER_KEY);
        }
        if (StringUtil.checkStr(mFilterDatas)) {
            setMultiDatas();
        }
        View view = inflater.inflate(R.layout.fragment_dialog_multi, container);

        filterList = (ListView) view.findViewById(R.id.filter_list);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        cbSelectAll = (CheckBox) view.findViewById(R.id.select_all);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        cbSelectAll.setOnClickListener(this);
        return view;
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
                    selectorEntity.setChecked(false);

                    multiSelecteDatas.add(selectorEntity);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理点击确定之后的数据传递
     */
    private void handleJsData() {

        if (mMultiCallback != null) {
            JSONArray jsonArray = new JSONArray();
            try {
                for (MultiSelectorEntity entity : multiSelecteDatas) {
                    if (entity.isChecked()) {
                        jsonArray.put(entity.getObj());
                    }
                }
                mJsData = new JSONObject();

                mJsData.put("type", mMultiSelectorType);
                mJsData.put("option", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialog();
        mAdapter = new MultiSelectorAdapter(multiSelecteDatas, getActivity());
        mAdapter.addSelectedAllListener(this);
        filterList.setAdapter(mAdapter);
    }

    public void addMultiCallback(SelectorCallback mSingleCallback) {
        this.mMultiCallback = mSingleCallback;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mMultiCallback != null) {
            mMultiCallback.onSingleSelecte(mJsData);
        }
        super.onDismiss(dialog);
    }


    @Override
    public void isSelecteAll(boolean isSelectAll) {
        cbSelectAll.setChecked(isSelectAll);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                handleJsData();
                dismiss();
                break;
            case R.id.select_all:
                mAdapter.setSelectAll(cbSelectAll.isChecked());
                break;
            default:
                break;
        }
    }
}
