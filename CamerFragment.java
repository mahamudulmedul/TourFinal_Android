package bd.org.bitm.mad.batch33.tourmate.fragment.Camera;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import bd.org.bitm.mad.batch33.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CamerFragment extends Fragment {
private Button takePhoto, imageUpload;
private ImageView imageView;

    public CamerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_camer, container, false);
        takePhoto = v.findViewById(R.id.takePhoto);
        imageUpload = v.findViewById(R.id.uploadImage);
        imageView = v.findViewById(R.id.imageView);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 111);
                }
            }
        });

        return v;
    }

}
