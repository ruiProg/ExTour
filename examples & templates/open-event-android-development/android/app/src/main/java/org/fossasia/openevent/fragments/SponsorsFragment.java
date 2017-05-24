package org.fossasia.openevent.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.fossasia.openevent.OpenEventApp;
import org.fossasia.openevent.R;
import org.fossasia.openevent.adapters.SponsorsListAdapter;
import org.fossasia.openevent.data.Sponsor;
import org.fossasia.openevent.dbutils.DataDownloadManager;
import org.fossasia.openevent.dbutils.DbSingleton;
import org.fossasia.openevent.events.SponsorDownloadEvent;
import org.fossasia.openevent.utils.NetworkUtils;
import org.fossasia.openevent.utils.ShowNotificationSnackBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by MananWason on 05-06-2015.
 */
public class SponsorsFragment extends BaseFragment {

    private List<Sponsor> mSponsors = new ArrayList<>();
    private SponsorsListAdapter sponsorsListAdapter;

    @BindView(R.id.txt_no_sponsors)
    TextView noSponsorsView;
    @BindView(R.id.sponsor_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list_sponsors)
    RecyclerView sponsorsRecyclerView;

    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private AppBarLayout.LayoutParams layoutParams;
    private int SCROLL_OFF = 0;

    private CompositeDisposable compositeDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        OpenEventApp.getEventBus().register(this);
        compositeDisposable = new CompositeDisposable();

        final DbSingleton dbSingleton = DbSingleton.getInstance();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        sponsorsListAdapter = new SponsorsListAdapter(getContext(), mSponsors,
                getActivity(), true);
        sponsorsRecyclerView.setAdapter(sponsorsListAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        sponsorsRecyclerView.setLayoutManager(linearLayoutManager);
        sponsorsRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.getChildCount() - 1) {
                    layoutParams.setScrollFlags(SCROLL_OFF);
                    toolbar.setLayoutParams(layoutParams);
                }
                sponsorsRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        //scrollup shows actionbar
        sponsorsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy < 0){
                    AppBarLayout appBarLayout;
                    appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
                    appBarLayout.setExpanded(true);
                }
            }
        });

        compositeDisposable.add(dbSingleton.getSponsorListObservable()
                .subscribe(new Consumer<ArrayList<Sponsor>>() {
                    @Override
                    public void accept(@NonNull ArrayList<Sponsor> sponsors) throws Exception {
                        mSponsors.clear();
                        mSponsors.addAll(sponsors);

                        sponsorsListAdapter.notifyDataSetChanged();
                        handleVisibility();
                    }
                }));

        return view;
    }

    private void handleVisibility() {
        if (sponsorsListAdapter.getItemCount() != 0) {
            noSponsorsView.setVisibility(View.GONE);
            sponsorsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noSponsorsView.setVisibility(View.VISIBLE);
            sponsorsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.list_sponsors;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OpenEventApp.getEventBus().unregister(this);
        if(compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        toolbar.setLayoutParams(layoutParams);
    }

    @Subscribe
    public void sponsorDownloadDone(SponsorDownloadEvent event) {
        if(swipeRefreshLayout == null)
            return;

        swipeRefreshLayout.setRefreshing(false);
        if (event.isState()) {
            sponsorsListAdapter.refresh();
            Timber.d("Refresh done");
        } else {
            Snackbar.make(swipeRefreshLayout, getActivity().getString(R.string.refresh_failed), Snackbar.LENGTH_LONG).setAction(R.string.retry_download, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refresh();
                }
            }).show();
            Timber.d("Refresh not done");
        }
    }

    private void refresh() {
        NetworkUtils.checkConnection(new WeakReference<>(getContext()), new NetworkUtils.NetworkStateReceiverListener() {
            @Override
            public void activeConnection() {
                //Internet is working
                DataDownloadManager.getInstance().downloadSponsors();
            }

            @Override
            public void inactiveConnection() {
                //Device is connected to WI-FI or Mobile Data but Internet is not working
                ShowNotificationSnackBar showNotificationSnackBar = new ShowNotificationSnackBar(getContext(),getView(),swipeRefreshLayout) {
                    @Override
                    public void refreshClicked() {
                        refresh();
                    }
                };
                //show snackbar will be useful if user have blocked notification for this app
                showNotificationSnackBar.showSnackBar();
                //show notification (Only when connected to WiFi)
                showNotificationSnackBar.buildNotification();
            }

            @Override
            public void networkAvailable() {
                // Network is available but we need to wait for activity
            }

            @Override
            public void networkUnavailable() {
                Snackbar.make(swipeRefreshLayout, getActivity().getString(R.string.refresh_failed), Snackbar.LENGTH_LONG).setAction(R.string.retry_download, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refresh();
                    }
                }).show();
                OpenEventApp.getEventBus().post(new SponsorDownloadEvent(true));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
