package com.appsxhone.wordmeaning;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

/**
 * Created by Sameer on 10-Feb-16.
 */
public class SettingsDialogFragment extends DialogFragment {
    ImageView closeButton, rateButton, contactButton;
    Switch soundSwitch;
//    SoundPlayer soundPlayer;
//    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_settings, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        closeButton = (ImageView) view.findViewById(R.id.btnSettingsClose);
        rateButton = (ImageView) view.findViewById(R.id.ratethegame);
        contactButton = (ImageView) view.findViewById(R.id.contactusButton);
        soundSwitch = (Switch) view.findViewById(R.id.switchSound);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsDialogFragment.this.dismiss();
            }
        });


//        mContext = getActivity();
//        soundPlayer = new SoundPlayer(mContext);

        soundSwitch.setChecked(true);
        soundSwitch.getThumbDrawable().setAlpha(0);
        soundSwitch.getTrackDrawable().setAlpha(0);
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    soundSwitch.setBackgroundResource(R.mipmap.sound_on);
//                    soundPlayer.resumeSound(mContext);
                }else{
                    soundSwitch.setBackgroundResource(R.mipmap.soundoff_button);
//                    soundPlayer.pauseSound(mContext);
                }
            }
        });

        if(soundSwitch.isChecked()){
            soundSwitch.setBackgroundResource(R.mipmap.sound_on);
//            soundPlayer.resumeSound(mContext);
        }else{
            soundSwitch.setBackgroundResource(R.mipmap.soundoff_button);
//            soundPlayer.pauseSound(mContext);
        }

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.appsxhone.wordmeaning")));
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        return view;
    }
}