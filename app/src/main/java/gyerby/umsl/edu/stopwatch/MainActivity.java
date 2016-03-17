package gyerby.umsl.edu.stopwatch;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TimeViewFragment.TimeViewFragmentDelegate, MainControllerFragment.MainFragmentListener {
    private static final String CONTROLLER_FRAGMENT_TAG = "CONTROLLER_FRAGMENT_TAG";
    TimeViewFragment mTimeViewFragment = null;
    MainControllerFragment mMainControllerFragment = null;
    LapListFragment mLapListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        //Recycleview is in a fragment (UI)
        //maintime and buttons are in a fragment (UI)
        //controller is in a retain state fragment (headless)
       FragmentManager manager = getSupportFragmentManager();
        mTimeViewFragment = (TimeViewFragment) manager.findFragmentById(R.id.MainTimeFragmentContainer);
        mLapListFragment = (LapListFragment) manager.findFragmentById(R.id.LapTimesFragmentContainer);
        mMainControllerFragment = (MainControllerFragment) manager.findFragmentByTag(CONTROLLER_FRAGMENT_TAG);


        //add the controller fragment
        if (mMainControllerFragment == null) {
            mMainControllerFragment = new MainControllerFragment();
            manager.beginTransaction()
                    .add(mMainControllerFragment, CONTROLLER_FRAGMENT_TAG)
                    .commit();
        }

        //prepare to setup times on UI fragments
        long ticks = mMainControllerFragment.getmLapTimeTicks();
        long ticks2= mMainControllerFragment.getmMainTimeTicks();

       //add Time Fragment (UI) to activity
       if(mTimeViewFragment == null){
            mTimeViewFragment = TimeViewFragment.newInstance(mMainControllerFragment.getmMainTimeTicks(), mMainControllerFragment.getmLapTimeTicks());
            manager.beginTransaction().add(R.id.MainTimeFragmentContainer, mTimeViewFragment).commit();
       }

        //add lap times to activity
        if(mLapListFragment ==null){
            mLapListFragment = new LapListFragment();
            manager.beginTransaction().add(R.id.LapTimesFragmentContainer,mLapListFragment).commit();
        }


        //register this activity as the coupled interface.
        mTimeViewFragment.setDelegate(this);

    }


    @Override
    public void startTime() {
      mMainControllerFragment.start();
    }

    @Override
    public void resetTime() {
        mMainControllerFragment.reset();
        mLapListFragment.updateUI();
    }

    @Override
    public void stopTime() {
       mMainControllerFragment.stop();
    }

    @Override
    public void newLap() {
        mMainControllerFragment.newLap();
        mLapListFragment.updateUI();
    }

    @Override
    public void updateMainTime(long ticks) {
        mTimeViewFragment.updateMainTime(ticks);
    }

    @Override
    public void updateLapTime(long ticks) {
        mTimeViewFragment.updateLapTime(ticks);
    }

    public List<TimeTracker.Lap> getLaps(){
        return mMainControllerFragment.getLaps();
    }
}
