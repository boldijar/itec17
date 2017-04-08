package com.bolnizar.code.pages.gallery;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.PhotoRecord;
import com.bumptech.glide.Glide;
import com.orm.SugarRecord;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Holder> {

    private List<PhotoRecord> mItems;
    private final GalleryListener mListener;

    GalleryAdapter(GalleryListener listener) {
        mListener = listener;
        mItems = SugarRecord.listAll(PhotoRecord.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PhotoRecord photoRecord = mItems.get(position);
        Glide.with(holder.image.getContext()).load(photoRecord.path).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            clickedItem(v, photoRecord);
        });
    }

    private void clickedItem(View v, PhotoRecord record) {
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.inflate(R.menu.menu_gallery_options);
        menu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_share) {
                mListener.onShareImage(record.path);
            }
            if (item.getItemId() == R.id.action_open) {
                mListener.onOpenImage(record.path);
            }
            if (item.getItemId()==R.id.action_directions){
                mListener.onNavigate(record);
            }
            return true;
        });
        menu.show();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @SuppressWarnings("WeakerAccess")
    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_imageview)
        ImageView image;

        public Holder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    public interface GalleryListener {
        void onShareImage(String path);

        void onOpenImage(String path);

        void onNavigate(PhotoRecord record);
    }
}
