package gyerby.umsl.edu.stopwatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gyerby on 3/6/2016.
 */
public class TimeTracker {
  //  private long mMainStartTimeInMillis;
    //private long mLapStartTimeInMillis;
    private long mMainTimeTicks, mLapTimeTicks;

    public List<Lap> getLaps() {
        return mLaps;
    }

    private ArrayList<Lap> mLaps;
    public TimeTracker(){
        mMainTimeTicks = mLapTimeTicks = 0;
        mLaps = new ArrayList<Lap>();
   }


    public void clearLaps(){
        mLaps.clear();
    }
    public void addLap() {
        mLaps.add(0,new Lap(mLapTimeTicks,mLaps.size()+1));
        mLapTimeTicks = 0;
     }



    public class Lap {
        private long mLapTime;
        private int mLapNumber;

        public Lap(long ticks,int lapnumber){
            mLapTime = ticks;
            mLapNumber = lapnumber;
        }
        public long getLapTime(){return mLapTime;}


        public int getLapNumber() {
            return mLapNumber;
        }
    }

    public long getLapTimeTicks() {
        return mLapTimeTicks;

    }

    public long getMainTimeTicks() {
        return mMainTimeTicks;
    }

    public void setLapTimeTicks(long value) {
        mLapTimeTicks = value;
    }

    public void setMainTimeTicks(long value) {
        mMainTimeTicks = value;
    }

    public static String getTimeFormat(long ticksInMS) {
        int ticksInSeconds = (int)ticksInMS/1000
                //, milliseconds = (int)((ticksInMS/10)%100)
                //,milliseconds = (int)(ticksInMS%100)
                ,milliseconds = (int)(ticksInMS%1000)
                , seconds = (int)(ticksInSeconds%60)
                , minutes = (int)((ticksInSeconds/60)%60);


        return String.format("%02d:%02d:%03d",minutes,seconds,milliseconds);


    }


}
