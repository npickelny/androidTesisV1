package Extra;

import android.app.Application;
import android.support.v7.widget.AppCompatTextView;

import com.example.nicolaspickelny.androidcustomkeyboard.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Nico on 24/01/2017.
 */

public final class AppFont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
