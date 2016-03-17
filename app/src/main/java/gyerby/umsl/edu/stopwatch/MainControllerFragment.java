package gyerby.umsl.edu.stopwatch;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.util.List;

/**
 * Created by gyerby on 3/6/2016.
 */
public class MainControllerFragment extends Fragment {
    private TimeTracker mTimeTrackerModel;
    private long mLapStartTimeInMillis,mMainStartTimeInMillis;

    public long getmMainTimeTicks() {
        return mMainTimeTicks;
    }

        public long getmLapTimeTicks() {
        return mLapTimeTicks;
    }

    public void setmLapTimeTicks(long mLapTimeTicks) {
        this.mLapTimeTicks = mLapTimeTicks;
    }

    private long mMainTimeTicks = 0;
    private long mLapTimeTicks = 0;
    private MainFragmentListener mListener;
    private Handler mHandler;
    private boolean isRunning;
    private boolean isReset;
    interface MainFragmentListener {
        void updateMainTime(long ticks);
        void updateLapTime(long ticks);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeTrackerModel = new TimeTracker();
        mLapStartTimeInMillis = mMainStartTimeInMillis = System.currentTimeMillis();
        setRetainInstance(true); //maintain state.
    }

    @Override
    public void onStop() {
        super.onStop();
        mListener = null;
        if (isRunning) {
            mHandler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity activity = getActivity();
        if (activity instanceof MainFragmentListener) {
            mListener = (MainFragmentListener) activity;
        }
        if (isRunning) {
            mHandler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateTimeInListener();
            updateTimes();
            mHandler.postDelayed(runnable, 1);
        }
    };

    private void updateTimeInListener() {
        if (mListener != null) {
            mListener.updateLapTime(mTimeTrackerModel.getLapTimeTicks());
            mListener.updateMainTime(mTimeTrackerModel.getMainTimeTicks());
        }
    }

    public void start() {
        if(!isReset){
            mLapStartTimeInMillis = mMainStartTimeInMillis = System.currentTimeMillis() - mMainTimeTicks;
           updateModel();

        } else {
            mMainStartTimeInMillis = mLapStartTimeInMillis = System.currentTimeMillis();
        }
         isReset = false;
         if (mHandler == null) {
            mHandler = new Handler();
            mHandler.postDelayed(runnable, 1);
            isRunning = true;
         }
    }

    public void stop() {
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
            isRunning = false;
            mHandler = null;
        }
    }

    public void reset(){
       mLapTimeTicks = 0;
       mMainTimeTicks = 0;
       mMainStartTimeInMillis = mLapStartTimeInMillis = System.currentTimeMillis();
        mTimeTrackerModel.clearLaps();
       updateModel();
        updateTimeInListener();
        isReset = true;
    }

    public void newLap(){
        mLapStartTimeInMillis = System.currentTimeMillis();
        mTimeTrackerModel.addLap();
        updateModel();
    }

    private void updateModel() {
        mTimeTrackerModel.setLapTimeTicks(mLapTimeTicks);
        mTimeTrackerModel.setMainTimeTicks(mMainTimeTicks);
    }

    public void updateTimes() {
        long currentTimeInMillis = System.currentTimeMillis();

            mMainTimeTicks = currentTimeInMillis - mMainStartTimeInMillis;
            mLapTimeTicks = currentTimeInMillis - mLapStartTimeInMillis;
           updateModel();
    }

    public List<TimeTracker.Lap> getLaps(){
        return mTimeTrackerModel.getLaps();
    }
}
