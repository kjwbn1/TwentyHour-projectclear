package com.kjw.twentyhour.network;

import android.app.Application;
import com.kjw.twentyhour.utils.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

import static com.kjw.twentyhour.utils.Constants.CHAT_SERVER_URL;

public class ChatApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
