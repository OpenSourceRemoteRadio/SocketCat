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
*///import java.io.IOException;
//import java.net.Socket;
import java.util.ArrayList;
//import java.util.Locale;


public class PcEthCommandRunnable implements Runnable {

	private PcEthServer read_server;

	/**
	 * @param read_server TCP server
	 */
	public PcEthCommandRunnable(PcEthServer read_server) 
	{
		this.read_server = read_server;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		Thread current_thread =  this.read_server.getHepta_Command_Thread();
		PcEthCommandList acknowledged_commands_list = read_server.getCommand_list_Acknowledged();
		PcEthCommandList received_commands_list = read_server.getCommand_list_Received();
		PcEthCommandList Command_list_to_Deliver = read_server.getCommand_list_to_Deliver();
		PcEthCommandList Command_list_Service_Response = read_server.getCommand_list_Service_Response();
		ArrayList<String> Operation_Command_String_List = read_server.getOperation_Command_String();	
		
		//while (write_server_accepted_socket.isConnected() && write_server_accepted_socket.isBound() && (!write_server_accepted_socket.isClosed()))
		while(this.read_server.isHepta_Thread_Run())
		{
			if(!received_commands_list.isEmpty())
			{
				//System.out.println("Commands thread queue not empty");
				PcEthCommand current_command = null;
				
				try 
				{
					current_command = received_commands_list.poll();
				} 
				catch (Exception e) 
				{
					current_command = null;
					e.printStackTrace();
					continue;
				}
				
				if(current_command != null)
				{
					//System.out.println("Dequeued packet from received queue " + current_command.getCommand_Id().toString());
					
					//System.out.println("ACKed list size " + acknowledged_commands_list.size());
					//System.out.println("Transfer type ==> " + current_command.getTransferType().toString());
					if(acknowledged_commands_list.contains(current_command))
					//if(true) //workaround
					{
						//Command_list_to_Deliver.add(current_command);							
						if(current_command.gettransfer_type().compareTo(TransferType.Poll_Mode.getTransfer_type_value())==0)
						{
							Command_list_to_Deliver.add(current_command);
							//System.out.println("Poll command received Id:  " + current_command.getCommand_Id().toString());
							acknowledged_commands_list.remove(current_command);
							
							synchronized (read_server) {
								read_server.notify();
							}
						}
						else if(current_command.gettransfer_type().compareTo(TransferType.Service_Mode.getTransfer_type_value())==0)
						{
							//System.out.println("Received response for " + current_command.getCommand_Id().toString());
							//System.out.println("packet type " + current_command.getPacket_type() );
							 if(current_command.getPacket_type() == Operation_Command_String_List.indexOf("ack"))
							 {
								 //System.out.println("Received ack for service " + current_command.getCommand_Id());
								 if(current_command.getLength()==0) //Service disabled
								 {
									 acknowledged_commands_list.remove(current_command);
								 }
							 }
							 else
							 {
								 Command_list_Service_Response.add(current_command);
							 }
						}
						else
						{
							System.out.println("Incorrect Transfer type\n");
						}
					}
					else
					{
						System.out.println("Incorrect commands received not found in ack list");
					}
				}
			}
			else
			{
				//System.out.println("Command queue empty");
				try 
				{
					synchronized(current_thread) 
					{
						//System.out.println("Command thread waiting");
						current_thread.wait();
						//System.out.println("Command thread wait done");
				    }
				} 
				catch (InterruptedException e) 
				{
					System.out.println("Interrupted error");
					e.printStackTrace();
				}
				catch (IllegalMonitorStateException e) 
				{
					System.out.println("Illegal Monitor error");
					e.printStackTrace();
				}
			}
		}
	}

}
