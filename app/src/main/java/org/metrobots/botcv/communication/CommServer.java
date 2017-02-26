package org.metrobots.botcv.communication;

import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.IServerListener;
import net.sf.lipermi.net.Server;

import java.io.IOException;
import java.net.Socket;

/**
 * Main communications server.
 */
public class CommServer {
    private CommImpl implementation;
    private CallHandler handler = new CallHandler();

    public CommServer(CommImpl impl) throws LipeRMIException {
        this.implementation = impl;

        handler.registerGlobal(CommInterface.class, implementation);
    }

    public void start(int port) throws IOException {
        Server server = new Server();
        server.bind(port, handler);
    }
}
