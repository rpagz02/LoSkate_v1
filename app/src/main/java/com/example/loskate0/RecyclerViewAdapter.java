package com.example.loskate0;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<MarkerInfo> mDataSet;



    RecyclerViewAdapter(List<MarkerInfo> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarkerInfo mI = mDataSet.get(position);
        holder.mTextView.setText(mI.getID());
        holder.mTextView2.setText(mI.getNotes());
        // Following 3 lines of code decode a string to bitmap
        Bitmap pic;
        byte[] decodedBytes = Base64.decode(mI.getImage(), 0);
        pic = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        holder.mImageView.setImageBitmap(pic);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView mTextView2;
        ImageView mImageView;

        ViewHolder(View v) {
            super(v);
            mTextView = itemView.findViewById(R.id.title);
            mTextView2 = itemView.findViewById(R.id.notes);
            mImageView = itemView.findViewById(R.id.image);
        }
    }
}