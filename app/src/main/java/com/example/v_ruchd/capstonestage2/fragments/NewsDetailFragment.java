package com.example.v_ruchd.capstonestage2.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.v_ruchd.capstonestage2.NewsDetailActivity;
import com.example.v_ruchd.capstonestage2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewsDetailFragment extends Fragment {
    public static final String DETAIL_URI = "detail";
    private WebView webView;
    private OnFragmentInteractionListener mListener;
    private Activity mActivity;
    private String newsUrl;
    private String title;
    private ProgressDialog mProgressDialog;

    public NewsDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_detail, container, false);
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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        Bundle bundle = getArguments();
        if(null!=bundle) {
            newsUrl = bundle.getString(getString(R.string.source_url_key));

            title = bundle.getString(getString(R.string.title_key));
            showProgressDialog();
        }
        if(mActivity instanceof NewsDetailActivity) {
            ((NewsDetailActivity) mActivity).setActionBarTitle(title);
        }
        webView = (WebView) mActivity.findViewById(R.id.web_view);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.loadUrl(newsUrl);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            stopProgressDialog();
        }


    }

    /**
     * Method to show progress dialog
     */
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
        if(!mActivity.isFinishing() && !mProgressDialog.isShowing()) {
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
