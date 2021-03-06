// Global variables; see the code below for their purpose.
var map;
var source;
var target;
var line;

// Main program code. The jQuery construct $(document).ready(function(){ ... }
// ensures that this is executed only when the page has been loaded.
$(document).ready(function(){
  // An object holding a position on the map.
  // See http://tinyurl.com/7mry4xl#LatLng.
  var latlng = new google.maps.LatLng(49.3139855, 6.8137583);
  // An associative array containing options for our initial maps view.
  // See http://tinyurl.com/7mry4xl#MapOptions.
  var mapOptions = {
    zoom: 15,
    center: latlng,
    mapTypeId: google.maps.MapTypeId.SATELLITE
  };
  // An object that will actually show the map in the DOM element with the given
  // id ("map_canvas" in this case) and with the given options.
  // See http://tinyurl.com/7mry4xl#Map.
  map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);

  // Two objects representing markers on the map.
  // See http://tinyurl.com/7mry4xl#Marker.
  source = new google.maps.Marker({map: map, draggable: true,
    position: new google.maps.LatLng(49.3139855,6.8137583)});
  target = new google.maps.Marker({map : map, draggable: true,
    position: new google.maps.LatLng(49.3135735,6.8105003)});

  // A Polyline object drawing this path on the map.
  // See http://tinyurl.com/7mry4xl#Polyline.
  var path = [ source.getPosition(), target.getPosition() ];
  line = new google.maps.Polyline({map: map, path: path,
      strokeColor: "blue", strokeWeight: 8, strokeOpacity: 0.5});

  // Add an event listener to the source and target marker that 
  // calls a function while they are being dragged.
  // See http://tinyurl.com/7mry4xl#MapsEventListener
  // and http://tinyurl.com/7mry4xl#Marker -> Subsection "Events".
  google.maps.event.addListener(source, 'dragend', redrawLine);
  google.maps.event.addListener(target, 'dragend', redrawLine);
});

// Redraw the line between source and target marker. Either do it on the client
// or via a backend on another machine (server).
function redrawLine() {
  redrawLineServer();
  //redrawLineClient();
}

// Implementation on the client.
function redrawLineClient() {
  var path = [ source.getPosition(), target.getPosition()];
  line.setPath(path);
}


// Implementation that talks to a server.
function redrawLineServer() {
  var url = "http://localhost:8888/?"
    + source.getPosition().lat() + "," + source.getPosition().lng() + ","
    + target.getPosition().lat() + "," + target.getPosition().lng();
  $.ajax(url, { dataType: "jsonp" });
}

// Function that is called when the server has sent its answer.
function redrawLineServerCallback(json) {
  //alert(json.path);
  //var path = [ new google.maps.LatLng(json.path[0], json.path[1]) ,
  //             new google.maps.LatLng(json.path[2], json.path[3]) ];
  var path = new Array();
  var i;
  var count = 0;
  var resultOfMod;
  for(i = 0; i < json.path.length; i++) {
    resultOfMod = i % 2;
    if (resultOfMod == 0){
	  path[count] = new google.maps.LatLng(json.path[i], json.path[i+1]);
	  count++;
    }
  }
  line.setPath(path);
}
