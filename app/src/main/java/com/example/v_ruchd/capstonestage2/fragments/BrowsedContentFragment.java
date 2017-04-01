package com.example.v_ruchd.capstonestage2.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.v_ruchd.capstonestage2.BrowsedContentActivity;
import com.example.v_ruchd.capstonestage2.R;
import com.example.v_ruchd.capstonestage2.adapters.BrowsedContentAdapter;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.OnBrowseContentItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowsedContentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowsedContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowsedContentFragment extends Fragment implements OnBrowseContentItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = BrowsedContentFragment.class.getSimpleName();
    private static final int NEWS_LOADER = 0;
    private RecyclerView mRecyclerView;
    private BrowsedContentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] dataSets = new String[13];


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Activity mActivity;
    private String selectedChannel;

    public BrowsedContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowsedContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowsedContentFragment newInstance(String param1, String param2) {
        BrowsedContentFragment fragment = new BrowsedContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browsed_content, container, false);

        dataSets[0] = "Item1";
        dataSets[1] = "Item2";
        dataSets[2] = "Item3";
        dataSets[3] = "Item4";
        dataSets[4] = "Item5";
        dataSets[5] = "Item6";
        dataSets[6] = "Item7";
        dataSets[7] = "Item8";
        dataSets[8] = "Item9";
        dataSets[9] = "Item10";
        dataSets[10] = "Item11";
        dataSets[11] = "Item12";
        dataSets[12] = "Item13";


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BrowsedContentAdapter(mActivity, dataSets);
        mAdapter.setRecylerViewItemListener(this);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        ((BrowsedContentActivity) mActivity).setActionBarTitle(getString(R.string.browsed_content));
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
                .onFragmentInteraction(null);
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

    public void onDataRetrieved(String selectedChannel) {
        this.selectedChannel=selectedChannel;
        getLoaderManager().restartLoader(NEWS_LOADER, null, this);
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
        void onFragmentInteraction(Uri uri);
    }


}
