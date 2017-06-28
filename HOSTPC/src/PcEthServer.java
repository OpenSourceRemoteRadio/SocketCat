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
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.SocketAddress;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.stream.XMLStreamException;



/**
 * 
 */


public class PcEthServer {

	/**
	 * 
	 */
	
	private int connection_port = 0;
	private ServerSocket Server_Socket_Object = null;	
	private Socket accepted_socket =  null;
	private DataInputStream socket_stream_in =  null;
	private DataOutputStream socket_stream_out =  null;
	private PcEthCommandList Command_list_to_Send = new PcEthCommandList();
	private PcEthCommandList Command_list_to_Acknowledge = new PcEthCommandList();
	private PcEthCommandList Command_list_Acknowledged = new PcEthCommandList();
	private PcEthCommandList Command_list_Received = new PcEthCommandList();
	private PcEthCommandList Command_list_to_Deliver = new PcEthCommandList();
	private PcEthCommandList Command_list_Service_Response = new PcEthCommandList();
	private Thread Hepta_Write_Thread = null;
	private Runnable Hepta_Write_Runnable = null;
	private Thread Hepta_Timer_Thread = null;
	private Runnable Hepta_Timer_Runnable = null;
	private Runnable Hepta_Read_Runnable = null;
	private Thread Hepta_Read_Thread = null;
	private boolean Hepta_Thread_Run = true;
	private boolean Alive_Packet_Received = false;
	
	private Runnable Hepta_Command_Runnable = null;
	private Thread Hepta_Command_Thread = null;
	
	private ModulesList Module_List;
	private int	TIMER_STAMP_MILLISECOND = 3000;
	private ConcurrentLinkedQueue<Short> Command_Id_List = new ConcurrentLinkedQueue<>();
	private ArrayList<String> Operation_Command_String = new ArrayList<String>(){/**
		 * 
		 */
		private static final long serialVersionUID = 4281535473030238302L;

	{
				   add("set");
				   add("get");
				   add("error");
				   add("ack");
				   add("manage");
				   }};
	
	
	
	/*public pc_eth_Server() throws XMLStreamException,IOException,FileNotFoundException,SecurityException 
	{
		Hepta_Tcp_Create_Server();
	}
	
	public pc_eth_Server(int connection_port) throws XMLStreamException,IOException,FileNotFoundException,SecurityException 
	{
		this.connection_port = connection_port;
		System.out.println("Setting connection port to " + this.connection_port);
		Hepta_Tcp_Create_Server();
	}
	
	
	public pc_eth_Server(String FID_XML_Path) throws XMLStreamException,IOException,FileNotFoundException,SecurityException 
	{
		File fid_xml_file = new File(FID_XML_Path);
		System.out.println("FID parsed from " + FID_XML_Path);
		XML_Reader fid_xml_reader = new XML_Reader(FID_XML_Path);
		ModulesList Extracted_Modules_list = fid_xml_reader.Create_Module_List_from_XML();
		if(Extracted_Modules_list != null)
		{
			setModule_List(Extracted_Modules_list);
		}
		Hepta_Tcp_Create_Server();
	}
		
	public pc_eth_Server(int connection_port,String FID_XML_Path) throws XMLStreamException, IOException,FileNotFoundException,SecurityException 
	{
		this.connection_port = connection_port;
		System.out.println("Setting connection port to " + this.connection_port);
		File fid_xml_file = new File(FID_XML_Path);
		System.out.println("FID parsed from " + FID_XML_Path);
		XML_Reader fid_xml_reader = new XML_Reader(FID_XML_Path);
		ModulesList Extracted_Modules_list = fid_xml_reader.Create_Module_List_from_XML();
		if(Extracted_Modules_list != null)
		{
			setModule_List(Extracted_Modules_list);
		}
		Hepta_Tcp_Create_Server();
	}*/
	
	public PcEthServer(String FID_XML_Path,String Command_XML_Path) throws XMLStreamException,IOException,FileNotFoundException,SecurityException 
	{
		//File fid_xml_file = new File(FID_XML_Path);
		System.out.println("FID parsed from " + FID_XML_Path);
		XmlReader fid_xml_reader = new XmlReader(FID_XML_Path);
		ModulesList Extracted_Modules_list = fid_xml_reader.Create_Module_List_from_XML();
		if(Extracted_Modules_list != null)
		{
			setModule_List(Extracted_Modules_list);
		}
		System.out.println("Commands parsed from " + Command_XML_Path);
		XmlReader command_xml_reader = new XmlReader(Command_XML_Path);
		ArrayList<String> Extracted_Commands_list = command_xml_reader.Create_Command_List_from_XML();
		if((Extracted_Commands_list != null)  && (!Extracted_Commands_list.isEmpty()))
		{
			this.Operation_Command_String.clear();
			this.Operation_Command_String.addAll(Extracted_Commands_list);			
		}
		Hepta_Tcp_Create_Server();
	}
	
	
	public PcEthServer(int connection_port,String FID_XML_Path,String Command_XML_Path) throws XMLStreamException, IOException,FileNotFoundException,SecurityException 
	{
		this.connection_port = connection_port;
		System.out.println("Setting connection port to " + this.connection_port);
		//File fid_xml_file = new File(FID_XML_Path);
		System.out.println("FID parsed from " + FID_XML_Path);
		XmlReader fid_xml_reader = new XmlReader(FID_XML_Path);
		ModulesList Extracted_Modules_list = fid_xml_reader.Create_Module_List_from_XML();
		if(Extracted_Modules_list != null)
		{
			setModule_List(Extracted_Modules_list);
		}
		System.out.println("Commands parsed from " + Command_XML_Path);
		XmlReader command_xml_reader = new XmlReader(Command_XML_Path);
		ArrayList<String> Extracted_Commands_list = command_xml_reader.Create_Command_List_from_XML();
		if((Extracted_Commands_list != null)  && (!Extracted_Commands_list.isEmpty()))
		{
			this.Operation_Command_String.clear();
			this.Operation_Command_String.addAll(Extracted_Commands_list);			
		}
		Hepta_Tcp_Create_Server();
	}
	
	/**
	 * @return the accepted_socket
	 */
	public synchronized Socket getAccepted_socket() {
		return accepted_socket;
	}


	/**
	 * @param accepted_socket the accepted_socket to set
	 */
	public synchronized void setAccepted_socket(Socket accepted_socket) {
		this.accepted_socket = accepted_socket;
	}


	/**
	 * @return the command_list_to_Send
	 */
	public PcEthCommandList getCommand_list_to_Send() {
		return Command_list_to_Send;
	}


	/**
	 * @param command_list_to_Send the command_list_to_Send to set
	 */
	public void setCommand_list_to_Send(PcEthCommandList command_list_to_Send) {
		Command_list_to_Send = command_list_to_Send;
	}


	/**
	 * @return the socket_stream_in
	 */
	public DataInputStream getSocket_stream_in() {
		return socket_stream_in;
	}


	/**
	 * @param socket_stream_in the socket_stream_in to set
	 */
	public void setSocket_stream_in(DataInputStream socket_stream_in) {
		this.socket_stream_in = socket_stream_in;
	}


	/**
	 * @return the socket_stream_out
	 */
	public DataOutputStream getSocket_stream_out() {
		return socket_stream_out;
	}


	/**
	 * @param socket_stream_out the socket_stream_out to set
	 */
	public void setSocket_stream_out(DataOutputStream socket_stream_out) {
		this.socket_stream_out = socket_stream_out;
	}


	/**
	 * @return the hepta_Write_Thread
	 */
	public Thread getHepta_Write_Thread() {
		return Hepta_Write_Thread;
	}


	/**
	 * @param hepta_Write_Thread the hepta_Write_Thread to set
	 */
	public void setHepta_Write_Thread(Thread hepta_Write_Thread) {
		Hepta_Write_Thread = hepta_Write_Thread;
	}


	/**
	 * @return the module_List
	 */
	public ModulesList getModule_List() {
		return Module_List;
	}


	/**
	 * @param module_List the module_List to set
	 */
	public void setModule_List(ModulesList module_List) {
		Module_List = module_List;
	}


	/**
	 * @return the operation_Command_String
	 */
	public ArrayList<String> getOperation_Command_String() {
		return Operation_Command_String;
	}

	/**
	 * @param operation_Command_String the operation_Command_String to set
	 */
	public void setOperation_Command_String(
			ArrayList<String> operation_Command_String) {
		Operation_Command_String = operation_Command_String;
	}

	private boolean Hepta_Tcp_Create_Server() throws IOException 
	{
		if(Server_Socket_Object == null)
		{
			{
				this.setHepta_Thread_Run(true);
				/*Server_Socket_Object = new ServerSocket(connection_port);
				System.out.println("Waiting for client on port " + Server_Socket_Object.getLocalPort() + "...");
				accepted_socket = Server_Socket_Object.accept();
				SocketAddress client_socket_address = accepted_socket.getRemoteSocketAddress();
				System.out.println("Just connected to " + client_socket_address);
				setSocket_stream_in(new DataInputStream(accepted_socket.getInputStream()));
				setSocket_stream_out(new DataOutputStream(accepted_socket.getOutputStream()));*/
				this.Hepta_Write_Runnable = new PcEthWriteRunnable(this);
				this.Hepta_Write_Thread = new Thread(this.Hepta_Write_Runnable,"Hepta_Write_Thread");
				
				this.Hepta_Timer_Runnable = new PcEthTimerRunnable(this);
				this.setHepta_Timer_Thread(new Thread(
						this.Hepta_Timer_Runnable, "Hepta_Timer_Thread"));
				
				this.Hepta_Read_Runnable = new PcEthReadRunnable(this);
				this.setHepta_Read_Thread(new Thread(
						this.Hepta_Read_Runnable, "Hepta_Read_Thread"));
				
				this.Hepta_Command_Runnable = new PcEthCommandRunnable(this);
				this.setHepta_Command_Thread(new Thread(
						this.Hepta_Command_Runnable, "Hepta_Command_Thread"));
				
				if (Hepta_Timer_Thread !=null) {
					this.Hepta_Timer_Thread.start();
				}
				
				if (Hepta_Read_Thread!=null) {
					this.Hepta_Read_Thread.start();
				}
				
				
				if(this.Hepta_Write_Thread != null)
				{
					for (int command_id_index = 1; command_id_index < 256; command_id_index++) 
					{
						this.Command_Id_List.add((short) command_id_index);
					}
					this.Hepta_Write_Thread.start();
				}
				
				if (Hepta_Command_Thread!=null) {
					this.Hepta_Command_Thread.start();
				}
				
				
				return true;
			} 
		}
		else
		{
			return false;
		}
	}
	
	
	/**
	 * @return the hepta_Timer_Thread
	 */
	public Thread getHepta_Timer_Thread() {
		return Hepta_Timer_Thread;
	}

	/**
	 * @param hepta_Timer_Thread the hepta_Timer_Thread to set
	 */
	public void setHepta_Timer_Thread(Thread hepta_Timer_Thread) {
		Hepta_Timer_Thread = hepta_Timer_Thread;
	}

	/**
	 * @return the tIMER_STAMP_MILLISECOND
	 */
	public int getTIMER_STAMP_MILLISECOND() {
		return TIMER_STAMP_MILLISECOND;
	}

	/**
	 * @param tIMER_STAMP_MILLISECOND the tIMER_STAMP_MILLISECOND to set
	 */
	public void setTIMER_STAMP_MILLISECOND(int tIMER_STAMP_MILLISECOND) {
		TIMER_STAMP_MILLISECOND = tIMER_STAMP_MILLISECOND;
	}

	/**
	 * @return the hepta_Read_Thread
	 */
	public Thread getHepta_Read_Thread() {
		return Hepta_Read_Thread;
	}

	/**
	 * @param hepta_Read_Thread the hepta_Read_Thread to set
	 */
	public void setHepta_Read_Thread(Thread hepta_Read_Thread) {
		Hepta_Read_Thread = hepta_Read_Thread;
	}

	/**
	 * @return the command_list_to_Acknowledge
	 */
	public PcEthCommandList getCommand_list_to_Acknowledge() {
		return Command_list_to_Acknowledge;
	}

	/**
	 * @param command_list_to_Acknowledge the command_list_to_Acknowledge to set
	 */
	public void setCommand_list_to_Acknowledge(
			PcEthCommandList command_list_to_Acknowledge) {
		Command_list_to_Acknowledge = command_list_to_Acknowledge;
	}

	/**
	 * @return the command_list_Acknowledged
	 */
	public PcEthCommandList getCommand_list_Acknowledged() {
		return Command_list_Acknowledged;
	}

	/**
	 * @param command_list_Acknowledged the command_list_Acknowledged to set
	 */
	public void setCommand_list_Acknowledged(PcEthCommandList command_list_Acknowledged) {
		Command_list_Acknowledged = command_list_Acknowledged;
	}

	/**
	 * @return the command_list_to_Deliver
	 */
	public PcEthCommandList getCommand_list_to_Deliver() {
		return Command_list_to_Deliver;
	}

	/**
	 * @param command_list_to_Deliver the command_list_to_Deliver to set
	 */
	public void setCommand_list_to_Deliver(PcEthCommandList command_list_to_Deliver) {
		Command_list_to_Deliver = command_list_to_Deliver;
	}

	/**
	 * @return the command_list_Received
	 */
	public PcEthCommandList getCommand_list_Received() {
		return Command_list_Received;
	}

	/**
	 * @param command_list_Received the command_list_Received to set
	 */
	public void setCommand_list_Received(PcEthCommandList command_list_Received) {
		Command_list_Received = command_list_Received;
	}

	/**
	 * @return the hepta_Command_Runnable
	 */
	public Runnable getHepta_Command_Runnable() {
		return Hepta_Command_Runnable;
	}

	/**
	 * @param hepta_Command_Runnable the hepta_Command_Runnable to set
	 */
	public void setHepta_Command_Runnable(Runnable hepta_Command_Runnable) {
		Hepta_Command_Runnable = hepta_Command_Runnable;
	}

	/**
	 * @return the hepta_Command_Thread
	 */
	public Thread getHepta_Command_Thread() {
		return Hepta_Command_Thread;
	}

	/**
	 * @param hepta_Command_Thread the hepta_Command_Thread to set
	 */
	public void setHepta_Command_Thread(Thread hepta_Command_Thread) {
		Hepta_Command_Thread = hepta_Command_Thread;
	}

	/**
	 * @return the hepta_Thread_Run
	 */
	public boolean isHepta_Thread_Run() {
		return Hepta_Thread_Run;
	}

	/**
	 * @param hepta_Thread_Run the hepta_Thread_Run to set
	 */
	public void setHepta_Thread_Run(boolean hepta_Thread_Run) {
		Hepta_Thread_Run = hepta_Thread_Run;
	}

	/**
	 * @return the server_Socket_Object
	 */
	public ServerSocket getServer_Socket_Object() {
		return Server_Socket_Object;
	}

	/**
	 * @param server_Socket_Object the server_Socket_Object to set
	 */
	public void setServer_Socket_Object(ServerSocket server_Socket_Object) {
		Server_Socket_Object = server_Socket_Object;
	}

	/**
	 * @return the command_Id_List
	 */
	public ConcurrentLinkedQueue<Short> getCommand_Id_List() {
		return Command_Id_List;
	}

	/**
	 * @param command_Id_List the command_Id_List to set
	 */
	public void setCommand_Id_List(ConcurrentLinkedQueue<Short> command_Id_List) {
		Command_Id_List = command_Id_List;
	}

	/**
	 * @return the connection_port
	 */
	public int getConnection_port() {
		return connection_port;
	}

	/**
	 * @param connection_port the connection_port to set
	 */
	public void setConnection_port(int connection_port) {
		this.connection_port = connection_port;
	}

	public boolean Hepta_pc_eth_Server_Close() 
	{
		this.setHepta_Thread_Run(false);
		
		try 
		{
			
			if(accepted_socket!=null)
			{
				accepted_socket.close();
			}
			if(Server_Socket_Object!=null)
			{
				Server_Socket_Object.close();
			}
			
			this.setServer_Socket_Object(null);
			this.setAccepted_socket(null);
			this.setSocket_stream_in(null);
			this.setSocket_stream_out(null);
			
			
			this.Hepta_Timer_Thread.join();
			System.out.println("Timer thread joined");	
			this.Hepta_Read_Thread.join();
			System.out.println("Read thread joined");
			
			while(this.Hepta_Write_Thread.isAlive())
			{
				synchronized (Hepta_Write_Thread) {
					Hepta_Write_Thread.notify();
				}
				this.Hepta_Write_Thread.join(1000);
			}
			System.out.println("Write thread joined");
			
			while(Hepta_Command_Thread.isAlive())
			{
				synchronized (Hepta_Command_Thread) {
					Hepta_Command_Thread.notify();
				}
				this.Hepta_Command_Thread.join(1000);
			}			
			System.out.println("Command thread joined");
			
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return the alive_packet_received
	 */
	public synchronized boolean isAlive_Packet_Received() {
		return Alive_Packet_Received;
	}

	/**
	 * @param alive_packet_received the alive_packet_received to set
	 */
	public synchronized void setAlive_Packet_Received(boolean alive_packet_received) {
		Alive_Packet_Received = alive_packet_received;
	}

	public PcEthCommand Hepta_Get_Service_Response()
	{
		if(Command_list_Service_Response.isEmpty())
		{
			return null;
		}
		/*else
		{*/
			return Command_list_Service_Response.poll();
		/*}*/
	}
	
	public int[] Hepta_Command_Transfer(String command_string) 
	{
		return Hepta_Command_Transfer(command_string,null);
	}
	

	
	//public Hepta_Command Hepta_Command_Transfer(String command_string, Integer[] data_array) 
	public int[] Hepta_Command_Transfer(String command_string, int[] data_array)
	{
		PcEthCommand decoded_command = null;
		 //PcEthCommand returned_command = null;
		 
		 if(accepted_socket == null)
		 {
			 return null;
		 }

		if(!(accepted_socket.isConnected() && accepted_socket.isBound() && (!accepted_socket.isClosed()))) 
		{
			return null;
		}
		 
		 if(this.Command_Id_List.isEmpty())
		 {
			 return null;
		 }
		 
		 Short current_command_id = this.Command_Id_List.remove();
		 
		 try {
			decoded_command = PcEthCommand.Interpret_CommandString(current_command_id, this,command_string,data_array!=null);
			 
			 
			 if(decoded_command != null)
			 {
				 if (true) 
			     {
					if (decoded_command.getData_array() == null) {
						decoded_command
						.setData_array(new int[decoded_command
								.getLength()]);
						if (data_array != null) {
							if (decoded_command.getLength() <= data_array.length) {
								System.arraycopy(data_array, 0,
										decoded_command.getData_array(), 0,
										decoded_command.getLength());
							} else {
								/*for (int data_index = 0; data_index < decoded_command.getLength(); data_index++) {
									decoded_command.getData_array()[data_index] = 0;
								}*/
								java.util.Arrays.fill(decoded_command.getData_array(), 0);
							}
						} else {
							java.util.Arrays.fill(decoded_command.getData_array(), 0);
						}
					}
				}

				 decoded_command.Hepta_ByteEncode_Command();
			    
			    Hepta_pc_eth_Server_Write_Command_Notify(decoded_command);
			    
			    if(decoded_command.gettransfer_type().compareTo(TransferType.Poll_Mode.getTransfer_type_value())==0)
			    {
			    	//wait for response
			    	try 
					{
						synchronized(this) 
						{
							//System.out.println("Main thread waiting for response");
							this.wait(5000);
							//System.out.println("Main thread received signal for id " + decoded_command.getCommand_Id().toString());
						}
						
						/*if(decoded_command.getTransferType() == TransferType.Poll_Mode.getTransfer_type_value())
						{*/
							this.Command_Id_List.add(current_command_id);
						/*}
						else if(decoded_command.getTransferType() == TransferType.Service_Mode.getTransfer_type_value())
						{
							if(decoded_command.getLength()==0)
							{
								this.Command_Id_List.add(current_command_id);
							}
						}*/
						
						if(Command_list_to_Deliver.contains(decoded_command))
				    	{
				    		decoded_command = Command_list_to_Deliver.poll();
				    		
				    		/*if(data_array!=null)
				    		{
				    			if(data_array.length >= decoded_command.getLength())
				    			{
						    		for (int data_index = 0; data_index < decoded_command.getLength(); data_index++) 
						    		{
						    			data_array[data_index]= decoded_command.getData_array()[data_index];	
									}
				    			}
				    		}*/
				    		return decoded_command.getData_array();
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
			    else
			    {
			    	int[] command_id =  new int[1];
			    	command_id[0] = decoded_command.getCommand_Id(); 
			    	return command_id;
			    }
			 }
			 else
			 {
			     return null;
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		 
		 return null;
		 
		 //return decoded_command;
	}

	void Hepta_pc_eth_Server_Write_Command_Notify(
			PcEthCommand decoded_command) {
	    Command_list_to_Send.add(decoded_command);
	     
	    if (Hepta_Write_Thread != null) 
		{
			synchronized (Hepta_Write_Thread) {
				Hepta_Write_Thread.notify();
			}
		}
		else
		{
			System.out.println("Write thread not found");
		}
		
		//System.out.println("Write thread notified");
		
	}

	/*public ArrayList<String> Get_Modules_Name_List() {
		if(this.Module_List!=null)
		{
			ArrayList<String> Modules_Name_List = new ArrayList<String>();
			
			for (Module_RegMap current_module : this.Module_List) {
				Modules_Name_List.add(current_module.getModule_Name());
			}
			
			return Modules_Name_List;
		}
		else
		{
			return null;
		}
	}*/

	public ModulesList Get_Modules_List() {
		return this.Module_List;
	}

	public char[][] Get_Modules_Name_List_Char() {
		if(this.Module_List!=null)
		{
			char[][] Modules_Name_List = new char [this.Module_List.size()][];
			
			for (int module_index = 0; module_index < this.Module_List.size(); module_index++) {
				ModuleRegMap current_module = this.Module_List.get(module_index);
				Modules_Name_List[module_index] = current_module.getModule_Name().toCharArray();  
			}
			
			return Modules_Name_List;
		}
		/*else
		{*/
			return null;
		//}
	}
	
	public String[] Get_Modules_Name_List() {
		if(this.Module_List!=null)
		{
			String[] Modules_Name_List = new String [this.Module_List.size()];
			
			for (int module_index = 0; module_index < this.Module_List.size(); module_index++) {
				ModuleRegMap current_module = this.Module_List.get(module_index);
				Modules_Name_List[module_index] = new String(current_module.getModule_Name());  
			}
			
			return Modules_Name_List;
		}
		/*else
		{*/
			return null;
		//}
	}

	/**
	 * @return the command_list_Service_Response
	 */
	public PcEthCommandList getCommand_list_Service_Response() {
		return Command_list_Service_Response;
	}

	/**
	 * @param command_list_Service_Response the command_list_Service_Response to set
	 */
	public void setCommand_list_Service_Response(
			PcEthCommandList command_list_Service_Response) {
		Command_list_Service_Response = command_list_Service_Response;
	}
	
	
	
}
