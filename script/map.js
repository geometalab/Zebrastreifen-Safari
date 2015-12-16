var bound;
var maxamount;
var zoom;
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
var searchPoints = L.geoJson(null, {
    onEachFeature: function (feature, layer) {
        layer.bindPopup(feature.properties.name);
    }
});
function showSearchPoints (geojson) {
    searchPoints.clearLayers();
    searchPoints.addData(geojson);
}

var API_URL = '//photon.komoot.de/api/?';
var map = L.map('map',{
    maxBounds:bounds,
    invalidateSize: true,
    minZoom: 7,
    layers: [osm, crossing],
    zoomControl: false,
    photonControl: true,
    photonControlOptions:
    {resultsHandler: showSearchPoints,
        placeholder: 'Suche...',
        position: 'topleft',
        url: API_URL,
        limit: 10,
        lang: "de"
    }
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
    zoom = map.getZoom();
    if(zoom == 18){
        maxamount = 100;
    } else {
        maxamount = 20;
    }
});
map.on('moveend', function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crosswalks/&bounds=' + bound + '&maxamount=' + maxamount,
        dataType:'json',
        method: 'get',
        success:function(response){
            var icon = new L.icon({
                iconUrl:"libs/script/leaflet/images/crossing.png",
                iconSize:[20,20]
            });
            crossing.clearLayers();
            var tmpCrossing = L.geoJson(response, {

                onEachFeature: function(features, layer){
                    if (features.properties.hasOwnProperty('amount')){
                        layer.on('mouseover', function() {
                            var hoverPopup = L.popup().setContent('Anzahl: ' + features.properties.amount);
                            layer.bindPopup(hoverPopup, {offset: new L.Point(0, -5)}).togglePopup();
                        });
                        layer.on('mouseout', function(){
                           layer.closePopup();
                        });
                        layer.on('click', function(){
                            var boundsMarker = layer.getLatLng();
                            var zoom = map.getZoom();
                            map.setView(boundsMarker, zoom + 2);
                        });
                    } else if(features.properties.rated){
                        layer.bindPopup('Nr. ' + features.properties.osm_node_id.toString() + '<br />' +
                            '<a href="crossing.html?crosswalk=' + features.properties.osm_node_id + '">Detailes</a> | ' +
                            '<a href="https://www.openstreetmap.org/node/' + features.properties.osm_node_id + '">OSM</a>',
                            {offset: new L.Point(0, -5)});
                    } else {
                        layer.bindPopup('Nr. ' + features.properties.osm_node_id.toString() + '<br />' +
                            'Nicht bewertet | ' +
                            '<a href="https://www.openstreetmap.org/node/' + features.properties.osm_node_id + '">OSM</a>',
                            {offset: new L.Point(0, -5)});
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
    "OSM Standard": osm,
    "MapBox Satellite": mapbox
};
var overlayMaps = {
    "Fussgängerstreifen": crossing
};
L.control.zoom().addTo(map);
map.addControl(new customControl());
if($(document).width() >= 800) {
    L.control.layers(baseMaps, overlayMaps).addTo(map);
} else {
    L.control.layers(baseMaps, overlayMaps, {position: 'topleft'}).addTo(map);
}
$('.leaflet-control-layers-overlays').after('<div class="leaflet-control-layers-separator"></div>' +
    '<div class="leaflet-control-layers-custom">' +
    '<label><span> <a href="http://giswiki.hsr.ch/Zebrasteifen-Safari#Legende ">Legende</a></span></label></div>'
);
