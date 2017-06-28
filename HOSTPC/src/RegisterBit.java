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
//import java.util.ArrayList;


public class RegisterBit {

	/**
	 * 
	 */
	
	private String Bit_Name;
    private int Module_Index;
    private String Module_Name;
    private int Address_offset;
    private int Bit_Position;
    private int Bit_Width;
    private String Bit_Description;
    private ConfigurationActionsList Value_Description = new ConfigurationActionsList();
    private RegisterCategory Category;
    //private String Code;
    //private String Bit_Struct_Member_Name;
    private String Register_Name;
	
	/**
	 * @return the bit_Name
	 */
	public String getBit_Name() {
		return Bit_Name;
	}

	/**
	 * @param bit_Name the bit_Name to set
	 */
	public void setBit_Name(String bit_Name) {
		Bit_Name = bit_Name;
	}

	/**
	 * @return the module_Index
	 */
	public int getModule_Index() {
		return Module_Index;
	}

	/**
	 * @param module_Index the module_Index to set
	 */
	public void setModule_Index(int module_Index) {
		Module_Index = module_Index;
	}

	/**
	 * @return the module_Name
	 */
	public String getModule_Name() {
		return Module_Name;
	}

	/**
	 * @param module_Name the module_Name to set
	 */
	public void setModule_Name(String module_Name) {
		Module_Name = module_Name;
	}

	/**
	 * @return the address_offset
	 */
	public int getAddress_offset() {
		return Address_offset;
	}

	/**
	 * @param address_offset the address_offset to set
	 */
	public void setAddress_offset(int address_offset) {
		Address_offset = address_offset;
	}

	/**
	 * @return the bit_Position
	 */
	public int getBit_Position() {
		return Bit_Position;
	}

	/**
	 * @param bit_Position the bit_Position to set
	 */
	public void setBit_Position(int bit_Position) {
		Bit_Position = bit_Position;
	}

	/**
	 * @return the bit_Width
	 */
	public int getBit_Width() {
		return Bit_Width;
	}

	/**
	 * @param bit_Width the bit_Width to set
	 */
	public void setBit_Width(int bit_Width) {
		Bit_Width = bit_Width;
	}

	/**
	 * @return the bit_Description
	 */
	public String getBit_Description() {
		return Bit_Description;
	}

	/**
	 * @param bit_Description the bit_Description to set
	 */
	public void setBit_Description(String bit_Description) {
		Bit_Description = bit_Description;
	}

	/**
	 * @return the value_Description
	 */
	public ConfigurationActionsList getValue_Description() {
		return Value_Description;
	}

	/**
	 * @param value_Description the value_Description to set
	 */
	/*public void setValue_Description(Configuration_Actions_List value_Description) {
		Value_Description = value_Description;
	}*/

	/**
	 * @return the category
	 */
	public RegisterCategory getCategory() {
		return Category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(RegisterCategory category) {
		Category = category;
	}

	/**
	 * @return the register_Name
	 */
	public String getRegister_Name() {
		return Register_Name;
	}

	/**
	 * @param register_Name the register_Name to set
	 */
	public void setRegister_Name(String register_Name) {
		Register_Name = register_Name;
	}

	public RegisterBit() {
		// TODO Auto-generated constructor stub
	}

/*	public ArrayList<String> getValue_Description_Name_List() {
		if(this.Value_Description !=null)
		{
			ArrayList<String> Configuration_Name_List = new ArrayList<String>();
			
			for (Configuration_Action current_config : this.Value_Description) {
				Configuration_Name_List.add(current_config.getConfiguration_result());
			}
			
			return Configuration_Name_List;
		}
		else
		{
			return null;
		}
	}*/

	public String[] Get_Value_Description_Name_List() {
		if(this.Value_Description !=null)
		{
			String[] Configuration_Name_List = new String [this.Value_Description.size()];
			
			for (int config_index = 0; config_index < this.Value_Description.size(); config_index++) {
				ConfigurationAction current_config = this.Value_Description.get(config_index);
				Configuration_Name_List[config_index] = new String(current_config.getConfiguration_result());  
			}
			
			return Configuration_Name_List;
		}
		/*else
		{*/
			return null;
		/*}*/
	}

}
