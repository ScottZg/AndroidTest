package com.shuqu.microcredit.Push;

import com.lyndon.dbutils.db.annotation.Column;
import com.lyndon.dbutils.db.annotation.Id;
import com.lyndon.dbutils.db.annotation.Table;

/**
 * push 消息
 * @author dingfangchao
 */
@Table(name = "PushMessage")
public class PushMessage {
	@Id(column = "MsgId")
	public String mMsgId;
	@Column(column = "Content")
	public String mContent;
	@Column(column = "CtrateTime")
	public long mCtrateTime;
	
	public PushMessage(){
		mCtrateTime = System.currentTimeMillis()/(1000*60*60);
	}
	
}
