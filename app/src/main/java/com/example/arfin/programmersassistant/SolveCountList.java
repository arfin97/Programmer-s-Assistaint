package com.example.arfin.programmersassistant;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SolveCountList extends ArrayAdapter<SolveCount> {
    private Activity context;
    List<SolveCount> solvecountlist;

    public SolveCountList(Activity context, List<SolveCount> solvecountlist) {
        super(context, R.layout.layout_solvecount_list, solvecountlist);
        this.context = context;
        this.solvecountlist = solvecountlist;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_solvecount_list, null, true);

        TextView textViewCf = (TextView) listViewItem.findViewById(R.id.textViewSolveCountCf);
        TextView textViewUva = (TextView) listViewItem.findViewById(R.id.textViewSolveCountUva);
        TextView textViewLoj  = (TextView) listViewItem.findViewById(R.id.textViewSolveCountLoj);

        SolveCount solvecount = solvecountlist.get(position);
        textViewCf.setText(solvecount.getCf());
        textViewUva.setText(solvecount.getUva());
        textViewLoj.setText(solvecount.getLoj());


        return listViewItem;
    }
}
