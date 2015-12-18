package com.example.fragmentdemo;


import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ViewDebug.FlagToString;

public class Text extends Activity {
//	����ת�Ľ���
	
//	ȫ�ֱ�������
	String msg;
	DevicePolicyManager policyManager;
	ComponentName componentName;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.identify);
		super.onCreate(savedInstanceState);
		
		policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(this, MyAdmin.class);

		if (!policyManager.isAdminActive(componentName)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
		}

		
//    	�Ȼ�ȡintent���ݹ���������
		String sender=getIntent().getExtras().getString("phonenum");
		String content=getIntent().getExtras().getString("mess_content");
		
	     String wipe="wipe";
         String lock="lock";
         String music="music";
		
     	  Pattern p_wipe=Pattern.compile(wipe);
     	  Pattern p_lock=Pattern.compile(lock);
     	  Pattern p_music=Pattern.compile(music);
     	  
          Matcher m_wipe=p_wipe.matcher(content);
          Matcher m_lock=p_lock.matcher(content);
          Matcher m_music=p_music.matcher(content);
          
          boolean result_wipe=m_wipe.find();  
          boolean result_lock=m_lock.find(); 
          boolean result_music=m_music.find(); 
          
//          ����������
          if(result_lock){
        	  String fqdnId="";
        	  String reg = "[0-9]{4}"; //������ʽ����������4������
              Pattern pattern = Pattern.compile(reg);
              Matcher matcher = pattern.matcher(content);
              if (matcher.find()) {// matcher.matchers() {
                  fqdnId = matcher.group();
              }
        	policyManager.resetPassword(fqdnId, 0);
  			policyManager.lockNow();
          }
     	  
//          �������
//          ���ã�����
          if(result_wipe){
        	  policyManager.wipeData(0);
          }
		

          if(result_music){
//		��������
//		http://blog.csdn.net/chuchu521/article/details/7848706
//		ϵͳ����ĵ���Ҫ����oncreate�У����ܷ���ǰ��
		  AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);  
		am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_PLAY_SOUND);
//        ��������
        MediaPlayer  mediaPlayer = MediaPlayer.create(Text.this,  R.raw.abc);
        mediaPlayer.start();
          }
		
//        ��ȡgps
//        ����ͨ��������ʱ����
      	 final LocationManager mgr = null;
      	 
      	 	GpsStatus.Listener listener = new GpsStatus.Listener() { 
            public void onGpsStatusChanged(int event) { 
            GpsStatus gpsstatus=mgr.getGpsStatus(null); 
                switch(event) 
                { 
                case GpsStatus.GPS_EVENT_FIRST_FIX:gpsstatus.getTimeToFirstFix();   
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS: 
                   //�õ������յ������ǵ���Ϣ������ ���ǵĸ߶Ƚǡ���λ�ǡ�����ȡ���α����ţ������Ǳ�ţ� 
            Iterable<GpsSatellite> allSatellites; 
            allSatellites = gpsstatus.getSatellites(); 
            Iterator it=allSatellites.iterator(); 
            String msg="jdksjfslkdflk;"; 
            while(it.hasNext()) 
            { 
                GpsSatellite oSat = (GpsSatellite) it.next() ;  
                 msg="azimuth:"+oSat.getAzimuth(); 
                msg+="\nprn:"+oSat.getPrn(); 
                msg+="\nsnr:"+oSat.getSnr(); 
            } 
            break; 
             
           case GpsStatus.GPS_EVENT_STARTED: 
                   //Event sent when the GPS system has started. 
           break; 
             
           case GpsStatus.GPS_EVENT_STOPPED: 
                  //Event sent when the GPS system has stopped.  
            break; 
             
           default : 
            break; 
                } 
                 
            } 
     }; 
     
     
//		���Ͷ���
		SmsManager smsManager = SmsManager.getDefault();// ����Ϣʱ��Ҫ��
		smsManager.sendTextMessage(sender, null, "coming from finding_phone", null,null); 
//		smsManager.sendTextMessage(sender, null, msg, null,null);
//		ע��������ݲ���Ϊ��
	}

}

