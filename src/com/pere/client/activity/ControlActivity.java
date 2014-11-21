package com.pere.client.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import com.pere.client.R;

/**
 * Created by eltntawy on 21/11/14.
 */
public class ControlActivity extends Activity implements DialogInterface.OnClickListener{

    Button btnBlackWhitePre;
    Button btnHomeEndPre;
    Button btnNavigationPre;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlChooser);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}