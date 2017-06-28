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
*///import java.util.Locale;


public enum RegisterCategory 
{
	None(0),
	Configuration(1),
    Status(2);
	
	private int category_value;
	
	
	private RegisterCategory(int category_value) 
	{
		this.setCategory_value(category_value);
	}


	/**
	 * @return the category_value
	 */
	public int getCategory_value() 
	{
		return category_value;
	}


	/**
	 * @param category_value the category_value to set
	 */
	public void setCategory_value(int category_value) 
	{
		this.category_value = category_value;
	}
	
	
	public static RegisterCategory Get_Category_by_String(String Category_String)
	{
		if(Category_String.equalsIgnoreCase("configuration"))
        {
            return RegisterCategory.Configuration;
        }
        else if (Category_String.equalsIgnoreCase("status"))
        {
            return RegisterCategory.Status;
        }
        else
        {
            return RegisterCategory.None;
        }
	}
	
	
	public String Get_String_by_Category()
	{
		String return_string = null;
		switch (this.getCategory_value()) 
		{
			case 1:
				return_string = new String("Configuration");
				break;
			case 2:
				return_string = new String("Status");
				break;
			default:
				break;
		}
		return return_string;
	}
	
	
}
