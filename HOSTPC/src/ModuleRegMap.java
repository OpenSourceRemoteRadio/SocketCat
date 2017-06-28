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
import java.util.ArrayList;
//import java.util.List;


public class ModuleRegMap {

	/**
	 * 
	 */
	
	private int Module_Index;
    private String Module_Name;
    private ArrayList<String> Instance_Name_List =  new ArrayList<String>();
    private ArrayList<String> Configurations_List =  new ArrayList<String>();
    private ArrayList<String> Services_List =  new ArrayList<String>();
    private ArrayList<ModuleResource> Resources = new ArrayList<ModuleResource>();
	
	
	public int getModule_Index() {
		return Module_Index;
	}

	public void setModule_Index(int module_Index) {
		Module_Index = module_Index;
	}

	public String getModule_Name() {
		return Module_Name;
	}

	public void setModule_Name(String module_Name) {
		Module_Name = module_Name;
	}

	/**
	 * @return the configurations_List
	 */
	public ArrayList<String> getConfigurations_List() {
		return Configurations_List;
	}

	/**
	 * @param configurations_List the configurations_List to set
	 */
	public void setConfigurations_List(ArrayList<String> configurations_List) {
		Configurations_List = configurations_List;
	}

	/**
	 * @return the services_List
	 */
	public ArrayList<String> getServices_List() {
		return Services_List;
	}

	/**
	 * @param services_List the services_List to set
	 */
	public void setServices_List(ArrayList<String> services_List) {
		Services_List = services_List;
	}

	public ModuleRegMap() 
	{
		
	}
	
	public ModuleRegMap(int module_index,String module_name)
    {
        this.Module_Index = module_index;
        this.Module_Name = module_name;
    }
	
	public static ModuleRegMap Get_ModuleRegmap_by_Module_Index(ArrayList<ModuleRegMap> Module_Regmap_List,int module_index) 
	{
		ModuleRegMap Module_Regmap_to_return = null;
        
		for (ModuleRegMap module : Module_Regmap_List) 
		{
			if (module_index == module.getModule_Index())
            {
                Module_Regmap_to_return = module;
                break;
            }
		}
		
        return Module_Regmap_to_return;
	}

	/**
	 * @return the instance_Name_List
	 */
	public ArrayList<String> getInstance_Name_List() {
		return Instance_Name_List;
	}

	/**
	 * @param instance_Name_List the instance_Name_List to set
	 */
	public void setInstance_Name_List(ArrayList<String> instance_Name_List) {
		Instance_Name_List = instance_Name_List;
	}

	/**
	 * @return the resources
	 */
	public ArrayList<ModuleResource> getResources() {
		return Resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(ArrayList<ModuleResource> resources) {
		Resources = resources;
	}

/*	public ArrayList<String> getResource_Name_List() {
		if(this.Resources !=null)
		{
			ArrayList<String> Resources_Name_List = new ArrayList<String>();
			
			for (ModuleResource current_resource : this.Resources) {
				Resources_Name_List.add(current_resource.getResource_Name());
			}
			
			return Resources_Name_List;
		}
		else
		{
			return null;
		}
	}*/

	public String[] Get_Instances_Name_List() {
		if(this.Instance_Name_List!=null)
		{
			String[] Instances_Name_List = new String [this.Instance_Name_List.size()];
			
			for (int instance_index = 0; instance_index < this.Instance_Name_List.size(); instance_index++) {
				Instances_Name_List[instance_index] = new String(this.Instance_Name_List.get(instance_index));
			}
			
			return Instances_Name_List;
		}
        /*else
		{*/
			return null;
		/*}*/
	}

	public String[] Get_Resources_Name_List() {
		if(this.Resources !=null)
		{
			String[] Resources_Name_List = new String [this.Resources.size()];
			
			for (int resource_index = 0; resource_index < this.Resources.size(); resource_index++) {
				ModuleResource current_resource = this.Resources.get(resource_index);
				Resources_Name_List[resource_index] = new String(current_resource.getResource_Name());  
			}
			
			return Resources_Name_List;
		}
		/*else
		{*/
			return null;
		/*}*/
	}



}
