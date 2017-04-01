package com.example.v_ruchd.capstonestage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.v_ruchd.capstonestage2.Constants;
import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.OnBrowseContentItemClickListener;

/**
 * Created by v-ruchd on 3/24/2017.
 */

public class InputSelectionAdapter extends RecyclerView.Adapter<InputSelectionAdapter.ViewHolder> {

    private String[] mInpuSelectionEnteries;
    private final LayoutInflater mLayoutInflater;
    //private Cursor cursor;
    private OnBrowseContentItemClickListener onBrowseContentItemClickListener;

    public InputSelectionAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mInpuSelectionEnteries = context.getResources().getStringArray(R.array.inputcategoryselectionenteries);
    }

    public void setRecyclerViewListener(OnBrowseContentItemClickListener onBrowseContentItemClickListener) {
        this.onBrowseContentItemClickListener = onBrowseContentItemClickListener;
    }

    @Override
    public InputSelectionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // View viewItem = mLayoutInflater.inflate(R.layout.chat_input_selection_item, null);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_input_selection_item, viewGroup, false);
        view.setFocusable(true);


        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InputSelectionAdapter.ViewHolder holder, int position) {
       // cursor = getItem(position);
        String categoryType =mInpuSelectionEnteries[position]; //cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE));
        holder.mCategoryBtn.setText(categoryType);

    }

    public void setData(String[] stringArray) {
        this.mInpuSelectionEnteries=stringArray;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //    private RelativeLayout mLinearLayout;
        public Button mCategoryBtn;

        public ViewHolder(View itemView) {

            super(itemView);
           // mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.layout_parent);
            mCategoryBtn = (Button) itemView.findViewById(R.id.category_item_btn);
            mCategoryBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
          //  cursor = getItem(position);
            String categoryType = mInpuSelectionEnteries[position];//cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE));
            Bundle bundle = new Bundle();
            bundle.putInt("viewtype", Constants.INPUT_CATEGORY_TYPE);
            bundle.putString("selecteddata", categoryType);
            if (null != onBrowseContentItemClickListener) {
                onBrowseContentItemClickListener.onClick(v, position, bundle);
            }
        }
    }

    //public void swapCursor(final Cursor cursor) {
        //this.cursor = cursor;
       // this.notifyDataSetChanged();
  //  }

    @Override
    public int getItemCount() {
        return mInpuSelectionEnteries.length;
      //  return this.cursor != null
          //      ? this.cursor.getCount()
          //      : 0;
    }

    public String getItem(final int position) {
        if(null!=mInpuSelectionEnteries && mInpuSelectionEnteries.length>0){
          return mInpuSelectionEnteries[position];
        }
        return null;
       /* if (this.cursor != null && !this.cursor.isClosed()) {
            this.cursor.moveToPosition(position);
        }

        return this.cursor;*/
    }
}
