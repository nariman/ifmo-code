package ru.ifmo.droid2016.tmdb.adapter;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.model.Movie;

public abstract class PopularMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ADAPTER";

    private static final int TYPE_MOVIE_HOLDER = 0;
    private static final int TYPE_LOADER_HOLDER = 1;

    private static final int PREV_PAGE = -1;
    private static final int INITIAL_PAGE = 0;
    private static final int NEXT_PAGE = 1;

    private final View.OnClickListener reloadBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setEnabled(false); // Double click safety
            onReloadPage();
        }
    };

    /**
     * Context (activity) instance
     */
    private Context context;

    /**
     * Recycler instance
     */
    private RecyclerView recycler;

    /**
     * Bundle with all important data, that may be transferred to other adapters
     */
    private PopularMoviesAdapterBundle bundle;

    public static class PopularMoviesAdapterBundle extends Fragment {
        /**
         * List of movies, that currently has adapter
         */
        private List<Movie> movies;

        /**
         * Threshold, at which needs to start loading prev/next page
         */
        private int visibleThreshold = 3;

        /**
         * First loaded page in the Recycler Adapter
         */
        private int firstLoadedPage;

        /**
         * Last loaded page in the Recycler Adapter
         */
        private int lastLoadedPage;

        /**
         * Indicates, that some data is already in loading process
         */
        private boolean isLoading;

        /**
         * Indicates, that first page has reached (nothing to load more)
         */
        private boolean isFirstPage;

        /**
         * Indicates, that last page has reached (nothing to load more)
         */
        private boolean isLastPage;

        /**
         * Page, that now is loading
         */
        private int waitingForPage;

        /**
         * Where to append a new data
         */
        private int waitingForDirection;

        /**
         * Indicates, that last attempt to loading data raises error
         */
        private boolean isError;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
    }

    public PopularMoviesAdapter(Context context, RecyclerView recycler) {
        this(context, recycler, 1); // Default initial page is 1
    }

    public PopularMoviesAdapter(Context context, RecyclerView recycler, int initialPage) {
        this(context, recycler, new PopularMoviesAdapterBundle());

        bundle.movies = new ArrayList<>();
        bundle.firstLoadedPage = bundle.lastLoadedPage = initialPage;

        bundle.isFirstPage = initialPage == 1;
        bundle.isLastPage = false;
        bundle.isLoading = true; // Initial page
        bundle.isError = false;

        onLoadPage(INITIAL_PAGE, initialPage);
    }

    public PopularMoviesAdapter(Context context, RecyclerView recycler, PopularMoviesAdapterBundle bundle) {
        this.context = context;
        this.recycler = recycler;
        this.bundle = bundle;

        if (bundle.isLoading) {
            onLoadPage(bundle.waitingForPage);
        }
    }

    /**
     * Returns bundle, that can be transferred to other adapter
     */
    public PopularMoviesAdapterBundle getBundle() {
        return bundle;
    }

    /**
     * Updates a status on the bottom of recycler view
     */
    private void updateStatus() {
        recycler.post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(getItemCount() - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bundle.movies.size() + 1; // Because loader holder, some magic w/ RV :)
    }

    @Override
    public int getItemViewType(int position) {
        if (position < bundle.movies.size()) {
            return TYPE_MOVIE_HOLDER;
        } else {
            return TYPE_LOADER_HOLDER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case TYPE_MOVIE_HOLDER:
                return new MovieHolder(inflater.inflate(R.layout.item_movie, parent, false));
            case TYPE_LOADER_HOLDER:
                return new LoaderHolder(inflater.inflate(R.layout.item_loader, parent, false));
            default:
                return null; // TODO: Throw IllegalArgument
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!bundle.isLoading) {
            // We starts w/ last page, because if RV is empty, loading to the bottom is prefer
            if (!bundle.isLastPage && position + bundle.visibleThreshold >= getItemCount()) {
                onLoadPage(NEXT_PAGE, bundle.lastLoadedPage + 1);
            } else if (!bundle.isFirstPage && position - bundle.visibleThreshold <= 0) {
                onLoadPage(PREV_PAGE, bundle.firstLoadedPage - 1);
            }
        }

        switch (holder.getItemViewType()) {
            case TYPE_MOVIE_HOLDER:
                ((MovieHolder) holder).onBind(bundle.movies.get(position));
                break;
            case TYPE_LOADER_HOLDER:
                ((LoaderHolder) holder).onBind(bundle.isLoading, bundle.isError, reloadBtnListener);
                break;
            default:
                break; // TODO: Throw IllegalArgument
        }
    }

    /**
     * Initiates a loading of a new data (page)
     *
     * @param waitingForDirection where to append a new data
     * @param waitingForPage      page that should be loaded
     */
    private void onLoadPage(int waitingForDirection, int waitingForPage) {
        bundle.isLoading = true;
        bundle.isError = false;

        bundle.waitingForDirection = waitingForDirection;
        bundle.waitingForPage = waitingForPage;

        updateStatus();
        onLoadPage(waitingForPage);
    }

    /**
     * Reloads a loader, if last attempt to load new data (page) finished with error
     */
    public void onReloadPage() {
        if (bundle.isError) {
            onLoadPage(bundle.waitingForDirection, bundle.waitingForPage);
        }
    }

    /**
     * Initiates a loading of a new data (page)
     *
     * @param page page that should be loaded
     */
    protected abstract void onLoadPage(int page);

    /**
     * Appends a new data (page)
     *
     * @param movies data to append
     */
    public void onLoadedPage(List<Movie> movies) {
        if (!bundle.isLoading) {
            return; // We aren't waiting for any data
        }

        if (null != movies) { // Data loading error checker
            switch (bundle.waitingForDirection) {
                case PREV_PAGE:
                    onLoadedPrevPage(movies);
                    break;
                case INITIAL_PAGE:
                    onLoadedInitialPage(movies);
                    break;
                case NEXT_PAGE:
                    onLoadedNextPage(movies);
                    break;
            }

            bundle.isError = false;
            bundle.isLoading = false;
        } else {
            bundle.isError = true; // But we're still waiting for data
        }

        updateStatus();
    }

    /**
     * Appends a new data to the start of the adapter
     *
     * @param data data to append
     */
    private void onLoadedPrevPage(final List<Movie> data) {
        if (0 == data.size()) {
            bundle.isFirstPage = true; // Result is ok + zero-length list = first page has reached
            return;
        }

        recycler.post(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (Movie movie : data) {
                    bundle.movies.add(i++, movie);
                }

                notifyItemRangeInserted(0, data.size());
            }
        });

        bundle.firstLoadedPage--;

        if (bundle.firstLoadedPage == 1) {
            bundle.isFirstPage = true; // First page has reached
        }
    }

    /**
     * Appends a new data to the adapter
     *
     * @param data data to append
     */
    private void onLoadedInitialPage(final List<Movie> data) {
        if (0 == data.size()) {
            return;
        }

        recycler.post(new Runnable() {
            @Override
            public void run() {
                for (Movie movie : data) {
                    bundle.movies.add(movie);
                }

                notifyDataSetChanged();
            }
        });
    }

    /**
     * Appends a new data to the end of the adapter
     *
     * @param data data to append
     */
    private void onLoadedNextPage(final List<Movie> data) {
        if (0 == data.size()) {
            bundle.isLastPage = true; // Result is ok + zero-length list = last page has reached
            return;
        }

        recycler.post(new Runnable() {
            @Override
            public void run() {
                for (Movie movie : data) {
                    bundle.movies.add(movie);
                }

                notifyItemRangeInserted(getItemCount() - data.size(), data.size());
            }
        });

        bundle.lastLoadedPage++;
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView overview;
        final SimpleDraweeView poster;
        final TextView rating;

        final LinearLayout ratingContainer;

        MovieHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            overview = (TextView) itemView.findViewById(R.id.overview);
            poster = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            rating = (TextView) itemView.findViewById(R.id.rating);

            poster.setAspectRatio(2f / 3);
            ratingContainer = (LinearLayout) itemView.findViewById(R.id.rating_container);
        }

        void onBind(Movie movie) {
            title.setText(movie.title.equals("") ? movie.originalTitle : movie.title);

            if (!movie.overview.equals("")) {
                overview.setText(movie.overview);
            }

            poster.setImageURI(movie.poster);
            rating.setText(movie.rating.toString());

            ratingContainer.setBackgroundResource(getRatingColor(movie.rating));
        }

        private int getRatingColor(double rating) {
            if (rating >= 9) {
                return R.color.movieRatingBlue;
            } else if (rating >= 7) {
                return R.color.movieRatingGreen;
            } else if (rating >= 5) {
                return R.color.movieRatingOrange;
            } else if (rating >= 3.5) {
                return R.color.movieRatingRed;
            } else {
                return R.color.movieRatingGray;
            }
        }
    }

    private static class LoaderHolder extends RecyclerView.ViewHolder {
        ProgressBar progress;
        TextView error;
        Button reload;

        LoaderHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);
            error = (TextView) itemView.findViewById(R.id.error);
            reload = (Button) itemView.findViewById(R.id.reload);
        }

        void onBind(boolean isLoading, boolean isError, View.OnClickListener reloadBtnListener) {
            progress.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            reload.setVisibility(View.GONE);

            if (isError) {
                error.setVisibility(View.VISIBLE);
                reload.setVisibility(View.VISIBLE);
                reload.setOnClickListener(reloadBtnListener);
                reload.setEnabled(true);
            } else if (isLoading) {
                progress.setVisibility(View.VISIBLE);
            }
        }
    }
}
