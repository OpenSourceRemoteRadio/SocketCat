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
import  java.lang.Integer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
//import java.util.BitSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PcEthCommand {

	/**
	 * 
	 */
	
	private String      Packet_Header_String = "alt";
    private short       Command_Id;
    private Integer     transfer_type; 	/**< Type of the command. Polling, sync or async interrupt*/
    private int     	packet_type; 	/**< Operation to be performed*/
    private int		    module_type; 	/**< Module to be accessed*/
    private int		    instant_num; 	/**< Instance number of the module*/
    private int		    resource_num;   /**< Resource number of the resource*/
    private int 	    address; 		/**< Offset from the base address of the resource*/
    private Integer     bit_mask; 		/**< Offset from the base address of the resource*/
    private Integer     bit_shift;		/**< Offset from the base address of the resource*/
    private int		    length; 		/**< Number of consecutive memory to be accessed in units of 32bits*/
    private int[] 		data_array;
    private byte[] 		byte_array;
	
	public PcEthCommand() 
	{
		/*Command_Id = 1; 
		transfer_type = 2;
        packet_type = 3;
        module_type = 4;
		address = 504; 	
	    bit_mask = 0xffffff0f; 	
		setLength(1);
		setData_array(new Integer[1]);
		getData_array()[0] = 8;
		instant_num = 0;
		bit_shift = 4;*/
	}
        
	public int hashCode()
	{
		return this.Command_Id;
	}

	
	public boolean equals( Object obj )
	{
		boolean flag = false;
		PcEthCommand compare_command = ( PcEthCommand )obj;
		
		if((Command_Id == compare_command.Command_Id))
		{
			flag = true;
		}

		return flag;
	}
	
	
	
	/**
	 * @return the byte_array
	 */
	public byte[] getByte_array() {
		return byte_array;
	}




	/**
	 * @param byte_array the byte_array to set
	 */
	public void setByte_array(byte[] byte_array) {
		this.byte_array = byte_array;
	}




	/**
	 * @return the data_array
	 */
	public int[] getData_array() {
		return data_array;
	}




	/**
	 * @param data_array the data_array to set
	 */
	public void setData_array(int[] data_array) {
		this.data_array = data_array;
	}




	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}




	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}




	/**
	 * @return the command_Id
	 */
	public Short getCommand_Id() {
		return Command_Id;
	}




	/**
	 * @param command_Id the command_Id to set
	 */
	public void setCommand_Id(Short command_Id) {
		Command_Id = command_Id;
	}




	/**
	 * @return the transfer_type
	 */
	public Integer gettransfer_type() {
		return transfer_type;
	}




	/**
	 * @param transfer_type the transfer_type to set
	 */
	public void settransfer_type(Integer transfer_type) {
		transfer_type = transfer_type;
	}




	/**
	 * @return the packet_type
	 */
	public Integer getPacket_type() {
		return packet_type;
	}




	/**
	 * @param packet_type the packet_type to set
	 */
	public void setPacket_type(Integer packet_type) {
		this.packet_type = packet_type;
	}


	public void Hepta_ByteEncode_Packet_Header(short id)
	{
		byte[] byte_to_return = new byte[PcEthPacketConfiguration.PACKET_HEADER_SIZE];
        ByteBuffer byte_buffer_to_return = ByteBuffer.allocate(PcEthPacketConfiguration.PACKET_HEADER_SIZE);
		byte_buffer_to_return.order(ByteOrder.LITTLE_ENDIAN);

        //Packet header
        //** header **//*
        byte[] header_string = this.Packet_Header_String.getBytes();
        byte_buffer_to_return.put(header_string);
        
        //** Id **//*
        byte_buffer_to_return.put((byte)id);
        
        //** length **//*
        byte_buffer_to_return.putInt(0);
        
        byte_buffer_to_return.put((byte) 0);
        
        byte_buffer_to_return.rewind();
        byte_buffer_to_return.get(byte_to_return,0,byte_to_return.length);

        //this.setByte_array(byte_to_return);
		this.setByte_array(byte_to_return);
	}


	public void Hepta_ByteEncode_Command()
    {
        int number_of_data_byte =  (4 * this.getData_array().length);
        
        byte[] byte_to_return = new byte[(PcEthPacketConfiguration.PACKET_HEADER_SIZE + PcEthPacketConfiguration.COMMAND_HEADER_SIZE) +number_of_data_byte];
        ByteBuffer byte_buffer_to_return = ByteBuffer.allocate((PcEthPacketConfiguration.PACKET_HEADER_SIZE + PcEthPacketConfiguration.COMMAND_HEADER_SIZE) + number_of_data_byte);
        ByteBuffer length_byte_buffer = ByteBuffer.allocate(4);
        length_byte_buffer.order(ByteOrder.LITTLE_ENDIAN);
		byte_buffer_to_return.order(ByteOrder.LITTLE_ENDIAN);

        //Packet header
        /** header **/
        byte[] header_string = this.Packet_Header_String.getBytes();
        byte_buffer_to_return.put(header_string);
        
        /** Id **/
        byte_buffer_to_return.put((byte)this.Command_Id);
        
        /** length **/
        byte_buffer_to_return.putInt((PcEthPacketConfiguration.COMMAND_HEADER_SIZE + number_of_data_byte));
        
        byte_buffer_to_return.put((byte) 0);
        
        //System.out.println(byte_buffer_to_return.position());	//9
        
        //Command header
        /** Module type **/
        byte Transfer_Type_Module_Index = (byte) this.module_type;
        Transfer_Type_Module_Index = (byte) ((Transfer_Type_Module_Index << 1) & 0xFE);
      
        /** Transfer type **/
        if(this.transfer_type == TransferType.Service_Mode.getTransfer_type_value())
        {
        	Transfer_Type_Module_Index = (byte) (Transfer_Type_Module_Index | 1);
        }
        
        byte_buffer_to_return.put(Transfer_Type_Module_Index);
        
        /** Instance and Packet type **/
        byte Operation_Type_Instance = (byte) this.instant_num;
        Operation_Type_Instance = (byte) ((Operation_Type_Instance << 4) & 0xF0);
        Operation_Type_Instance = (byte) (Operation_Type_Instance  | ((byte) this.packet_type));
        byte_buffer_to_return.put(Operation_Type_Instance);
        
        
        byte_buffer_to_return.put((byte) this.resource_num);
        
        /** Address **/
        byte_buffer_to_return.putShort((short) this.address);
        /** Bit Mask **/
        byte_buffer_to_return.putInt(this.bit_mask);
        /** Length **/
        
        
        /*byte_buffer_to_return.putShort((short) (this.getLength() & 0xFFFF));
        
        byte_buffer_to_return.put((byte) 0);*/
        
        //byte_buffer_to_return.putInt((this.getLength() & 0x3FFFFF));
        length_byte_buffer.putInt((this.getLength() & 0x3FFFFF));
        length_byte_buffer.position(0);
        
        //byte_buffer_to_return.position(byte_buffer_to_return.position() - 1);
        byte_buffer_to_return.put(length_byte_buffer.get());
        byte_buffer_to_return.put(length_byte_buffer.get());
        byte_buffer_to_return.put(length_byte_buffer.get());
        
        
        //reserved
        byte_buffer_to_return.put((byte) 0);
        byte_buffer_to_return.put((byte) 0);
        byte_buffer_to_return.put((byte) 0);
        
        /*byte_buffer_to_return.put((byte) 0);
        byte_buffer_to_return.put((byte) 0);*/
        //System.out.println(byte_buffer_to_return.position());
        
        
        //Data array
        for (int data_index = 0; data_index < this.getData_array().length; data_index++) {
			byte_buffer_to_return.putInt((this.getData_array()[data_index])  << (this.bit_shift) );
		}
        
        //System.out.println(byte_buffer_to_return.position());
        
        byte_buffer_to_return.rewind();
        byte_buffer_to_return.get(byte_to_return,0,byte_to_return.length);

        this.setByte_array(byte_to_return);
        
    }




	public static PcEthCommand Interpret_CommandString(Short Command_Id,PcEthServer current_server, String command_string, boolean data_available) 
	{
		PcEthCommand decoded_command = new PcEthCommand();
		ModulesList module_list = current_server.getModule_List();
		List<String> extracted_command_string = new ArrayList<String>();
		Matcher command_word_splitt_matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command_string);
		while (command_word_splitt_matcher.find())
		{
			extracted_command_string.add(command_word_splitt_matcher.group(1).replace("\"", "")); 
		}
		
		if(!extracted_command_string.isEmpty())
		{
			decoded_command.Command_Id = new Short(Command_Id);
			ArrayList<String> Operation_command_list = current_server.getOperation_Command_String();
			String operation_string = extracted_command_string.get(0);		
			
			
			if(Operation_command_list!=null)
			{
				if(Operation_command_list.contains(operation_string))
				{
					decoded_command.packet_type = Operation_command_list.indexOf(operation_string.toLowerCase(Locale.ENGLISH) );
                    boolean decoder_return = false;
                    
                    if (decoded_command.packet_type != -1) 
                    {
						if (module_list != null) 
						{
							if (operation_string.equalsIgnoreCase("set")) {
								decoder_return = decoded_command
										.Interpret_Set_Command(module_list,
												extracted_command_string,
												data_available);
							} else if (operation_string.equalsIgnoreCase("get")) {
								decoder_return = decoded_command
										.Interpret_Get_Command(module_list,
												extracted_command_string);
							} else if (operation_string
									.equalsIgnoreCase("service")) {
								decoder_return = decoded_command
										.Interpret_Service_Command(module_list,
												extracted_command_string);
							} else if (operation_string
									.equalsIgnoreCase("config")) {
								decoder_return = decoded_command
										.Interpret_Config_Command(module_list,
												extracted_command_string,data_available);
							}
							else
							{
								decoder_return = decoded_command
										.Interpret_Default_Command(module_list,extracted_command_string,data_available);
							}
							
						} else {
							decoder_return = decoded_command
									.Interpret_Default_Command(extracted_command_string,data_available);
						}
					}
                    
					if(decoder_return == false)
                    {
                        decoded_command = null;
                    }
                }
                else
                {
                    decoded_command = null;
                }
			}
			
		}
		
		return decoded_command;
	}




	private boolean Interpret_Default_Command(ModulesList module_list,
			List<String> extracted_command_string, boolean data_available) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean Interpret_Service_Command(ModulesList module_list,
			List<String> extracted_command_string) {
		
		String module_name = extracted_command_string.get(1);
        ModuleRegMap selected_module = null;
        
        this.transfer_type =  TransferType.Service_Mode.getTransfer_type_value();

        for (ModuleRegMap current_module : module_list) 
        {
        	if(module_name.equalsIgnoreCase(current_module.getModule_Name()))
            {
                this.module_type = current_module.getModule_Index();
                selected_module = current_module;
                break;
            }
		}
        
        if (selected_module!=null)
        {
            String instance_number = extracted_command_string.get(2);
            Integer instance_number_int = null;
            Boolean instance_found = false;
            
            try {
				instance_number_int = Integer.parseInt(instance_number);
				this.instant_num = instance_number_int;
			} catch (NumberFormatException e) {
				
				for (int instance_index = 0; instance_index < selected_module.getInstance_Name_List().size(); instance_index++) 
				{
					String instance_name = selected_module.getInstance_Name_List().get(instance_index);
					if(instance_number.equalsIgnoreCase(instance_name))
					{
						this.instant_num = instance_index;
						instance_found = true;
						break;
					}
				}
				
				if(instance_found== false)
				{
					e.printStackTrace();
					return false;
				}
			}
        }
        else
        {
            return false;
        }

        

        /*if ((selected_module != null))
        {*/
        	 String service_number = extracted_command_string.get(3);
             Integer service_number_int = null;
             Boolean service_found = false;
             
             try {
            	 service_number_int = Integer.parseInt(service_number);
 				this.address = service_number_int;
 			} catch (NumberFormatException e) {
 				
 				for (int service_index = 0; service_index < selected_module.getServices_List().size(); service_index++) 
 				{
 					String service_name = selected_module.getServices_List().get(service_index) ;
 					if(service_number.equalsIgnoreCase(service_name))
 					{
 						this.address = service_index;
 						service_found = true;
 						break;
 					}
 				}
 				
 				if(service_found== false)
 				{
 					e.printStackTrace();
 					return false;
 				}
 			}
        /*}
        else
        {
        	return false;
        }*/

        this.bit_mask = 0;
        this.bit_shift = 0;
                
    	String enable_disable = extracted_command_string.get(4);
    	int parsed_int  = 0;
        try {
        	parsed_int = Integer.parseInt(enable_disable);
        	if(parsed_int == 1 || parsed_int==0)
        	{
        		this.length = parsed_int;
        		return true;
        	}
        	else
        	{
        		return false;
        	}
		} catch (NumberFormatException e) {
			if(enable_disable.equalsIgnoreCase("enable"))
			{
				this.length = 1;
        		return true;
			}
			else if(enable_disable.equalsIgnoreCase("disable"))
			{
				this.length = 0;
        		return true;
			}
			else
			{
        		return false;
			}
		}
    		
	}




	private boolean Interpret_Default_Command(
			List<String> extracted_command_string, boolean data_available) {
		// TODO Auto-generated method stub
		return false;
	}




	private boolean Interpret_Config_Command(ModulesList module_list,
			List<String> extracted_command_string, boolean data_available) {
		
		String module_name = extracted_command_string.get(1);
        ModuleRegMap selected_module = null;
        
        this.transfer_type =  TransferType.Poll_Mode.getTransfer_type_value();

        for (ModuleRegMap current_module : module_list) 
        {
        	if(module_name.equalsIgnoreCase(current_module.getModule_Name()))
            {
                this.module_type = current_module.getModule_Index();
                selected_module = current_module;
                break;
            }
		}
        
        if (selected_module!=null)
        {
            String instance_number = extracted_command_string.get(2);
            Integer instance_number_int = null;
            Boolean instance_found = false;
            
            try {
				instance_number_int = Integer.parseInt(instance_number);
				this.instant_num = instance_number_int;
			} catch (NumberFormatException e) {
				
				for (int instance_index = 0; instance_index < selected_module.getInstance_Name_List().size(); instance_index++) 
				{
					String instance_name = selected_module.getInstance_Name_List().get(instance_index);
					if(instance_number.equalsIgnoreCase(instance_name))
					{
						this.instant_num = instance_index;
						instance_found = true;
						break;
					}
				}
				
				if(instance_found== false)
				{
					e.printStackTrace();
					return false;
				}
			}
        }
        else
        {
            return false;
        }

        

        /*if ((selected_module != null))
        {*/
        	 String config_number = extracted_command_string.get(3);
             Integer config_number_int = null;
             Boolean config_found = false;
             
             try {
            	 config_number_int = Integer.parseInt(config_number);
 				this.address = config_number_int;
 			} catch (NumberFormatException e) {
 				
 				for (int config_index = 0; config_index < selected_module.getConfigurations_List().size(); config_index++) 
 				{
 					String config_name = selected_module.getConfigurations_List().get(config_index) ;
 					if(config_number.equalsIgnoreCase(config_name))
 					{
 						this.address = config_index;
 						config_found = true;
 						break;
 					}
 				}
 				
 				if(config_found== false)
 				{
 					e.printStackTrace();
 					return false;
 				}
 			}
        /*}
        else
        {
        	return false;
        }*/

    	String length_or_data = extracted_command_string.get(4);
    	int parsed_int  = 0;
        try {
        	parsed_int = Integer.parseInt(length_or_data);
		} catch (NumberFormatException e) {
			return false;
		}
        
    	//if(data_available)
        if(true)
    	{
    		this.bit_mask = 0;
            this.bit_shift = 0;
            this.length = parsed_int;
            return true;
    	}
    	else
    	{
    		this.bit_mask = 0;
            this.bit_shift = 0;
            this.length = 1;
            this.data_array =  new int[1];
            this.data_array[0] = parsed_int;
            return true;
    	}
	}




	private boolean Interpret_Get_Command(ModulesList module_list,
			List<String> extracted_command_string) {
		// TODO Auto-generated method stub
		
		String module_name = extracted_command_string.get(1);
        ModuleRegMap selected_module = null;
        Register selected_register = null;
        //Register_Bit selected_register_bit = null;
        ModuleResource selected_module_resource = null;
        
        this.transfer_type =  TransferType.Poll_Mode.getTransfer_type_value();

        for (ModuleRegMap current_module : module_list) 
        {
        	if(module_name.equalsIgnoreCase(current_module.getModule_Name()))
            {
                this.module_type = current_module.getModule_Index();
                selected_module = current_module;
                break;
            }
		}
        
        if (selected_module!=null)
        {
            String instance_number = extracted_command_string.get(2);
            Integer instance_number_int = null;
            Boolean instance_found = false;
            
            try {
				instance_number_int = Integer.parseInt(instance_number);
				this.instant_num = instance_number_int;
			} catch (NumberFormatException e) {
				
				for (int instance_index = 0; instance_index < selected_module.getInstance_Name_List().size(); instance_index++) 
				{
					String instance_name = selected_module.getInstance_Name_List().get(instance_index);
					if(instance_number.equalsIgnoreCase(instance_name))
					{
						this.instant_num = instance_index;
						instance_found = true;
						break;
					}
				}
				
				if(instance_found== false)
				{
					e.printStackTrace();
					return false;
				}
			}
             
        }
        else
        {
            return false;
        }
        
        
       /* if ((selected_module != null))
        {*/
        	 String resource_number = extracted_command_string.get(3);
             Integer resource_number_int = null;
             Boolean resource_found = false;
             
             try {
            	 resource_number_int = Integer.parseInt(resource_number);
 				this.resource_num = resource_number_int;
 			} catch (NumberFormatException e) {
 				
 				for (int resource_index = 0; resource_index < selected_module.getResources().size(); resource_index++) 
 				{
 					String resource_name = selected_module.getResources().get(resource_index).getResource_Name() ;
 					if(resource_number.equalsIgnoreCase(resource_name))
 					{
 						selected_module_resource = selected_module.getResources().get(resource_index);
 						this.resource_num = selected_module.getResources().get(resource_index).getResource_Index();
 						resource_found = true;
 						break;
 					}
 				}
 				
 				if(resource_found== false)
 				{
 					e.printStackTrace();
 					return false;
 				}
 			}
        /*}
        else
        {
        	return false;
        }*/


        if ((selected_module_resource != null))
        {
           String register_name = extracted_command_string.get(4);
            
           Integer address_offset = 0;

           if ((register_name.contains("[") == true) && (register_name.contains("]") == true) && (register_name.endsWith("]")==true))
            {
                int address_offset_start_index = register_name.indexOf("[");
                int address_offset_end_index = register_name.indexOf("]");
                String register_offset = register_name.substring(address_offset_start_index + 1, (address_offset_end_index - address_offset_start_index) - 1);
                register_name = register_name.substring(0,address_offset_start_index);
                
                try {
					address_offset = Integer.parseInt(register_offset);
				} catch (NumberFormatException e) {
					address_offset = 0;
					//e.printStackTrace();
				}
                
            }
           
           for (Register current_register : selected_module_resource.getRegister_Offsets()) 
           {
        	   if (register_name.equalsIgnoreCase(current_register.getRegister_Name()))
               {
                   this.address = current_register.getAddress_offset() + address_offset;
                   selected_register = current_register;
                   break;
               }
           }
           
        }
        else
        {
            return false;
        }

        if(selected_register!=null)
        {
	        if(extracted_command_string.size()==5)
	        {
	        	this.bit_mask = 0xFFFFFFFF;
	            this.bit_shift = 0;
	            this.length = 1;
	            return true;
	        }
        }
        else
        {
        	return false;
        }
        

		/*if(selected_register!=null)
        {*/
            String bit_name_length = extracted_command_string.get(5);
            Integer length = 0;
            boolean length_parse = false;

            try {
				length = Integer.parseInt(bit_name_length);
				length_parse = true;
			} catch (NumberFormatException e) {
				length_parse = false;
				//e.printStackTrace();
			}
           
            
            if(length_parse == false)
            {
                for (RegisterBit current_register_bit : selected_register.getBits()) 
                {
                	if (bit_name_length.equalsIgnoreCase(current_register_bit.getBit_Name()))
                    {
                        Integer one = 1;
                        
                        if(current_register_bit.getBit_Width() == 32)
                        {
                        	this.bit_mask = 0xffffffff;
                            this.bit_shift = 0;
                        }
                        else
                        {
                        	Integer mask = (one << (current_register_bit.getBit_Width())) - 1;
                        	this.bit_mask = mask << (current_register_bit.getBit_Position()) ;
                            this.bit_shift = current_register_bit.getBit_Position();
                        }
                        //selected_register_bit = current_register_bit;
                        
                        if(extracted_command_string.size()==7)
                        {
                        	String length_string = extracted_command_string.get(6);
                        	 try {
                 				this.length = Integer.parseInt(length_string);
                                return true;
                 			} catch (NumberFormatException e) {
                 				this.length = 1;
                            	return true;
                 			}
                        }
                        /*else
                        {*/
                        	this.length = 1;
                        	return true;
                        //}
                    }
				}
                
            }
            else
            {
            	this.bit_mask = 0xFFFFFFFF;
                this.bit_shift = 0;
                this.length = length;
                return true;
            }
        
       /* }        
		else
        {
            return false;
        }*/
		
        return false;
		
	}




	private boolean Interpret_Set_Command(ModulesList module_list,
			List<String> extracted_command_string, boolean data_available) {
		String module_name = extracted_command_string.get(1);
        ModuleRegMap selected_module = null;
        ModuleResource selected_module_resource = null;
        Register selected_register = null;
        RegisterBit selected_register_bit = null;
        
        this.transfer_type =  TransferType.Poll_Mode.getTransfer_type_value();

        for (ModuleRegMap current_module : module_list) 
        {
        	if(module_name.equalsIgnoreCase(current_module.getModule_Name()))
            {
                this.module_type = current_module.getModule_Index();
                selected_module = current_module;
                break;
            }
		}
        
        if (selected_module!=null)
        {
            String instance_number = extracted_command_string.get(2);
            Integer instance_number_int = null;
            Boolean instance_found = false;
            
            try {
				instance_number_int = Integer.parseInt(instance_number);
				this.instant_num = instance_number_int;
			} catch (NumberFormatException e) {
				
				for (int instance_index = 0; instance_index < selected_module.getInstance_Name_List().size(); instance_index++) 
				{
					String instance_name = selected_module.getInstance_Name_List().get(instance_index);
					if(instance_number.equalsIgnoreCase(instance_name))
					{
						this.instant_num = instance_index;
						instance_found = true;
						break;
					}
				}
				
				if(instance_found== false)
				{
					e.printStackTrace();
					return false;
				}
			}
        }
        else
        {
            return false;
        }

        

        /*if ((selected_module != null))
        {*/
        	 String resource_number = extracted_command_string.get(3);
             Integer resource_number_int = null;
             Boolean resource_found = false;
             
             try {
            	 resource_number_int = Integer.parseInt(resource_number);
 				this.resource_num = resource_number_int;
 			} catch (NumberFormatException e) {
 				
 				for (int resource_index = 0; resource_index < selected_module.getResources().size(); resource_index++) 
 				{
 					String resource_name = selected_module.getResources().get(resource_index).getResource_Name() ;
 					if(resource_number.equalsIgnoreCase(resource_name))
 					{
 						selected_module_resource = selected_module.getResources().get(resource_index);
 						this.resource_num = selected_module.getResources().get(resource_index).getResource_Index();
 						resource_found = true;
 						break;
 					}
 				}
 				
 				if(resource_found== false)
 				{
 					e.printStackTrace();
 					return false;
 				}
 			}
        /*}
        else
        {
        	return false;
        }*/
        

        if ((selected_module_resource != null))
        {
           String register_name = extracted_command_string.get(4);
            
           Integer address_offset = 0;

           if ((register_name.contains("[") == true) && (register_name.contains("]") == true) && (register_name.endsWith("]")==true))
            {
                int address_offset_start_index = register_name.indexOf("[");
                int address_offset_end_index = register_name.indexOf("]");
                String register_offset = register_name.substring(address_offset_start_index + 1, (address_offset_end_index - address_offset_start_index) - 1);
                register_name = register_name.substring(0,address_offset_start_index);
                
                try {
					address_offset = Integer.parseInt(register_offset);
				} catch (NumberFormatException e) {
					address_offset = 0;
					//e.printStackTrace();
				}
                
            }
           
           for (Register current_register : selected_module_resource.getRegister_Offsets()) 
           {
        	   if (register_name.equalsIgnoreCase(current_register.getRegister_Name()))
               {
                   this.address = current_register.getAddress_offset() + address_offset;
                   selected_register = current_register;
                   break;
               }
           }
           
        }
        else
        {
            return false;
        }
                
        if(selected_register!=null)
        {
	        if(extracted_command_string.size()==6)
	        {
	        	String length_or_data = extracted_command_string.get(5);
	        	int parsed_int  = 0;
	            try {
	            	parsed_int = Integer.parseInt(length_or_data);
				} catch (NumberFormatException e) {
					return false;
				}
	            
	        	if(data_available)
	        	{
	        		this.bit_mask = 0;
	                this.bit_shift = 0;
	                this.length = parsed_int;
	                return true;
	        	}
	        	else
	        	{
	        		this.bit_mask = 0;
	                this.bit_shift = 0;
	                this.length = 1;
	                this.data_array =  new int[1];
	                this.data_array[0] = parsed_int;
	                return true;
	        	}
	        }
        }
        else
        {
        	return false;
        }
        

		/*if(selected_register!=null)
        {*/
            String bit_name = extracted_command_string.get(5);
            this.length = 1;
            for (RegisterBit current_register_bit : selected_register.getBits()) 
            {
            	if (bit_name.equalsIgnoreCase(current_register_bit.getBit_Name()))
                {
                    Integer one = 1;
                    if(current_register_bit.getBit_Width() == 32)
                    {
                    	this.bit_mask = 0xffffffff;
                        this.bit_shift = 0;
                    }
                    else
                    {
                    	Integer mask = (one << (current_register_bit.getBit_Width())) - 1;
                    	this.bit_mask = mask << (current_register_bit.getBit_Position()) ;
                        this.bit_shift = current_register_bit.getBit_Position();
                    }
                    selected_register_bit = current_register_bit;
                    break;
                }
			}
        /*}        
		else
        {
            return false;
        }*/
		
		
		
		

		if (selected_register_bit != null)
        {
            String data_config_string = extracted_command_string.get(6);
            boolean config_found = false;
            Integer data;
            
            if (selected_register_bit.getValue_Description() != null)
            {
            	for (ConfigurationAction current_configuration : selected_register_bit.getValue_Description()) 
            	{
            		if (data_config_string.equalsIgnoreCase(current_configuration.getConfiguration_result()))
                    {
                        config_found = true;
                        this.data_array = new int[1];
                        this.data_array[0] = current_configuration.getConfiguration_value();
                        break;
                    }
				} 	
            }
            
            if(config_found == false)
            {
                try {
					data = Integer.parseInt(data_config_string);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
                
                    this.data_array = new int[1];
                    this.data_array[0] = data;
                    return true;
                
            }

        }
        else
        {
            return false;
        }


        return true;
	}




	public int Decode_Packet_Header_Byte(byte[] byte_packet_header) 
	{
		if(byte_packet_header.length != PcEthPacketConfiguration.PACKET_HEADER_SIZE)
		{
			return -1;
		}
		
		try {
			ByteBuffer byte_buffer_to_decode = ByteBuffer.wrap(byte_packet_header);
			byte_buffer_to_decode.order(ByteOrder.LITTLE_ENDIAN);
			
			byte_buffer_to_decode.position(3);
			
			this.Command_Id = (short) (byte_buffer_to_decode.get() & 0xff);
	
			int length_in_bytes = -1;
			//length_in_bytes = (int) (byte_buffer_to_decode.getShort() & 0xffff) ;
			length_in_bytes = (int) (byte_buffer_to_decode.getInt()) ;
			
			return length_in_bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}




	public boolean Decode_Packet_Byte(byte[] byte_remaining_packet) {
		
		try {
			ByteBuffer byte_buffer_to_decode = ByteBuffer.wrap(byte_remaining_packet);
			byte_buffer_to_decode.order(ByteOrder.LITTLE_ENDIAN);
			ByteBuffer length_byte_buffer = ByteBuffer.allocate(4);
			length_byte_buffer.order(ByteOrder.LITTLE_ENDIAN);
			
	        //Command header
			
			Byte Module_Index_transmission_Type = byte_buffer_to_decode.get();
			/** Transfer type **/
			this.transfer_type = Module_Index_transmission_Type & 0x1;
			/** Module type **/
			this.module_type = (Module_Index_transmission_Type & 0xFE)>>1;
                        
            
            Byte Packet_Type_Instance = byte_buffer_to_decode.get();
            /** Instance number **/
	        this.instant_num = (Packet_Type_Instance & 0xF0) >> 4;
	        /** Packet type **/
            this.packet_type = (Packet_Type_Instance & 0xF);
	        
            this.resource_num = (int) byte_buffer_to_decode.get();

            /** Address **/
	        this.address = byte_buffer_to_decode.getShort();
	        /** Bit Mask **/
	        this.bit_mask = byte_buffer_to_decode.getInt();
	        /** Length **/
	        //this.length = byte_buffer_to_decode.getShort();
	        
	        length_byte_buffer.put(byte_buffer_to_decode.get());
	        length_byte_buffer.put(byte_buffer_to_decode.get());
	        length_byte_buffer.put(byte_buffer_to_decode.get());
	        length_byte_buffer.put((byte) 0);
	        length_byte_buffer.position(0);
	        
	        //this.length = (byte_buffer_to_decode.getInt() & 0x3FFFFF);
	        this.length  = (length_byte_buffer.getInt()  & 0x3FFFFF) ;
	        
	        
	        
	        //byte_buffer_to_decode
	        //byte_buffer_to_decode.position(byte_buffer_to_decode.position() - 1);
	        
	        //byte_buffer_to_decode.get();
	        
	        //reserved
	        byte_buffer_to_decode.get();
	        byte_buffer_to_decode.get();
	        byte_buffer_to_decode.get();
	        
	        
	        
	        /*byte_buffer_to_decode.get();
	        byte_buffer_to_decode.get();*/
	        
	        //System.out.println("Length ==> " + Integer.toString(this.length));
	        
	        if(this.length > 0)
	        {
		        this.data_array = new int[this.length]; 
		        //Data array
		        for (int data_index = 0; data_index < this.length; data_index++) 
		        {
		        	this.getData_array()[data_index] = (byte_buffer_to_decode.getInt()); 
				}
	        }
	        
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
