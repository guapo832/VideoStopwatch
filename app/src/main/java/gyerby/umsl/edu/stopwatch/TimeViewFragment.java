package gyerby.umsl.edu.stopwatch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeViewFragment extends Fragment {

    private final static int HOURS_IN_MS = 36000000;
    private final static int MIN_IN_MS = 60000;

   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MAINTIME = "edu.umsl.gyerby.stopwatch.maintime";
    private static final String ARG_LAPTIME = "edu.umsl.gyerby.stopwatch.laptime";
    private static final String RUN_STATE_KEY ="edu.umsl.gyerby.stopwatch.runstate_label" ;
    private static final String MAIN_TIME_KEY = "mtk";
    private static final String LAP_TIME_KEY = "ltk";

    private Button mStartStopButton = null;
    private Button mResetLapButton = null;
    private long mMainTime =0;
    private long mLapTime =0;
    private CharSequence mMainTimeCharSeq = null;
            private CharSequence mLapTimeCharSeq =null;

    private TextView mMainTimeTextView = null;
    private TextView mLapTimeTextView = null;
    private TimeViewFragmentDelegate mDelegate;

    public enum RunState {
        running,
        stopped
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RUN_STATE_KEY, this.mRunState);
        outState.putCharSequence(MAIN_TIME_KEY, this.mMainTimeTextView.getText());
        outState.putCharSequence(LAP_TIME_KEY, this.mLapTimeTextView.getText());
    }

    private RunState mRunState = null;
    interface TimeViewFragmentDelegate {
        void startTime();
        void resetTime();
        void stopTime();
        void newLap();
    }


    public void setDelegate(TimeViewFragmentDelegate delegate) {
        mDelegate = delegate;
    }
    public void setRunState(RunState value){
        this.mRunState = value;
        setButtonListeners(value);
    }
    private void setButtonListeners(RunState value){
        if(mStartStopButton !=null) {
            switch (value) {
                case stopped:
                    mStartStopButton.setOnClickListener(startTime());
                    mResetLapButton.setOnClickListener(resetTime());
                    mStartStopButton.setText(R.string.startstopbutton_start);
                    mResetLapButton.setText(R.string.lapresetbutton_reset);
                    break;
                case running:
                    mStartStopButton.setOnClickListener(stopTime());
                    mResetLapButton.setOnClickListener(newLap());
                    mStartStopButton.setText(R.string.startstopbutton_stop);
                    mResetLapButton.setText(R.string.lapresetbutton_lap);
                    break;
            }
        }
    }

    private View.OnClickListener resetTime() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDelegate !=null){
                    mDelegate.resetTime();
                }
            }
        };
    }

    private View.OnClickListener newLap() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDelegate !=null){
                    mDelegate.newLap();
                }
            }
        };
    }


    public TimeViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mainTime provides total number of milliseconds since main time started. .
     * @param lapTime provides total number of milliseconds since laptime was started..
     * @return A new instance of fragment TimeFragment.
     */
     public static TimeViewFragment newInstance(long mainTime, long lapTime) {
        TimeViewFragment fragment = new TimeViewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MAINTIME, mainTime);
        args.putLong(ARG_LAPTIME, lapTime);
        fragment.setRunState(RunState.stopped);
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        if(savedInstanceState !=null){
            this.mRunState = (RunState)savedInstanceState.getSerializable(RUN_STATE_KEY);
            this.mLapTimeCharSeq= savedInstanceState.getCharSequence(LAP_TIME_KEY);
            this.mMainTimeCharSeq = savedInstanceState.getCharSequence(MAIN_TIME_KEY);

        } else {
            this.mLapTimeCharSeq = (CharSequence)TimeTracker.getTimeFormat(0);
            this.mMainTimeCharSeq = (CharSequence)TimeTracker.getTimeFormat(0);



        }
        mStartStopButton = (Button) view.findViewById(R.id.startstopbutton);
        mResetLapButton = (Button) view.findViewById(R.id.lapresetbutton);
        if(this.mRunState==null) this.mRunState = RunState.stopped;
            setRunState(this.mRunState);

        mMainTimeTextView = (TextView) view.findViewById(R.id.maintime_textview);
        mLapTimeTextView= (TextView) view.findViewById(R.id.laptime_textview);

        if (getArguments() != null) {
            mMainTime = getArguments().getLong(ARG_MAINTIME);
            mLapTime = getArguments().getLong(ARG_LAPTIME);
        }
        updateMainTime(mMainTime);
        updateLapTime(mLapTime);
        mMainTimeTextView.setText(mMainTimeCharSeq);
        mLapTimeTextView.setText(mLapTimeCharSeq);

        mLapTimeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));



        return view;
    }

    public void updateMainTime(long ms){
        mMainTimeTextView.setText(TimeTracker.getTimeFormat(ms));
    }

    public void updateLapTime(long ms) {
        mLapTimeTextView.setText(TimeTracker.getTimeFormat(ms));
    }

    private View.OnClickListener stopTime() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDelegate != null) {
                    mDelegate.stopTime();
                    setRunState(RunState.stopped);
                    mStartStopButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorGo));
                }
            }
        };
    }

    private View.OnClickListener startTime() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDelegate != null) {
                    mDelegate.startTime();
                    setRunState(RunState.running);
                    mStartStopButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorStop));
                }
            }
        };
    }


}
