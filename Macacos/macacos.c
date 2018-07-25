#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

typedef enum {EAST, WEST} Destination;
void monkey(int id, Destination dest);
void WaitUtilSafeToCross(Destination dest);
void CrossRavine(int id, Destination dest);
void DoneWithCrossing(Destination dest);

int monkey_count[2] = {0,0};
sem_t mutex[2];
sem_t max_on_rope;
sem_t rope;

int main(){
	sem_init(&mutex[EAST], 0, 1);
	sem_init(&mutex[WEST], 0, 1);
	sem_init(&max_on_rope, 0, 5);
	sem_init(&rope, 0, 1);

	

	return 0;
}

void monkey(int id, Destination dest){
	WaitUtilSafeToCross(dest);
	CrossRavine(id, dest);
	DoneWithCrossing(dest);
}

void CrossRavine(int id, Destination dest){
	static char dest_string[2][5] = { "EAST", "WEST" };
	printf("Macaco %d %s", id, dest_string[dest] );
}
