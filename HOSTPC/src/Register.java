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


public class Register {

	/**
	 * 
	 */
	
	private String Register_Name;
    private int Module_index;
    private String Module_Name;
    private int Address_offset;
    private int Register_Size_in_bytes;
    private Integer Power_On_Reset_Value;
    private RegisterBitsList Bits = new RegisterBitsList();
    private RegisterCategory Category;
    private RegisterPermission Permission;
    private String Register_Description;
    private String Code;
    private String Struct_Definition_Name;
	
	
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
	 * @return the register_Size_in_bytes
	 */
	public int getRegister_Size_in_bytes() {
		return Register_Size_in_bytes;
	}

	/**
	 * @param register_Size_in_bytes the register_Size_in_bytes to set
	 */
	public void setRegister_Size_in_bytes(int register_Size_in_bytes) {
		Register_Size_in_bytes = register_Size_in_bytes;
	}

	/**
	 * @return the bits
	 */
	public RegisterBitsList getBits() {
		return Bits;
	}

	/**
	 * @param bits the bits to set
	 */
	/*public void setBits(RegisterBitsList bits) {
		Bits = bits;
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
	 * @return the permission
	 */
	public RegisterPermission getPermission() {
		return Permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(RegisterPermission permission) {
		Permission = permission;
	}

	/**
	 * @return the register_Description
	 */
	public String getRegister_Description() {
		return Register_Description;
	}

	/**
	 * @param register_Description the register_Description to set
	 */
	public void setRegister_Description(String register_Description) {
		Register_Description = register_Description;
	}

	/**
	 * @return the module_index
	 */
	public int getModule_index() {
		return Module_index;
	}

	/**
	 * @param module_index the module_index to set
	 */
	public void setModule_index(int module_index) {
		Module_index = module_index;
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

	public Register() 
	{
	}
	
	public Register(Register duplicate)
	{
		this.Register_Name = new String(duplicate.Register_Name);
        this.Module_index = duplicate.Module_index;
        this.Module_Name = new String(duplicate.Module_Name);
        this.Address_offset = duplicate.Address_offset;
        this.Register_Size_in_bytes = duplicate.Register_Size_in_bytes;
        this.Power_On_Reset_Value = duplicate.Power_On_Reset_Value;
        this.Bits = new RegisterBitsList(); 
        this.Bits.addAll(duplicate.Bits);
        this.Category = duplicate.Category;
        this.Permission = duplicate.Permission;
        if (duplicate.Register_Description != null)
        {
        	this.Register_Description = new String(duplicate.Register_Description);
        }
        if (duplicate.Code != null)
        {
            this.Code = new String(duplicate.Code) ;
        }
        if (duplicate.Struct_Definition_Name != null)
        {
            this.Struct_Definition_Name = new String(duplicate.Struct_Definition_Name);
        }
	}
/*
	public ArrayList<String> getBits_Name_List() {
		if(this.Bits !=null)
		{
			ArrayList<String> Bits_Name_List = new ArrayList<String>();
			
			for (Register_Bit current_bit : this.Bits) {
				Bits_Name_List.add(current_bit.getBit_Name());
			}
			
			return Bits_Name_List;
		}
		else
		{
			return null;
		}
	}*/

	public String[] Get_Bits_Name_List() {
		if(this.Bits !=null)
		{
			String[] Bits_Name_List = new String [this.Bits.size()];
			
			for (int bit_index = 0; bit_index < this.Bits.size(); bit_index++) {
				RegisterBit current_bit = this.Bits.get(bit_index);
				Bits_Name_List[bit_index] = new String(current_bit.getBit_Name());  
			}
			
			return Bits_Name_List;
		}
		else
		{
			return null;
		}
	}

}
