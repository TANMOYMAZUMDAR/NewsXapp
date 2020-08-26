package com.example.newsx;

import android.widget.Filter;
import android.widget.Toast;

import java.util.ArrayList;

public class FilterSourceList extends Filter {
    private AdapterSourceList adapter;
    private ArrayList<ModeSourceList> filterList;

    public FilterSourceList(AdapterSourceList adapter, ArrayList<ModeSourceList> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //check constraint validity
        if(constraint !=null&& constraint.length()>0){
            //change to upper case to make it not case sensitive
            constraint=constraint.toString().toUpperCase();
            //store our filtered data
            ArrayList<ModeSourceList> filterModels=new ArrayList<>();
            for(int i=0;i<filterList.size();i++){
                if(filterList.get(i).getName().toUpperCase().contains(constraint)){
                    filterModels.add(filterList.get(i));
                }
            }

            results.count=filterModels.size();
            results.values=filterModels;

        }
        else{
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      adapter.sourceLists=(ArrayList<ModeSourceList>) results.values;
      adapter.notifyDataSetChanged();
    }
}
