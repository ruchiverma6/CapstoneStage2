package com.example.v_ruchd.capstonestage2.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.adapters.NewsListAdapter;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.OnDataItemClickListener;
import com.example.v_ruchd.capstonestage2.modal.Articles;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment implements OnDataItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = NewsListFragment.class.getSimpleName();
    private static final int NEWS_LOADER = 0;
    private RecyclerView mRecyclerView;
    private NewsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AppCompatActivity mActivity;
    private String selectedChannel;

    private TextView emptyDataTextView;

    private ProgressDialog mProgressDialog;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity =   (AppCompatActivity) getActivity();;
        showProgressDialog();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_layout, container, false);
        emptyDataTextView=(TextView)view.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new NewsListAdapter(mActivity);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter.setRecylerViewItemListener(this);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view, int position, Bundle bundle) {
        Log.v(TAG, "" + mRecyclerView.getChildAdapterPosition(view));


        ((OnFragmentInteractionListener) getActivity())
                .onFragmentInteraction(bundle);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = NewsContract.ArticleEntry.buildNewsArticleWithChannel(selectedChannel);
        return new CursorLoader(mActivity, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onDataRetrieved(String selectedChannel, List<Articles> articleList) {
        stopProgressDialog();

        if(!mActivity.isFinishing()) {
            this.selectedChannel = selectedChannel;
            if (articleList == null || articleList.size() == 0) {
                emptyDataTextView.setVisibility(View.VISIBLE);
            } else {
                emptyDataTextView.setVisibility(View.GONE);
                if(!mActivity.isFinishing()) {
                    getLoaderManager().restartLoader(NEWS_LOADER, null, this);
                }
            }
        }
    }

    public void onDataError(String message) {
        stopProgressDialog();
        emptyDataTextView.setVisibility(View.VISIBLE);
        emptyDataTextView.setText(message);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Bundle result);
    }

    /**
     * Method to show progress dialog
     */
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
        if(!mActivity.isFinishing()) {
            mProgressDialog.show();
        }
    }

    //Method to stop progress dialog.
    private void stopProgressDialog() {
        if (!mActivity.isFinishing() && null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
