function run() {
  selector = document.getElementById('expression').value
  stompClient = Stomp.client('ws://' + window.location.hostname + ':' + window.location.port + '/hello');
  stompClient.connect({}, function(frame) {
    stompClient.subscribe('/topic/greetings', function() {}, {
      "selector": selector
    })
  });
}
