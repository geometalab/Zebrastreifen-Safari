
var southWest = L.latLng(45.7300, 5.8000),
    northEast = L.latLng(47.9000, 10.600),
    bounds = L.latLngBounds(southWest, northEast);

var map = L.map('map',{
    maxBounds:bounds
}).setView([46.8259, 8.2000], 9);



L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy;<a href="http://openstreetmap.org">OpenStreetMap</a> contributors',
}).addTo(map);