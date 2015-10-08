
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


//$.ajax({
//    url:url,
//    dataType:'json',
//    method: 'get',
//    data: {type: 'crossing'},
//    success:function(response){
//     var icon = new L.icon({
//     iconUrl:"libs/script/leaflet/images/crossing.png",
//     iconSize:[20,20]
//     });
//        var crossing = L.geoJson(response, {
//
//            // add the points to the layer, but first reproject the coordinates to WGS 84
//            pointToLayer: function (feature, latlng) {
//                var newlatlng = unproject(latlng);
//                return L.marker(newlatlng);
//            }
//
//        });
//        crossing.addTo(map);
//    }
//});
//var geojsonFeature = {
//    "type": "Feature",
//    "properties": {
//        "name": "Coors Field",
//        "popupContent": "This is where the Rockies play!"
//    },
//    "geometry": {
//        "type": "Point",
//        "coordinates": [8.2000, 46.8259]
//    }
//};
//
//L.geoJson(geojsonFeature).addTo(map);


L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy;<a href="http://openstreetmap.org">OpenStreetMap</a> contributors',
}).addTo(map);