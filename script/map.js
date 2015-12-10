var bound;
var maxamount;
var crossing = new L.layerGroup();
var earthradius = 6378137;
var southWest = L.latLng(45.7300, 5.8000),
    northEast = L.latLng(47.9000, 10.600),
    bounds = L.latLngBounds(southWest, northEast);
var osm = L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'v0.1 | Project data © <a href="http://opendatacommons.org/licenses/odbl/1.0/"> ODbL </a>' +
        '| Map © <a href="http://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>' +
    ' | <a href="http://www.openstreetmap.org/fixthemap">Improve this map</a>'
});
var mapbox = L.tileLayer("http://api.tiles.mapbox.com/v4/sfkeller.k0onh2me/{z}/{x}/{y}.png" +
    "?access_token=pk.eyJ1Ijoic2ZrZWxsZXIiLCJhIjoia3h4T3pScyJ9.MDLSUwpRpPqaV7SVfGcZDw", {
    attribution: "https://www.mapbox.com/about/maps/"
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
var customControl =  L.Control.extend({

    options: {
        position: 'topleft'
    },

        onAdd: function (map) {
            // createMap control element with standard leaflet control styling
            var container = L.DomUtil.create('div', 'leaflet-control leaflet-bar fitbounds'),
                link = L.DomUtil.create('a', 'controlicon', container);
            link.href = '#';
            link.title = 'Zur gesamten Schweiz zoomen';
            link.innerHTML = '<img src="img/swissMap.png" width="25px" height="25px">';

            // use leaflets fitBounds method to fit view to the createBounds
            L.DomEvent.on(link, 'click', L.DomEvent.stop).on(link, 'click', function () {
                map.fitBounds(bounds);
            });

            return container;
        }

});

var baseMaps = {
    "OSM": osm,
    "Mapbox": mapbox
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
map.addControl(new customControl());
if($(document).width() >= 800) {
    L.control.layers(baseMaps, overlayMaps).addTo(map);
} else {
    L.control.layers(baseMaps, overlayMaps, {position: 'topleft'}).addTo(map);
}
