package com.bolnizar.code.pages.gallery;

import com.bolnizar.code.R;
import com.bolnizar.code.pages.map.ImageFragment;
import com.bolnizar.code.view.fragments.BaseFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends BaseFragment implements GalleryAdapter.GalleryListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private MenuItem mToolbarItem;
    private boolean mIsGrid = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.gallery));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        initList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gallery, menu);
        mToolbarItem = menu.findItem(R.id.action_gallery_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gallery_list) {
            mIsGrid = !mIsGrid;
            mToolbarItem.setIcon(mIsGrid ? R.drawable.ic_view_stream_black_24dp : R.drawable.ic_view_module_black_24dp);
            initList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), mIsGrid ? 2 : 1);
        mRecycler.setLayoutManager(manager);
        mRecycler.setAdapter(new GalleryAdapter(this));
    }

    @Override
    public void onShareImage(String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    @Override
    public void onOpenImage(String path) {
        ImageFragment.newInstance(path).show(getChildFragmentManager(), "aleaalea");
    }
}
