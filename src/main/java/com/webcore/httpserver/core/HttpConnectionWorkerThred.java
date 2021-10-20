package com.webcore.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThred extends Thread{
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    private Socket socket;

    public HttpConnectionWorkerThred(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            int _byte;

            while ((_byte = inputStream.read()) >= 0){
                System.out.print((char) _byte);
            }

            String html = "<html><head><title>Connected</title></head><body><p>You're connected</p></body></html>";

            final String CRLF = "\n\r"; //13, 10

            String response = "HTTP/1.1 200 OK" + CRLF + //Status Line: HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                    "Content-Length" + html.getBytes().length + CRLF + //HEADER
                    CRLF + html + CRLF + CRLF;

            outputStream.write(response.getBytes());

            LOGGER.info("Connection Processing Finished");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        }finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if(socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}
