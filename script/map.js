var map = L.map('map').setView([46.8259, 8.2000], 13);

L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy;<a href="http://openstreetmap.org">OpenStreetMap</a> contributors',
    maxZoom: 18
}).addTo(map);