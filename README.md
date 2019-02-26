# LocalNetwork
DISCLAIMER: I've found out that discovery using broadcast UDP is not guaranteed. It will work with most routers, but some might not allow it by default. Same with devices - works on most devices, but then you need fixes for specific devices.

Local network library for connecting Android devices on WiFi. One device serves as a server, others are clients. Library takes care of
discovery of server by clients. Library uses services and separate threads with sockets to handle communication.

### Wi-Fi Aware
In new O Developer Preview, Android team created something called Wi-Fi aware, and you might want to check that out: https://developer.android.com/preview/features/wifi-aware.html 

This library is nowhere near done. But you can get it from jcenter if you want: 
```groovy
compile 'com.github.jokr:localnet:0.1'
```

## Usage
There are two main parts of this library: server and client. One device should be server and others should be clients.
Server uses LocalServer class, and clients LocalClient class.

There are two phases: discovery and session. In discovery phase client devices discover server on wifi.

### Discovery | Client
Client should start LocalClient instance and call `connect()`. You will probably want to `setDiscoveryReceiver(*DiscoveryStatusReceiver impl*)`
then you can get notified if server is discovered successfully. You will be also notified when server starts session.
```java
public interface DiscoveryStatusReceiver {
        public void onDiscoveryTimeout();
        public void onServerDiscovered();
        public void onSessionStart();
    }
```

### Discovery | Server
Server should start LocalServer instance and call `init()`. From there it is listening for clients to discover it.
```java
localServer = new LocalServer(getContext());
localServer.init();
```
You can receive information when client connects by providing receiver in `localServer.setReceiver` method. 
```java
    @Override
    public void onClientConnected(Payload<?> payload) {
        // do something
    }
```
You will see a lot of **Payload<?>** through this library. It is basically a way for library to send any your object.
Make your own class implements Serializable and pack it with Payload: `new Payload<YourClass>(new YourClass(...))`.

Once you are ready to end discovery and start session call `localServer.setSession(*LocalSesion impl)` and discovery phase ends and session starts.

### LocalSession class
Main part of session is LocalSession class. This is abstract class you should extend and implement all session/game logic here. This code will
run in service. Main methods to override are:
```java
    @Override
    public void onCreate(Bundle bundle, ConnectedClients connectedClients){
        // session is created
    }

    @Override
    public void onReceiveMessage(long recipientId, Payload payload) {
        // received message from client
    }

    @Override
    public void onEvent(Payload<?> payload) {
        // received event from LocalServer instance
    }
```
From here you can call `sendMessage(long recipientId, Payload<?> payload)` to send message to client, or `sendBroadcastMessage(Payload<?> payload)` to send message to all clients.
You can also send message to your LocalServer instance and then possibly show it on UI with `sendUiEvent(Payload<?> payload)`

### Session | Server
To start a session, just call `localServer.setSession(*LocalSesion impl*)` and provide implementation as descibed above.
Just like you can send events from your LocalSession to LocalServer, you can send events other way - from LocalServer to your LocalSession implementation by calling `localServer.sendLocalSessionEvent(Payload<?>)` and this will call
onEvent method in your LocalSession.

### Session | Client
Client can send messages to server by calling localClient.sendSessionMessage(Payload<?>) and receive messages from server by setting receiver:
```java
localClient.setReceiver(new LocalClient.MessageReceiver() {
            @Override
            public void onMessageReceived(Payload<?> payload) {
                // Payload has object you've sent from LocalSession
            }
        });
```
