var zoom;
var bound;
var maxamount;
var southWest = L.latLng(45.7300, 5.8000),
    northEast = L.latLng(47.9000, 10.600),
    bounds = L.latLngBounds(southWest, northEast);
var map = L.map('map',{
    maxBounds:bounds,
    invalidateSize: true
}).setView([46.8259, 8.2000], 9);

function unproject(latlng){
    var point = new L.Point(latlng.lng,latlng.lat);
    return L.Projection.SphericalMercator.unproject(point.divideBy(6378137));
}

map.on('moveend', function() {
    zoom = map.getZoom();
    bound = map.getBounds().toBBoxString();
});
$(document).ready(function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crosswalks/zoom=' + zoom + '&bounds=' + bound + '&maxamount=3',
        dataType:'json',
        method: 'get',
        success:function(response){
            var icon = new L.icon({
                iconUrl:"libs/script/leaflet/images/crossing.png",
                iconSize:[40,40]
            });
            var crossing = L.geoJson(response, {

                onEachFeature: function(features, layer){
                    if (features.properties.osm_node_id.constructor === Array){
                        layer.bindPopup("Zoom zu klein um Details anzuzeigen ");
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
            crossing.addTo(map);
        }
    });
});
L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy;<a href="http://openstreetmap.org">OpenStreetMap</a> contributors'
}).addTo(map);