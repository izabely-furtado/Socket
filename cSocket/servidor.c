/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <pthread.h>
#include <unistd.h>
#include <sys/sysinfo.h>

pthread_mutex_t lock;
pthread_t threads[];
char buffer[256];

void error(char *msg)
{
    perror(msg);
    exit(1);
}

//criando threads
void CriaThread(int newsockfd) {
	int i, k;
	//int nProc = get_nprocs();
	int nProc = 11;		//10 clientes para ler msg e um para enviar resposta

	//criando vetor de threads
	threads[nProc] = (pthread_t *) malloc(nProc * sizeof (pthread_t));

	//dando a cada thread um objetivo
	pthread_create(&(threads[0]), NULL, &Write, newsockfd)
	
	for (i = 1; i < nProc; i++) {
		pthread_create(&(threads[i]), NULL, &Read, newsockfd);
	}
	
	//mandando bala
	for (k = 0; k < nProc; k++) {
		pthread_join(threads[k], NULL);
	}
}

//escrevendo no buffer
void *Write(int newsockfd){
	int n;
	
	n = write(newsockfd,"I got your message",18);
     if (n < 0) {
		 error("ERROR writing to socket");
	}
}

//lendo do buffer
void *Read(int newsockfd) {
	
    bzero(buffer,256);
    n = read(newsockfd,buffer,255);
    if (n < 0) {
		error("ERROR reading from socket");
    }
    while (strcmp(buffer, "bye")!=0){
    	printf("Here is the message: %s\n",buffer);
    }
    printf("Comunicação encerrada");

}

int main(int argc, char *argv[])
{
     int sockfd, newsockfd, portno, clilen;
     char buffer[256];
     struct sockaddr_in serv_addr, cli_addr;
     int n;
     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
     sockfd = socket(AF_INET, SOCK_STREAM, 0);
     if (sockfd < 0) 
        error("ERROR opening socket");
     bzero((char *) &serv_addr, sizeof(serv_addr));
     portno = atoi(argv[1]);
     serv_addr.sin_family = AF_INET;
     serv_addr.sin_addr.s_addr = INADDR_ANY;
     serv_addr.sin_port = htons(portno);
     if (bind(sockfd, (struct sockaddr *) &serv_addr,
              sizeof(serv_addr)) < 0) 
              error("ERROR on binding");
     listen(sockfd,5);
     clilen = sizeof(cli_addr);
     newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
     if (newsockfd < 0) 
          error("ERROR on accept");


     CriaThread(newsockfd);
     
     return 0; 
}
