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
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
import java.net.Socket;

/**
 * 
 */

/**
 * alive_command
 *
 */
public class PcEthTimerRunnable implements Runnable {

	/**
	 * 
	 */
	
	private PcEthServer read_server;
	
	public PcEthTimerRunnable(PcEthServer read_server) 
	{
		this.read_server = read_server;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() 
	{
		//Thread current_thread =  this.read_server.getHepta_Timer_Thread();
		//int alive_packet_missed_count = 0;
		
		while(read_server.isHepta_Thread_Run())
		{
			Socket write_server_accepted_socket = read_server.getAccepted_socket();
			
			if(write_server_accepted_socket!=null)
			{
				if(write_server_accepted_socket.isConnected() && write_server_accepted_socket.isBound() && (!write_server_accepted_socket.isClosed())) 
				{
					try {
						Thread.sleep(this.read_server.getTIMER_STAMP_MILLISECOND());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//System.out.println("Timer thread");
					
					//read_server.getSocket_stream_out().write(0);
					PcEthCommand alive_command = new PcEthCommand();
					alive_command.Hepta_ByteEncode_Packet_Header((short) 0);
					
					read_server.Hepta_pc_eth_Server_Write_Command_Notify(alive_command);
	
					
					/*if(read_server.isAlive_Packet_Received()==false)
					{
						alive_packet_missed_count = alive_packet_missed_count+1;
						if(alive_packet_missed_count >= Ethernet_Packet.ALIVE_PACKET_MAX_ERROR)
						{
							System.out.println("Alive packet not received. Closing socket..!");
							try {
								DataInputStream socket_stream_in =  read_server.getSocket_stream_in();
								if(socket_stream_in != null)
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
					else
					{
						read_server.setAlive_Packet_Received(false);
						alive_packet_missed_count = 0;
					}*/
				}
			}
			//write_server_accepted_socket.close();
		}
	}
}
