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
//import java.util.List;


public class ModuleResource {

	/**
	 * 
	 */
	
	private int Resource_Index;
	private String Resource_Name;
	//private int Module_index;
	//private String Module_Name;
    private RegistersList Register_Offsets = new RegistersList();  
	
	public ModuleResource(String Resource_Name, int Resource_Index, String Module_Name, int Module_index) 
	{
	    this.setResource_Index(Resource_Index);
        this.setResource_Name(new String(Resource_Name));
        //this.Module_index = Module_index;
        //this.Module_Name = new String(Module_Name);
	}

	/**
	 * @return the register_Offsets
	 */
	public RegistersList getRegister_Offsets() {
		return Register_Offsets;
	}

	/**
	 * @param register_Offsets the register_Offsets to set
	 */
	public void setRegister_Offsets(RegistersList register_Offsets) {
		Register_Offsets = register_Offsets;
	}

	/**
	 * @return the resource_Name
	 */
	public String getResource_Name() {
		return Resource_Name;
	}

	/**
	 * @param resource_Name the resource_Name to set
	 */
	public void setResource_Name(String resource_Name) {
		Resource_Name = resource_Name;
	}

	/**
	 * @return the resource_Index
	 */
	public int getResource_Index() {
		return Resource_Index;
	}

	/**
	 * @param resource_Index the resource_Index to set
	 */
	public void setResource_Index(int resource_Index) {
		Resource_Index = resource_Index;
	}

/*	public ArrayList<String> getRegister_Name_List() {
		if(this.Register_Offsets !=null)
		{
			ArrayList<String> Registers_Name_List = new ArrayList<String>();
			
			for (Register current_register : this.Register_Offsets) {
				Registers_Name_List.add(current_register.getRegister_Name());
			}
			return Registers_Name_List;
		}
		
		else
		{
			return null;
		}
	}*/

	public String[] Get_Registers_Name_List() {
		if(this.Register_Offsets !=null)
		{
			String[] Register_Name_List = new String [this.Register_Offsets.size()];
			
			for (int register_index = 0; register_index < this.Register_Offsets.size(); register_index++) {
				Register current_module = this.Register_Offsets.get(register_index);
				Register_Name_List[register_index] = new String(current_module.getRegister_Name());  
			}
			return Register_Name_List;
		}
		return null;
	}

}
