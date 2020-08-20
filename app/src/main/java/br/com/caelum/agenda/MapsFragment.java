package br.com.caelum.agenda;

import android.util.Log;
import br.com.caelum.locate.Localizador;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends SupportMapFragment {
    @Override
    public void onResume() {
        super.onResume();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                Localizador localizador = new Localizador(getActivity());
                LatLng local = localizador.getCoordenada("Travessa Municipal 132 Centro São Bernardo do Campo");

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(local);
                markerOptions.title("Coordenada da BatCaverna");
                markerOptions.draggable(false);

                googleMap.setTrafficEnabled(true);
                googleMap.addMarker(markerOptions);

                Log.i("MAPA", "Coordenada da BatCaverna" + local);

                centralizaNo(local, googleMap);
            }
        });
    }

    private void centralizaNo(LatLng local, GoogleMap mapa) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(local, 18);
        mapa.moveCamera(cameraUpdate);
    }
}
