package com.vchecker.obd;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.view.WindowManager;

public class PreferenceSetup extends PreferenceActivity {
	 @SuppressWarnings("deprecation")
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر���
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��

        addPreferencesFromResource(R.xml.preference_obd_setup);  
    }  
}
