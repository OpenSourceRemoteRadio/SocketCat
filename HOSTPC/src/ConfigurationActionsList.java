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
import java.util.Collection;


public class ConfigurationActionsList extends ArrayList<ConfigurationAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 681853962385384090L;

	/**
	 * 
	 */
	public ConfigurationActionsList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param initialCapacity
	 */
	public ConfigurationActionsList(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param c
	 */
	public ConfigurationActionsList(
			Collection<? extends ConfigurationAction> c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	
	public ConfigurationAction Get_Configuration_Action_by_ActionString(String Action_Sring)
    {
		ConfigurationAction Configuration_Action_to_return = null;
        for (ConfigurationAction current_config_action : this) 
        {
        	if (current_config_action.getConfiguration_result().equalsIgnoreCase(Action_Sring))
            {
        		Configuration_Action_to_return = current_config_action;
                break;
            }
		}
        return Configuration_Action_to_return;
    }
	
	
	public ConfigurationAction Get_Configuration_Action_by_ActionValue(Integer Action_Value)
    {
		ConfigurationAction Configuration_Action_to_return = null;
        for (ConfigurationAction current_config_action : this) 
        {
        	if (current_config_action.getConfiguration_value() == Action_Value)
            {
        		Configuration_Action_to_return = current_config_action;
                break;
            }
		}
        return Configuration_Action_to_return;
    }

}
