package com.blahblah.addpio;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer extends Thread {
    private static final int UDP_SERVER_PORT = 6297;
    private static final int MAX_UDP_DATAGRAM_LEN = 1024;
    private Context mContext;

    public UdpServer(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void run() {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(UDP_SERVER_PORT);
            byte[] receiveData = new byte[MAX_UDP_DATAGRAM_LEN];
            byte[] sendData;
            while(true)
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String data = new String(receivePacket.getData());
                data = data.substring(0, receivePacket.getLength());
                String[] parts = data.split(":");
                int pinNumber;
                int extraNumber;
                String response;
                if (parts.length < 2 || parts.length > 3) {
                    response = mContext.getString(R.string.error_input_format);
                } else {
                    String direction = parts[0];
                    if (!(direction.equals("in") || direction.equals("out"))) {
                        response = mContext.getString(R.string.invalid_direction);
                    } else {
                        try {
                            pinNumber = Integer.parseInt(parts[1]);
                            try {
                                extraNumber = (parts.length == 2) ? 0 : Integer.parseInt(parts[2]);
                                if (direction.equals("in")) {
                                    response = ((MainActivity)mContext).getInput(pinNumber, extraNumber);
                                } else {
                                    response = ((MainActivity)mContext).setOutput(pinNumber, extraNumber);
                                }
                            } catch (NumberFormatException x) {
                                response = mContext.getString(R.string.invalid_extra_number) + " " + parts[2];
                            }
                        } catch (NumberFormatException x) {
                            response = mContext.getString(R.string.invalid_pin_number) + " " + parts[1];
                        }
                    }
                }
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
