#include <stdio.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>
#include <semaphore.h>
#include <cstring>
#include <stdlib.h>
using namespace std;

static sem_t mutex;

void *handler(void *name){
	sem_wait(&mutex);
	for(int i = 0; i < 10; i++){
		cout << "Thread " + *(string*)name + " " << i << endl;
	}
	sem_post(&mutex);
	return NULL;
}

int main(){
	string names[2] = {"thread 1", "thread 2"};

	pthread_t thread1, thread2;

	sem_init(&mutex, 0, 1);

	pthread_create(&thread1, NULL, handler, &names[0]);
	pthread_create(&thread2, NULL, handler, &names[1]);

	pthread_join(thread1, NULL);
	pthread_join(thread2, NULL);

	sem_destroy(&mutex);

	return 0;
}
