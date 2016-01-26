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
    attribution: 'v0.1 | Project data © <a href="http://opendatacommons.org/licenses/odbl/1.0/"> ODbL </a>' +
    '| © <a href="https://www.mapbox.com/about/maps/">Mapbox</a> © <a href="http://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>' +
    ' | <a href="http://www.openstreetmap.org/fixthemap">Improve this map</a>'
});
var mapboxTiles = L.tileLayer('https://api.mapbox.com/v4/mapbox.streets/{z}/{x}/{y}.png?access_token=' +
    "pk.eyJ1Ijoic3RhY2t0cmFjZSIsImEiOiJjaWh2cGI3aDkwMjlqdXNrb2l2eDl5dGgyIn0.96Y4mk2kaLNPoQb0URVi8g", {
    attribution: 'v0.1 | Project data © <a href="http://opendatacommons.org/licenses/odbl/1.0/"> ODbL </a>' +
    '| © <a href="https://www.mapbox.com/about/maps/">Mapbox</a> © <a href="http://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>' +
    ' | <a href="http://www.openstreetmap.org/fixthemap">Improve this map</a>'
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

var map = L.map('map',{
    maxBounds:bounds,
    invalidateSize: true,
    minZoom: 7,
    layers: [osm, crossing],
    zoomControl: false,
});
var hash = new L.hash(map);
map.attributionControl.setPrefix("");


function unproject(latlng){
    var point = new L.Point(latlng.lng,latlng.lat);
    return L.Projection.SphericalMercator.unproject(point.divideBy(earthradius));
}


map.on('moveend', function() {
    var bounds1 = map.getBounds();
    var min = L.latLng(bounds1._southWest.lat, bounds1._southWest.lng);
    var max = L.latLng(bounds1._northEast.lat, bounds1._northEast.lng);

    var mercator_min = L.Projection.SphericalMercator.project(min);
    var mercator_max = L.Projection.SphericalMercator.project(max);

    var minx = mercator_min.x * earthradius,
        miny = mercator_min.y * earthradius,
        maxx = mercator_max.x * earthradius,
        maxy = mercator_max.y * earthradius;

    bound = [minx, miny, maxx, maxy].toString();
    zoom = map.getZoom();
    maxamount = zoom * 12;
});
map.on('moveend', function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crosswalks/&bounds=' + bound + '&maxamount=' + maxamount,
        dataType:'json',
        method: 'get',
        success:function(response){
            $("#error").hide();
            var icon_clustered = new L.icon({
                iconUrl: "img/Zebra_2_brackets.png",
                iconSize: [25,18]
            });
            var icon_rated = new L.icon({
                iconUrl: "img/Zebra_2_check_2.png",
                iconSize: [18,18]
            });
            var icon_normal = new L.icon({
                iconUrl:"libs/script/leaflet/images/crossing.png",
                iconSize:[18,18]
            });
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
                            '<a href="crossing?crosswalk=' + features.properties.osm_node_id + '">Details</a> | ' +
                            '<a target="_blank" href="https://www.openstreetmap.org/node/' + features.properties.osm_node_id + '">OSM</a>',
                            {offset: new L.Point(0, -5)});
                    } else {
                        layer.bindPopup('Nr. ' + features.properties.osm_node_id.toString() + '<br />' +
                            'Nicht bewertet | ' +
                            '<a target="_blank" href="https://www.openstreetmap.org/node/' + features.properties.osm_node_id + '">OSM</a>',
                            {offset: new L.Point(0, -5)});
                    }
                },
                // add the points to the layer
                pointToLayer: function (feature, latlng) {
                    if(feature.properties.hasOwnProperty('amount')){
                        var icon = icon_clustered;
                    } else if(feature.properties.rated){
                        var icon = icon_rated;
                    } else {
                        var icon = icon_normal;
                    }
                    var newlatlng = unproject(latlng);
                    return L.marker(newlatlng, {
                        icon:icon
                    });
                }

            });
            crossing.clearLayers();
            tmpCrossing.addTo(crossing);
        },
        error: function(){
            $("#error").text("Die Zebrastreifen konnten nicht geladen werden");
            setTimeout(function() {
                $("#error").fadeOut().empty();
            }, 5000);
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


function checkUrl() {
    // get hash  the URL
    var hash = window.location.hash;
    // check if something is in the hash
    if (hash) {
        var splitted = hash.split("/");
        if (splitted.length == 3) {
            var zoom = parseInt(splitted[0].substr(1)),
                lat = parseFloat(splitted[1]),
                lng = parseFloat(splitted[2]);
            // check if parameters are within the swiss createBounds and the allowed zoom levels
            if (zoom >= 7 &&
                lat >= bounds[0] && lat <= bounds[2] &&
                lng >= bounds[1] && lng <= bounds[3]
            ) {
                return true
            }
        }
    }
}
function centerFromStorage(){
    if (localStorage.getItem("zoom") !== null) {
    var center = [];
    center[0] = localStorage.getItem('lat');
    center[1] = localStorage.getItem('lng');
    center[2] = localStorage.getItem('zoom');
    return center
}
    return false
}

    // if no tile_url parameters given or parameters are faulty
    if(!checkUrl()){
        var center = centerFromStorage();
        // initiate leafelt.hash plugin, but overwrite its center
        var hash = new L.Hash(map);
        if (center) {
            var cords = new L.latLng(center[0], center[1]);
            map.setView(cords, center[2])
        }
        // set default center (Switzerland) if no other center is found
        else{
            var hash = new L.Hash(map);;
            map.fitBounds(bounds);
        }
    }
    else{
        // initiate leaflet.hash plugin
        var hash = new L.Hash(map);
    }
    map.on('moveend', function() {
        var center = map.getCenter(),
            zoom = map.getZoom();
        localStorage.removeItem("lat");
        localStorage.removeItem("lng");
        localStorage.removeItem("zoom");
        localStorage.setItem("lat", center.lat.toString());
        localStorage.setItem("lng", center.lng.toString());
        localStorage.setItem("zoom", zoom.toString());
    });
var baseMaps = {
    "OSM Swiss Style": osm,
    "MapBox Satellite": mapbox,
    "Mapbox Streets": mapboxTiles
};
var overlayMaps = {
    "Fussgängerstreifen": crossing
};
var geosearch = new L.Control.GeoSearch({
    position: "topleft",
    provider: new L.GeoSearch.Provider.OpenStreetMap({countrycodes: 'ch'}),
    searchLabel: 'Suche...',
    notFoundMessage: 'Kein Ergebnis gefunden'
}).addTo(map);
L.control.zoom().addTo(map);
map.addControl(new customControl());
if($(document).width() >= 800) {
    L.control.layers(baseMaps, overlayMaps).addTo(map);
} else {
    L.control.layers(baseMaps, overlayMaps, {position: 'topleft', size: [25,25]}).addTo(map);
}
$('.leaflet-control-layers-overlays').after('<div class="leaflet-control-layers-separator"></div>' +
    '<div class="leaflet-control-layers-custom">' +
    '<label><span> <a href="http://giswiki.hsr.ch/Zebrasteifen-Safari#Legende ">Legende</a></span></label></div>'
);
