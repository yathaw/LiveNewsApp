package com.example.live.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.live.R;
import com.example.live.adapter.TechcrunchAdapter;
import com.example.live.api.ApiClient;
import com.example.live.api.ApiInterface;
import com.example.live.models.techcrunch.ArticlesItem;
import com.example.live.models.techcrunch.Techcrunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopheadlinesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    public static final String API_KEY = "c227cbd759164c75979e47249b3b93c4";

    private RecyclerView techcrunch_recyclerView;
    private List<ArticlesItem> techarticlesItems = new ArrayList<>();
    private TechcrunchAdapter techcrunchAdapter;
    private Activity context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage, techcrunch_title, techcrunch_start_divider;
    private Button btnRetry, bthShare;
    private Timer timer;
    private int position =0;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_topheadline, container, false);

        swipeRefreshLayout = rootview.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        // Techcrunch Recycler View
        techcrunch_recyclerView = rootview.findViewById(R.id.htechcrunch_recycler_view);
        LinearLayoutManager techcrunch_layoutManager = new LinearLayoutManager(getActivity());
        techcrunch_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        techcrunch_recyclerView.setLayoutManager(techcrunch_layoutManager);
        techcrunch_recyclerView.setItemAnimator(new DefaultItemAnimator());
        techcrunch_recyclerView.setNestedScrollingEnabled(false);


        TechcrunchOnLoadingSwipeRefresh();

        techcrunch_title = rootview.findViewById(R.id.techcrunch_title);
        context = getActivity();



        return rootview;
    }

    private void TechcrunchOnLoadingSwipeRefresh()
    {
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadTechcrunch();
                    }
                }
        );
    }

    public void LoadTechcrunch()
    {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String sources = String.valueOf("techcrunch");

        Call<Techcrunch> call;
        call = apiInterface.getTechcrunch(sources, API_KEY);

        call.enqueue(new Callback<Techcrunch>() {
            @Override
            public void onResponse(Call<Techcrunch> call, Response<Techcrunch> response)
            {
                if (response.isSuccessful() && response.body().getArticles() != null)
                {
                    if (!techarticlesItems.isEmpty())
                    {
                        techarticlesItems.clear();
                    }

                    techarticlesItems = response.body().getArticles();
                    techcrunchAdapter = new TechcrunchAdapter(techarticlesItems);

                    techcrunch_recyclerView.setAdapter(techcrunchAdapter);
                    techcrunchAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                    techcrunch_title.setVisibility(View.VISIBLE);

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

//                    techcrunch_initListener();
                }
            }

            class SliderTimer extends TimerTask
            {

                @Override
                public void run()
                {
                    final boolean flag = true;
                    final boolean[] end = new boolean[1];
                    while (flag && (getActivity()!= null))
                    {
                        try
                        {
                            Thread.sleep(6000);
                            getActivity().runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if(position == techcrunchAdapter.getItemCount() -1 )
                                    {
                                        end[0] = true;
                                    }
                                    else if (position ==0)
                                    {
                                        end[0] = false;
                                    }

                                    if (!end[0])
                                    {
                                        position ++;
                                    }
                                    else
                                    {
                                        position --;
                                    }
                                    techcrunch_recyclerView.smoothScrollToPosition(position);
                                }
                            });
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }



            @Override
            public void onFailure(Call<Techcrunch> call, Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);
                techcrunch_title.setVisibility(View.INVISIBLE);

                String errorCode;
                switch (call.hashCode()) {
                    case 404:
                        errorCode = "404 Not found";
                        break;

                    case 500:
                        errorCode = "500 server broken";
                        break;

                    default:
                        errorCode = "unknown error";
                        break;
                }

                showErrorMessage(
                        R.drawable.nosearchresult,
                        "No Result",
                        "Please try again!\n"+
                                errorCode
                );
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Top Head Lines");
    }

    @Override
    public void onRefresh() {

    }

    private void showErrorMessage(int imageView, String title, String message) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TechcrunchOnLoadingSwipeRefresh();
            }
        });
    }
}
