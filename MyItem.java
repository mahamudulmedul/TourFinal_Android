package bd.org.bitm.mad.batch33.tourmate;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Mobile App Develop on 5/19/2018.
 */

public class MyItem implements ClusterItem {
    private LatLng latLng;
    private String title;
    private String snippet;

    public MyItem(LatLng latLng, String title, String snippet) {
        this.latLng = latLng;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
