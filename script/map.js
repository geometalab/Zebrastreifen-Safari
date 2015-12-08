var bound;
var maxamount;
var crossing = new L.layerGroup();
var earthradius = 6378137;
var southWest = L.latLng(45.7300, 5.8000),
    northEast = L.latLng(47.9000, 10.600),
    bounds = L.latLngBounds(southWest, northEast);
var osm = L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy;<a href="http://openstreetmap.org">OpenStreetMap</a> contributors',
});
var map = L.map('map',{
    maxBounds:bounds,
    invalidateSize: true,
    minZoom: 7,
    layers: [osm, crossing],
    zoomControl: false
}).setView([46.8259, 8.2000], 9);
var hash = new L.hash(map);

function unproject(latlng){
    var point = new L.Point(latlng.lng,latlng.lat);
    return L.Projection.SphericalMercator.unproject(point.divideBy(earthradius));
}


map.on('moveend', function() {
    var bounds = map.getBounds();
    var min = L.latLng(bounds._southWest.lat, bounds._southWest.lng);
    var max = L.latLng(bounds._northEast.lat, bounds._northEast.lng);

    var mercator_min = L.Projection.SphericalMercator.project(min);
    var mercator_max = L.Projection.SphericalMercator.project(max);

    var minx = mercator_min.x * earthradius,
        miny = mercator_min.y * earthradius,
        maxx = mercator_max.x * earthradius,
        maxy = mercator_max.y * earthradius;

    bound = [minx, miny, maxx, maxy].toString();
});
map.on('moveend', function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crosswalks/&bounds=' + bound + '&maxamount=3',
        dataType:'json',
        method: 'get',
        success:function(response){
            var icon = new L.icon({
                iconUrl:"libs/script/leaflet/images/crossing.png",
                iconSize:[40,40]
            });
            crossing.clearLayers();
            var tmpCrossing = L.geoJson(response, {

                onEachFeature: function(features, layer){
                    if (features.properties.hasOwnProperty('amount')){
                        layer.bindPopup("Zoom zu klein um Details anzuzeigen - Anzahl: " + features.properties.amount);
                    } else {
                        layer.bindPopup(features.properties.osm_node_id.toString() + '<br />' +
                            '<a href="crossing.html?crosswalk=' + features.properties.osm_node_id + '">Mehr Anzeigen...</a>');
                    }

                },
                // add the points to the layer
                pointToLayer: function (feature, latlng) {
                    var newlatlng = unproject(latlng);
                    //console.log(latlng);
                    return L.marker(newlatlng, {
                        icon:icon
                    });
                }

            });
            tmpCrossing.addTo(crossing);
        }
    });
});


var baseMaps = {
    "OSM": osm
};
var overlayMaps = {
    "Zebrastreifen": crossing
};
var geosearch = new L.Control.GeoSearch({
    position:"topleft",
    provider: new L.GeoSearch.Provider.OpenStreetMap({countrycodes: 'ch'}),
    searchLabel: 'Address',
    notFoundMessage: 'No result found...'
}).addTo(map);
L.control.zoom().addTo(map);
L.control.layers(baseMaps, overlayMaps).addTo(map);