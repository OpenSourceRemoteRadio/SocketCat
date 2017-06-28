/*
Copyright (c) 2017, BigCat Wireless Pvt Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.

    * Neither the name of the copyright holder nor the names of its contributors
      may be used to endorse or promote products derived from this software
      without specific prior written permission.



THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
//import java.nio.ByteBuffer;
//import java.util.Iterator;

/**
 * 
 */

/**
 * 
 *
 */
public class PcEthReadRunnable implements Runnable {

	
	private PcEthServer read_server;
	
	/**
	 * @param hepta_pc_eth_Server 
	 * 
	 */
	public PcEthReadRunnable(PcEthServer read_server) {
		this.read_server = read_server;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		PcEthCommandList wait_commands_list = read_server.getCommand_list_to_Acknowledge();
		PcEthCommandList acknowledged_commands_list = read_server.getCommand_list_Acknowledged();
		PcEthCommandList received_commands_list = read_server.getCommand_list_Received();
		Thread Hepta_Command_Thread = read_server.getHepta_Command_Thread();
		//Thread current_thread =  this.read_server.getHepta_Read_Thread();
		
		
		while (read_server.isHepta_Thread_Run()) 
		{
			try {
				read_server.setServer_Socket_Object(new ServerSocket(read_server.getConnection_port()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			System.out.println("Waiting for client on port " + read_server.getServer_Socket_Object().getLocalPort() + "...");
			try {
				read_server.setAccepted_socket(read_server.getServer_Socket_Object().accept());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				if(read_server.isHepta_Thread_Run())
				{
					System.out.println("Socket accept failed. Retrying...!");
					continue;
				}
				else
				{
					return;
				}
			}
			SocketAddress client_socket_address = read_server.getAccepted_socket().getRemoteSocketAddress();
			System.out.println("Just connected to " + client_socket_address);
			try {
				read_server.setSocket_stream_in(new DataInputStream(read_server.getAccepted_socket().getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				read_server.setSocket_stream_out(new DataOutputStream(read_server.getAccepted_socket().getOutputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Socket read_server_accepted_socket = read_server.getAccepted_socket();
			try {
				read_server_accepted_socket.setKeepAlive(true);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (read_server_accepted_socket!=null) 
			{
				DataInputStream socket_stream_in =  read_server.getSocket_stream_in();
				
				if (read_server_accepted_socket
						.isConnected()
						&& read_server_accepted_socket.isBound()
						&& (!read_server_accepted_socket.isClosed())) 
				{
					FRESH_READ_STREAM:
					while(true)
					{
						PcEthCommand current_command = null;
						byte[] byte_packet_header = new byte[PcEthPacketConfiguration.PACKET_HEADER_SIZE];
						//ByteBuffer byte_buffer_to_return = ByteBuffer.allocate(64 + number_of_data_byte);
						int byte_len, byte_received;
						try {
							byte_len = socket_stream_in.read(byte_packet_header, 0,
									PcEthPacketConfiguration.PACKET_HEADER_SIZE);
						} catch (Exception e) {
							if(read_server.isHepta_Thread_Run())
							{
								System.out
								.println("Read exception occured. Reconnecting..!");
								//continue RECONNECT_SERVER;
								break;
							}
							/*else
							{*/
								return;
							/*}*/
						}

						if (byte_len != PcEthPacketConfiguration.PACKET_HEADER_SIZE) {
							System.out.println("TCP read less than expected. Read "
									+ byte_len + " bytes");

							/*if (byte_len == -1) {
								System.out.println("Read -1. Closing socket");
								System.out
								.println("Read exception occured. Reconnecting..!");
								//continue RECONNECT_SERVER;
								break;
							}
							if(read_server.isHepta_Thread_Run())
							{
								continue;
							}
							else
							{
								return;
							}*/
							
							if (byte_len == -1) {
								if(read_server.isHepta_Thread_Run())
								{
									System.out.println("Read -1. Closing socket");
									System.out
									.println("Read exception occured. Reconnecting..!");
									//continue RECONNECT_SERVER;
									break;
								}
								/*else
								{*/
									return;
								/*}*/
							}
						}
						
						String packet_header = new String(byte_packet_header, 0, 3);

						if (packet_header.compareToIgnoreCase("alt") == 0) {
							current_command = new PcEthCommand();
							int length_in_bytes = current_command
									.Decode_Packet_Header_Byte(byte_packet_header);
							if (length_in_bytes > 0) {
								byte[] byte_remaining_packet = new byte[length_in_bytes];
								byte_len = 0;
								byte_received = 0;
								do {
									try {
										byte_received = socket_stream_in.read(
												byte_remaining_packet, byte_len,
												length_in_bytes - byte_len);
										if (byte_received != -1) {
											byte_len = byte_len + byte_received;
										} else {
											continue FRESH_READ_STREAM;
										}
									} catch (Exception e) {
										continue FRESH_READ_STREAM;
									}
								} while (byte_len < length_in_bytes);

								if (current_command
										.Decode_Packet_Byte(byte_remaining_packet)) {
									//System.out.println("Decoded packet " + current_command.getCommand_Id().toString() + " completely and adding in received queue");
									received_commands_list.add(current_command);

									if (Hepta_Command_Thread != null) {
										synchronized (Hepta_Command_Thread) {
											Hepta_Command_Thread.notify();
										}
									} else {
										System.out
												.println("Command thread not found");
									}
								} else {
									current_command = null;
									System.out
											.println("TCP packet not decoded properly");
									continue;
								}
							} else if (length_in_bytes == 0) //its an acknowledgement message
							{
								//System.out.println("Received ACK for id = "+ current_command.getCommand_Id()  +"\n");
								/*	
								for (Hepta_Command wait_commands : wait_commands_list) {
									System.out.println("Received ACK for id = "+ current_command.getCommand_Id()  +"\n");
								}*/
								
								if(current_command.getCommand_Id() == 0)
								{
									read_server.setAlive_Packet_Received(true);
								}
								else
								{
									if (wait_commands_list.contains(current_command)) {
										if (wait_commands_list.remove(current_command)) {
											acknowledged_commands_list
													.add(current_command);
										}
									} else {
										System.out
												.println("Received incorrect ACK for id = "
														+ current_command
																.getCommand_Id() + "\n");
									}
								}
							}
						} else {
							System.out.println("TCP incorrect header");
							continue;
						}
					}	
				}
				
				RECONNECT_SERVER:
				try {
					if(socket_stream_in!=null)
					{
						socket_stream_in.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(read_server.getAccepted_socket()!=null)
					{
						read_server.getAccepted_socket().close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(read_server.getServer_Socket_Object()!=null)
					{
						read_server.getServer_Socket_Object().close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				read_server.setServer_Socket_Object(null);
				read_server.setAccepted_socket(null);
				read_server.setSocket_stream_in(null);
				read_server.setSocket_stream_out(null);
			}
		}
		
/*		
		Socket read_server_accepted_socket = read_server.getAccepted_socket();
		
		if (read_server_accepted_socket!=null) 
		{
			Hepta_Command_List wait_commands_list = read_server.getCommand_list_to_Acknowledge();
			Hepta_Command_List acknowledged_commands_list = read_server.getCommand_list_Acknowledged();
			Hepta_Command_List received_commands_list = read_server.getCommand_list_Received();
			
			Thread Hepta_Command_Thread = read_server.getHepta_Command_Thread();
			
			DataInputStream socket_stream_in =  read_server.getSocket_stream_in();
			Thread current_thread =  this.read_server.getHepta_Read_Thread();
			
			FRESH_READ_STREAM: 
			while (read_server.isHepta_Thread_Run()) 
			{
					if (read_server_accepted_socket
						.isConnected()
						&& read_server_accepted_socket.isBound()
						&& (!read_server_accepted_socket.isClosed())) {
					Hepta_Command current_command = null;
					byte[] byte_packet_header = new byte[Ethernet_Packet.PACKET_HEADER_SIZE];
					//ByteBuffer byte_buffer_to_return = ByteBuffer.allocate(64 + number_of_data_byte);
					int byte_len, byte_received;
					try {
						byte_len = socket_stream_in.read(byte_packet_header, 0,
								Ethernet_Packet.PACKET_HEADER_SIZE);
					} catch (Exception e) {
						System.out
						.println("Read exception");
						return;
						//continue;
					}

					if (byte_len != Ethernet_Packet.PACKET_HEADER_SIZE) {
						System.out.println("TCP read less than expected. Read "
								+ byte_len + " bytes");

						if (byte_len == -1) {
							System.out.println("Closing socket");
							read_server.Hepta_pc_eth_Server_Close();
						}
						continue;
					}

					String packet_header = new String(byte_packet_header, 0, 3);

					if (packet_header.compareToIgnoreCase("alt") == 0) {
						current_command = new Hepta_Command();
						int length_in_bytes = current_command
								.Decode_Packet_Header_Byte(byte_packet_header);
						if (length_in_bytes > 0) {
							byte[] byte_remaining_packet = new byte[length_in_bytes];
							byte_len = 0;
							byte_received = 0;
							do {
								try {
									byte_received = socket_stream_in.read(
											byte_remaining_packet, byte_len,
											length_in_bytes - byte_len);
									if (byte_received != -1) {
										byte_len = byte_len + byte_received;
									} else {
										continue FRESH_READ_STREAM;
									}
								} catch (Exception e) {
									continue FRESH_READ_STREAM;
								}
							} while (byte_len < length_in_bytes);

							if (current_command
									.Decode_Packet_Byte(byte_remaining_packet)) {
								//System.out.println("Decoded packet " + current_command.getCommand_Id().toString() + " completely and adding in received queue");
								received_commands_list.add(current_command);

								if (Hepta_Command_Thread != null) {
									synchronized (Hepta_Command_Thread) {
										Hepta_Command_Thread.notify();
									}
								} else {
									System.out
											.println("Command thread not found");
								}
							} else {
								current_command = null;
								System.out
										.println("TCP packet not decoded properly");
								continue;
							}
						} else if (length_in_bytes == 0) //its an acknowledgement message
						{
							//System.out.println("Received ACK for id = "+ current_command.getCommand_Id()  +"\n");
								
							for (Hepta_Command wait_commands : wait_commands_list) {
								System.out.println("Received ACK for id = "+ current_command.getCommand_Id()  +"\n");
							}

							if (wait_commands_list.contains(current_command)) {
								if (wait_commands_list.remove(current_command)) {
									acknowledged_commands_list
											.add(current_command);
								}
							} else {
								System.out
										.println("Received incorrect ACK for id = "
												+ current_command
														.getCommand_Id() + "\n");
							}
						}
					} else {
						System.out.println("TCP incorrect header");
						continue;
					}
				}
			}
		}*/
	}
}
