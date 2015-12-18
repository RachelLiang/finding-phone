package com.example.fragmentdemo;

  import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.sax.StartElementListener;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.*;
  
  public class SmsRecevier extends BroadcastReceiver {
		DevicePolicyManager policyManager;
		ComponentName componentName;
   private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
		
		private String db_ques;
		private  String db_answ;
		private SharedPreferences sp;
		private Boolean result_db_answ;
       private SQLiteDatabase db;
       private DBopenhelper helper;
       private Cursor c;
	   private SmsRecevier_check recevier_check; 
	private SmsRecevier_check recevier;

	
      String regEx="123"; 
      Pattern p_regEx=Pattern.compile(regEx);
	  
      public SmsRecevier() {
          Log.v("TAG", "SmsRecevier create");
      }
  
      @Override
      public void onReceive(Context context, Intent intent) {

          policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
          componentName = new ComponentName(context, DeviceAdminReceiver.class);//http://stackoverflow.com/questions/10904841/avoid-securityexception-because-of-no-active-admin-owned-by
//        componentName = new ComponentName(context, MyAdmin.class);//�����Ǵ���ǰ�Ĵ����ﳭ�����ģ��������ﲻ֪������ô��������

  		if (!policyManager.isAdminActive(componentName)) {
  			Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
  			intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
  			context.startActivity(intent1);
  		}
          
      			
         Object[] pdus = (Object[]) intent.getExtras().get("pdus");
         if (pdus != null && pdus.length > 0) {
             SmsMessage[] messages = new SmsMessage[pdus.length];
             for (int i = 0; i < pdus.length; i++) {
                 byte[] pdu = (byte[]) pdus[i];
                 messages[i] = SmsMessage.createFromPdu(pdu);
             }
             for (SmsMessage message : messages) {
                 String content = message.getMessageBody();// �õ���������
                 String sender = message.getOriginatingAddress();// �õ�����Ϣ�ĺ���
                  
                 
                 if(db_answ!=null){
                	 Pattern p_db_answ=Pattern.compile(db_answ);
                	 Matcher m_db_answ=p_db_answ.matcher(content);
                	 result_db_answ=m_db_answ.find();
                 }
                 
//                  ������������ʽ��ƥ��
               Matcher m_regEx=p_regEx.matcher(content);

//				find�ǲ���ƥ��
//               matches����ȫƥ��
               boolean result_regEx=m_regEx.matches();  
               
              if(result_regEx){
            	  
//            	  ���ݿ����
	        helper =new DBopenhelper(context,"main_tb", null, 1);
	        db= helper.getWritableDatabase();
	        c=db.query("main_tb",null,null,null,null,null,null);
	        int count=(int) (c.getCount()*Math.random());
	        
	        c.moveToFirst();
	        c.move(count);
	        db_ques=c.getString(1); // ע����1��2������0��1,sqlite���ص�
	        db_answ=c.getString(2);
	       
	        sp=context.getSharedPreferences("answer", context.MODE_MULTI_PROCESS);
	        Editor editor=sp.edit();
	        editor.putString("db_answ", db_answ);
	        editor.commit();
	        
	        recevier = new SmsRecevier_check();
	        IntentFilter filter = new IntentFilter();
	        filter.addAction(ACTION);
	        filter.addAction("register.success.finish");
	        filter.setPriority(1000);//�������ȼ����
	        context.registerReceiver(recevier, filter);
	        
//            	  ���Ͷ���
		SmsManager smsManager = SmsManager.getDefault();// ����Ϣʱ��Ҫ��
		smsManager.sendTextMessage(sender, null, "coming from finding_phone", null,null); 
		smsManager.sendTextMessage(sender, null, db_ques, null,null); 
      
//            	  ����
//            	  policyManager.resetPassword("1234", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
//            	  policyManager.resetPassword("1234", 0);
//            	  policyManager.lockNow();
	       
//		http://stackoverflow.com/questions/4957461/unregister-broadcast-receiver-android
		context.unregisterReceiver(this);
            	  
                }

              if(result_db_answ!=null&&result_db_answ){
        SmsManager smsManager = SmsManager.getDefault();// ����Ϣʱ��Ҫ��
		smsManager.sendTextMessage(sender, null, "welcome ,please make your requset from SmsRecevier", null,null); 
            	  
              }
             }
         }
     }
 }
