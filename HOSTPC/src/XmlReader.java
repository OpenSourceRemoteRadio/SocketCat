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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class XmlReader {

	
	private XMLStreamReader XML_Stream_Reader;
	
	
	/**
	 * @return the xML_Stream_Reader
	 */
	public XMLStreamReader getXML_Stream_Reader() {
		return XML_Stream_Reader;
	}


	/**
	 * @param xML_Stream_Reader the xML_Stream_Reader to set
	 */
	public void setXML_Stream_Reader(XMLStreamReader xML_Stream_Reader) {
		XML_Stream_Reader = xML_Stream_Reader;
	}

	/**
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 * 
	 */
    public XmlReader(String XML_Path) throws XMLStreamException, FileNotFoundException,SecurityException 
	{
		InputStream xml_input_stream = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		xml_input_stream = new FileInputStream(XML_Path);
		this.setXML_Stream_Reader(factory.createXMLStreamReader(xml_input_stream));
	}
	
	
	public ModulesList Create_Module_List_from_XML()
	{
		ModulesList module_list_to_return = null;
		ModuleRegMap current_module_regmap = null;
		ModuleResource current_module_resource = null;
		Register current_register = null;
		RegisterBit current_register_bit = null;
		
		if (XML_Stream_Reader != null) 
		{
			try 
			{
				while(XML_Stream_Reader.hasNext())
				{
					XML_Stream_Reader.next();
					
					switch (XML_Stream_Reader.getEventType()) 
					{
						case XMLStreamReader.START_ELEMENT:
							
							String Local_Name_Start_Element = new String(XML_Stream_Reader.getLocalName());
							//System.out.println("Event type Start element : " + Local_Name_Start_Element);
							
							if(Local_Name_Start_Element.equalsIgnoreCase("Modules"))
							{
								module_list_to_return = new ModulesList();	
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Module"))
							{
								if (module_list_to_return!=null) 
								{
									String Module_Name = XML_Stream_Reader
											.getAttributeValue(null, "Name");
									String Module_Index = XML_Stream_Reader
											.getAttributeValue(null, "Index");
									if ((Module_Name != null)
											&& (Module_Index != null)) {
										int Module_Index_Int = -1;
										try {
											Module_Index_Int = Integer
													.parseInt(Module_Index);
										} catch (NumberFormatException e) {
											Module_Index_Int = -1;
											e.printStackTrace();
										}

										current_module_regmap = new ModuleRegMap(
												Module_Index_Int, Module_Name);
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Resources"))
							{
								if(current_module_regmap!=null)
								{
									current_module_regmap.getResources().clear();									
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Resource"))
							{
								if(current_module_regmap!=null)
								{
									String Resource_Name = XML_Stream_Reader
											.getAttributeValue(null, "Name");
									String Resource_Index = XML_Stream_Reader
											.getAttributeValue(null, "Index");		
									
									if ((Resource_Name != null)
											&& (Resource_Index != null))
									{
										current_module_resource =  new ModuleResource(Resource_Name, Integer.parseInt(Resource_Index), current_module_regmap.getModule_Name(), current_module_regmap.getModule_Index());
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Registers"))
							{ 
								if(current_module_resource!=null)
								{
									current_module_resource.getRegister_Offsets().clear();
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Register"))
							{
								if (current_module_resource!=null) 
								{
									String Register_Name = XML_Stream_Reader
											.getAttributeValue(null, "Name");
									String Register_Address = XML_Stream_Reader
											.getAttributeValue(null, "Address");
									String Register_Size_in_bytes = XML_Stream_Reader
											.getAttributeValue(null,
													"Size_in_bytes");
									String Register_Category = XML_Stream_Reader
											.getAttributeValue(null, "Category");
									String Register_Permission = XML_Stream_Reader
											.getAttributeValue(null,
													"Permission");
									if ((Register_Name != null)
											&& (Register_Address != null)
											&& (Register_Size_in_bytes != null)) {
										current_register = new Register();
										current_register
												.setRegister_Name(Register_Name);

										int Register_Address_int = -1;
										int Register_Size_in_bytes_int = -1;
										try {
											Register_Address_int = Integer
													.parseInt(Register_Address);
											Register_Size_in_bytes_int = Integer
													.parseInt(Register_Size_in_bytes);
										} catch (NumberFormatException e) {
											e.printStackTrace();
											current_register = null;
											continue;
										}

										current_register
												.setAddress_offset(Register_Address_int);
										current_register
												.setRegister_Size_in_bytes(Register_Size_in_bytes_int);

										if ((Register_Category != null)) {
											current_register
													.setCategory(RegisterCategory
															.Get_Category_by_String(Register_Category));
										}

										if ((Register_Permission != null)) {
											current_register
													.setPermission(RegisterPermission
															.Get_Permission_by_String(Register_Permission));
										}
									}
								}
								
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Register_Description"))
							{
								if (current_register!=null) 
								{
									while (XML_Stream_Reader.hasNext()) {
										XML_Stream_Reader.next();
										if (XML_Stream_Reader.getEventType() == XMLStreamReader.CHARACTERS) {
											if (current_register != null) {
												String Register_Descritpion = XML_Stream_Reader
														.getText();
												if (Register_Descritpion != null) {
													current_register
															.setRegister_Description(Register_Descritpion);
												}
											}
											break;
										}
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Bits"))
							{
								if(current_register!=null)
								{
									current_register.getBits().clear();
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Bit"))
							{
								if (current_register!=null) 
								{
									String Bit_Name = XML_Stream_Reader
											.getAttributeValue(null, "Name");
									String Bit_Position = XML_Stream_Reader
											.getAttributeValue(null, "Position");
									String Bit_Width = XML_Stream_Reader
											.getAttributeValue(null, "Width");
									String Bit_Category = XML_Stream_Reader
											.getAttributeValue(null, "Category");
									if ((Bit_Name != null)
											&& (Bit_Position != null)
											&& (Bit_Width != null)) {
										current_register_bit = new RegisterBit();
										current_register_bit
												.setBit_Name(Bit_Name);
										
										current_register_bit
										.setAddress_offset(current_register.getAddress_offset());
										current_register_bit
										.setModule_Index(current_register.getModule_index());
										current_register_bit
										.setModule_Name(current_register.getModule_Name());

										int Bit_Position_int = -1;
										int Bit_Width_int = -1;
										try {
											Bit_Position_int = Integer
													.parseInt(Bit_Position);
											Bit_Width_int = Integer
													.parseInt(Bit_Width);
										} catch (NumberFormatException e) {
											e.printStackTrace();
											current_register_bit = null;
											continue;
										}

										
										current_register_bit
												.setBit_Position(Bit_Position_int);
										current_register_bit.setBit_Width(Bit_Width_int);
										

										if ((Bit_Category != null)) {
											current_register_bit
													.setCategory(RegisterCategory
															.Get_Category_by_String(Bit_Category));
										}
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Bit_Description"))
							{
								if (current_register_bit!=null) 
								{
									while (XML_Stream_Reader.hasNext()) {
										XML_Stream_Reader.next();
										if (XML_Stream_Reader.getEventType() == XMLStreamReader.CHARACTERS) {
											if (current_register_bit != null) {
												String Bit_Descritpion = XML_Stream_Reader
														.getText();
												if (Bit_Descritpion != null) {
													current_register_bit
															.setBit_Description(Bit_Descritpion);
												}
											}
											break;
										}
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Configurations"))
							{
								if(current_register_bit == null)
								{
									if(current_module_regmap!=null)
									{
										current_module_regmap.getConfigurations_List().clear(); 
									}
								}
								if(current_register_bit!=null)
								{
									current_register_bit.getValue_Description().clear();
								}								
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Configuration"))
							{
								if(current_register_bit == null)
								{
									if(current_module_regmap!=null)
									{
										String Name_String = XML_Stream_Reader
												.getAttributeValue(null, "Name");
																			
										if (Name_String != null)
										{
											current_module_regmap.getConfigurations_List().add(Name_String);
										}
									}
								}
								if (current_register_bit!=null) 
								{
									String Result_Sring = XML_Stream_Reader
											.getAttributeValue(null, "Result");
									String Value_String = XML_Stream_Reader
											.getAttributeValue(null, "Value");
									
									if ((Result_Sring != null)
											&& (Value_String != null)) {
										
										int Value_int = -1;
										
										try {
											Value_int = Integer
													.parseInt(Value_String);
										} catch (NumberFormatException e) {
											e.printStackTrace();
											continue;
										}

										ConfigurationAction current_configuration = new ConfigurationAction(Value_int, Result_Sring);
										if(current_configuration!=null)
										{
											current_register_bit.getValue_Description().add(current_configuration);
										}
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Instances"))
							{
								if(current_module_regmap!=null)
								{
									current_module_regmap.getInstance_Name_List().clear(); 
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Instance"))
							{
								if(current_module_regmap!=null)
								{
									String Name_String = XML_Stream_Reader
											.getAttributeValue(null, "Name");
																		
									if (Name_String != null)
									{
										current_module_regmap.getInstance_Name_List().add(Name_String);
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Configurations"))
							{
								if(current_module_regmap!=null)
								{
									current_module_regmap.getConfigurations_List().clear(); 
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Configuration"))
							{
								if(current_module_regmap!=null)
								{
									String Name_String = XML_Stream_Reader
											.getAttributeValue(null, "Name");
																		
									if (Name_String != null)
									{
										current_module_regmap.getConfigurations_List().add(Name_String);
									}
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Services"))
							{
								if(current_module_regmap!=null)
								{
									current_module_regmap.getServices_List().clear(); 
								}
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("Service"))
							{
								if(current_module_regmap!=null)
								{
									String Name_String = XML_Stream_Reader
											.getAttributeValue(null, "Name");
																		
									if (Name_String != null)
									{
										current_module_regmap.getServices_List().add(Name_String);
									}
								}
							}
							break;
						case XMLStreamReader.END_ELEMENT				:
							String Local_Name_End_Element = new String(XML_Stream_Reader.getLocalName());
							//System.out.println("Event type Start element : " + Local_Name_End_Element);
							
							if(Local_Name_End_Element.equalsIgnoreCase("Bit"))
							{
								if((current_register_bit!=null) && (current_register!=null))
								{
									current_register.getBits().add(current_register_bit);
								}
								current_register_bit = null;
							}
							else if(Local_Name_End_Element.equalsIgnoreCase("Register"))
							{
								if((current_register!=null) && (current_module_resource!=null))
								{
									current_module_resource.getRegister_Offsets().add(current_register);
								}
								current_register = null;
							}
							else if(Local_Name_End_Element.equalsIgnoreCase("Resource"))
							{
								if((current_module_resource!=null) && (current_module_regmap !=null))
								{
									current_module_regmap.getResources().add(current_module_resource);
								}
								current_module_resource = null;
							}
							else if(Local_Name_End_Element.equalsIgnoreCase("Module"))
							{
								if((current_module_regmap!=null) && (module_list_to_return!=null))
								{
									module_list_to_return.add(current_module_regmap);
								}
								current_module_regmap = null;	
							}
							
							
							break;
						case XMLStreamReader.CHARACTERS				:
							//System.out.println("Event type Character : " + XML_Stream_Reader.getText());
							break;
						case XMLStreamReader.COMMENT					:
							//System.out.println("Event type Comment : " + XML_Stream_Reader.getText());
							break;
						case XMLStreamReader.START_DOCUMENT			:
							break;
						case XMLStreamReader.END_DOCUMENT			:
							XML_Stream_Reader.close();
							break;
						/*case XMLStreamReader.PROCESSING_INSTRUCTION:
							break;
						case XMLStreamReader.ENTITY_REFERENCE		:
							break;
						case XMLStreamReader.DTD                   :
							break;
						case XMLStreamReader.ATTRIBUTE				:
							System.out.println("Event type Attribute : " + XML_Stream_Reader.getLocalName());
							System.out.println("Attributte count " + XML_Stream_Reader.getAttributeCount());
							break;
						case XMLStreamReader.NAMESPACE				:
							System.out.println("Event type Namespace : " + XML_Stream_Reader.getLocalName());
							break;
						case XMLStreamReader.SPACE					:
							break;
						case XMLStreamReader.CDATA					:
							break;*/

						default:
							//System.out.println("Event type Not in switch case" );
							break;
					}
					
				}
			} 
			catch (XMLStreamException e) 
			{
				module_list_to_return = null;
				e.printStackTrace();
			}
			
		}
		
		
		return module_list_to_return;
	}


	public ArrayList<String> Create_Command_List_from_XML() {
		ArrayList<String> commands_list_to_return = null;
		
		if (XML_Stream_Reader != null) 
		{
			try 
			{
				while(XML_Stream_Reader.hasNext())
				{
					XML_Stream_Reader.next();
					
					switch (XML_Stream_Reader.getEventType()) 
					{
						case XMLStreamReader.START_ELEMENT:
							
							String Local_Name_Start_Element = new String(XML_Stream_Reader.getLocalName());
							//System.out.println("Event type Start element : " + Local_Name_Start_Element);
							
							if(Local_Name_Start_Element.equalsIgnoreCase("Commands"))
							{
								commands_list_to_return = new ArrayList<String>();	
							}
							else if(Local_Name_Start_Element.equalsIgnoreCase("command"))
							{
								if (commands_list_to_return!=null) 
								{
									String Command_Name = XML_Stream_Reader
											.getAttributeValue(null, "Name");

									if ((Command_Name != null)) {
										commands_list_to_return.add(Command_Name);
									}
								}
							}
						case XMLStreamReader.CHARACTERS				:
							//System.out.println("Event type Character : " + XML_Stream_Reader.getText());
							break;
						case XMLStreamReader.COMMENT					:
							//System.out.println("Event type Comment : " + XML_Stream_Reader.getText());
							break;
						case XMLStreamReader.START_DOCUMENT			:
							break;
						case XMLStreamReader.END_DOCUMENT			:
							XML_Stream_Reader.close();
							break;
						/*case XMLStreamReader.PROCESSING_INSTRUCTION:
							break;
						case XMLStreamReader.ENTITY_REFERENCE		:
							break;
						case XMLStreamReader.DTD                   :
							break;
						case XMLStreamReader.ATTRIBUTE				:
							System.out.println("Event type Attribute : " + XML_Stream_Reader.getLocalName());
							System.out.println("Attributte count " + XML_Stream_Reader.getAttributeCount());
							break;
						case XMLStreamReader.NAMESPACE				:
							System.out.println("Event type Namespace : " + XML_Stream_Reader.getLocalName());
							break;
						case XMLStreamReader.SPACE					:
							break;
						case XMLStreamReader.CDATA					:
							break;*/

						default:
							//System.out.println("Event type Not in switch case" );
							break;
					}
					
				}
			} 
			catch (XMLStreamException e) 
			{
				commands_list_to_return = null;
				e.printStackTrace();
			}
			
		}
		
		commands_list_to_return.add("error");
		commands_list_to_return.add("ack");
		commands_list_to_return.add("manage");
		
		return commands_list_to_return;
		
	}
	
	

}
