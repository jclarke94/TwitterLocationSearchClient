package com.servedonline.bellandroidassignment.activities.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.servedonline.bellandroidassignment.R;
import com.servedonline.bellandroidassignment.activities.Activity.MainActivity;
import com.servedonline.bellandroidassignment.activities.Entity.Status;
import com.servedonline.bellandroidassignment.activities.Network.GsonRequest;
import com.servedonline.bellandroidassignment.activities.Network.JSON.StatusResponse;
import com.servedonline.bellandroidassignment.activities.Utils.Listitem;

import java.util.ArrayList;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import static com.servedonline.bellandroidassignment.activities.Activity.MainActivity.CONSUMER_KEY;

public class SearchFragment extends Fragment {

    public static final String BACKSTACK_TAG = "_seacrhFragment";
    private static final String ITEMS_KEY = "_items";


    private static final int TYPE_SEARCH = 1;
    private static final int TYPE_LOADING = 2;

    private RecyclerView rvItems;
    private GridLayoutManager layoutManager;
    private SearchAdapter adapter;

    ArrayList<Listitem> items = new ArrayList<>();

    private Handler handler;

    private EditText etSearch;
    private ImageButton btnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = getLayoutInflater().inflate(R.layout.fragment_search, container, false);

        etSearch = v.findViewById(R.id.etSearchBar);
        btnSearch = v.findViewById(R.id.ibSearchBtn);

        ((MainActivity) getActivity()).hideFab();

        handler = new Handler();

        rvItems = (RecyclerView) v.findViewById(R.id.rvSearchItems);
        layoutManager = new GridLayoutManager(getContext(), 1);
        adapter = new SearchAdapter();
        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);


        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList(ITEMS_KEY);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etSearch.getText().equals(null) && !etSearch.getText().equals("")) {
                    setupItems();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.en_search_field_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(ITEMS_KEY, items);
    }

    private void setupItems() {
        if (items.size() == 0) {
            items.add(new LoadingItem());
            adapter.notifyDataSetChanged();
        } else {
            items.clear();
            items.add(new LoadingItem());
            adapter.notifyDataSetChanged();
        }

//        ((MainActivity) getActivity()).testConnection();

        //todo

        String keyword = etSearch.getText().toString();
        keyword = keyword.replaceAll("\\s", "");
        keyword = keyword.replaceAll("#", "%23");
        keyword = keyword.replaceAll("@", "%40");
        String url = ((MainActivity)getActivity()).getBaseUrl() +"?q=" + keyword + "&count=100";

        CommonsHttpOAuthConsumer commonsHttpOAuthConsumer = new CommonsHttpOAuthConsumer(((MainActivity)getActivity()).getConsumerKey(), ((MainActivity) getActivity()).getConsumerSecret());

        try {
            url = commonsHttpOAuthConsumer.sign(url);
            Log.d("VOLLEY", "URL = " + url);
        } catch (OAuthExpectationFailedException e) {
            Log.d("VOLLEY", "OAuthExpectationFailedException");
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            Log.d("VOLLEY", "OAuthMessageSignerException");
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            Log.d("VOLLEY", "OAuthCommunicationException");
            e.printStackTrace();
        }

        GsonRequest gsonRequest = new GsonRequest(url, StatusResponse.class, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                StatusResponse statusResponse = (StatusResponse) response;
                if (statusResponse.getStatuses().length > 0) {
                    Log.d("VOLLEY", String.valueOf(statusResponse.getStatuses().length));
                } else {
                    Log.d("VOLLEY", "no length");
                }

                items.clear();

                for (Status status: statusResponse.getStatuses()) {
                    items.add(new SearchItem(status));
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", "ERROR: " + error);

                Toast.makeText(getContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        ((MainActivity) getActivity()).getRequestQueue().add(gsonRequest);
    }

    public class SearchAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_SEARCH) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.listitem_search_item, parent, false);
                return new SearchViewHolder(v);
            } else {
                View v = getActivity().getLayoutInflater().inflate(R.layout.listitem_generic_loading, parent, false);
                return new LoadingViewHolder(v);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int viewType = items.get(position).getViewType();

            if (viewType == TYPE_SEARCH) {
                SearchViewHolder sHolder = (SearchViewHolder) holder;
                final SearchItem item = (SearchItem) items.get(position);

                sHolder.tvUserName.setText(item.getStatus().getUser().getName());
                sHolder.tvScreenName.setText(item.getStatus().getUser().getScreen_name());
                sHolder.tvTweetContent.setText(item.getStatus().getText());

                sHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = new Bundle();
                        args.putParcelable(ViewTweetFragment.STATUS_KEY, item.getStatus());
                        ViewTweetFragment fragment = new ViewTweetFragment();
                        fragment.setArguments(args);
                        ((MainActivity) getActivity()).navigate(fragment, BACKSTACK_TAG);
                    }
                });
            } else {
                LoadingViewHolder lHolder = (LoadingViewHolder) holder;
                LoadingItem item = (LoadingItem) items.get(position);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getViewType();
        }
    }

    @SuppressLint("ParcelCreator")
    private class SearchItem extends Listitem {

        private Status status;

        public SearchItem(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public int getViewType() {
            return TYPE_SEARCH;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUserName, tvScreenName, tvTweetContent;
        public Button btnMore;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTweetContent = itemView.findViewById(R.id.tvTweetContent);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }

    @SuppressLint("ParcelCreator")
    private class LoadingItem extends Listitem {

        @Override
        public int getViewType() {
            return TYPE_LOADING;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
