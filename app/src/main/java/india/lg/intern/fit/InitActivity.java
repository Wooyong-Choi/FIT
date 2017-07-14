package india.lg.intern.fit;

import android.content.Intent;
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
        addSlide(AppIntroFragment.newInstance("Test1", "for test", R.drawable.ic_navigate_next_white, Color.parseColor("#FFB2F5")));
        addSlide(AppIntroFragment.newInstance("Test2", "for test", R.drawable.ic_navigate_next_white, Color.parseColor("#1DDB16")));
        addSlide(AppIntroFragment.newInstance("Test3", "for test", R.drawable.ic_navigate_next_white, Color.parseColor("#CC3D3D")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

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
