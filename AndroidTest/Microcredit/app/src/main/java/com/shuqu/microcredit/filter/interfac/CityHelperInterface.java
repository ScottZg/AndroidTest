package com.shuqu.microcredit.filter.interfac;

import android.app.Activity;
import android.view.ViewGroup;

import com.shuqu.microcredit.filter.entity.CityModel;

import java.util.List;


/**
 * Created by wuxin on 16/8/1.
 */
public interface CityHelperInterface {
    Activity getActivity();
    List<CityModel> getFocusCitysDatas();
    List<CityModel> getHotCitysDatas();
    ViewGroup getFocusLayout();
    ViewGroup getHotLayout();
    int getMaxSelect();
    String getSelectType();
}
