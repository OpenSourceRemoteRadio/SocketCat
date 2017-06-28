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

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;


public class Program {

	/**
	 * 
	 */
	public Program() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PcEthServer hepta_server = null;
		
		//String XML_Path = null;
		String current_directory = System.getProperty("user.dir");
		
/*		if(args[0].length() == 0)
		{
			XML_Path = new String(current_directory + "\\hepta_a10_2x4_Definition.xml");
		}
		else
		{
			XML_Path = new String(args[0]);
		}*/
		
		try 
		{
			hepta_server = new PcEthServer(49408,current_directory + "\\rau_Definition.xml",current_directory + "\\rau_Commands.xml");
		} 
		catch (XMLStreamException e) 
		{
			System.out.println("Cannot parse XML file");
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			System.out.println("XML file not found");
			e.printStackTrace();
		} catch (IOException e) 
		{
			System.out.println("TCP connection error");
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			System.out.println("XML file security error");
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//hepta_server.Hepta_pc_eth_Server_Close();
		while(true);
		//hepta_server.Hepta_pc_eth_Server_Close();
		
	//	int[] cap_data = hepta_server.Hepta_Command_Transfer("get ctx_write dpd_rev_path_dpd_ctx_cbuf_wr memory Buffer 5");
		
		//int[] cap_data = hepta_server.Hepta_Command_Transfer("get ctx_write dpd_rev_path_dpd_ctx_cbuf_wr memory Buffer 16372");
/*		int[] temp = new int[65536];
		temp[0] =  -1;
		
		//hepta_server.Hepta_Command_Transfer("set ptx dpd_rev_path_dpd_ptx_pbuf_rd_0 memory Buffer 8192",temp);
		
		int[] service_id = hepta_server.Hepta_Command_Transfer("service downlink downlink CPRI_Link0_In_Zero_Power enable");
		while(true)
		{
			Hepta_Command service_response = hepta_server.Hepta_Get_Service_Response();
			if(service_response!=null)
			{
				System.out.println("Main received response from service id "+ service_response.getCommand_Id().toString());
			}
		}*/
		
/*		
		for (int command_index = 0; command_index < 4; command_index++) {
			temp = hepta_server.Hepta_Command_Transfer("get cobsrx_write dpd_rev_path_dpd_cobsrx_cbuf_wr memory Buffer 16384");
		}
	
		Hepta_Command new_command = new Hepta_Command();
		int[] ret = null;
		//new_command.Hepta_ByteEncode_Command();
		
//		new_command = hepta_server.Hepta_Command_Transfer("set cpri 0 \"L1_CONFIG\" tx_enable \"Enable Transmission\"");
		//new_command = hepta_server.Hepta_Command_Transfer("set cpri cpri_wrapper_top_cpri_llink_1 cpu \"L1_CONFIG\" tx_enable \"Enable Transmission\"");
		//new_command = hepta_server.Hepta_Command_Transfer("set cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable \"Enable Transmission\"");
		
		//ret = hepta_server.Hepta_Command_Transfer("get cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable");
		
		//valid
	//	ret = hepta_server.Hepta_Command_Transfer("get cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\"");
		
	//	ret = hepta_server.Hepta_Command_Transfer("get capctrl cpri_capture memory_interface \"Buffer\" 10");
		
		ret = hepta_server.Hepta_Command_Transfer("get capctrl cpri_capture memory_interface \"Buffer\" 1024");
		
		for (int run = 0; run < 100; run++) {
			ret = hepta_server.Hepta_Command_Transfer("get capctrl cpri_capture memory_interface \"Buffer\" 1024");				
		}
		
		
		int[] temp = new int[65536];
		temp[0] = 1;
		
		hepta_server.Hepta_Command_Transfer("set cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\" 3");
		
		temp[0] = 3;
		
		hepta_server.Hepta_Command_Transfer("set cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\" 1",temp);
		hepta_server.Hepta_Command_Transfer("set cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable 1");
		
		ret = hepta_server.Hepta_Command_Transfer("get capctrl cpri_capture memory_interface \"Buffer\" 1024");
	
		for (int i = 0; i < temp.length; i++) {
			temp[i] = i;
		}
		
		
		temp[0] = 34;
		
				
		//ArrayList<String> List_of_Module_Names =  hepta_server.Get_Modules_Name_List();
		char[][] List_of_Module_Names_char = hepta_server.Get_Modules_Name_List_Char();
		String[] List_of_Module_Names_String = hepta_server.Get_Modules_Name_List();
		
		
		ModulesList List_of_Modules = hepta_server.Get_Modules_List();
		
		//ArrayList<String> List_of_Instances_Names =  List_of_Modules.get(0).getInstance_Name_List();
		String[] List_of_Instances_Names_String = List_of_Modules.get(0).Get_Instances_Name_List();
		
		
		//ArrayList<String> List_of_Resources_Names =  List_of_Modules.get(0).getResource_Name_List();
		String[] List_of_Resource_Names_String = List_of_Modules.get(0).Get_Resources_Name_List();
		
		ArrayList<ModuleResource> List_of_Resource =  List_of_Modules.get(0).getResources();
		
		
		//ArrayList<String> List_of_Registers_Names =  List_of_Resource.get(0).getRegister_Name_List();
		RegistersList List_of_Registers = List_of_Resource.get(0).getRegister_Offsets();
		String[] List_of_Registers_Names =  List_of_Resource.get(0).Get_Registers_Name_List();
		
		//ArrayList<String> List_of_Bits_Names =  List_of_Registers.get(0).getBits_Name_List();
		RegisterBitsList List_of_Bits = List_of_Registers.get(0).getBits();
		String[] List_of_Bits_Names =  List_of_Registers.get(0).Get_Bits_Name_List();
		
		
		//ArrayList<String> List_of_Configurations_Names =  List_of_Bits.get(0).getValue_Description_Name_List();
		Configuration_Actions_List List_of_Configurations = List_of_Bits.get(0).getValue_Description();
		String[] List_of_Configurations_Names =  List_of_Bits.get(0).Get_Value_Description_Name_List();
		
		for (int command_index = 0; command_index < 300; command_index++) {
			hepta_server.Hepta_Command_Transfer("get cpri cpri_bridge_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable 1");			
			if(command_index>=127)
			{
				System.out.println("Command >=127");
			}
		}
		
		
		//new_command = hepta_server.Hepta_Command_Transfer("set cpri cpri_wrapper_top_cpri_llink_0 cpu \"L1_CONFIG\" 1",temp);
		//new_command = hepta_server.Hepta_Command_Transfer("get cpri cpri_wrapper_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable",temp);
		//new_command = hepta_server.Hepta_Command_Transfer("get cpri cpri_wrapper_top_cpri_llink_0 cpu \"L1_CONFIG\" 1",temp);
		//new_command = hepta_server.Hepta_Command_Transfer("get cpri cpri_wrapper_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable",temp);

		//new_command = hepta_server.Hepta_Command_Transfer("get capctrl capture_cpri_out_link1_sync_capture memory_interface \"Buffer\" 8192",temp);
		//new_command = hepta_server.Hepta_Command_Transfer("get cpri cpri_wrapper_top_cpri_llink_0 cpu \"L1_CONFIG\" tx_enable",temp);
		//new_command = hepta_server.Hepta_Command_Transfer("get sysid sysid_qsys_0 Register_Map \"id\" id",temp);
		//new_command = hepta_server.Hepta_Command_Transfer("get sysid sysid_qsys_0 Register_Map \"timestamp\" \"timestamp\"",temp);
		
		//new_command = hepta_server.Hepta_Command_Transfer("get sysid sysid_qsys_0 Register_Map \"timestamp\" 1",temp);
		
		
		//new_command = hepta_server.Hepta_Command_Transfer("set dfd 0 \"DxC Pattern Buf CTRL0\" Start \"Start Pattern\"");
		//new_command = hepta_server.Hepta_Command_Transfer("set dfd 0 \"DxC Pattern Buf CTRL0\" Start \"Start Pattern\"");
			
		
		//if (hepta_server !=null) 
		if (false)
		{
			try 
			{
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hepta_server.getCommand_list_to_Send().add(new_command);
			
			Thread current_thread =  hepta_server.getHepta_Write_Thread();
			
			if (current_thread != null) 
			{
				synchronized (current_thread) {
					current_thread.notify();
				}
			}
			else
			{
				System.out.println("Write thread not found");
			}
			
			System.out.println("Write thread notified");
			
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			hepta_server.Hepta_pc_eth_Server_Close();
			
		}
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true);*/
		
		/*hepta_server.Hepta_pc_eth_Server_Close();
		System.out.println("Exiting main");
		return;*/
	}

}
