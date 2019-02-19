package bd.org.bitm.mad.batch33.tourmate.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bd.org.bitm.mad.batch33.tourmate.R;

import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.Utils.ForecastWeather;
import bd.org.bitm.mad.batch33.tourmate.model.Moment;

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.MomentViewHolder> {
        private Context context;
        private List<Moment> moments;

        public MomentListAdapter(List<Moment> moments) {
                this.context = context;
                this.moments = moments;
        }

        @NonNull
        @Override
        public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_list_item,parent,false);
               return new MomentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MomentViewHolder holder, int position) {
                Moment moment = moments.get(position);
                Bitmap bitmap = BitmapFactory.decodeFile(moment.getPhotoPath());
                holder.imageView.setImageBitmap(bitmap);

                holder.titleTv.setText("Capture 1");
                holder.dateTv.setText(moment.getDate()+"");
                holder.fileNameTv.setText(moment.getFileName()+""+moment.getFormatName());
        }

        @Override
        public int getItemCount() {
                return moments.size();
        }

        public class MomentViewHolder extends RecyclerView.ViewHolder {
                private ImageView imageView;
                TextView fileNameTv,titleTv,dateTv;
                public MomentViewHolder(View itemView) {
                        super(itemView);
                        imageView = itemView.findViewById(R.id.momentIv);
                        fileNameTv = itemView.findViewById(R.id.fileNameTv);
                        titleTv = itemView.findViewById(R.id.momentTitleTv);
                        dateTv = itemView.findViewById(R.id.dateTv);
                }
        }
}
