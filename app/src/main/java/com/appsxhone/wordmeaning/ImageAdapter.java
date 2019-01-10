package com.appsxhone.wordmeaning;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Sameer on 09-Feb-16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String word;
    int r,level;
    private char[] mCharacter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private Integer[] mThumbIds = {R.mipmap.a_01, R.mipmap.b_01, R.mipmap.c_01, R.mipmap.d_01, R.mipmap.e_01,
            R.mipmap.f_01, R.mipmap.g_01, R.mipmap.h_01, R.mipmap.i_01, R.mipmap.j_01, R.mipmap.k_01, R.mipmap.l_01,
            R.mipmap.m_01, R.mipmap.n_01, R.mipmap.o_01, R.mipmap.p_01, R.mipmap.q_01, R.mipmap.r_01, R.mipmap.s_01,
            R.mipmap.t_01, R.mipmap.u_01, R.mipmap.v_01, R.mipmap.w_01, R.mipmap.x_01, R.mipmap.y_01, R.mipmap.z_01};

    int ref(String word, int position) {
        for (int i = 0; i < mCharacter.length; i++) {
            if (mCharacter[i] == word.charAt(position)) {
                r = i;
                break;
            }
        }
        return r;
    }

    public ImageAdapter(Context c, String word, int level) {
        mContext = c;
        this.word = word;
        this.level = level;
    }

    public int getCount() {
        return word.length();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            switch (level){
                case 1:
                    imageView.setLayoutParams(new GridView.LayoutParams(280,280));
                    break;
                case 2:
                    imageView.setLayoutParams(new GridView.LayoutParams(230, 230));
                    break;
                case 3:
                    imageView.setLayoutParams(new GridView.LayoutParams(190, 190));
                    break;
                case 4:
                    imageView.setLayoutParams(new GridView.LayoutParams(170, 170));
                    break;
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[ref(word, position)]);
        return imageView;
    }
}
