package com.shuqu.microcredit.UI.Adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.shuqu.microcredit.UI.fragment.WebFragment;
import java.util.ArrayList;

/**
 * Class Info ：
 * Created by Lyndon on 16/7/7.
 */
public class CustomFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<WebFragment> mFragments;
    public CustomFragmentAdapter(FragmentManager fm, ArrayList<WebFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }


    @Override public Fragment getItem(int position) {
        if(mFragments != null && mFragments.size() > position){
            return mFragments.get(position);
        }
        return null;
    }


    @Override public int getCount() {
        if(mFragments != null){
            return mFragments.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
