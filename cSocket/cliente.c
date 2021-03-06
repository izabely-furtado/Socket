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
    exit(0);
}

//criando threads
void CriaThread(int sockfd) {
	int i, k;
	//int nProc = get_nprocs();
	int nProc = 11;		//10 clientes e um para ler msgs do servidor

	//criando vetor de threads
	threads[nProc] = (pthread_t *) malloc(nProc * sizeof (pthread_t));

	//dando a cada thread um objetivo
	
	pthread_create(&(threads[0]), NULL, &Read, sockfd);
	
	for (i = 1; i < nProc; i++) {
		pthread_create(&(threads[i]), NULL, &Write, sockfd);
	}
	
	//mandando bala
	for (k = 0; k < nProc; k++) {
		pthread_join(threads[k], NULL);
	}
}

//escrevendo no buffer
int *Write(int sockfd){
	int n;
	
	do{
		bzero(buffer,256);
		fgets(buffer,255,stdin);
		printf("Please enter the message: ");
		
		n = write(sockfd,buffer,strlen(buffer));
	
		if (n < 0) {
			error("ERROR writing to socket");
		}

	}while (strcmp(buffer, "bye")!=0)
	
	return sockfd;
}

//lendo do buffer
void *Read(int sockfd) {
	int n;
    n = read(sockfd,buffer,255);
    if (n < 0) 
         error("ERROR reading from socket");
    printf("%s\n",buffer);
}


int main(int argc, char *argv[])
{
    int sockfd, portno, n;

    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[256];
    if (argc < 3) {
       fprintf(stderr,"usage %s hostname port\n", argv[0]);
       exit(0);
    }
    portno = atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");
    
    CriaThread(sockfd);
    
    return 0;
}
