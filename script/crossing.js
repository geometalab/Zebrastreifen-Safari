$(document).ready(function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crosswalk/' + location.search.split('crosswalk=')[1],
        dataType: 'json',
        method: 'get',
        success : function (data){
            var json = data;
            document.getElementById("rating_title").textContent = "Detailansicht";
            if (json.hasOwnProperty('error')){
                $("#table").hide();
                document.getElementById("error").textContent = "Fehler: 404 Zebrastreifen nicht gefunden";
                document.getElementById("reason").textContent = "Grund: " + json.reason;
            } else {
                $("#error").hide();
                $("#reason").hide();

                /*changes the value of the table*/
                document.getElementById("node").textContent = json.osm_node_id;
                if(json.traffic_signals){
                    document.getElementById("traffic_signal").textContent = "Lichtsignal vorhanden";
                } else {
                    document.getElementById("traffic_signal").textContent = "Kein Lichtsignal";
                }

                if(json.island){
                    document.getElementById("island").textContent = "Insel vorhanden";
                } else {
                    document.getElementById("island").textContent = "Keine Insel";
                }



                if(!json.unmarked){
                    document.getElementById("unmarked").textContent = "Markierung vorhanden";
                } else {
                    document.getElementById("unmarked").textContent = "Keine Bodenmarkierung";
                }

                if(json.button_operated){
                    document.getElementById("button_operated").textContent = "Knopf vorhanden";
                } else {
                    document.getElementById("button_operated").textContent = "Kein Knopf vorhanden";
                }

                if(json.sloped_curb.localeCompare("no") == 0){
                    document.getElementById("sloped_curb").textContent = "Nicht vorhanden";
                } else if(json.sloped_curb.localeCompare("one") == 0)  {
                    document.getElementById("sloped_curb").textContent = "Auf einer Seite";
                } else {
                    document.getElementById("sloped_curb").textContent = "Auf beiden Seiten";
                }

                if(json.tactile_paving){
                    document.getElementById("tactile_paving").textContent = "Vorhanden";
                } else {
                    document.getElementById("tactile_paving").textContent = "Nicht Vorhanden";
                }

                if(json.traffic_signals_vibration){
                    document.getElementById("traffic_signals_vibration").textContent = "Vorhanden";
                } else {
                    document.getElementById("traffic_signals_vibration").textContent = "Nicht Vorhanden";
                }

                if(json.traffic_signals_sound){
                    document.getElementById("traffic_signals_sound").textContent = "Vorhanden";
                } else {
                    document.getElementById("traffic_signals_sound").textContent = "Nicht Vorhanden";
                }
                var table = $('#t_rating');

                for(var i = 0; i < json.ratings.length; i++) {
                    var row = $('<tr></tr>');
                    var data = $('<td></td>');
                    row.append($('<th class="t_mobile"></th>').text("Userbewertung"));
                    row.append($('<th class="t_mobile"></th>').text("Benutzer: "));
                    row.append($('<td></td>').text(json.ratings[i].username));
                    row.append($('<th class="t_mobile"></th>').text( "Übersicht: "));
                    row.append($('<td></td>').text(json.ratings[i].spatial_clarity));
                    row.append($('<th class="t_mobile"></th>').text("Beleuchtung: "));
                    row.append($('<td></td>').text(json.ratings[i].illumination));
                    row.append($('<th class="t_mobile"></th>').text("Verkehr: "));
                    row.append($('<td></td>').text(json.ratings[i].traffic));
                    row.append($('<th class="t_mobile"></th>').text("Kommentar: "));
                    if(json.ratings[i].comment == null){
                        row.append($('<td></td>').text("-"));
                    } else {
                        row.append($('<td></td>').text(json.ratings[i].comment));
                    }
                    row.append($('<th class="t_mobile"></th>').text("Bild: "));
                    if(json.ratings[i].image_weblink == null){
                        row.append($('<td></td>').text("Kein Bild vorhanden"));
                    } else {
                        row.append($('<td></td>').prepend('<img id="theImg" src="https://d1cuyjsrcm0gby.cloudfront.net/'+ json.ratings[i].image_weblink +'/thumb-320.jpg" />'));
                    }
                    table.append(row);
                }
            }

        }
    });
});






