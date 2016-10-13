package net.john.mapsandbox;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by John on 7/25/2016.
 */
public class SubmissionDialogFragment extends DialogFragment {
    //markeroptions are set when instantiated
    private MarkerOptions markerOptions;
    private LatLng mLatLng;

    private Button mTeeButton;
    private Button mHoleButton;
    private Button mNodeButton;

    static SubmissionDialogFragment newInstance(LatLng latLng, Marker markerOptions) {
        SubmissionDialogFragment subFrag = new SubmissionDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("latlng", latLng);
        args.putParcelable("markerPptions", markerOptions)
        subFrag.setArguments(args);

        return subFrag;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        markerOptions = getArguments().getParcelable("markerOptions");
        mLatLng = getArguments().getParcelable("latlng");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_submission, container, false);

        TextView latLngLabel = (TextView) v.findViewById(R.id.submission_latlng);
        latLngLabel.setText(mLatLng.toString());

        mTeeButton = (Button) v.findViewById(R.id.tee_butt);
        mTeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mHoleButton = (Button) v.findViewById(R.id.hole_butt);
        mNodeButton = (Button) v.findViewById(R.id.node_butt);


        return v;
    }

}
