var json ={

    "node": 3179298669,
    "image": null,
    "osm": {
        "traffic_signals": false,
        "island": false,
        "unmarked": true,
        "button_operated": false,
        "slooped_curb": "no",
        "tactile_paving": false,
        "traffic_signals_vibration": false,
        "traffic_signals_sound": false
},
    "ratings":
    [
        {
            "comment": null,
            "username": "Alex Eugster",
            "overview": "Gut",
            "illumination": "Schlecht",
            "traffic": "Wenig"
        },
        {
            "comment": "Dies ist ein Kommentar",
            "username": "Mike Marti",
            "overview": "Schlecht",
            "illumination": "Schlecht",
            "traffic": "Viel"
        }
    ]
};
/* TODO generate table with javascript instead of html*/

/*changes the value of the table*/
document.getElementById("node").textContent = json.node;
document.getElementById("image").src= json.image;
if(json.osm.traffic_signals){
    document.getElementById("traffic_signal").textContent = "Lichtsignal vorhanden";
} else {
    document.getElementById("traffic_signal").textContent = "Kein Lichtsignal";
}

if(json.osm.island){
    document.getElementById("island").textContent = "Insel vorhanden";
} else {
    document.getElementById("island").textContent = "Keine Insel";
}

if(json.osm.island){
    document.getElementById("island").textContent = "Insel vorhanden";
} else {
    document.getElementById("island").textContent = "Keine Insel";
}

if(!json.osm.unmarked){
    document.getElementById("unmarked").textContent = "Markierung vorhanden";
} else {
    document.getElementById("unmarked").textContent = "Keine Bodenmarkierung";
}

if(json.osm.button_operated){
    document.getElementById("button_operated").textContent = "Knopf vorhanden";
} else {
    document.getElementById("button_operated").textContent = "Kein Knopf vorhanden";
}

if(json.osm.slooped_curb){
    document.getElementById("slooped_curb").textContent = "Geeignet";
} else {
    document.getElementById("slooped_curb").textContent = "Nicht geeignet";
}

if(json.osm.tactile_paving){
    document.getElementById("tactile_paving").textContent = "Vorhanden";
} else {
    document.getElementById("tactile_paving").textContent = "Nicht Vorhanden";
}

if(json.osm.traffic_signals_vibration){
    document.getElementById("traffic_signals_vibration").textContent = "Vorhanden";
} else {
    document.getElementById("traffic_signals_vibration").textContent = "Nicht Vorhanden";
}

if(json.osm.traffic_signals_sound){
    document.getElementById("traffic_signals_sound").textContent = "Vorhanden";
} else {
    document.getElementById("traffic_signals_sound").textContent = "Nicht Vorhanden";
}
var table = $('#t_rating');

for(var i = 0; i < json.ratings.length; i++) {
    var row = $('<tr></tr>');
    var data = $('<td></td>');
    row.append($('<th class="t_mobile"></th>').text("Userbewertung"));
    row.append($('<th class="t_mobile"></th>').text("Kommentar: "));
    if(json.ratings[i].comment == null){
        row.append($('<td></td>').text("-"));
    } else {
        row.append($('<td></td>').text(json.ratings[i].comment));
    }
    row.append($('<th class="t_mobile"></th>').text("Benutzer: "));
    row.append($('<td></td>').text(json.ratings[i].username));
    row.append($('<th class="t_mobile"></th>').text("Uebersicht: "));
    row.append($('<td></td>').text(json.ratings[i].overview));
    row.append($('<th class="t_mobile"></th>').text("Beleuchtung: "));
    row.append($('<td></td>').text(json.ratings[i].illumination));
    row.append($('<th class="t_mobile"></th>').text("Verkehr: "));
    row.append($('<td></td>').text(json.ratings[i].traffic));
    table.append(row);
}





