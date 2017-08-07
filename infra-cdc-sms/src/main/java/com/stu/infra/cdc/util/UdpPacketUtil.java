package com.stu.infra.cdc.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.LoggerFactory;

public class UdpPacketUtil {
	
public static String sendPacket(String host, int port, int timeOut, String message) throws Exception {
		
		DatagramSocket clientSocket = new DatagramSocket();
		clientSocket.setSoTimeout(timeOut * 1000);

		InetAddress IPAddress = InetAddress.getByName(host);
		
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		sendData = message.getBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		LoggerFactory.getLogger(UdpPacketUtil.class).info("SEND: "+message);
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		
		String response = new String(receivePacket.getData());
		LoggerFactory.getLogger(UdpPacketUtil.class).info("RESP: " + response);
		clientSocket.close();
		
		return response;
	}

}
