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

/**
 * @file hepta_a10_2x4_tcp_client.c
 * @brief TCP Client APIs are defined in this file
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
#include <netinet/tcp.h>

/******************************************************************************
 * Include private Header files
******************************************************************************/

#include "hps_eth_firmware.h"
#include "hps_eth_protocol.h"
#include "hps_eth_command_format.h"
char tcp_pkt_headr[3]="alt";
//extern socket_manager_header hepta_a10_2x4_socket_manager;

/**
 *
 * @ingroup SocketManager
 * @brief Prints the command attributes.
 * @param pCmdHeader Pointer to the Command header.
 */

int DebugLevel=DEBUG_LEVEL_ERROR;
 void debug_dump_commandheader(pCommand_Header pCmdHeader){

	DEBUG_PRINT(DEBUG_LEVEL_INFO,("OperationType %x \n",pCmdHeader->Operation_Type));
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("TransferType %x \n",pCmdHeader->Transfer_Type));
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("Module_Index %x \n",pCmdHeader->Module_Index));
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("Instance_Num %x \n",pCmdHeader->Instance));
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("Address %x \n",pCmdHeader->Address));
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("Bit Mask %x \n",pCmdHeader->Bit_Mask));
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("Length %x \n",pCmdHeader->Length));
}

/**
 *
 * @ingroup SocketManager
 * @brief Binds the client with the server through specified port number.
 * @param pSocket_Manager Pointer to the Socket Manager.
 * @param port_no  it is char pointer to a Port number to connect with server.
 * @return Returns 0 if connection is bind successful.
 * @return Returns -1 if connection is bind not successful.
 *
 */
//int tcp_create_client(char* port_no,psocket_manager_header pSocket_Manager,char *server_ip_address)
void *tcp_create_client(psocket_manager_header pSocket_Manager)
{
	int ret;
	struct addrinfo hints, *servinfo, *addrinfo_ptr;
	//memset(&hints, 0, sizeof hints);
	hints.ai_flags = 0;			/* Input flags.  */
	hints.ai_family = 0;		/* Protocol family for socket.  */
	hints.ai_socktype = 0;		/* Socket type.  */
	hints.ai_protocol = 0;		/* Protocol for socket.  */
	hints.ai_addrlen = 0;		/* Length of socket address.  */
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;
	int server_index = 0;
	//int return_status = 0;
	socket_handles *acquired_server_details;
	struct timeval timeout;
	int optval;
	int optlen;

	timeout.tv_sec =  SEND_TIME_OUT_SEC;
	timeout.tv_usec = 0;
	//int tcp_timeout ;

	//tcp_timeout = CONNECTION_TIME_OUT_SEC * 1000;

	pthread_mutex_lock(&pSocket_Manager->lock);

	for (server_index = 0; server_index < pSocket_Manager->number_of_server; ++server_index) {
		if(pSocket_Manager->ServerClientDetails[server_index].connecting==false)
		{
			pSocket_Manager->ServerClientDetails[server_index].connecting = true;
			break;
		}
	}

	pthread_mutex_unlock(&pSocket_Manager->lock);
	acquired_server_details = (pSocket_Manager->ServerClientDetails) + server_index;

	printf("Server acquired for creating %d IP %s Port %s\n",server_index,acquired_server_details->pserver_ip_address,acquired_server_details->port_no);

	while(pSocket_Manager->is_thread)
	{
		printf("Thread Run in server %d\n",pSocket_Manager->is_thread );
		//pthread_mutex_lock(&pSocket_Manager->lock);
		acquired_server_details->connecting = true;
		acquired_server_details->connected = false;
		acquired_server_details->reading = false;
		//pthread_mutex_unlock(&pSocket_Manager->lock);

		if ((ret = getaddrinfo(acquired_server_details->pserver_ip_address, acquired_server_details->port_no, &hints, &servinfo)) != 0)
		{
			fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(ret));
			continue;
		}

		for (addrinfo_ptr=servinfo; addrinfo_ptr != NULL; addrinfo_ptr = addrinfo_ptr->ai_next)
		{
			if((acquired_server_details->connectedfd = socket(addrinfo_ptr->ai_family, addrinfo_ptr->ai_socktype, addrinfo_ptr->ai_protocol)) < 0)
			{
				perror("server: socket");
				continue;
			}
			break;
		}

		printf("Server waiting %d IP %s Port %s\n",server_index,acquired_server_details->pserver_ip_address,acquired_server_details->port_no);

		ret = connect(acquired_server_details->connectedfd, addrinfo_ptr->ai_addr, addrinfo_ptr->ai_addrlen) ;
		if(ret < 0)
		{
			close(acquired_server_details->connectedfd);
			perror("server: connection is failed \n");
			continue;
		}
		else
		{
			acquired_server_details->addrinfo_ptr = addrinfo_ptr;
			printf("Server connected %d IP %s Port %s\n",server_index,acquired_server_details->pserver_ip_address,acquired_server_details->port_no);
			pthread_mutex_lock(&pSocket_Manager->lock);
			acquired_server_details->connecting = false;
			acquired_server_details->connected = true;
			pthread_mutex_unlock(&pSocket_Manager->lock);


			timeout.tv_sec =  RECV_TIME_OUT_SEC;
			timeout.tv_usec = 0;
			if (setsockopt (acquired_server_details->connectedfd, SOL_SOCKET, SO_RCVTIMEO, (char *)&timeout,
					sizeof(timeout)) < 0)
			{
				printf("setsockopt SO_RCVTIMEO failed\n");
			}

			/*timeout.tv_sec =  SEND_TIME_OUT_SEC;
			timeout.tv_usec = 0;
			if (setsockopt (acquired_server_details->connectedfd, SOL_SOCKET, SO_SNDTIMEO, (char *)&timeout,
						sizeof(timeout)) < 0)
			{
				printf("setsockopt SO_SNDTIMEO failed\n");
			}*/

			/*The TCP user timeout controls how long transmitted data may remain unacknowledged before a connection is forcefully closed*/
			//setsockopt (acquired_server_details->connectedfd, SOL_TCP, TCP_USER_TIMEOUT, (char*) &tcp_timeout, sizeof (tcp_timeout));


			optval = TCP_KEEPALIVE_ENABLE;
			optlen = sizeof(optlen);
			if(setsockopt(acquired_server_details->connectedfd, SOL_SOCKET, SO_KEEPALIVE, &optval, optlen) < 0) {
			   printf("setsockopt SO_KEEPALIVE failed\n");
			}

			optval = TCP_KEEPCNT_VALUE;

			if(setsockopt(acquired_server_details->connectedfd, SOL_TCP, TCP_KEEPCNT, &optval, optlen) < 0) {
			   printf("setsockopt TCP_KEEPCNT failed\n");
			}

			optval = TCP_KEEPIDLE_VALUE;

			if(setsockopt(acquired_server_details->connectedfd, SOL_TCP, TCP_KEEPIDLE, &optval, optlen) < 0) {
			   printf("setsockopt TCP_KEEPIDLE failed\n");
			}

			optval = TCP_KEEPINTVL_VALUE;

			if(setsockopt(acquired_server_details->connectedfd, SOL_TCP, TCP_KEEPINTVL, &optval, optlen) < 0) {
			   printf("setsockopt TCP_KEEPINTVL failed\n");
			}

			//return_status = pthread_create((pSocket_Manager->pTransportReadThreadHandle)+ server_index, NULL, transport_read_thread, pSocket_Manager);
			pthread_join(pSocket_Manager->pTransportReadThreadHandle[server_index],NULL);
			tcpClient_socket_close(acquired_server_details);
			//return NULL;
		}
	}

	return NULL;
}

/**
 * @ingroup SocketManager
 * @brief Reads the data from ethernet buffer and validates the header.
 * @param pConnectionDetails Pointer to the socket_handles structure.
 * @return Returns pointer to the packet header if data is read successfuly or NULL if error in reading from socket.
 */

pPacket_Header tcp_read(psocket_manager_header pSocketManager,socket_handles *pConnectionDetails)
{
	int return_int = 0;
	//unsigned int read_len = 0;
	char *pdata = NULL;
	unsigned int j = 0;

	pPacket_Header pTcpPacket=0;
	pPacket_Header tmp=0;

	allocate:
	pTcpPacket=malloc(TCP_PACKET_HEADER_SIZE);

	receive:
	//return_int = recv(pConnectionDetails->connectedfd,pTcpPacket,TCP_PACKET_HEADER_SIZE,MSG_WAITALL);
	return_int = recvfrom(pConnectionDetails->connectedfd,pTcpPacket,TCP_PACKET_HEADER_SIZE,MSG_WAITALL,\
			pConnectionDetails->addrinfo_ptr->ai_addr,&(pConnectionDetails->addrinfo_ptr->ai_addrlen));

	if(return_int == -1)
	{
		if(pSocketManager->is_thread == true)
		{
			goto receive ;
		}
		else
		{
			free(pTcpPacket);
			printf("Socket read to be closed\n");
			return NULL;
		}
	}

	if(return_int == 0)
	{
		free(pTcpPacket);
		printf("Socket closed at other end\nReconnecting...!");
		return NULL;
	}

	if(return_int < TCP_PACKET_HEADER_SIZE)
	{
		DEBUG_PRINT(DEBUG_LEVEL_ERROR,("Invalid TCP packet read. Reading again!\n"));
		//free(pTcpPacket);
		goto receive ;
		//return NULL;
	}

	if(strncmp((char *)pTcpPacket->Header_String,(char *)TCP_PACKET_HEADER_STRING,TCP_PACKET_HEADER_STRING_SIZE))
	{
		DEBUG_PRINT(DEBUG_LEVEL_ERROR,("Invalid header. Reading again!\n"));
		goto receive ;
		//return pTcpPacket;
	}

	//printf("Received packet Id %d Length %d\n",pTcpPacket->Id,pTcpPacket->Length);
	if(pTcpPacket->Id==0 && pTcpPacket->Length == 0)
	{
		//printf("Received alive packet\n");
		goto receive ;
	}

	if(pTcpPacket->Length > 0)
	{
		tmp=malloc(TCP_PACKET_HEADER_SIZE+pTcpPacket->Length);
		//memcpy(tmp,pTcpPacket,TCP_PACKET_HEADER_SIZE);
		for(j=0;j<TCP_PACKET_HEADER_SIZE;j++)
				    	{
					      tmp[j]=pTcpPacket[j];
				    	}
		j = 0;
		free(pTcpPacket);
		pTcpPacket=tmp;
		pdata = pTcpPacket->pData;
	}

	return_int=0;

	return_int = recv(pConnectionDetails->connectedfd,pdata,pTcpPacket->Length,MSG_WAITALL);

	if(return_int>=pTcpPacket->Length)
	{
		DEBUG_PRINT(DEBUG_LEVEL_INFO,("%s %d received packet more than expected\n",__FUNCTION__,__LINE__));
		return pTcpPacket;
	}
	else
	{
		DEBUG_PRINT(DEBUG_LEVEL_ERROR,("%s %d received packet less than expected\n",__FUNCTION__,__LINE__));
		free(pTcpPacket);
		goto allocate;
	}

	return pTcpPacket;
}

/**
 * @ingroup SocketManager
 * @brief Writes the data from buffer to the socket.
 * @param pConnectionDetails Pointer to the socket_handles structure.
 * @param pTcpPacket Pointer to the packet header that contains the buffer to be transferred.
 */

void tcp_write(socket_handles *pConnectionDetails,pPacket_Header pTcpPacket)
{
	unsigned int written_len;
	unsigned int length;
	//char *ptr=(char *)pTcpPacket;


	length=TCP_PACKET_HEADER_SIZE+pTcpPacket->Length;
	written_len = write(pConnectionDetails->connectedfd,pTcpPacket,TCP_PACKET_HEADER_SIZE+(pTcpPacket->Length));

	if(written_len<length)
	{
		while((length-written_len)!=0)
		{
			//ptr=((char *)pTcpPacket)+written_len;
			length=length-written_len;
			written_len = write(pConnectionDetails->connectedfd,pTcpPacket,length);
		}
	}
	return;
}

/**
 * @ingroup SocketManager
 * @brief This function will check the whether received packet has valid transport header
 * @param pTcpPacket Pointer to the  Packet Header structure.
 * @return true for success and false for failure
 */
bool isvalid_transportpacket(pPacket_Header pTcpPacket)
{
	bool status=false;
	if(pTcpPacket!=NULL)
	{
		if(!strncmp(pTcpPacket->Header_String,TCP_PACKET_HEADER_STRING,TCP_PACKET_HEADER_STRING_SIZE))
		{
			status=true;  //valid packet
		}
	}
	return status;
}


/**
 * @ingroup SocketManager
 * @brief Close the socket connection.
 * @param pSocket_Manager Pointer to the Socket Manager header.
 */

void tcpClient_socket_close(void * handles_ptr)
{
	socket_handles * pSocket_handles_ptr = (socket_handles *)handles_ptr;
	int return_int = close(pSocket_handles_ptr->connectedfd);
	printf("Close return %d\n",return_int);
	pSocket_handles_ptr->connected = false;
	pSocket_handles_ptr->connecting = false;
	pSocket_handles_ptr->reading = false;
	return;
}




/**
 * @ingroup SocketManager
 * @brief This function will append the queue
 * @param pcommand_queue_head Pointer to the  command_queue_head structure and pointer to the data.
 */

bool command_queue_append(pcommand_queue_head pHead,void *data, int id)
{
	pcommand_queue Queue=malloc(sizeof(command_queue));
	pcommand_queue ptr;
	if(Queue==NULL)
		return false;
	Queue->Next=NULL;
	Queue->pQueueData=data;
	Queue->id=id;
	pthread_mutex_lock(&pHead->queue_lock);
	if(pHead->Head==NULL)
	{
		pHead->Head=(struct command_queue *)Queue;
	}
	else
	{
		ptr=(pcommand_queue)pHead->Head;
		while(ptr->Next)
			ptr=(pcommand_queue)ptr->Next;
		ptr->Next=(struct command_queue *)Queue;
	}
	/*release condition sleep whoever waiting for this queue*/
	pHead->num_of_nodes++;

	//to maintain maximum queue entry count
	if(pHead->num_of_nodes >= MAX_QUEUE_NODES)
	{
		//printf("Number of nodes in %s exceeds limit %d\n",pHead->queue_name,pHead->num_of_nodes);
		ptr = (pcommand_queue)pHead->Head;
		if(ptr != NULL)
		{
			pHead->Head = ptr->Next;
			free(ptr->pQueueData);
			free(ptr);
			pHead->num_of_nodes--;
		}
	}

	//printf("Number of nodes in %s queue %d\n",pHead->queue_name,pHead->num_of_nodes);

	pthread_mutex_unlock(&pHead->queue_lock);
	return true;
}

/**
 * @ingroup SocketManager
 * @brief This function will dequeue the first data from the queue
 * @param pcommand_queue_head Pointer to the  command_queue_head structure.
 */

void* command_queue_dequeue(pcommand_queue_head pHead)
{
	pcommand_queue Queue=NULL;
	void * ptr=NULL;

	pthread_mutex_lock(&pHead->queue_lock);
	if(pHead->Head==NULL)
	{
		pthread_mutex_unlock(&pHead->queue_lock);
		return NULL;
	}
	if(pHead->Head!=NULL)
	{
		Queue=(pcommand_queue)pHead->Head;
		pHead->Head = (struct command_queue *)((pcommand_queue)pHead->Head)->Next;
	}
	pHead->num_of_nodes--;
	pthread_mutex_unlock(&pHead->queue_lock);
	ptr=Queue->pQueueData;
	free(Queue);
	return ptr;
}

/**
 * @ingroup SocketManager
 * @brief This function initializes the command_queue_head.
 * @param queue Pointer to the  command_queue_head structure.
 */

bool init_function_queue(pcommand_queue_head queue,char * queue_name)
{
	//static int i=1;
	pthread_mutex_init(&queue->queue_lock,NULL);
	queue->Head=NULL;
	queue->queue_name=strdup(queue_name);
	return true;
}


/**
 * @ingroup SocketManager
 * @brief This function will send acknowldegement for packet in transport layer
 * @param pInpacket Pointer to the packet containing header and data.
 */

pPacket_Header alloc_transport_ackpacket(pPacket_Header pInpacket)
{
	unsigned int i = 0;
	pPacket_Header ptr=malloc(sizeof(Packet_Header));
	//memcpy(ptr,pInpacket,sizeof(Packet_Header));
	for(i=0;i<sizeof(Packet_Header);i++)
				    	{
			ptr[i]=pInpacket[i];
				    	}
	i = 0;
	//strncpy((char*)ptr->Header_String,(char*)pInpacket->Header_String,TCP_PACKET_HEADER_STRING_SIZE);
	//ptr->Id=pInpacket->Id;
	ptr->Length=0;
	return ptr;
}

/**
 * @ingroup SocketManager
 * @brief This thread which reads data from the socket and verifies the transport header.
 * @param ptr Pointer to the  socket_manager_header structure.
 */

void * transport_read_thread(void *ptr)
{
	psocket_manager_header handler=ptr;
	pPacket_Header pRdWrTcpPacket;
	pPacket_Header pAckPacket;
	bool valid_packet=true;
	pCommand_Header Command_Header_ptr =  NULL;

	int server_index = 0;
	//int return_status = 0;
	socket_handles *acquired_server_details;

	pthread_mutex_lock(&handler->lock);

	for (server_index = 0; server_index < handler->number_of_server; ++server_index) {
		if(handler->ServerClientDetails[server_index].connecting==false && handler->ServerClientDetails[server_index].connected==true && handler->ServerClientDetails[server_index].reading==false)
		{
			handler->ServerClientDetails[server_index].reading = true;
			break;
		}
	}

	pthread_mutex_unlock(&handler->lock);
	acquired_server_details = (handler->ServerClientDetails) + server_index;

	printf("Server acquired for reading %d IP %s Port %s\n",server_index,acquired_server_details->pserver_ip_address,acquired_server_details->port_no);

	while(handler->is_thread)
	{
		valid_packet=true;

		pRdWrTcpPacket=(pPacket_Header)tcp_read(handler, (handler->ServerClientDetails) + server_index);

		if(pRdWrTcpPacket==NULL)
		{
			//acquired_server_details->connected = false;
			//acquired_server_details->connecting = false;
			acquired_server_details->reading = false;
			return NULL;
		}

		if(isvalid_transportpacket(pRdWrTcpPacket)==false)
		{
			/*packet is corrupted */
			valid_packet=false;
			free(pRdWrTcpPacket);
			pRdWrTcpPacket=NULL;
		}

		if(valid_packet && pRdWrTcpPacket!=NULL)
		{
			pRdWrTcpPacket->server_index = server_index;
			Command_Header_ptr = (pCommand_Header)pRdWrTcpPacket->pData;
			Command_Header_ptr->server_index = server_index;

			DEBUG_PRINT(DEBUG_LEVEL_INFO,("Valid Packet Is received \n"));
			DEBUG_PRINT(DEBUG_LEVEL_INFO,("Packet length %x \n",pRdWrTcpPacket->Length));

			/*Create Ack Packet */
			pAckPacket=alloc_transport_ackpacket(pRdWrTcpPacket);
			debug_dump_commandheader((pCommand_Header)pRdWrTcpPacket->pData);
			/*queue the ack packet into write queue*/
			command_queue_append(&handler->WriteQueueHead,pAckPacket,pAckPacket->Id);
			/*wake up write thread*/
			pthread_mutex_lock(&handler->lock);
			pthread_cond_signal(&handler->WriteThreadCondition);
			pthread_mutex_unlock(&handler->lock);

			command_queue_append(&handler->CommandQueueHead,pRdWrTcpPacket,pRdWrTcpPacket->Id);
			/*wake up command thread*/
			pthread_mutex_lock(&handler->lock);
			pthread_cond_signal(&handler->CommandThreadCondition);
			pthread_mutex_unlock(&handler->lock);
		}
	}

	return NULL;
}



/**
 * @ingroup SocketManager
 * @brief This thread which writes data to the socket when ever write list is queued.
 * @param ptr Pointer to the  socket_manager_header structure.
 */

void * transport_write_thread(void *ptr)
{
	psocket_manager_header handler=ptr;
	pPacket_Header pRdWrTcpPacket;
	//pPacket_Header pTcpPacket;
	pCommand_Header pCommandhdr;
	int server_index = 0;

	while(handler->is_thread)
	{
		/**< has to sleep until write queue got node*/
		pthread_mutex_lock(&handler->lock);
		pthread_cond_wait(&handler->WriteThreadCondition,&handler->lock);
		pthread_mutex_unlock(&handler->lock);

		/**< get the first node from the queue*/
		//get until the queue is empty

		pRdWrTcpPacket=command_queue_dequeue(&handler->WriteQueueHead);


		while(pRdWrTcpPacket!=NULL)
		{
			pCommandhdr=(pCommand_Header)pRdWrTcpPacket->pData;

			debug_dump_commandheader(pCommandhdr);

			//printf("Sending packet to %d server\n",pRdWrTcpPacket->server_index);
			server_index = pRdWrTcpPacket->server_index;
			pRdWrTcpPacket->server_index = 0;
			tcp_write((handler->ServerClientDetails) + server_index,pRdWrTcpPacket);

			free(pRdWrTcpPacket);

			pRdWrTcpPacket=command_queue_dequeue(&handler->WriteQueueHead);
		}
	}
	return NULL;
}

/**
 * @ingroup SocketManager
 * @brief This function will write commands to command queue after the read/write command packet has been acknowledged.
 * @param ptr pointer to the socket_manager_header packet.
 */

void * command_thread(void *ptr)
{
	psocket_manager_header handler=ptr;
	int return_status;
	pPacket_Header pRdWrTcpPacket;
	pCommand_Header pCmdHeader;
	unsigned int * data_ptr;
	//pPacket_Header pTempTcpPacket;

	while(handler->is_thread)
	{
		/**< has to sleep until write queue got node*/

		pthread_mutex_lock(&handler->lock);
		pthread_cond_wait(&handler->CommandThreadCondition,&handler->lock);
		pthread_mutex_unlock(&handler->lock);


		pRdWrTcpPacket=command_queue_dequeue(&handler->CommandQueueHead);
		while(pRdWrTcpPacket!=NULL)
		{

			pCmdHeader=(pCommand_Header)pRdWrTcpPacket->pData;
			data_ptr = (unsigned int *)pCmdHeader->pData;
			debug_dump_commandheader(pCmdHeader);

			return_status=hepta_a10_2x4_command_decoder(pCmdHeader,pRdWrTcpPacket->Id);

			if(return_status < 0)
			{
				DEBUG_PRINT(DEBUG_LEVEL_ERROR,("Command Failed,sending Error Response\n"));
				pRdWrTcpPacket->Length=sizeof(Command_Header)+sizeof(unsigned int);
				pCmdHeader->Operation_Type = OPERATION_TYPE_ERROR;
				pCmdHeader->Length = 1;
				*(data_ptr)=return_status;
			}

			if(pCmdHeader->Transfer_Type == TRANSFER_TYPE_SERVICE)
			{
				pCmdHeader->Operation_Type = OPERATION_TYPE_ACK;
			}


			command_queue_append(&handler->WriteQueueHead,pRdWrTcpPacket,pRdWrTcpPacket->Id);

			pthread_mutex_lock(&handler->lock);
			pthread_cond_signal(&handler->WriteThreadCondition);
			pthread_mutex_unlock(&handler->lock);


			pRdWrTcpPacket=command_queue_dequeue(&handler->CommandQueueHead);
		}
	}
	return handler;
}

/**
 * @ingroup SocketManager
 * @brief Sends service message to the server with Id as id. This function queues the command in the write queue.
 * @param Id Command id for the service message to be sent.
 * @param module_index Module type that should be specified in the service message
 * @param pdata Data pointer to send data
 * @param Length Number of 32bit offsets
 * @return Status 0 on queueing the service message in the write queue.
 * @return Status -1 on fail .
 */



int send_service_message(psocket_manager_header socket_handler,unsigned int server_index,unsigned int Id,Module_Index_Enum_Def module_index,unsigned int *pdata,unsigned int Length)
{
	pPacket_Header pTcpPacket=0;
	pCommand_Header pCmdHeader;
	unsigned int *data_ptr;
	unsigned int data_index = 0;
	unsigned int j =0;

	if(socket_handler==NULL)
	{
		return -1;
	}

	if((pdata!=NULL)  && (Length > 0))
	{
		pTcpPacket=(pPacket_Header)malloc(TCP_PACKET_HEADER_SIZE+sizeof(Command_Header)+(sizeof(int)*Length));
	}
	else
	{
		pTcpPacket=(pPacket_Header)malloc(TCP_PACKET_HEADER_SIZE+sizeof(Command_Header));
	}

	//strncpy((char*)pTcpPacket->Header_String,TCP_PACKET_HEADER_STRING,TCP_PACKET_HEADER_STRING_SIZE);
	for(j=0;j<TCP_PACKET_HEADER_STRING_SIZE;j++)
		    	{
			pTcpPacket->Header_String[j]=tcp_pkt_headr[j];
		    	}
		j = 0;
	pTcpPacket->Id = Id;
	pTcpPacket->server_index = server_index;

	if((pdata!=NULL)  && (Length > 0))
	{
		pTcpPacket->Length = sizeof(Command_Header) + (sizeof(int)*Length);
	}
	else
	{
	pTcpPacket->Length = sizeof(Command_Header);
	}

	pCmdHeader=(pCommand_Header)pTcpPacket->pData;

	pCmdHeader->Transfer_Type = TRANSFER_TYPE_SERVICE;
	pCmdHeader->Operation_Type = OPERATION_TYPE_SERVICE;
	pCmdHeader->Address = 0;
	pCmdHeader->Instance = 0;
	pCmdHeader->server_index = server_index;
	
	pCmdHeader->Module_Index = module_index;

	data_ptr = (unsigned int *)pCmdHeader->pData;
	if((pdata!=NULL)  && (Length > 0))
	{
		pCmdHeader->Length = Length;
		for(data_index = 0;data_index < Length;data_index++)
		{
			data_ptr[data_index] = pdata[data_index];
		}
	}
	else
	{
		pCmdHeader->Length = 0;
	}

	command_queue_append(&socket_handler->WriteQueueHead,pTcpPacket,pTcpPacket->Id);
	pthread_mutex_lock(&socket_handler->lock);
	pthread_cond_signal(&socket_handler->WriteThreadCondition);
	pthread_mutex_unlock(&socket_handler->lock);

	return 0;

}


/**
 * @ingroup SocketManager
 * @brief Sends service disable message to the server with Id as id. This function queues the command in the write queue.
 * @param Id Command id for the service message to be disabled.
 * @param module_index Module type that should be specified in the service message
 * @return Status 0 on queueing the service message in the write queue.
 * @return Status -1 on fail .
 */


int send_service_ack_disable(psocket_manager_header socket_handler,unsigned int server_index,unsigned int Id,Module_Index_Enum_Def module_index)
{
	pPacket_Header pTcpPacket=0;
	pCommand_Header pCmdHeader;
    unsigned int j =0;
	if(socket_handler==NULL)
	{
		return -1;
	}

	pTcpPacket=(pPacket_Header)malloc(TCP_PACKET_HEADER_SIZE+sizeof(Command_Header));

	//strncpy((char*)pTcpPacket->Header_String,TCP_PACKET_HEADER_STRING,TCP_PACKET_HEADER_STRING_SIZE);
	for(j=0;j<TCP_PACKET_HEADER_STRING_SIZE;j++)
			    	{
				pTcpPacket->Header_String[j]=tcp_pkt_headr[j];
			    	}
			j = 0;
	pTcpPacket->Id = Id;
	pTcpPacket->Length = sizeof(Command_Header);
	pTcpPacket->server_index = server_index;

	pCmdHeader=(pCommand_Header)pTcpPacket->pData;

	pCmdHeader->Transfer_Type = TRANSFER_TYPE_SERVICE;
	pCmdHeader->Operation_Type = OPERATION_TYPE_ACK;
	pCmdHeader->Address = 0;
	pCmdHeader->Instance = 0;
	pCmdHeader->Length = 0;
	pCmdHeader->server_index = server_index;
	pCmdHeader->Module_Index = module_index;


	command_queue_append(&socket_handler->WriteQueueHead,pTcpPacket,pTcpPacket->Id);
	pthread_mutex_lock(&socket_handler->lock);
	pthread_cond_signal(&socket_handler->WriteThreadCondition);
	pthread_mutex_unlock(&socket_handler->lock);

	return 0;
}


/**
 * @ingroup SocketManager
 * @brief IPv4 address checker
 * @param str IPv4 number from the user
 * @return Returns 1 if its a valid IP address, 0 if its not valid
 */


int isValidIp4 (char *str)
{
	int segs = 0;   /* Segment count. */
	int chcnt = 0;  /* Character count within segment. */
	int accum = 0;  /* Accumulator for segment. */

	/* Catch NULL pointer. */

	if (str == NULL)
		return 0;

	/* Process every character in string. */

	while (*str != '\0') {
		/* Segment changeover. */

		if (*str == '.') {
			/* Must have some digits in segment. */

			if (chcnt == 0)
				return 0;

			/* Limit number of segments. */

			if (++segs == 4)
				return 0;

			/* Reset segment values and restart loop. */

			chcnt = accum = 0;
			str++;
			continue;
		}
		/* Check numeric. */

		if ((*str < '0') || (*str > '9'))
			return 0;

		/* Accumulate and check segment. */

		if ((accum = accum * 10 + *str - '0') > 255)
			return 0;

		/* Advance other segment specific stuff and continue loop. */

		chcnt++;
		str++;
	}

	/* Check enough segments and enough characters in last segment. */

	if (segs != 3)
		return 0;

	if (chcnt == 0)
		return 0;

	/* Address okay. */

	return 1;
}


/**
 * @ingroup HandlerManager
 * @brief Gets scheduler handler and creates client connection.
 * @return Returns 0 if connection is established.
 */

int socket_manager_init(psocket_manager_header socket_manager_header_ptr)
{
	int return_status;
	int server_index = 0;
	psocket_manager_header handler=socket_manager_header_ptr;
	DEBUG_PRINT(DEBUG_LEVEL_INFO,("%s %d %p \n",__FUNCTION__,__LINE__,handler));

	return_status = pthread_mutex_init(&handler->lock,NULL);
	return_status = pthread_cond_init(&handler->condition_signal,NULL);

	handler->is_thread=1;

	/*init write Queue*/
	return_status = init_function_queue(&handler->WriteQueueHead,"Write Queue");
	/*init command Queue*/
	return_status = init_function_queue(&handler->CommandQueueHead,"Command Queue");
	/*create Transport Socket Read thread */
	return_status = pthread_cond_init(&handler->WriteThreadCondition,NULL);
	return_status = pthread_cond_init(&handler->CommandThreadCondition,NULL);
	//return_status = pthread_create(&handler->pTransportReadThreadHandle, NULL, transport_read_thread, handler);
	/*Create Transport Socket Write Thread */
	return_status = pthread_create(&handler->pTransportWriteThreadHandle, NULL, transport_write_thread, handler);
	/*Create Command Layer Thread */
	return_status = pthread_create(&handler->pCommandThreadHandle, NULL, command_thread, handler);

	for (server_index = 0; server_index < handler->number_of_server; ++server_index)
	{
		handler->ServerClientDetails[server_index].server_index = server_index;
		return_status = pthread_create((handler->pServerConnectThreadHandle) + server_index, NULL,(void *)tcp_create_client,(void *) handler);
	}

	struct sched_param params;
	params.sched_priority = sched_get_priority_max(SCHED_FIFO);
	int result = pthread_setschedparam(handler->pCommandThreadHandle,SCHED_FIFO,&params);

	printf("Result command thread priority %d\n",result);

	return return_status;
}

int main()
{
return 0;
}
