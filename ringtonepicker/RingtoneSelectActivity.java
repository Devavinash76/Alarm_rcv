package com.example.appmeno.ringtonepicker;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.appmeno.util.Utils;
import com.notebook.notes.R;

import static com.example.appmeno.AlarmCreateActivity.EXTRA_ALARM_RINGTONE;

public class RingtoneSelectActivity extends AppCompatActivity {

    TextView textViewRingtone;
    String selectedRingTone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone_select);
        findViewById(R.id.layoutRing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRingTonePicker();
            }
        });
        findViewById(R.id.layoutMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMusicPicker();
            }
        });

        findViewById(R.id.buttonDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(EXTRA_ALARM_RINGTONE,selectedRingTone);
                setResult(RESULT_OK,result);
                finish();
            }
        });

        findViewById(R.id.buttonRevert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        if(getIntent().hasExtra(EXTRA_ALARM_RINGTONE)){
            selectedRingTone = getIntent().getStringExtra(EXTRA_ALARM_RINGTONE);
        }

        textViewRingtone = findViewById(R.id.textViewRingtone);

        Uri defaultRingTone = RingtoneUtils.getSystemRingtoneTone();
        try {
            if (!TextUtils.isEmpty(selectedRingTone)) {
                defaultRingTone = Uri.parse(selectedRingTone);
            }
        }catch (Exception e){
            Log.e("RingtoneSelectActivity","Exception In parsing",e);
        }

        if(defaultRingTone!=null){
            String name = RingtoneUtils.getRingtoneName(RingtoneSelectActivity.this, defaultRingTone);
            textViewRingtone.setText(Utils.getValueOrEmptyString(name));
        }
    }


    public void showRingTonePicker(){
        RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog
                .Builder(RingtoneSelectActivity.this, getSupportFragmentManager())

                //Set title of the dialog.
                //If set null, no title will be displayed.
                .setTitle("Select ringtone")

                //set the currently selected uri, to mark that ringtone as checked by default.
                //If no ringtone is currently selected, pass null.
//                .setCurrentRingtoneUri(/* Prevously selected ringtone Uri */)

                //Set true to allow allow user to select default ringtone set in phone settings.
                .displayDefaultRingtone(true)

                //Set true to allow user to select silent (i.e. No ringtone.).
                .displaySilentRingtone(true)

                //set the text to display of the positive (ok) button.
                //If not set OK will be the default text.
                .setPositiveButtonText("SET RINGTONE")

                //set text to display as negative button.
                //If set null, negative button will not be displayed.
                .setCancelButtonText("CANCEL")

                //Set flag true if you want to play the sample of the clicked tone.
                .setPlaySampleWhileSelection(true)

                //Set the callback listener.
                .setListener(new RingtonePickerListener() {
                    @Override
                    public void OnRingtoneSelected(@NonNull String ringtoneName, Uri ringtoneUri) {
                        //Do someting with selected uri...
                        if(ringtoneUri != null) {
                            String name = RingtoneUtils.getRingtoneName(RingtoneSelectActivity.this, ringtoneUri);
                            textViewRingtone.setText(Utils.getValueOrEmptyString(name));
                            selectedRingTone = ringtoneUri.toString();
                        }
                    }
                });

        //Add the desirable ringtone types.
//        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);

        //Display the dialog.
        ringtonePickerBuilder.show();
    }

    public void showMusicPicker(){
        RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog
                .Builder(RingtoneSelectActivity.this, getSupportFragmentManager())

                //Set title of the dialog.
                //If set null, no title will be displayed.
                .setTitle("Select Music")

                //set the currently selected uri, to mark that ringtone as checked by default.
                //If no ringtone is currently selected, pass null.
//                .setCurrentRingtoneUri(/* Prevously selected ringtone Uri */)

                //set the text to display of the positive (ok) button.
                //If not set OK will be the default text.
                .setPositiveButtonText("SET RINGTONE")

                //set text to display as negative button.
                //If set null, negative button will not be displayed.
                .setCancelButtonText("CANCEL")

                //Set flag true if you want to play the sample of the clicked tone.
                .setPlaySampleWhileSelection(true)

                //Set the callback listener.
                .setListener(new RingtonePickerListener() {
                    @Override
                    public void OnRingtoneSelected(@NonNull String ringtoneName, Uri ringtoneUri) {
                        //Do someting with selected uri...
                        if(ringtoneUri != null) {
                            textViewRingtone.setText(Utils.getValueOrEmptyString(ringtoneName));
                            selectedRingTone = ringtoneUri.toString();
                        }
                    }
                });

        //Add the desirable ringtone types.
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_MUSIC);

        //Display the dialog.
        ringtonePickerBuilder.show();
    }
}
