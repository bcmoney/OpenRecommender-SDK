/***************************************************************************
 * This program is free software; you can redistribute it and/or modify *
 * it under the terms of the GNU General Public License as published by *
 * the Free Software Foundation; either version 2 of the License, or *
 * (at your option) any later version. *
 *
 * This program is distributed in the hope that it will be useful, *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the *
 * GNU General Public License for more details. *
 * 
 * You should have received a copy of the GNU General Public License *
 * along with this program; if not, write to the *
 * Free Software Foundation, Inc., *
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 * client.c *
 ***************************************************************************/

    #include <stdio.h>
    #include <stdlib.h>
    #include <unistd.h>
    #include <errno.h>
    #include <string.h>
    #include <netdb.h>
    #include <sys/types.h>
    #include <netinet/in.h>
    #include <sys/socket.h>

     

    #define MAXDATASIZE 1024 // max buffer size

    int main(int argc, char *argv[])
    {
        int sockfd; // main socket file descriptor
    unsigned int PORT; // port number
    int numbytes; // # of bytes recieved by recv() system call
        char buf[MAXDATASIZE]; //buffer which contains data coming in from recv() system call
        struct hostent *he; // we don't wanna type ip's all the time now to we :)
        struct sockaddr_in their_addr; // connector's address information

    //if user doesn't provide the clients hostname and port number
        if (argc != 3) {
            fprintf(stderr,"usage: client hostname and port number\n");
            exit(1);
        }

    // get the ip addy for the hostname provided by arg[1]
        if ((he=gethostbyname(argv[1])) == NULL) {
            perror("gethostbyname");
            exit(1);
        }
    
        if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
            perror("socket");
            exit(1);
        }
    
    PORT = atoi(argv[2]); // turn this into a int value :)
    
        host.sin_family = AF_INET;
        host.sin_port = htons(PORT); // short, network byte order
           host.sin_addr = *((struct in_addr *)he->h_addr);
        memset(&(their_addr.sin_zero), '\0', 8); // zero the rest of the struct

    
    //connect to thy host
        if (connect(sockfd, (struct sockaddr *)&host, sizeof(struct sockaddr)) < 0)
    {
            perror("connect");
            exit(1);
        }

    printf("Connected to %s\n", inet_ntoa(their_addr));
    
    //recieve data from the host
        if ((numbytes = recv(sockfd, buf, MAXDATASIZE, 0)) < 0)
    {
            perror("recv");
            exit(1);
        }

        buf[numbytes-1] = '\0';

        printf("Received: %s",buf); //print out what you recieved

        close(sockfd);

        return 0;
    }