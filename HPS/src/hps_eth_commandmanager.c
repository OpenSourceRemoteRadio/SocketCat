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
*//**
 * @file hepta_a10_2x4_commandmanager.c
 * @author BigCat Wireless
 * @ingroup hepta_a10_2x4_commandmanager
 * @brief Application file for CommandManager module
 * */

/******************************************************************************
 * Include public/global Header files
******************************************************************************/

#include <stdio.h>		
#define _GNU_SOURCE 
#define __USE_GNU	 
#include <stdlib.h>      
#include <string.h>      
#include <unistd.h>      
#include <sys/types.h>   
#include <sys/socket.h>  
#include <sys/select.h>  
#include <netinet/in.h>  
#include <arpa/inet.h>   
#include <netdb.h>       
#include <signal.h>      
#include <unistd.h>      
#include <stdio.h>       
#include <stdlib.h>      
#include <string.h>      
#include <unistd.h>      
#include <sys/types.h>   
#include <sys/socket.h>  
#include <netinet/in.h>  
#include <arpa/inet.h>   
#include <sys/mman.h>    
#include <netdb.h>       
#include <sys/stat.h>    
#include <fcntl.h>       
#include <pthread.h>     
#include <sys/ioctl.h>   
#include <stdbool.h>

/******************************************************************************
 * Include private Header files
******************************************************************************/

#include "hps_eth_commandmanager.h"
#include "hps_eth_protocol.h"
#include "hps_eth_firmware.h"
#include "hps_eth_command_format.h"
/*
#include "hepta_a10_2x4_sysid.h"
#include "hepta_a10_2x4_cpri.h"
#include "hepta_a10_2x4_spi.h"
#include "hepta_a10_2x4_capctrl.h"
#include "hepta_a10_2x4_jesdtx.h"
#include "hepta_a10_2x4_jesdrx.h"
#include "hepta_a10_2x4_downlink.h"
#include "hepta_a10_2x4_regmap_dfd.h"
#include "hepta_a10_2x4_uplink.h"
#include "hepta_a10_2x4_dpd_reg_map_dfd.h"
#include "hepta_a10_2x4_dpd_reg_map.h"
#include "hepta_a10_2x4_goertzel_capture.h"
#include "hepta_a10_2x4_goertzel_spectrum.h"
#include "hepta_a10_2x4_ilc.h"
#include "hepta_a10_2x4_timer.h"
#include "hepta_a10_2x4_hepta_dpd_xcorr.h"
#include "hepta_a10_2x4_ptx.h"
#include "hepta_a10_2x4_pobsrx.h"
#include "hepta_a10_2x4_ctx_write.h"
#include "hepta_a10_2x4_cobsrx_write.h"
#include "hepta_a10_2x4_tse_transmit.h"
#include "hepta_a10_2x4_tse_capture.h"
#include "hepta_a10_2x4_ethernet.h"
#include "hepta_a10_2x4_mm2st.h"
#include "hepta_a10_2x4_avst_to_avmm.h"
#include "hepta_a10_2x4_msgdma.h"
*/

#define TOTAL_COMMANDS 4
#define TOTAL_MODULES 26


/*command_response hepta_a10_2x4_command_response[TOTAL_COMMANDS][TOTAL_MODULES]={ \
        {        Hepta_A10_2x4_sysid_reg_write        ,        Hepta_A10_2x4_cpri_reg_write        ,        Hepta_A10_2x4_spi_reg_write        ,        Hepta_A10_2x4_capctrl_reg_write        ,        Hepta_A10_2x4_jesdtx_reg_write        ,        Hepta_A10_2x4_jesdrx_reg_write        ,        Hepta_A10_2x4_downlink_reg_write        ,        Hepta_A10_2x4_regmap_dfd_reg_write        ,        Hepta_A10_2x4_uplink_reg_write        ,        Hepta_A10_2x4_dpd_reg_map_dfd_reg_write        ,        Hepta_A10_2x4_dpd_reg_map_reg_write        ,            Hepta_A10_2x4_ilc_reg_write        ,        Hepta_A10_2x4_timer_reg_write        ,        Hepta_A10_2x4_hepta_dpd_xcorr_reg_write        ,        Hepta_A10_2x4_ptx_reg_write        ,        Hepta_A10_2x4_pobsrx_reg_write        ,        Hepta_A10_2x4_ctx_write_reg_write        ,        Hepta_A10_2x4_cobsrx_write_reg_write        ,        Hepta_A10_2x4_tse_transmit_reg_write        ,        Hepta_A10_2x4_tse_capture_reg_write        ,        Hepta_A10_2x4_ethernet_reg_write        ,        Hepta_A10_2x4_mm2st_reg_write        ,        Hepta_A10_2x4_avst_to_avmm_reg_write        ,        Hepta_A10_2x4_msgdma_reg_write  ,        hepta_a10_2x4_goertzel_capture_reg_write        ,        hepta_a10_2x4_goertzel_spectrum_reg_write       }, \
        {        Hepta_A10_2x4_sysid_reg_read        ,        Hepta_A10_2x4_cpri_reg_read        ,        Hepta_A10_2x4_spi_reg_read        ,        Hepta_A10_2x4_capctrl_reg_read        ,        Hepta_A10_2x4_jesdtx_reg_read        ,        Hepta_A10_2x4_jesdrx_reg_read        ,        Hepta_A10_2x4_downlink_reg_read        ,        Hepta_A10_2x4_regmap_dfd_reg_read        ,        Hepta_A10_2x4_uplink_reg_read        ,        Hepta_A10_2x4_dpd_reg_map_dfd_reg_read        ,        Hepta_A10_2x4_dpd_reg_map_reg_read        ,               Hepta_A10_2x4_ilc_reg_read        ,        Hepta_A10_2x4_timer_reg_read        ,        Hepta_A10_2x4_hepta_dpd_xcorr_reg_read        ,        Hepta_A10_2x4_ptx_reg_read        ,        Hepta_A10_2x4_pobsrx_reg_read        ,        Hepta_A10_2x4_ctx_write_reg_read        ,        Hepta_A10_2x4_cobsrx_write_reg_read        ,        Hepta_A10_2x4_tse_transmit_reg_read        ,        Hepta_A10_2x4_tse_capture_reg_read        ,        Hepta_A10_2x4_ethernet_reg_read        ,        Hepta_A10_2x4_mm2st_reg_read        ,        Hepta_A10_2x4_avst_to_avmm_reg_read        ,        Hepta_A10_2x4_msgdma_reg_read        ,        hepta_a10_2x4_goertzel_capture_reg_read        ,        hepta_a10_2x4_goertzel_spectrum_reg_read  }, \
        {        Hepta_A10_2x4_sysid_config        ,        Hepta_A10_2x4_cpri_config        ,        Hepta_A10_2x4_spi_config        ,        Hepta_A10_2x4_capctrl_config        ,        Hepta_A10_2x4_jesdtx_config        ,        Hepta_A10_2x4_jesdrx_config        ,        Hepta_A10_2x4_downlink_config        ,        Hepta_A10_2x4_regmap_dfd_config        ,        Hepta_A10_2x4_uplink_config        ,        Hepta_A10_2x4_dpd_reg_map_dfd_config        ,        Hepta_A10_2x4_dpd_reg_map_config        ,               Hepta_A10_2x4_ilc_config        ,        Hepta_A10_2x4_timer_config        ,        Hepta_A10_2x4_hepta_dpd_xcorr_config        ,        Hepta_A10_2x4_ptx_config        ,        Hepta_A10_2x4_pobsrx_config        ,        Hepta_A10_2x4_ctx_write_config        ,        Hepta_A10_2x4_cobsrx_write_config        ,        Hepta_A10_2x4_tse_transmit_config        ,        Hepta_A10_2x4_tse_capture_config        ,        Hepta_A10_2x4_ethernet_config        ,        Hepta_A10_2x4_mm2st_config        ,        Hepta_A10_2x4_avst_to_avmm_config        ,        Hepta_A10_2x4_msgdma_config      ,        	hepta_a10_2x4_goertzel_capture_config        ,        hepta_a10_2x4_goertzel_spectrum_config    }, \
        {        Hepta_A10_2x4_sysid_service        ,        Hepta_A10_2x4_cpri_service        ,        Hepta_A10_2x4_spi_service        ,        Hepta_A10_2x4_capctrl_service        ,        Hepta_A10_2x4_jesdtx_service        ,        Hepta_A10_2x4_jesdrx_service        ,        Hepta_A10_2x4_downlink_service        ,        Hepta_A10_2x4_regmap_dfd_service        ,        Hepta_A10_2x4_uplink_service        ,        Hepta_A10_2x4_dpd_reg_map_dfd_service        ,        Hepta_A10_2x4_dpd_reg_map_service        ,          Hepta_A10_2x4_ilc_service        ,        Hepta_A10_2x4_timer_service        ,        Hepta_A10_2x4_hepta_dpd_xcorr_service        ,        Hepta_A10_2x4_ptx_service        ,        Hepta_A10_2x4_pobsrx_service        ,        Hepta_A10_2x4_ctx_write_service        ,        Hepta_A10_2x4_cobsrx_write_service        ,        Hepta_A10_2x4_tse_transmit_service        ,        Hepta_A10_2x4_tse_capture_service        ,        Hepta_A10_2x4_ethernet_service        ,        Hepta_A10_2x4_mm2st_service        ,        Hepta_A10_2x4_avst_to_avmm_service        ,        Hepta_A10_2x4_msgdma_service    ,        hepta_a10_2x4_goertzel_capture_service        ,        hepta_a10_2x4_goertzel_spectrum_service        }, \
        }; */
 command_response hepta_a10_2x4_command_response[TOTAL_COMMANDS][TOTAL_MODULES]={};
		
/******************************************************************************
 * Custom Function definition
******************************************************************************/

/**
 * @ingroup CommandManager
 * @brief Decodes the packets and calls respective modules API from Command LUT
 * @param pCmdHeader Command packet
 * @param packet_id Command packet id
 * @retval 0 If command received is correct and executed successfully
 * @retval -1 If command received is incorrect
 * */


int hepta_a10_2x4_command_decoder(pCommand_Header pCmdHeader,unsigned int packet_id)
{
    command_response current_command = NULL;

#ifdef SERVER_CONNECTION_TEST
    printf("OperationType %x \n",pCmdHeader->Operation_Type);
	printf("TransferType %x \n",pCmdHeader->Transfer_Type);
	printf("Module_Index %x \n",pCmdHeader->Module_Index);
	printf("Instance_Num %x \n",pCmdHeader->Instance);
	printf("Resource index %x \n",pCmdHeader->Resource);
	printf("Address %x \n",pCmdHeader->Address);
	printf("Bit Mask %x \n",pCmdHeader->Bit_Mask);
	printf("Length %x \n",pCmdHeader->Length);
	printf("Server index %x \n",pCmdHeader->server_index);
	return 0;
#else
    if((pCmdHeader->Operation_Type < TOTAL_COMMANDS) && (pCmdHeader->Module_Index < TOTAL_MODULES))
    {
        current_command = (command_response)hepta_a10_2x4_command_response[pCmdHeader->Operation_Type][pCmdHeader->Module_Index];
    }
    if(current_command != NULL)
    {
        current_command(pCmdHeader,packet_id);
        return 0;
    }
    else
    {
        return -1;
    }
#endif
}

