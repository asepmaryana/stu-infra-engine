package com.stu.infra.cdc.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.LoggerFactory;

public class ThreadPooledServer implements Runnable {
	
	private byte[] receiveData = new byte[1024];
	
	private int serverPort   = 9870;
	
	private DatagramSocket serverSocket = null;
	
	private boolean isStopped = false;
	
	protected Thread runningThread = null;
	
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    public ThreadPooledServer(int port) {
        this.serverPort = port;
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        this.serverSocket.close();
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new DatagramSocket(this.serverPort);
        } catch (IOException e) {
        	throw new RuntimeException("Cannot open port "+this.serverPort, e);
        }
    }
    
	public void run() {
		
		synchronized(this){ this.runningThread = Thread.currentThread(); }
		
		openServerSocket();
		
		LoggerFactory.getLogger(ThreadPooledServer.class).info("ThreadPooledServer Started.") ;
		
		while(!isStopped())
        {
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			}
			catch (IOException e) {
				LoggerFactory.getLogger(ThreadPooledServer.class).error("ThreadPooledServer error receive packet.", e) ;
			}			
			
			this.threadPool.execute(new SmsUdpResponder(serverSocket, receivePacket));
        }
        this.threadPool.shutdown();
        LoggerFactory.getLogger(ThreadPooledServer.class).info("ThreadPooledServer Stopped.") ;
	}

}
