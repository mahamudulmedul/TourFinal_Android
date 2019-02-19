package bd.org.bitm.mad.batch33.tourmate.fragment.Moment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bd.org.bitm.mad.batch33.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewGalleryFragment extends Fragment {
private String photoPath;
private ImageView imageView;

    public ViewGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_gallery, container, false);
        imageView = v.findViewById(R.id.fullImage);

        Bundle bundle = getArguments();
        photoPath = bundle.getString("photoPath");

        setPic();

        return v;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = 900;
        int targetH = 1200;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        Log.d("photo H and photo w", photoH+" "+photoW);
        Log.d("target H and target w", targetH+" "+targetW);

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }



}
