package com.renasoft.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by BTSC-2 on 20/04/2016.
 */
public class Trailer {
    private String url;
    private String name;
    private Context mcontext;

    public Trailer(String name, String url, Context context) {
        this.name = name;
        this.url = url;
        this.mcontext = context;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void playTrailer() {
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(Uri.parse(this.getUrl()));

        if (trailerIntent.resolveActivity(mcontext.getPackageManager()) != null) {
            mcontext.startActivity(Intent.createChooser(trailerIntent, mcontext.getString(R.string.app_chooser_text)));
        } else {
            Toast.makeText(mcontext, mcontext.getString(R.string.no_apps), Toast.LENGTH_SHORT).show();
        }
    }
}
