package com.shuqu.microcredit.UI.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shuqu.microcredit.BRApplication;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Interface.IMainDrawerOptionCallBack;
import com.shuqu.microcredit.Interface.IUserLoginCallback;
import com.shuqu.microcredit.Interface.UserApi;
import com.shuqu.microcredit.Model.MainDrawerOptionItem;
import com.shuqu.microcredit.Model.MainDrawerOptionsResult;
import com.shuqu.microcredit.NetWork.NetUtil;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.Adapter.MainDrawerLvAdapter;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 16/9/11.
 */
public class DrawerFragment extends BaseFragment implements AdapterView.OnItemClickListener, IUserLoginCallback {

    public static final String TAG = "TAG_MainDrawerFragment";
    ImageView mIvUserHeader;
    TextView mTvUsername;
    TextView mTvPhone;
    TextView mTvTip;
    ListView mLvOptions;

    private MainDrawerLvAdapter mAdapter;
    private List<MainDrawerOptionItem> mList;
    private IMainDrawerOptionCallBack mCallBack;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_drawer;
    }

    @Override
    protected void initView() {
        mIvUserHeader = (ImageView) mRootView.findViewById(R.id.iv_user_head);
        mTvUsername = (TextView) mRootView.findViewById(R.id.tv_username);
        mTvPhone = (TextView) mRootView.findViewById(R.id.tv_phone);
        mTvTip = (TextView) mRootView.findViewById(R.id.tv_tip);
        mLvOptions = (ListView) mRootView.findViewById(R.id.lv_options);

        mAdapter = new MainDrawerLvAdapter(mParentActivity, mList);
        mLvOptions.setAdapter(mAdapter);
        mLvOptions.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        UserApi.registLoginCallback(this);

        new GetOptionsTask(mParentActivity).execute();
    }

    private void handlerResult(MainDrawerOptionsResult result) {
        if (result != null && result.isSuccess()) {
            mList = result.getResult();
            mAdapter.setData(mList);
        }
    }

    public void addOnOptionClickCallBack(IMainDrawerOptionCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(mCallBack != null) {
            mCallBack.onOptionClick(mList.get(position));
        }
    }

    @Override
    public void onUserLogin() {
        // FIXME: 16/9/20 
        mTvUsername.setText("登陆了"); //用户名
        mTvPhone.setText("登陆了哈哈哈");
    }

    @Override
    public void onUserLogout() {
        mTvUsername.setText(""); //用户名
        mTvPhone.setText("");
    }

    private class GetOptionsTask extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private MainDrawerOptionsResult mResult;

        public GetOptionsTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String lastData = PrefsUtils.getString(mContext, ParamKeys.HOME_DRAWER_LAST_DATA, "");
            mResult = handleItemsResult(lastData);
            BRApplication.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handlerResult(mResult);
                }
            });

            String json = NetUtil.requestData(mContext, NetUtil.HTTP_METHOD_POST, null, null, Urls.METHOS_GET_CUSTOMDATA);
            if(!lastData.equals(json)) {
                PrefsUtils.putValue(mContext, ParamKeys.HOME_DRAWER_LAST_DATA, json);
                mResult = handleItemsResult(json);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean equals) {
            super.onPostExecute(equals);
            if(!equals) {
                handlerResult(mResult);
            }
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private MainDrawerOptionsResult handleItemsResult (String json) {
        if(StringUtil.checkStr(json)) {
            try {
                MainDrawerOptionsResult result = new MainDrawerOptionsResult();
                JSONObject jsonObject = new JSONObject(json);
                result.setCode(jsonObject.optInt("code", -1));
                result.setMessage(jsonObject.optString("message"));
                if (result.isSuccess()) {
                    List<MainDrawerOptionItem> list = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.optJSONArray("result");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        MainDrawerOptionItem item = MainDrawerOptionItem.parse(jsonArray.optJSONObject(i));
                        list.add(item);
                    }
                    result.setResult(list);
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserApi.unRegistLoginCallback(this);
    }
}
