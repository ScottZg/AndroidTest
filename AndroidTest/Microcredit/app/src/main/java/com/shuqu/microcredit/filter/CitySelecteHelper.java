package com.shuqu.microcredit.filter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.DisplayUtil;
import com.shuqu.microcredit.Utils.ToastUtils;
import com.shuqu.microcredit.filter.entity.CityModel;
import com.shuqu.microcredit.filter.interfac.CityHelperInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxin on 16/7/28.
 */
public class CitySelecteHelper {

    public static final String JS_SELECT_TYPE_SINGLE = "single";
    public static final String JS_SELECT_TYPE_MULTI = "multi";

    private int mTextSize;
    private int mTextPadding;
    private int itemLayoutMinWeith;
    private int imageCloseWeith;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private CityHelperInterface mInterface;

    public CitySelecteHelper(CityHelperInterface inteface) {
        this.mInterface = inteface;
        this.mActivity = inteface.getActivity();
        mTextSize = DisplayUtil.sp2px(mActivity, 14);
        mTextPadding = DisplayUtil.dp2px(mActivity, 10);
        itemLayoutMinWeith = DisplayUtil.dp2px(mActivity, 101);
        imageCloseWeith = DisplayUtil.dp2px(mActivity, 16);
        mInflater = LayoutInflater.from(mActivity);
    }

    /**
     * 通过cityModel获取子view
     * @param viewGroup
     * @param cityModel
     * @return
     */
    public View getViewByCityModel(ViewGroup viewGroup, CityModel cityModel) {

        if(viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = viewGroup.getChildAt(i);
                CityModel temp = (CityModel) childView.getTag();
                if (cityModel.equals(temp)) {
                    return childView;
                }
            }
        }
        return null;
    }
    /**
     * 添加关注view
     * @param cityModel
     * @return
     */
    public boolean addFocusItemView(CityModel cityModel) {
        if (cityModel == null) {
            return false;
        }

        if (mInterface.getFocusLayout().getChildCount() >= mInterface.getMaxSelect()) {
            if(JS_SELECT_TYPE_MULTI.equals(mInterface.getSelectType())) {
                ToastUtils.show(mActivity, "最多只可关注" + mInterface.getMaxSelect() +" 个城市哦");
                return false;
            } else if(JS_SELECT_TYPE_SINGLE.equals(mInterface.getSelectType())) {
                mInterface.getFocusLayout().removeAllViews();
                mInterface.getFocusCitysDatas().clear();
            }
        }
        //已经含有这个城市
        if (getViewByCityModel(mInterface.getFocusLayout(), cityModel) != null) {
            return false;
        }
        //给item添加根布局
        LinearLayout rootLayout = (LinearLayout) mInflater.inflate(R.layout.item_focus_root, mInterface.getFocusLayout(), false);

        RelativeLayout itemLayout;
        TextView tvFocus;
        ImageView imageViewClose;

        RelativeLayout.LayoutParams itemLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, DisplayUtil.dp2px(mActivity, 44));
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams tvLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, DisplayUtil.dp2px(mActivity, 36));
        tvLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        itemLayout = new RelativeLayout(mActivity);

        int textWeigh = (cityModel.getAreaName().length()) * mTextSize;
        if ((textWeigh + imageCloseWeith / 2 + mTextPadding * 2) < itemLayoutMinWeith) {
            itemLayoutParams.width = itemLayoutMinWeith;
        } else {
            itemLayoutParams.width = textWeigh + imageCloseWeith / 2 + mTextPadding * 2;
        }
        itemLayout.setLayoutParams(itemLayoutParams);

        tvFocus = new TextView(mActivity);
        tvLayoutParams.width = itemLayoutParams.width - imageCloseWeith /2 ;

        tvFocus.setTextSize(14);
        tvFocus.setBackgroundResource(R.drawable.shape_hot_city_bg_normal);
        tvFocus.setLayoutParams(tvLayoutParams);
        tvFocus.setGravity(Gravity.CENTER);

        imageViewClose = new ImageView(mActivity);
        imageViewClose.setLayoutParams(imageParams);
        imageViewClose.setImageResource(R.mipmap.city_focus_close);

        tvFocus.setText(cityModel.getAreaName());

        itemLayout.addView(tvFocus);
        itemLayout.addView(imageViewClose);
        rootLayout.setTag(cityModel);

        rootLayout.addView(itemLayout);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityModel temp = (CityModel) view.getTag();
                removeFocusCity(temp);
            }
        });

        mInterface.getFocusLayout().addView(rootLayout);
        if (!mInterface.getFocusCitysDatas().contains(cityModel)) {
            mInterface.getFocusCitysDatas().add(cityModel);
        }
        return true;
    }
    /**
     * 统一处理移除关注
     * @param cityModel
     * @param
     */
    public void removeFocusCity(CityModel cityModel) {

        if (cityModel == null) {
            return;
        }
        View removeViewFocus = getViewByCityModel(mInterface.getFocusLayout(), cityModel);
        View removeViewHot = getViewByCityModel(mInterface.getHotLayout(), cityModel);
        if (removeViewFocus != null) {
            mInterface.getFocusLayout().removeView(removeViewFocus);
            if (mInterface.getFocusCitysDatas().contains(cityModel)) {
                mInterface.getFocusCitysDatas().remove(cityModel);
            }
        }

        if (removeViewHot != null) {
            removeViewHot.setSelected(false);
        }
    }
    /**
     * 处理热门城市点击逻辑
     * @param cityModel
     * @param view
     */
    public void handleHotCityClick(CityModel cityModel, View view) {
        if (view.isSelected()) {
            removeFocusCity(cityModel);
            view.setSelected(false);
            return;
        }

        if (addFocusItemView(cityModel)) {
            view.setSelected(true);
        }
    }
    /**
     * 检查json是否有数据
     * @param filterDatas
     * @param key
     * @return
     */
    public static boolean checkJsArrayNull(String filterDatas, String key) {
        JSONObject jsonObject = null;
        int length = 0;
        try {
            jsonObject = new JSONObject(filterDatas);
            JSONArray option = jsonObject.optJSONArray(key);
            length = option.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return length == 0;
    }
}
