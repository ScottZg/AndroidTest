package com.shuqu.microcredit.Admin;

import android.content.Context;
import com.lyndon.dbutils.DbUtils;
import com.lyndon.dbutils.db.sqlite.Selector;
import com.shuqu.microcredit.Push.PushMessage;
import java.util.List;

public class DBAdmin {
    private static DbUtils mUtils;
    private static DBAdmin mInstance;
    private static String DB_Name = "local.db";

    //FIXME when change table
    private static int DB_Version = 1;


    public static DBAdmin getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBAdmin();
        }
        if (mUtils == null) {
            mUtils = DbUtils.create(context, DB_Name, DB_Version,
                    new DbUtils.DbUpgradeListener() {

                        @Override
                        public void onUpgrade(DbUtils dbUtils, int oldVersion,
                                              int newVersion) {
                            if (oldVersion != newVersion) {
                                try {
                                    // push 消息
                                    List<PushMessage> pushDatas = dbUtils.findAll(Selector.from(PushMessage.class));
                                    dbUtils.dropTable(PushMessage.class);
                                    dbUtils.saveAll(pushDatas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
        return mInstance;
    }


    public boolean clearAllData() {
        try {
            if (mUtils == null) {
                return false;
            }
            mUtils.dropDb();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean savePushMsg(PushMessage data) {
        if (mUtils == null) {
            return false;
        }
        synchronized (mUtils) {
            try {
                if (data != null) {
                    mUtils.saveOrUpdate(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }


    public boolean savePushMsg(String id, String msg) {
        if (mUtils == null) {
            return false;
        }
        PushMessage data = new PushMessage();
        data.mMsgId = id;
        data.mContent = msg;
        synchronized (mUtils) {
            try {
                if (data != null) {
                    mUtils.saveOrUpdate(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }


    public boolean hasMessage(PushMessage data) {
        if (mUtils == null) {
            return false;
        }
        synchronized (mUtils) {
            try {
                List<PushMessage> datas = mUtils.findAll(Selector.from(
                        PushMessage.class).where("MsgId", "=", data.mMsgId));
                if (datas == null || datas.size() == 0) {
                    return false;
                }
                else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    public boolean hasMessage(String id) {
        if (mUtils == null) {
            return false;
        }
        synchronized (mUtils) {
            try {
                List<PushMessage> datas = mUtils.findAll(Selector.from(
                        PushMessage.class).where("MsgId", "=", id));
                if (datas == null || datas.size() == 0) {
                    return false;
                }
                else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    public boolean deleteOldMessage(long currentTime, int days) {
        if (mUtils == null) {
            return false;
        }
        currentTime = currentTime / (1000 * 60 * 60);
        synchronized (mUtils) {
            try {
                List<PushMessage> datas = mUtils.findAll(PushMessage.class);
                mUtils.dropTable(PushMessage.class);
                PushMessage data;
                for (int i = 0; i < datas.size(); ++i) {
                    data = datas.get(i);
                    if (currentTime - data.mCtrateTime < days * 24) {
                        mUtils.saveOrUpdate(data);
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }


    private DBAdmin() {
		/* DO NOT NEW ME */
    }

}
