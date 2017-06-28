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
public enum RegisterPermission 
{
	None(0),
    Read_Only(1),
    Write_Only(2),
    Read_Write(3);
	
	private int permission_value;
	
	private RegisterPermission(int permission_value) 
	{
		this.setPermission_value(permission_value);
	}

	/**
	 * @return the permission_value
	 */
	public int getPermission_value() {
		return permission_value;
	}

	/**
	 * @param permission_value the permission_value to set
	 */
	public void setPermission_value(int permission_value) {
		this.permission_value = permission_value;
	}
	
	public static RegisterPermission Get_Permission_by_String(String Permission_String)
	{
		if(Permission_String.equalsIgnoreCase("r/w"))
        {
            return RegisterPermission.Read_Write;
        }
        else if (Permission_String.equalsIgnoreCase("ro"))
        {
        	return RegisterPermission.Read_Only;
        }
        else if (Permission_String.equalsIgnoreCase("wo"))
        {
        	return RegisterPermission.Write_Only;
        }
        else
        {
        	return RegisterPermission.None;
        }
	}
	
	
	public String Get_String_by_Permission()
	{
		String return_string = null;
		switch (this.getPermission_value()) 
		{
			case 1:
				return_string = new String("ro");
				break;
			case 2:
				return_string = new String("wo");
				break;
			case 3:
				return_string = new String("r/w");
				break;
			default:
				break;
		}
		return return_string;
	}
}
