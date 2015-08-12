package org.jboss.as.quickstarts.datagrid.hotrod;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/footballManager")
public class CommandDao {

    @OnMessage
    public String sayHello(String name) {
        System.out.println("Command received:" + name + "'");
        return ("Processing " + name + " from Football Manager");
    }

    @OnOpen
    public void helloOnOpen(Session session) {
        System.out.println("Connection established: " + session.getId());
    }
    
    @OnClose
    public void helloOnClose(CloseReason reason) {
        System.out.println("Connection closed: " + reason.getReasonPhrase());
    }
}