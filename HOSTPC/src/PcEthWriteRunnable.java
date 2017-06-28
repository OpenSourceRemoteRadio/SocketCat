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
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;




public class PcEthWriteRunnable implements Runnable {

	/**
	 * 
	 */
	
	private PcEthServer write_server;
	
	public PcEthWriteRunnable(PcEthServer write_server) 
	{
		this.write_server = write_server;
	}

	@Override
	public void run() 
	{
		PcEthCommandList write_commands_list = write_server.getCommand_list_to_Send();
		PcEthCommandList wait_commands_list = write_server.getCommand_list_to_Acknowledge();
		Thread current_thread =  this.write_server.getHepta_Write_Thread();
		while (write_server.isHepta_Thread_Run()) 
		{
			if (!write_commands_list.isEmpty()) 
			{
				//System.out.println("Write thread queue not empty");
				PcEthCommand current_command = null;
				
				
			      Socket write_server_accepted_socket = write_server.getAccepted_socket();
			     
			      
				
				if (write_server_accepted_socket!=null) 
				{
					if (write_server_accepted_socket.isConnected()
							&& write_server_accepted_socket.isBound()
							&& (!write_server_accepted_socket.isClosed())) {

					
						DataOutputStream socket_stream_out =  write_server.getSocket_stream_out();
						
						
						try {
							current_command = write_commands_list.poll();
						} catch (Exception e) {
							current_command = null;
							e.printStackTrace();
							continue;
						}
						 

						if (current_command != null) {
							if (socket_stream_out != null) {
								try {
									//socket_stream_out.write(current_command.getByte_array());
									/*current_command.setData_array(null);
									current_command.setByte_array(null);*/
									PcEthCommand wait_command = new PcEthCommand();
									wait_command.setCommand_Id(current_command
											.getCommand_Id());
									wait_commands_list.add(wait_command);
									//Thread.sleep(millis, nanos);
									socket_stream_out.write(current_command
											.getByte_array());
								} catch (IOException e) {
									//write_commands_list.add(current_command);
									//e.printStackTrace();									
								}
								
							}
						}
					}
				
				}
			
			
				else
				{
				
					continue;
				}
			      
			}
			else 
			{
				//System.out.println("Write thread queue empty");
				try {
					synchronized (current_thread) {
						//System.out.println("Write thread waiting");
						current_thread.wait();
						//System.out.println("Write thread wait done");
					}
				} catch (InterruptedException e) {
					System.out.println("Interrupted error");
					e.printStackTrace();
				} catch (IllegalMonitorStateException e) {
					System.out.println("Illegal Monitor error");
					e.printStackTrace();
				}
			}
		}
	}
	
}
