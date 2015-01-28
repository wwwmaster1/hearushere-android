package nl.hearushere.app.data;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import nl.hearushere.app.Constants;

@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, fieldVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
public class Walk {

    public Walk() {
    }

    public static class List extends ArrayList<Walk> {

        private static final long serialVersionUID = 1L;

        public Walk findById(String id) {
            for (Walk w : this) {
                if (id.equals(w.getId())) {
                    return w;
                }
            }
            return null;
        }

    }

    @JsonProperty("_id")
    private String id;

    @JsonProperty
    private java.util.List<double[]> areas;

    @JsonProperty
    private String title;

    @JsonProperty
    private int radius;

    @JsonProperty
    private String image;

    @JsonProperty
    private String credits;

    @JsonProperty
    private String description;

    @JsonProperty("autoplay")
    private boolean tracksSynchronized;

    @JsonProperty
    private java.util.List<Track> sounds;

    private transient LatLng mCenter;

    public ArrayList<ArrayList<LatLng>> getPoints() {
        ArrayList<ArrayList<LatLng>> result = new ArrayList<>();
        for (double[] area : areas) {
            ArrayList<LatLng> list = new ArrayList<>();
            for (int i = 0; i < area.length; i += 2) {
                list.add(new LatLng(area[i], area[i + 1]));
            }
            result.add(list);
        }
        return result;
    }

    public String getImageUrl() {
        if (image == null) {
            return null;
        }
        return Constants.CONTENT_URL_PREFIX + image;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getCredits() {
        return credits;
    }

    public String getCreditsUrl() {
        if (credits == null) {
            return null;
        }
        return Constants.CONTENT_URL_PREFIX + credits.replace("walks/", "");
    }

    public String getDescription() {
        return description;
    }

    public boolean areTracksSynchronized() {
        return tracksSynchronized;
    }

    public void setTracksSynchronized(boolean tracksSynchronized) {
        this.tracksSynchronized = tracksSynchronized;
    }

    public String getFormattedDistanceTo(Location current) {

        if (current == null) {
            return "";
        }

        LatLng center = getCenter();

        float[] results = new float[3];
        Location.distanceBetween(center.latitude, center.longitude,
                current.getLatitude(), current.getLongitude(), results);
        double distance = results[0];

        if (distance > 1000) {
            return String.format("%.2f km", distance / 1000);
        } else {
            return String.format("%.0f m", distance);
        }
    }

    private LatLng getCenter() {
        if (mCenter == null) {
            double minLat = 0, minLng = 0, maxLat = 0, maxLng = 0;
            boolean first = true;
            for (double[] area : areas) {
                for (int i = 0; i < area.length; i += 2) {
                    if (first) {
                        minLat = area[i];
                        maxLat = area[i];
                        minLng = area[i + 1];
                        maxLng = area[i + 1];
                        first = false;
                        continue;
                    }
                    minLat = Math.min(minLat, area[i]);
                    maxLat = Math.max(maxLat, area[i]);
                    minLng = Math.min(minLng, area[i+1]);
                    maxLng = Math.max(maxLng, area[i + 1]);
                }
            }
            mCenter = new LatLng((maxLat-minLat)/2, (maxLng-minLng)/2);
        }
        return mCenter;
    }

    public int getRadius() {
        return radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.List<Track> getSounds() {
        return sounds;
    }
}