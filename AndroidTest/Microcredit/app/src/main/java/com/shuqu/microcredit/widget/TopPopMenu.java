package com.shuqu.microcredit.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.shuqu.microcredit.Interface.IBrMenuOption;
import com.shuqu.microcredit.Model.MenuItem;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.MenuIconMap;
import java.util.ArrayList;

/**
 * Class Info ：pop menu
 * Created by Lyndon on 16/6/2.
 */
public class TopPopMenu extends PopupWindow {

    LinearLayout mContentView;
    LayoutInflater mInflater;
    IBrMenuOption mCall;

    public TopPopMenu(Context context, int layoutId, IBrMenuOption call){
        super(context);
        if(context == null) return;
        mCall = call;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = (LinearLayout) mInflater.inflate(layoutId, null);

        this.setContentView(mContentView);
        this.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        //		this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        this.setBackgroundDrawable(dw);
        this.update();
    }

    public void setMenuData(ArrayList<MenuItem> menuData){
        initMenuInfo(mInflater, menuData, mCall);
    }

    private void initMenuInfo(LayoutInflater inflater, ArrayList<MenuItem>
            menuData, final IBrMenuOption call){
        mContentView.removeAllViews();
        for(int i = 0; i < menuData.size(); ++i){
            View menuItem = inflater.inflate(R.layout.menu_item, null);
            ImageView icon = (ImageView) menuItem.findViewById(R.id.iv_menu_icon);
            TextView title = (TextView) menuItem.findViewById(R.id.tv_menu_title);
            int iconId = MenuIconMap.getMenuIcon(menuData.get(i).mIcon);
            if(iconId != MenuIconMap.ERROR_ICON_ID) {
                icon.setVisibility(View.VISIBLE);
                icon.setImageResource(iconId);
            }else{
                icon.setVisibility(View.GONE);
            }
            title.setText(menuData.get(i).mTitle);
            final String eventId = menuData.get(i).mEventId;
            menuItem.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(call != null){
                        call.menuItemClicked(eventId);
                    }
                }
            });
            mContentView.addView(menuItem);
        }
    }
}
