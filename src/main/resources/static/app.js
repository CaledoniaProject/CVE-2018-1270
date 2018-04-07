function run() {
  selector = document.getElementById('expression').value
  stompClient = Stomp.client('ws://localhost:8080/hello');
  stompClient.connect({}, function(frame) {
    stompClient.subscribe('/topic/greetings', function() {}, {
      "selector": selector
    })
  });
}
