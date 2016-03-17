package gyerby.umsl.edu.stopwatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gyerby on 3/7/2016.
 */
public class LapListFragment extends Fragment {
    private RecyclerView mLapRecyclerView;
    private LapAdapter mAdapter;

    private class LapHolder extends RecyclerView.ViewHolder{

        private TextView mLapTimeTextView;
        private TextView mLapNumberTextView;
        public LapHolder(View itemView){
            super(itemView);
            mLapTimeTextView = (TextView) itemView.findViewById(R.id.laptime_textview);
            mLapNumberTextView= (TextView) itemView.findViewById(R.id.lapnumber_textview);


        }
        public void bindLap(TimeTracker.Lap lap){
            mLapTimeTextView.setText(TimeTracker.getTimeFormat(lap.getLapTime()));
            mLapNumberTextView.setText(String.format("Lap %d",lap.getLapNumber()));
        }
    }
    private class LapAdapter extends RecyclerView.Adapter<LapHolder>{
        private List<TimeTracker.Lap> mLaps;
        public LapAdapter(List<TimeTracker.Lap> laps){
            mLaps = laps;
        }

        @Override
        public LapHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                   .inflate(R.layout.lap_item_layout, parent, false);
            return new LapHolder(view);
        }

        @Override
        public void onBindViewHolder(LapHolder holder, int position) {
            TimeTracker.Lap lap = mLaps.get(position);
            holder.bindLap(lap);
        }

        @Override
        public int getItemCount() {

           return mLaps.size();
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_lap_list,container,false);
        mLapRecyclerView = (RecyclerView)view.findViewById(R.id.lap_recycler_view);
        mLapRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLapRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        mLapRecyclerView.setHasFixedSize(true);
        updateUI();
        return view;
    }

    public void updateUI(){
        MainActivity mainActivity = (MainActivity) getActivity();
        List<TimeTracker.Lap> laps = mainActivity.getLaps();
        mAdapter = new LapAdapter(laps);
        mLapRecyclerView.setAdapter(mAdapter);
    }
}
