package india.lg.intern.fit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Wooyong on 2017-07-06.
 */
public class InitActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add test slide
        addSlide(AppIntroFragment.newInstance("How to start", "Click the collect button and start your travel!", R.drawable.tutorial11, Color.parseColor("#25A599")));
        addSlide(AppIntroFragment.newInstance("Collect", "after your trip, just click stop button and you can get your records! see your track, spot and album!", R.drawable.tutorial22, Color.parseColor("#25A599")));
        addSlide(AppIntroFragment.newInstance("Share", "You can share your favorite photo to Instagram with auto-hastag!", R.drawable.tutorial33, Color.parseColor("#25A599")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#000000"));
        setSeparatorColor(Color.parseColor("#000000"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}